import java.io.*;
import java.net.*;

public class EcoCliente
{
	private BufferedReader buferLectura;
	private DataOutputStream buferSalida;
	private Socket cliente;
	private String host, mensajeServ, mensaje = " ";
	private int puerto = 1234;

	public EcoCliente ()
	{
		try
		{
			buferLectura = new BufferedReader (new InputStreamReader (System.in));
			System.out.println ();
			System.out.print ("\nEscribe la direccion del host:\t");
			host = buferLectura.readLine ();
			while (true)
			{
				cliente = new Socket (host, puerto);
				//System.out.println ("Conexion establecida.");
				//buferSalida = new DataOutputStream (cliente.getOutputStream ());
				//while (true)
				//{
				buferLectura = new BufferedReader (new InputStreamReader (System.in));
				System.out.println ("\n\nEscribe un mensaje: ");
				mensaje = buferLectura.readLine ();
				if(mensaje.equals("Salir"))
				{
					cliente.close();
					buferSalida.close ();
					buferLectura.close ();
					System.exit(0);
				}
				buferSalida = new DataOutputStream (cliente.getOutputStream ());
				buferSalida.writeUTF (mensaje);
				buferLectura = new BufferedReader (new InputStreamReader (cliente.getInputStream ()));
				mensajeServ = buferLectura.readLine ();
				System.out.println ("\n\nMensaje recibido: " + mensajeServ);
				//buferSalida.flush();
				//buferSalida.close();
				//buferSalida.close ();
				//buferLectura.close ();
				//cliente.close ();
			}
			
			/*buferSalida.close ();
			buferLectura.close ();
			cliente.close ();*/
		}catch (Exception ex)
		{
			ex.printStackTrace ();
		}
	}

	public static void main(String[] args)
	{
		new EcoCliente ();
	}
}