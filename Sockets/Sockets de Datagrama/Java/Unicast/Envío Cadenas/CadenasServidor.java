import java.net.*;
import java.io.*;

public class CadenasServidor
{
	public CadenasServidor ()
	{
		try
		{
			DatagramSocket servidor = new DatagramSocket (7777);
			System.out.println ("Servicio iniciado...Esperando datagramas.\n");
			for (;;)
			{
				DatagramPacket paquete = new DatagramPacket (new byte [65535], 65535);
				servidor.receive (paquete);
				System.out.print ("\nDatagrama recibido desde -> " + paquete.getAddress ().getHostAddress () + ":");
				System.out.println (paquete.getPort () + "\n\nCon los datos: " + new String (paquete.getData (), 0, paquete.getLength ()));
			}
		}catch (Exception ex)
		{
			ex.printStackTrace ();
		}
	}

	public static void main(String[] args)
	{
		new CadenasServidor ();
	}
}