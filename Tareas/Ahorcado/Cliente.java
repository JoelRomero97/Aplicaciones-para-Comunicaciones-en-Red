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
	private InetAddress dst;
	private BufferedReader br;
	private Scanner sc;
	private String host, datos;
	private int puerto;
	private DatagramSocket cliente;
	private DatagramPacket paquete;

	public Cliente ()
	{
		conecta ();
		juega ();
	}

	public void conecta ()
	{
		try
		{
			//Flujo orientado a caracter asociado a la entrada por teclado para que el usuario ingrese una cadena
			br = new BufferedReader (new InputStreamReader (System.in));
			sc = new Scanner (System.in);
			System.out.println ("\n\nIntroduce la direccion IP: ");
			host = sc.next ();
			System.out.println ("\n\nIntroduce el puerto: ");
			puerto = sc.nextInt ();
			try
			{
				dst = InetAddress.getByName (host);					//Resolvemos la direccion IP
			}catch (UnknownHostException u)
			{
				System.err.println ("Direccion invalida.\n");
				System.exit (1);
			}
			//Creamos el socket sin asociarlo a ningun puerto, por lo tanto se le asociará automáticamente
			cliente = new DatagramSocket ();
			System.out.println ("Escribe una cadena. <Enter> para enviar\n\n\"Salir\" Para terminar\n\n");
		}catch (Exception e)
		{
			e.printStackTrace ();
		}
	}

	public void juega ()
	{
		try
		{
			for (;;)
			{
				String datos = br.readLine ();
				if (datos.compareToIgnoreCase ("Salir") == 0)			//Si el usuario teclea la palabra "Salir"
				{
					byte [] b = datos.getBytes ();						//Convertimos cadena a un arreglo de bytes
					//Creamos el paquete de datagrama con el arreglo de bytes, la longitud, la direccion y el puerto
					paquete = new DatagramPacket (b, b.length, dst, puerto);
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
							paquete = new DatagramPacket (b2, n, dst, puerto);
							cliente.send (paquete);									//Enviamos el paquete
							b2 = new byte [65535];
						}
						bais.close ();												//Cerramos el arreglo orientado a bytes
					}else
					{
						paquete = new DatagramPacket (b, b.length, dst, puerto);
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

	public void clear ()
	{
		System.out.print("\033[H\033[2J");
    	System.out.flush();
	}

	public static void main(String[] args)
	{
		new Cliente ();
	}
}