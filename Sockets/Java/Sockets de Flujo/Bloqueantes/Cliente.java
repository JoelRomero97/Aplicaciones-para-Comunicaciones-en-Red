import java.io.*;
import java.net.*;

public class Cliente
{
	private BufferedReader buferLectura;
	private DataOutputStream buferSalida;
	private Socket cliente;
	private String host, mensajeServ, mensaje = " ";
	private int puerto = 1234;

	public Cliente ()
	{
		try
		{
			buferLectura = new BufferedReader (new InputStreamReader (System.in));
			System.out.println ();
			System.out.print ("\nEscribe la direccion del host:\t");
			host = buferLectura.readLine ();
			cliente = new Socket (host, puerto);
			System.out.println ("Conexion establecida.");

			while (!mensaje.equals ("Salir"))
			{
				buferLectura = new BufferedReader (new InputStreamReader (System.in));
				System.out.println ("\n\nEscribe un mensaje: ");
				mensaje = buferLectura.readLine ();
				buferSalida = new DataOutputStream (cliente.getOutputStream ());
				buferSalida.writeUTF (mensaje);
				buferLectura = new BufferedReader (new InputStreamReader (cliente.getInputStream ()));
				mensajeServ = buferLectura.readLine ();
				System.out.println ("\n\nMensaje recibido: " + mensajeServ);
			}
			buferSalida.close ();
			buferLectura.close ();
			cliente.close ();
		}catch (Exception ex)
		{
			ex.printStackTrace ();
		}
	}

	public static void main(String[] args)
	{
		new Cliente ();
	}
}