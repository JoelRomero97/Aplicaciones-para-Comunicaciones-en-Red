/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.awt.image.BufferedImage;

/**
 *
 * @author JCVELMON
 */
public class Servidor extends Conexion {

    DataInputStream input;
    BufferedInputStream bis;
    BufferedOutputStream bos;
    BufferedImage img;
    int in;
    byte[] byteArray;
    
    public Servidor() throws IOException{super("servidor");} 
      public void startServer()//Método para iniciar el servidor
    {
        try
        {
            int x=1;
            System.out.println("Esperando conexion..."); //Esperando conexión
            cs = ss.accept(); //Accept comienza el socket y espera una conexión desde un cliente
            //System.out.println("Juego Iniciado");
            System.out.println ("Cliente conectado desde -> " + cs.getInetAddress () + ":" + cs.getPort());
            

            ss.close();//Se finaliza la conexión con el cliente
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}
    
