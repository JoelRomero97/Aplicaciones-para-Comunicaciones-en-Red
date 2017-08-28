import java.net.*;
import java.io.*;

////////////////////////////////////////////////////////////////////////////////////////
////							ArchivoServidor.java 								////
////																				////
////																				////
//// Servidor que se encarga de recibir un archivo seleccionado por un cliente a 	////
//// partir de un JFileChooser, mostrando el porcentaje de envío y el nombre del	////
//// archivo en pantalla.															////
//// Instrucciones: Compilar y ejecutar primero ArchivoServidor.java				////
////				Posteriormente, compilar y ejecutar ArchivoCliente.java			////
////																				////
////																				////
//// Autor: Romero Gamarra Joel Mauricio											////
////////////////////////////////////////////////////////////////////////////////////////

public class ArchivoServidor
{
	private ServerSocket servidor;
	private Socket cliente;
	private int puerto = 9876;
	private DataInputStream dis;
	private DataOutputStream dos;
	private String nombre;
	private byte [] b;

	public ArchivosServidor ()
	{
		try
		{
			servidor = new ServerSocket (puerto);
			System.out.println ("Servicio iniciado... esperando clientes.\n\n");
			for (;;)
			{
				cliente = servidor.accept ();
				System.out.println ("Cliente conectado desde -> " + cliente.getInetAddress () + ":" + cliente.getPort());
				dis = new DataInputStream (cliente.getInputStream ());
				nombre = dis.readUTF ();
				System.out.println ("Recibiendo archivo " + nombre);
				dos = new DataOutputStream (new FileOutputStream (nombre));
				long r = 0, tam;
				int n = 0, porcentaje;
				tam = dis.readLong ();
				while (r < tam)
				{
					b = new byte [1500];
					n = dis.read (b);
					dos.write (b, 0, n);
					dos.flush ();
					r += n;
					porcentaje = (int) ((r * 100) / tam);
					System.out.print ("\rRecibido el " + porcentaje + "%");
				}
				System.out.println ("Archivo recibido...");
				dos.close ();
				dis.close ();
				cliente.close ();
			}
		}catch (Exception ex)
		{
			ex.printStackTrace ();
		}
	}

	public static void main(String[] args)
	{
		new ArchivoServidor ();
	}
}