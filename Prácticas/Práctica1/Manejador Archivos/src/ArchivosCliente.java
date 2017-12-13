/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.*;
import java.net.*;
public class ArchivosCliente 
{
    private DataOutputStream buferSalida;
    private DataInputStream buferEntrada;
    private Socket cliente;
    private String host = "127.0.0.1", nombreArch, ruta;
    private int i = 0, puerto = 9876, porcentaje, n;
    private long enviados, tamanioArchivo;
    

    public ArchivosCliente (File [] archivo)
    {
        try
        {
            while (i < archivo.length)
            {
                nombreArch = (archivo [i]).getName ();
                ruta = (archivo [i]).getAbsolutePath ();
                tamanioArchivo = (archivo [i]).length ();
                if ((archivo [i]).isFile ())
                    envia (archivo [i], nombreArch, ruta, tamanioArchivo, 'a', "");
                else
                    envia (archivo [i], nombreArch, ruta, tamanioArchivo, 'd', nombreArch);
                i ++;
            }
            System.out.println ("\n\nArchivo enviado correctamente.\n");
            buferSalida.close ();
            buferEntrada.close ();
            cliente.close ();
        }catch (Exception ex)
        {
        ex.printStackTrace ();
        }
    }

    public void envia (File archivo, String nombreArch, String ruta, long tamanioArchivo, char tipo, String DirPadre)
    {
        try
        {
            cliente = new Socket (host, puerto);
            buferSalida = new DataOutputStream (cliente.getOutputStream ());
            buferSalida.writeChar (tipo);
            buferSalida.flush ();
            buferSalida.writeUTF (nombreArch);
            buferSalida.flush ();
            buferSalida.writeUTF (DirPadre);
            buferSalida.flush ();
            if (tipo == 'a')														//Si se va a copiar un archivo
            {
                System.out.println ("\n\nInicia la transferencia del archivo " + nombreArch);
                enviados = 0;
                porcentaje = 0;
                buferEntrada = new DataInputStream (new FileInputStream (ruta));
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
            }else
            {
                System.out.println ("\n\nInicia la transferencia del directorio " + nombreArch);
                File [] directorio = archivo.listFiles ();
                int i = 0;
                while (i < directorio.length)
                {
                    nombreArch = (directorio [i]).getName ();
                    ruta = (directorio [i]).getAbsolutePath ();
                    tamanioArchivo = (directorio [i]).length ();
                    if ((directorio [i]).isFile ())
                    {
                        //System.out.println("ENVIANDO COMO RUTA  "+ DirPadre);
                        envia (directorio [i], nombreArch, ruta, tamanioArchivo, 'a', DirPadre);
                    }else
                    {
                        //System.out.println("ENVIANDO COMO RUTA  "+ DirPadre+"\\"+nombreArch);
                        envia (directorio [i], nombreArch, ruta, tamanioArchivo, 'd', DirPadre+"\\"+nombreArch);
                    }
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
}
