#!/bin/bash
 
echo -e '\nGenerando 3500 numeros aleatorios\n'

rm numeros.txt

#echo '0' > numeros.txt
for ((i = 0; i < 3500 ; i = (i + 1))); do
	echo $(($RANDOM % 1000)) >> numeros.txt
done

echo -e "Numeros generados :)\n"