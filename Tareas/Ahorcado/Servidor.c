#include <sys/types.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <sys/socket.h>
#include <netdb.h>
#include <arpa/inet.h>
#include "juega.c"
#define BUF_SIZE 500

int main (int argc, char const * argv [])
{
	struct addrinfo hints;							//Estructura para las indicaciones del socket
	struct addrinfo * result;						//Lista con direcciones para conexión
	struct addrinfo * pt;							//Apuntador a la lista de direcciones 'result'
	int descriptor;									//Descriptor de socket que regresa la funcion 'socket'
	int direcciones;								//Lista de direcciones para la conexion
	char * puerto = (char *) malloc (sizeof (char));//Para guardar el puerto para la conexion
	char direccion_cliente [NI_MAXHOST];			//Para guardar la direccion del cliente
	char puerto_cliente [NI_MAXSERV];				//Para guardar el puerto del cliente
	struct sockaddr_storage direccion_envio;		//Para guardar la direccion a donde se va a enviar el datagrama
	socklen_t longitud_estructura;					//Para guardar la longitud de la estructura apuntada por 'direccion_envio'
	socklen_t longitud_host = 0;					//Para la función getnameinfo
	ssize_t longitud_mensaje;						//Para guardar la longitud del mensaje recibido
	char mensaje [BUF_SIZE];						//Para guardar el mensaje recibido
	char * dificultad;								//Para seleccionar la dificultad del juego
	char * palabra;									//Para guardar la palabra con la que se va a jugar

	if (argc != 2)
	{
		printf ("\nError, faltan indicar el numero de puerto, ejemplo: '%s 1234'\n\n", argv [0]);
		exit (0);
	}else
		puerto = (char *) argv [1];					//Guardamos el puerto para la conexion
	//system ("clear");

	//Especificaciones para la creación del socket
	memset (&hints, 0, sizeof (struct addrinfo));	//Limpiamos la estructura
	hints.ai_family = AF_INET6;						//Permite IPv4 e IPv6
	hints.ai_socktype = SOCK_DGRAM;					//Socket de datagrama
	hints.ai_flags = AI_PASSIVE;					//Para usar la funcion bind
	hints.ai_protocol = 0;							//Cualquier protocolo (no lo especificamos)
	hints.ai_canonname = NULL;						//Nombre canónico
	hints.ai_addr = NULL;							//Apuntador a la direccion del socket
	hints.ai_next = NULL;							//Apuntador al siguiente elemento de la lista

	//Obtenemos una lista de direcciones para la conexion
	direcciones = getaddrinfo (NULL, puerto, &hints, &result);
	if (direcciones != 0)
	{
		printf ("Error al obtener la lista de direcciones: %s\n", gai_strerror (direcciones));
		exit (0);
	}

	//Recorremos la lista de direcciones obtenida hasta una conexión exitosa
	for (pt = result; pt != NULL; pt = pt -> ai_next)
	{
		descriptor = socket (pt -> ai_family, pt -> ai_socktype, pt -> ai_protocol);
		if (descriptor == -1)
			continue;

		//Modificamos las opciones de socket
		int op = 0;
		setsockopt (descriptor, IPPROTO_IPV6, IPV6_V6ONLY, &op, sizeof (op));

		//Verificamos la conexión
		int conexion;
		conexion = getnameinfo ((struct sockaddr *)&hints, longitud_host, direccion_cliente, sizeof (direccion_cliente),
								puerto_cliente, sizeof (puerto_cliente), NI_NUMERICHOST | NI_NUMERICSERV);
		if (conexion == 0)
			printf ("Cliente conectado desde %s : %s\n", direccion_cliente, puerto_cliente);
		printf ("%d\n", pt -> ai_addrlen);

		//Convertimos una direccion numerica en una cadena de caracteres
		char address [NI_MAXHOST];
		if (inet_ntop (pt -> ai_family, pt -> ai_addr, address, pt -> ai_addrlen) != NULL)
		{
			printf ("\nDireccion: %s\n", address);
			for (int i = 0; i < 16; i ++)
				printf ("%02x ", (pt -> ai_addr) -> sa_data [i]);
			printf ("\n");
		}

		//Tratamos de asociar el socket a un puerto (Si es 0, se asoció correctamente)
		int intento_conexion;
		intento_conexion = bind (descriptor, pt -> ai_addr, pt -> ai_addrlen);
		if (intento_conexion == 0)
			break;
		close (descriptor);
	}

	if (pt == NULL)
	{
		printf("\nError en la conexion\n");
		exit (0);
	}
	freeaddrinfo (result);							//Liberamos la memoria
	
	//Enviamos y recibimos datagramas en un ciclo infinito
	int direccion;
	dificultad = (char *) malloc (sizeof (char));
	palabra = (char *) malloc (sizeof (char));
	for (;;)
	{
		longitud_estructura = sizeof (struct sockaddr_storage);
		longitud_mensaje = recvfrom (descriptor, mensaje, BUF_SIZE, 0,
									(struct sockaddr *) & direccion_envio, &longitud_estructura);
		if (longitud_mensaje == -1)
		{
			printf ("\nError al recibir el mensaje\n");
			exit (0);
		}
		direccion = getnameinfo ((struct sockaddr *) & direccion_envio, longitud_estructura,
								direccion_cliente, NI_MAXHOST, puerto_cliente, NI_MAXSERV, NI_NUMERICSERV);
		if (direccion == 0)
		{
			//printf ("Se recibieron %ld bytes desde %s : %s\n", longitud_mensaje, direccion_cliente, puerto_cliente);
			if (mensaje [0] == '1')
				strcpy (dificultad, "Faciles.txt");
			else if (mensaje [0] == '2')
				strcpy (dificultad, "Medias.txt");
			else if (mensaje [0] == '3')
				strcpy (dificultad, "Dificiles.txt");
			else
			{
				printf("Error al seleccionar la dificultad\n");
				exit (0);
			}
			palabra = seleccionar_palabra (dificultad);
			printf("Palabra seleccionada: '%s'\n", palabra);
		}
		else
		{
			printf ("Error al convertir la direccion de socket: %s\n", gai_strerror (direccion));
			exit (0);
		}
		if (sendto (descriptor, mensaje, longitud_mensaje, 0,
					(struct sockaddr *) &direccion_envio,
					longitud_estructura) != longitud_mensaje)
		{
			printf ("Error al escribir el paquete\n");
			exit (0);
		}
	}
	return 0;
}
