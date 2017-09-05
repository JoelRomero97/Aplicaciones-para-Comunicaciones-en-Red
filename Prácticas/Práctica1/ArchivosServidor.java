import java.net.*;
import java.io.*;

////////////////////////////////////////////////////////////////////////////////////////
////							ArchivosServidor.java 								////
////																				////
////																				////
//// Servidor que se encarga de recibir un archivo seleccionado por un cliente a 	////
//// partir de un JFileChooser, mostrando el porcentaje de envío y el nombre del	////
//// archivo en pantalla.															////
//// Instrucciones: Compilar y ejecutar primero ArchivosServidor.java				////
////				Posteriormente, compilar y ejecutar ArchivoCliente.java			////
////																				////
////																				////
//// Autor: Romero Gamarra Joel Mauricio											////
////////////////////////////////////////////////////////////////////////////////////////

public class ArchivosServidor
{
	private ServerSocket servidor;
	private Socket cliente;
	private int puerto = 9876, n = 0, porcentaje = 0;
	private DataInputStream dis;
	private DataOutputStream dos;
	private String nombre, nombreDirectorio;
	private long tam, r;
	private char tipo;

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
				recibe ();
			}
		}catch (Exception ex)
		{
			ex.printStackTrace ();
		}
	}

	public void recibe ()
	{
		r = 0;
		try
		{
			dis = new DataInputStream (cliente.getInputStream ());
			tipo = dis.readChar ();
			nombre = dis.readUTF ();
			if (tipo == 'a')
			{
				tam = dis.readLong ();
				System.out.println ("Recibiendo archivo " + nombre);
				dos = new DataOutputStream (new FileOutputStream (nombre));
				while (r < tam)
				{
					byte [] b = new byte [1500];
					n = dis.read (b);
					dos.write (b, 0, n);
					dos.flush ();
					r += n;
					porcentaje = (int) ((r * 100) / tam);
					System.out.print ("\rRecibido el " + porcentaje + "%");
				}
				System.out.println ("Archivo recibido...");
			}else
			{
				System.out.println ("Recibiendo directorio " + nombre);
				File directorio = new File (nombre);
				boolean bol = directorio.mkdir ();
				dos = new DataOutputStream (new FileOutputStream (nombre));
				recibe ();
			}
			dos.close ();
			dis.close ();
			cliente.close ();
		}catch (Exception e)
		{
			e.printStackTrace ();
		}
	}

	public static void main(String[] args)
	{
		new ArchivosServidor ();
	}
}