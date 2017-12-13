/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Natalia
 */
import java.net.*;
import java.io.*;

public class ArchivosServidor
{
	private ServerSocket servidor;
	private Socket cliente;
	private int puerto = 9876, n = 0, porcentaje = 0;
	private DataInputStream dis;
	private DataOutputStream dos;
	private String nombre,DirPadre;
	private long tam, r;
	private char tipo;
    String rutaS="C:\\Users\\Joel_\\Desktop\\ESCOM\\Aplicaciones para Comunicaciones de Red\\Prácticas\\Práctica1\\Manejador Archivos\\Servidor";
    String carpeta="";

	public ArchivosServidor ()
	{
		try
		{
			servidor = new ServerSocket (puerto);
                        servidor.setReuseAddress(true);
			System.out.println ("Servicio iniciado... esperando clientes.\n\n");
			for (;;)
			{
				cliente = servidor.accept ();
				System.out.println ("Cliente conectado desde -> " + cliente.getInetAddress () + ":" + cliente.getPort());
				recibe (rutaS);
			}
		}catch (Exception ex)
		{
			ex.printStackTrace ();
		}
	}

	public void recibe (String ruta)
	{
		r = 0;
		try
		{
			dis = new DataInputStream (cliente.getInputStream ());
			tipo = dis.readChar ();
			nombre = dis.readUTF ();
                        DirPadre = dis.readUTF ();
                        
			if (tipo == 'a')
			{
                            
				tam = dis.readLong ();
				if(DirPadre=="")
				{
                    System.out.println ("Recibiendo archivo " + nombre +"        RUTA DESTINO "+ruta+DirPadre+nombre);
                    dos = new DataOutputStream (new FileOutputStream (ruta+DirPadre+nombre));
                }else
                {
                    System.out.println ("Recibiendo archivo " + nombre +"        RUTA DESTINO "+ruta+DirPadre+"\\"+nombre);
                    dos = new DataOutputStream (new FileOutputStream (ruta+DirPadre+"\\"+nombre));
                }
				while (r < tam)
				{
					byte [] b = new byte [1500];
					n = dis.read (b);
					dos.write (b, 0, n);
					dos.flush ();
					r += n;
					porcentaje = (int) ((r * 100) / tam);
					System.out.print ("\rRecibido el " + porcentaje + "%");
				}
				System.out.println ("\nArchivo recibido...\n\n");
                        
			}else
			{
				System.out.println ("Recibiendo directorio " + nombre + "        RUTA DESTINO "+ruta+DirPadre);
				boolean bol = new File (ruta+DirPadre+"\\").mkdir ();
                System.out.println ("Directorio creado...\n\n");
			}
			dos.close ();
			dis.close ();
			cliente.close ();
		}catch (Exception e)
		{
			e.printStackTrace ();
		}
	}
	public static void main(String[] args)
	{
		new ArchivosServidor ();
	}
        
}