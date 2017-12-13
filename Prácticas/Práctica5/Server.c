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

typedef struct
{
	int host_port;
	char host_name [10];
	int * bucket;
	int size_bucket;
	int offset;
}datos;

void Servidor (int * a);
void shell (int * array, int size);

int main (int argv, char ** argc)
{
	if(argv < 2)
	{
		printf("Error, falta indicar el numero de cubetas.\n");
		printf ("Ejemplo: '%s 10'", argc [0]);
		exit(0);
  	}
	int i, puerto = 5000, cubetas = atoi (argc [1]);
	int * puertos = (int *) malloc (cubetas * sizeof(int));
	for (i = 0; i < cubetas; i ++)
		puertos [i] = (puerto + i);
	printf ("\nCreando %d hilos servidor\n", cubetas);
	pthread_t * servidor = (pthread_t *) calloc (cubetas, sizeof (pthread_t));
	for (i = 0; i < cubetas; i ++, puerto ++)
		pthread_create (&(servidor [i]), NULL, (void *) &Servidor, (void *) &(puertos [i]));
	for (i = 0; i < cubetas; i ++, puerto ++)
		pthread_join ((servidor [i]), NULL);
	free (servidor);
	free (puertos);
	return 0;
}
	
void Servidor(int * a)
{
	int host_port = *a;
	struct sockaddr_in my_addr;
	int hsock;
	int * p_int;
	socklen_t addr_size = 0;
	int * csock;
	struct sockaddr_in sadr;
	hsock = socket (AF_INET, SOCK_STREAM, 0);
	if(hsock == -1)
	{
		printf("Error initializing socket %d\n", errno);
		exit(0);
	}
	p_int = (int *) malloc (sizeof (int));
	*p_int = 1;
	if ((setsockopt(hsock, SOL_SOCKET, SO_REUSEADDR, (char*)p_int, sizeof(int)) == -1 )||
		(setsockopt(hsock, SOL_SOCKET, SO_KEEPALIVE, (char*)p_int, sizeof(int)) == -1 ))
	{
		printf ("Error setting options %d\n", errno);
		free (p_int);
		exit (0);
	}
	free (p_int);
	my_addr.sin_family = AF_INET ;
	my_addr.sin_port = htons (host_port);	
	memset (&(my_addr.sin_zero), 0, 8);
	my_addr.sin_addr.s_addr = INADDR_ANY ;
	if (bind(hsock,(struct sockaddr*)&my_addr, sizeof(my_addr)) == -1 )
	{
		fprintf(stderr,"Error binding to socket, make sure nothing else is listening on this port %d\n",errno);
		exit(0);
	}
	if (listen (hsock, 10) == -1)
	{
		fprintf (stderr, "Error listening %d\n",errno);
		exit (0);
	}
	addr_size = sizeof (struct sockaddr_in);
	
	while (1)
	{
		csock = (int *) malloc (sizeof (int));
		if ((*csock = accept (hsock, (struct sockaddr*) &sadr, &addr_size))!= -1)
		{
			printf ("Conexión recibida desde: %s\n", inet_ntoa (sadr.sin_addr));
			int bytecount, buffer;
			if ((bytecount = recv (*csock, &buffer, sizeof (int), 0))== -1)
			{
				fprintf (stderr, "Error receiving data %d\n", errno);
				exit (0);
			}
			int i, tamanio=ntohl(buffer);
			int * array = (int *) calloc (tamanio, sizeof (int));
			for (i = 0; i < tamanio; i ++)
			{
				if ((bytecount = recv (*csock, &buffer, sizeof (int), 0)) == -1)
				{
					fprintf (stderr, "Error receiving data %d\n", errno);
					exit (0);
				}
				array [i] = ntohl (buffer);
			}
	      	shell (array, tamanio);
			for(i = 0; i < tamanio; i ++)
			{
				int entero = htonl (array [i]);
				if((bytecount = send (*csock, &entero, sizeof (int), 0)) == -1)
				{
					fprintf (stderr, "Error receiving data %d\n", errno);
					exit (0);
				}
			}
			free (csock);
			break;
		}else
			fprintf (stderr, "Error accepting %d\n", errno);
	}
}
	
void shell (int * array, int size)
{
   int i, j, temp, k = size / 2;
	while (k > 0)
	{
		for (i = k; i < size; i ++)
		{
			temp = array [i];
			j = i - k;
			while (j >= 0 && array [j] > temp)
			{
				array [j + k] = array [j];
				j -= k;
			}
			array [j + k] = temp;
		}
		k /= 2;
	}
	return;
}
