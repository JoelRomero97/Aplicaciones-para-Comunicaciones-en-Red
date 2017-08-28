import java.io.*;
import java.net.*;

public class ObjetosServidor
{
	public ObjetosServidor ()
	{
		try
		{
			int puerto = 7000;
			ServerSocket servidor = new ServerSocket (puerto);
			servidor.setReuseAddress (true);
			System.out.println ("Servicio iniciado, esperando cliente...");
			for (;;)
			{
				Socket cliente = servidor.accept ();
				System.out.println ("Cliente conectado desde " + cliente.getInetAddress () + ":" + cliente.getPort ());
				ObjectOutputStream oos = new ObjectOutputStream (cliente.getOutputStream ());
				ObjectInputStream ois = new ObjectInputStream (cliente.getInputStream ());
				System.out.println ("Preparando para recibir un objeto...\n");

				//Instanciamos un objeto de la clase X para recibirlo
				X objeto1 = (X) ois.readObject ();
				System.out.println ("Objeto recibido con los siguientes datos\nNombre: " + objeto1.getNombre ());
				System.out.println ("Apellido: " + objeto1.getApellido () + "\nEdad: " + objeto1.getEdad ());
				System.out.println ("Boleta: " + objeto1.getBoleta ());

				//Instanciamos un objeto de la clase X para enviarlo
				X objeto2 = new X ("Luisa", "Rosales", 21, 2016631234);
				System.out.println ("Enviando objeto con los siguientes datos\nNombre: " + objeto2.getNombre ());
				System.out.println ("Apellido: " + objeto2.getApellido () + "\nEdad: " + objeto2.getEdad ());
				System.out.println ("Boleta: " + objeto2.getBoleta ());
				oos.writeObject (objeto2);
				oos.flush ();
				System.out.println ("Termina conexion con cliente...\n\n");
				ois.close ();
				oos.close ();
				cliente.close ();
			}
		}catch (Exception e)
		{
			e.printStackTrace ();
		}
	}

	public static void main(String[] args)
	{
		new ObjetosServidor ();
	}
}