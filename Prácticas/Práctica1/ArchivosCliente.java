import java.io.*;
import java.net.*;
import javax.swing.JFileChooser;

////////////////////////////////////////////////////////////////////////////////////////
////							ArchivosCliente.java 								////
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

public class ArchivosCliente
{
	private JFileChooser opcionArch;
	private DataOutputStream buferSalida;
	private DataInputStream buferEntrada;
	private Socket cliente;
	private String host = "127.0.0.1", nombreArch, ruta;
	private int i = 0, puerto = 9876, porcentaje, n;
	private long enviados, tamanioArchivo;

	public ArchivosCliente ()
	{
		try
		{
			opcionArch = new JFileChooser ();
			opcionArch.setMultiSelectionEnabled (true);							//Para transferir varios archivos
			int opcionSelec = opcionArch.showOpenDialog (null);
			if (opcionSelec == JFileChooser.APPROVE_OPTION)
			{
				File [] archivo = opcionArch.getSelectedFiles ();
				cliente = new Socket (host, puerto);
				while (i < archivo.length)
				{
					enviados = 0;
					nombreArch = (archivo [i]).getName ();
					ruta = (archivo [i]).getAbsolutePath ();
					tamanioArchivo = (archivo [i]).length ();
					System.out.println ("\n\nInicia la transferencia del archivo " + ruta + "\n");
					porcentaje = 0;
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
					i ++;
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
		new ArchivosCliente ();
	}
}