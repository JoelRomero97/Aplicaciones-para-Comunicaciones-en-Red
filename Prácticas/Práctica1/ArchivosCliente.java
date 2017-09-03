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
	private File [] archivo;

	public ArchivosCliente ()
	{
		try
		{
			opcionArch = new JFileChooser ();
			opcionArch.setMultiSelectionEnabled (true);							//Para transferir varios archivos
			opcionArch.setFileSelectionMode (JFileChooser.FILES_AND_DIRECTORIES);
			int opcionSelec = opcionArch.showOpenDialog (null);
			if (opcionSelec == JFileChooser.APPROVE_OPTION)
			{
				archivo = opcionArch.getSelectedFiles ();
				while (i < archivo.length)
				{
					nombreArch = (archivo [i]).getName ();
					ruta = (archivo [i]).getAbsolutePath ();
					tamanioArchivo = (archivo [i]).length ();
					if ((archivo [i]).isFile ())
						envia (archivo [i], nombreArch, ruta, tamanioArchivo, 'a');
					else
						envia (archivo [i], nombreArch, ruta, tamanioArchivo, 'd');
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

	public void envia (File archivo, String nombreArch, String ruta, long tamanioArchivo, char tipo)
	{
		try
		{
			cliente = new Socket (host, puerto);
			buferSalida = new DataOutputStream (cliente.getOutputStream ());
			buferSalida.writeChar (tipo);
			buferSalida.flush ();
			buferSalida.writeUTF (nombreArch);
			buferSalida.flush ();
			if (tipo == 'a')														//Si se va a copiar un archivo
			{
				System.out.println ("\n\nInicia la transferencia del archivo " + nombreArch + "\n");
				enviados = 0;
				porcentaje = 0;
				buferEntrada = new DataInputStream (new FileInputStream (ruta));
				buferSalida.writeLong (tamanioArchivo);
				buferSalida.flush ();
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
			}else
			{
				System.out.println ("\n\nInicia la transferencia del directorio " + nombreArch + "\n");
				File [] directorio = archivo.listFiles ();
				int i = 0;
				while (i < directorio.length)
				{
					nombreArch = (directorio [i]).getName ();
					ruta = (directorio [i]).getAbsolutePath ();
					tamanioArchivo = (directorio [i]).length ();
					if ((directorio [i]).isFile ())
						envia (directorio [i], nombreArch, ruta, tamanioArchivo, 'a');
					else
						envia (directorio [i], nombreArch, ruta, tamanioArchivo, 'd');
					i ++;
				}
			}
			buferSalida.close ();
			buferEntrada.close ();
			cliente.close ();
		}catch (Exception e)
		{
			e.printStackTrace ();
		}
	}

	public static void main(String[] args)
	{
		new ArchivosCliente ();
	}
}