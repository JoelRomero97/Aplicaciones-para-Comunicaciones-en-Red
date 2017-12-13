import java.net.*;
import java.io.*;
import java.util.Scanner;

////////////////////////////////////////////////////////////////////////////////////////
////								Cliente.java 									////
////																				////
////																				////
//// Con la implementación de Socket de datagrama, se crea un servicio en el que 	////
//// un cliente envia cadenas al servidor y este las recibe.						////
//// Instrucciones: Compilar y ejecutar Servidor.java								////
////				Posteriormente, compilar y ejecutar Cliente.java				////
////																				////
////																				////
//// Autor: Romero Gamarra Joel Mauricio											////
////////////////////////////////////////////////////////////////////////////////////////

public class Cliente
{
	public Cliente ()
	{
		try
		{
			InetAddress dst = null;
			//Flujo orientado a caracter asociado a la entrada por teclado para que el usuario ingrese una cadena
			BufferedReader br = new BufferedReader (new InputStreamReader (System.in));
			Scanner sc = new Scanner (System.in);
			//String host = "127.0.0.1";
			System.out.println ("\n\nIntroduce la direccion IP: ");
			String host = sc.next ();
			System.out.println ("\n\nIntroduce el puerto: ");
			int puerto = sc.nextInt ();
			//int puerto = 7777;
			try
			{
				dst = InetAddress.getByName (host);					//Resolvemos la direccion IP
			}catch (UnknownHostException u)
			{
				System.err.println ("Direccion invalida.\n");
				System.exit (1);
			}
			//Creamos el socket sin asociarlo a ningun puerto, por lo tanto se le asociará
			DatagramSocket cliente = new DatagramSocket ();
			System.out.println ("Escribe una cadena. <Enter> para enviar\n\n\"Salir\" Para terminar\n\n");
			for (;;)
			{
				String datos = br.readLine ();
				if (datos.compareToIgnoreCase ("Salir") == 0)			//Si el usuario teclea la palabra "Salir"
				{
					byte [] b = datos.getBytes ();						//Convertimos cadena a un arreglo de bytes
					//Creamos el paquete de datagrama con el arreglo de bytes, la longitud, la direccion y el puerto
					DatagramPacket paquete = new DatagramPacket (b, b.length, dst, puerto);
					cliente.send (paquete);								//Enviamos el paquete
					break;
				}else
				{
					byte [] b = datos.getBytes ();
					if (b.length >= 65535)											//Si es más de 65,535 bytes
					{
						ByteArrayInputStream bais = new ByteArrayInputStream (b);	//Creamos un arreglo orientado a bytes
						int n = 0;
						byte [] b2 = new byte [65535];								//Vamos a leer bloques de 65,535 en 65,535 bytes
						while ((n = bais.read (b2)) != -1)
						{
							//Creamos nuestro paquete de datagrama con el arreglo, los bytes leidos, la direccion y el puerto
							DatagramPacket paquete = new DatagramPacket (b2, n, dst, puerto);
							cliente.send (paquete);									//Enviamos el paquete
							b2 = new byte [65535];
						}
						bais.close ();												//Cerramos el arreglo orientado a bytes
					}else
					{
						DatagramPacket paquete = new DatagramPacket (b, b.length, dst, puerto);
						cliente.send (paquete);										//Enviamos el paquete
					}
				}
			}
			cliente.close ();
			br.close ();
		}catch (Exception e)
		{
			e.printStackTrace ();
		}
	}

	public static void main(String[] args)
	{
		new Cliente ();
	}
}