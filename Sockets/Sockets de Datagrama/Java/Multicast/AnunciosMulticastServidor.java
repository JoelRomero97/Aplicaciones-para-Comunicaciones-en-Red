import java.net.*;
import java.io.*;

public class AnunciosMulticastServidor
{
	public AnunciosMulticastServidor ()
	{
		try
		{
			String dir = "231.1.1.1";											//Direccion clase D (Multicast)
			int puertoCliente = 8001;											//Puerto donde van a escuchar clientes
			int puertoServidor = 8000;
			MulticastSocket servidor = new MulticastSocket (puertoServidor);	//Puerto donde va a escuchar el servidor
			servidor.setReuseAddress (true);
			servidor.setTimeToLive (255);
			InetAddress grupo = null;
			try
			{
				grupo = InetAddress.getByName (dir);							//Valido que sea una direccion valida
			}catch (UnknownHostException u)
			{
				System.out.println ("Direccion de grupo invalida");
				System.exit (1);
			}
			servidor.joinGroup (grupo);											//Me uno al grupo multicast
			System.out.println ("Servicio iniciado y unido al grupo " + dir);
			System.out.println ("Comienza envio de anuncios...");
			String anuncio = "Anuncio";
			for (;;)
			{
				byte [] b = anuncio.getBytes ();
				DatagramPacket paquete = new DatagramPacket (b, b.length, grupo, puertoCliente);
				servidor.send (paquete);										//Envio el mensaje al puerto donde escuchan los clientes
				try
				{
					Thread.sleep (5000);
				}catch (InterruptedException ie)
				{
					//
				}
			}
		}catch (Exception e)
		{
			e.printStackTrace ();
		}
	}

	public static void main(String[] args)
	{
		new AnunciosMulticastServidor ();
	}
}