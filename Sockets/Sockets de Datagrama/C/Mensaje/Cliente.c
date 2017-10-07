#include <sys/types.h>
#include <sys/socket.h>
#include <netdb.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#define BUF_SIZE 500

int main (int argc, char const * argv [])
{
	struct addrinfo hints;								//Estructura para las indicaciones del socket
	struct addrinfo * result;							//Lista con direcciones para conexión
	struct addrinfo * pt;								//Apuntador a la lista de direcciones 'result'
	int descriptor;										//Descriptor de socket que regresa la funcion 'socket'
	int direcciones;									//Lista de direcciones para la conexion
	ssize_t longitud_mensaje;							//Para guardar la longitud del mensaje a enviar
	ssize_t bytes_recibidos;							//Para guardar los bytes recibidos
	char mensaje [BUF_SIZE];							//Para guardar el mensaje ingresado
	char * puerto = (char *) malloc (sizeof (char));	//Para guardar el puerto para la conexion
	char * direccion = (char *) malloc (sizeof (char));	//Para guardar la direccion para la conexion

	if (argc < 3)
	{
		printf ("\nError, faltan indicar el host, numero de puerto de puerto y mensaje. \n\n");
		printf ("Ejemplo: '%s 127.0.0.1 1234 este es el mensaje a enviar'\n\n", argv [0]);
		exit (0);
	}else
	{
		direccion = (char *) argv [1];
		puerto = (char *) argv [2];
	}

	//Especificaciones para la creación del socket
	memset (&hints, 0, sizeof (struct addrinfo));		//Limpiamos la estructura
	hints.ai_family = AF_UNSPEC;						//Permite IPv4 e IPv6
	hints.ai_socktype = SOCK_DGRAM;						//Socket de datagrama
	hints.ai_flags = 0;									//Para usar la funcion bind
	hints.ai_protocol = 0;								//Cualquier protocolo (no lo especificamos)

	//Obtenemos una lista de direcciones para la conexión
	direcciones = getaddrinfo (direccion, puerto, &hints, &result);
	if (direcciones != 0)
	{
		printf ("Error al obtener la lista de direcciones: %s\n", gai_strerror (direcciones));
		exit (0);
	}

	for (pt = result; pt != NULL; pt = pt -> ai_next)
	{
		descriptor = socket (pt -> ai_family, pt -> ai_socktype, pt -> ai_protocol);

		//Tratamos de realizar la conexión (Si es distinto de -1, se conectó correctamente)
		int intento_conexion;
		intento_conexion = connect (descriptor, pt -> ai_addr, pt -> ai_addrlen);
		if (intento_conexion != -1)
			break;
		close (descriptor);
	}

	if (pt == NULL)
	{
		printf("\nError en la conexion\n");
		exit (0);
	}
	freeaddrinfo (result);								//Liberamos la memoria

	//Comenzamos a leer el mensaje ingresado y enviar y recibir datagramas
	for (int i = 3; i < argc; i ++)
	{
		longitud_mensaje = strlen (argv [i]) + 1;		//Para saber los bytes a enviar (contando al caracter nulo)
		if (longitud_mensaje > BUF_SIZE)
			printf ("El mensaje del argumento %d es demasiado largo y no se enviará completo\n", i);

		//Enviamos el datagrama
		if (write (descriptor, argv [i], longitud_mensaje) != longitud_mensaje)
		{
			printf ("Error al escribir el paquete\n");
			exit (0);
		}
		bytes_recibidos = read (descriptor, mensaje, BUF_SIZE);
		if (bytes_recibidos == -1)
		{
			printf ("\nError al recibir el mensaje\n");
			exit (0);
		}
		printf ("Se recibieron %ld bytes: %s\n", bytes_recibidos, mensaje);
	}
	return 0;
}