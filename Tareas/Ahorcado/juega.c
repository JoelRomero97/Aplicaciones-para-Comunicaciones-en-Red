#include <time.h>
#include "juega.h"

char * seleccionar_palabra (char * dificultad)
{
	FILE * archivo;										//Para abrir la palabra a usar en el juego
	char * palabra = (char *) malloc (sizeof (char));
	int numero_palabra, i;
	archivo = fopen (dificultad, "r");
	if (archivo == NULL)
	{
		printf("Error al abrir el archivo con las palabras.\n");
		exit (0);
	}
	srand(time(NULL));
	numero_palabra = rand () % 26;
	for (i = 0; i < numero_palabra; i ++)
		fscanf (archivo, "%s\n", palabra);
	return palabra;
}