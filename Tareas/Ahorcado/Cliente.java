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
	private int puerto, fallos;
	private DatagramSocket cliente;
	private DatagramPacket paquete;
	private byte [] b;

	public void clear ()
	{
		System.out.print("\033[H\033[2J");
    	System.out.flush();
	}

	public Cliente ()
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
			//Creamos el socket sin asociarlo a ningun puerto, por lo tanto se le asociará
			cliente = new DatagramSocket ();
			clear ();
			System.out.println ("\t\t\t\tAhorcado\n\n");
			System.out.println ("Selecciona la dificultad\n\n");
			System.out.println ("1. Fácil\n2. Media\n3. Difícil\n");
			datos = br.readLine ();
			b = datos.getBytes ();
			paquete = new DatagramPacket (b, b.length, dst, puerto);
			cliente.send (paquete);										//Enviamos el paquete

			//Recibimos la palabra
			paquete = new DatagramPacket (new byte [11], 11);
			cliente.receive (paquete);
			datos = new String (paquete.getData (), 0, paquete.getLength ());
			//System.out.println ("Palabra recibida: " + datos);

			clear ();
			fallos = 6;

			//Comienza el juego
			for (;;)
			{
				System.out.println ("\n\nIntentos Restantes: " + fallos);
				datos = br.readLine ();
				if (datos.compareToIgnoreCase ("Salir") == 0)			//Si el usuario teclea la palabra "Salir"
				{
					b = datos.getBytes ();						//Convertimos cadena a un arreglo de bytes
					
					//Creamos el paquete de datagrama con el arreglo de bytes, la longitud, la direccion y el puerto
					paquete = new DatagramPacket (b, b.length, dst, puerto);
					cliente.send (paquete);								//Enviamos el paquete
					break;
				}else
				{
					b = datos.getBytes ();
					paquete = new DatagramPacket (b, b.length, dst, puerto);
					cliente.send (paquete);										//Enviamos el paquete
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