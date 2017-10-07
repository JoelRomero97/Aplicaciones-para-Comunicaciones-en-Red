#include <sys/types.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <sys/socket.h>
#include <netdb.h>
#include <arpa/inet.h>
#define BUF_SIZE 500

int main(int argc, char const *argv[])
{
	struct addrinfo hints;							//Estructura para las indicaciones del socket
	struct addrinfo * result;						//Lista con direcciones para conexión
	struct addrinfo * pt;							//Apuntador a la lista de direcciones 'result'
	int descriptor;									//Descriptor de socket que regresa la funcion 'socket'
	int direcciones, puerto;						//Lista de direcciones y puerto para la conexion
	char direccion_cliente [NI_MAXHOST];			//Para guardar la direccion del cliente
	char puerto_cliente [NI_MAXSERV];				//Para guardar el puerto del cliente
	struct sockaddr_storage direccion_envio;		//Para guardar la direccion a donde se va a enviar el datagrama
	socklen_t longitud_envio;						//Para guardar la longitud de la estructura apuntada por 'direccion_envio'
	socklen_t longitud_host;						//Para la función getnameinfo
	ssize_t longitud_mensaje;						//Para guardar la longitud del mensaje recibido
	char mensaje [BUF_SIZE];						//Para guardar el mensaje recibido
	if (argc != 2)
	{
		printf ("\nError, faltan indicar el numero de puerto, ejemplo: '%s 1234'\n\n", argv [0]);
		exit (0);
	}else
		puerto = atoi (argv [1]);					//Guardamos el puerto para la conexion

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
		int opciones = setsockopt (descriptor, IPPROTO_IPV6, IPV6_V6ONLY, &op, sizeof (op));

		//Verificamos la conexión
		int conexion;
		conexion = getnameinfo ((struct sockaddr *)&hints, longitud_host, direccion_cliente, sizeof (direccion_cliente)
								puerto_cliente, sizeof (puerto_cliente), NI_NUMERICHOST | NI_NUMERICSERV);
		if (conexion == 0)
			printf ("Cliente conectado desde %s : %s\n", direccion_cliente, puerto_cliente);
		printf ("%d\n", pt -> ai_addrlen);

		//Convertimos una direccion numerica en una cadena de caracteres
		char address [NI_MAXHOST];
		if (inet_ntop (pt -> ai_family, pt -> ai_addr, address, pt -> ai_addrlen) != NULL)
			printf ("\nDireccion: %s", address);
	}
	return 0;
}