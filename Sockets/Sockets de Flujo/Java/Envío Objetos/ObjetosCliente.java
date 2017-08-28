import java.io.*;
import java.net.*;

public class ObjetosCliente
{
	public ObjetosCliente ()
	{
		try
		{
			String host = "127.0.0.1";
			int puerto = 7000;
			Socket cliente = new Socket (host, puerto);
			System.out.println ("Conexion establecida, preparado para enviar un objeto.\n");
			X objeto1 = new X ("Pepe", "Arenas", 20, 2016630139);												//Creamos objeto de la clase X
			ObjectOutputStream oos = new ObjectOutputStream (cliente.getOutputStream ());
			System.out.println ("Se enviara un objeto con los siguientes datos\nNombre: " + objeto1.getNombre ());
			System.out.println ("Apellido: " + objeto1.getApellido () + "\nEdad: " + objeto1.getEdad () + "\nBoleta: " + objeto1.getBoleta ());
			oos.writeObject (objeto1);
			oos.flush ();
			System.out.println ("Preparandose para recibir un objeto");
			ObjectInputStream ois = new ObjectInputStream (cliente.getInputStream ());					//Asocio flujo de lectura al socket
			X objeto2 = (X) ois.readObject ();																//Serializamos el objeto X
			System.out.println ("Objeto recibido con los datos\nNombre: " + objeto2.getNombre ());
			System.out.println ("Apellido: " + objeto2.getApellido () + "\nEdad: " + objeto2.getEdad () + "\nBoleta: " + objeto2.getBoleta ());
			System.out.println ("\n\nTermina aplicacion...");
			ois.close ();
			oos.close ();
			cliente.close ();
		}catch (Exception e)
		{
			e.printStackTrace ();
		}
	}

	public static void main(String[] args)
	{
		new ObjetosCliente ();
	}
}