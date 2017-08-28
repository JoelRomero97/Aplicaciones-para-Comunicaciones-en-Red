import java.io.*;
import java.net.*;
import javax.swing.JFileChooser;

////////////////////////////////////////////////////////////////////////////////////////
////							ArchivoCliente.java 								////
////																				////
////																				////
//// Cliente que va a enviar algún archivo seleccionado por el cliente mediante un	////
//// JFileChooser hacia el servidor por conducto de un socket cliente de flujo 		////
//// bloqueante, indicando la ruta del archivo y el porcentaje del archivo enviado 	////
//// en bloques de 1,500 bytes ya que es el tamaño del MTU.							////
////																				////
////																				////
//// Autor: Romero Gamarra Joel Mauricio											////
////////////////////////////////////////////////////////////////////////////////////////

public class ArchivoCliente
{
	private JFileChooser opcionArch;
	private File archivo;
	private DataOutputStream buferSalida;
	private DataInputStream buferEntrada;
	private Socket cliente;
	private String host = "127.0.0.1", nombreArch, ruta;
	private int puerto = 9876;
	private long tamanioArchivo, enviados;

	public ArchivoCliente ()
	{
		try
		{
			opcionArch = new JFileChooser ();
			//opcionArch.setMultiSelectionEnabled (true);							Para transferir varios archivos
			int opcionSelec = opcionArch.showOpenDialog (null);
			if (opcionSelec == JFileChooser.APPROVE_OPTION)
			{
				archivo = opcionArch.getSelectedFile ();
				cliente = new Socket (host, puerto);
				nombreArch = archivo.getName ();
				ruta = archivo.getAbsolutePath ();
				tamanioArchivo = archivo.length ();
				System.out.println ("\n\nInicia la transferencia del archivo " + ruta + "\n");
				int porcentaje = 0, n;
				buferSalida = new DataOutputStream (cliente.getOutputStream ());
				buferEntrada = new DataInputStream (new FileInputStream (ruta));
				buferSalida.writeUTF (nombreArch);
				buferSalida.flush ();
				buferSalida.writeLong (tamanioArchivo);
				buferSalida.flush ();
				while (enviados < tamanioArchivo)
				{
					byte [] b = new byte [1500];
					n = buferEntrada.read (b);
					buferSalida.write (b, 0, n);							//Escribimos desde el byte 0 hasta el byte n
					buferSalida.flush ();
					enviados += n;
					porcentaje = (int)((enviados * 100) / tamanioArchivo);
					System.out.print ("\rEnviados: " + porcentaje + " %");
				}
				System.out.println ("\n\nArchivo enviado correctamente.\n");
			}
			buferSalida.close ();
			buferEntrada.close ();
			cliente.close ();
		}catch (Exception ex)
		{
			ex.printStackTrace ();
		}
	}

	public static void main(String[] args)
	{
		new ArchivoCliente ();
	}
}