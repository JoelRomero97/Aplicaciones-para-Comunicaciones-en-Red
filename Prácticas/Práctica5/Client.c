#include <fcntl.h>
#include <string.h>
#include <stdlib.h>
#include <errno.h>
#include <stdio.h>
#include <netinet/in.h>
#include <resolv.h>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <pthread.h>
#include <sys/types.h>
#include <netdb.h> 
#define ARRAY_LENGTH 3500
#define NUM_MAX 1000

typedef struct
{
	int host_port;
	char host_name [10];
	int *bucket;
	int size_bucket;
	int offset;
}datos;

void leer_archivo (int * numeros);
void crear_cubeta (datos * Estructuras, int num_cubetas, int * numeros);
int llenar_cubeta (datos * Estructuras, int numBucket, int limInf, int limSup, int * numeros);
void * Cliente (datos * Estructura);

int resultado [ARRAY_LENGTH];
int total_offset = 0;

int main (int argv, char** argc)
{
	int i, test, num_cubetas, numeros [ARRAY_LENGTH];
	printf ("Escribe el número de cubetas:\n");
	scanf("%d",&num_cubetas);
	leer_archivo (numeros);
	printf ("\nSe ha inicializado el arreglo\n");
	datos * Estructuras = (datos*) calloc (num_cubetas, sizeof( datos*));
	printf("Se han creado %d cubetas\n", num_cubetas);
	crear_cubeta (Estructuras, num_cubetas, numeros);

	printf ("\nCreando hilos cliente...\n");
	pthread_t * cliente = (pthread_t *) calloc (num_cubetas, sizeof (pthread_t));	
	for (i = 0; i < num_cubetas; i ++)
    	pthread_create (&(cliente [i]), NULL, (void *) Cliente, &Estructuras [i]);
	for(i = 0; i < num_cubetas; i ++)
    	pthread_join (cliente [i], (void *) &test);
	usleep (10000);
	FILE * numeros_ordenados;
	numeros_ordenados = fopen ("numeros_ordenados.txt", "w");
	for (i = 0; i < ARRAY_LENGTH; i ++)
		fprintf (numeros_ordenados, "%d\n", resultado [i]);
	fclose (numeros_ordenados);
	printf("\nNumeros ordenados correctamente en 'numeros_ordenados.txt'\n\n");
	return 0;
}


void leer_archivo (int * numeros)
{
	int i;
	FILE * numeros;
	numeros = fopen ("numeros.txt", "r");
	if (numeros == NULL)
	{
		printf("Error al abrir el archivo\n");
		exit(1);
	}else
	{
		for (i = 0; i < ARRAY_LENGTH; i ++)
			fscanf (numeros, "%d", &numeros [i]);
		numeros [i] = '\0';
		fclose (numeros);
	}
}


void crear_cubeta (datos * Estructuras, int num_cubetas, int * numeros)
{
	int i, count, limInf = 0, Range, modulo;
	printf("Inicializando cubetas.\n");
	Range = ((NUM_MAX / num_cubetas) - 1);
	modulo = ARRAY_LENGTH%num_cubetas;
	for (i = 0; i < num_cubetas; i ++)
	{
		printf("Inicializando cubeta: %d.\n", i);
		Estructuras [i].host_port = (5000 + i);
		strcpy (&*(Estructuras [i].host_name), "127.0.0.1");
		if (modulo != 0 && i == (num_cubetas - 1))
			count = llenar_cubeta (Estructuras, i, limInf, limInf + Range + (modulo) - 1, numeros);				
		else
			count = llenar_cubeta (Estructuras, i, limInf, limInf + Range, numeros);
		Estructuras [i].offset = total_offset;
		total_offset = total_offset + count;
		limInf += Range + 1;
	}	
}

int llenar_cubeta (datos * Estructuras, int numBucket, int limInf, int limSup, int * numeros)
{
	int * rangeNums = (int *) calloc (ARRAY_LENGTH, sizeof (int));
	int i, count = 0;
	for(i = 0; i < ARRAY_LENGTH; i ++)
	{
		if(numeros [i] >= limInf && numeros [i] <= limSup)
		{
			rangeNums [count] = numeros [i];
			++ count;
		}
	}
	Estructuras [numBucket].size_bucket = count;	
	(Estructuras [numBucket].bucket) = (int *) calloc (count, sizeof (int));
	for (i = 0; i < count; i ++)
    	Estructuras [numBucket].bucket [i] = rangeNums [i];
	return count;
}

void * Cliente (datos * Estructura)
{
	int host_port = (*Estructura).host_port;
	char * host_name = malloc (16 * sizeof (char));
	strcpy (host_name, (*Estructura).host_name);
	struct sockaddr_in my_addr;
	int bytecount, hsock, err;
	int * p_int;

	hsock = socket (AF_INET, SOCK_STREAM, 0);
	if(hsock == -1)
	{
		printf("Error initializing socket %d\n",errno);
		exit(0);
	}
	p_int = (int *) malloc (sizeof (int));
	* p_int = 1;
		
	if((setsockopt(hsock, SOL_SOCKET, SO_REUSEADDR, (char*)p_int, sizeof(int)) == -1 )||
		(setsockopt(hsock, SOL_SOCKET, SO_KEEPALIVE, (char*)p_int, sizeof(int)) == -1 ) )
	{
		printf("Error setting options %d\n", errno);
		free (p_int);
		exit (0);
	}
	
	free (p_int);

	my_addr.sin_family = AF_INET;
	my_addr.sin_port = htons (host_port);
	
	memset (& (my_addr.sin_zero), 0, 8);
	my_addr.sin_addr.s_addr = inet_addr (host_name);

	if (connect( hsock, (struct sockaddr*)&my_addr, sizeof(my_addr)) == -1 )
	{
		if ((err = errno) != EINPROGRESS)
		{
			fprintf(stderr, "Error connecting socket %d\n", errno);
			exit(0);
		}
	}

	int numero_convertido = htonl ((*Estructura).size_bucket);
	if( (bytecount=send(hsock, &numero_convertido, sizeof(int),0))== -1)
	{
		fprintf(stderr, "Error sending data %d\n", errno);
		exit(0);
	}

	int i;
	for (i = 0; i < (*Estructura).size_bucket; i ++)
	{
		int dato_convertido = htonl ((*Estructura).bucket [i]);
		if( (bytecount = send (hsock, &dato_convertido, sizeof (int), 0))== -1)
		{
			fprintf(stderr, "Error sending data %d\n", errno);
			exit(0);
		}
	}
	
	int n;
	for (i = 0; i < Estructura [0].size_bucket; i ++)
	{
		int dato;
		n = read (hsock, &dato, sizeof (int));
		if (n < 0) 
			perror("ERROR al intentar leer del socket");
		resultado [Estructura [0].offset + i] = ntohl (dato);
	}
	return (void *) 1;
}
