import java.io.*;
import java.net.*;

public class EcoServidor
{
	private ServerSocket servidor;									//Socket servidor
	private Socket cliente;											//Socket cliente que se conecta
	private DataInputStream lecturaCliente;							//Para leer lo que envíe el cliente
	private String ecoServidor = " ";								//Cadena que enviará el cliente
	private int puerto = 1234;										//Puerto que se utilizará
	private DataOutputStream datosCualquierTipo;
	//private InetSocketAdress direccion;

	public EcoServidor ()
	{
		try
		{
			servidor = new ServerSocket (puerto);									//Creamos un socket servidor en el puerto 1234
			//direccion = new InetSocketAdress ("10.1.1.1");
			//servidor = new ServerSocket (puerto, 5, direccion);					//Para trabajar solo con la NIC con direccion 10.1.1.1
			while (true)
			{
				cliente = servidor.accept ();
				System.out.println ("\nConexion establecida desde -> " + cliente.getInetAddress () + " : " + cliente.getPort ());
				datosCualquierTipo = new DataOutputStream (cliente.getOutputStream ());
				lecturaCliente = new DataInputStream (cliente.getInputStream ());
				ecoServidor = lecturaCliente.readUTF ();
				if(ecoServidor.equals("Salir"))
				{
					cliente.close();
					datosCualquierTipo.close ();
					System.exit(0);
				}
				System.out.println ("Mensaje recibido en el servidor: " + ecoServidor);
				datosCualquierTipo.writeUTF (ecoServidor + " eco");
				datosCualquierTipo.flush ();
				datosCualquierTipo.close ();
			}
			//cliente.close ();
		}catch (Exception ex)
		{
			ex.printStackTrace ();
		}
	}

	public static void main(String[] args)
	{
		new EcoServidor ();
	}
}