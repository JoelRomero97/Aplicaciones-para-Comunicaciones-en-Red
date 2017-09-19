/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cliente;

import Server.Conexion;
import gui.PlayGame;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.FileChannel;
import javax.imageio.ImageIO;

/**
 *
 * @author JCVELMON
 */
public class Cliente extends Conexion {
    
    protected DataOutputStream output;
    protected BufferedInputStream bis;
    protected BufferedOutputStream bos;
    //protected ServerSocket server;
    //protected Socket connection;
    protected byte[] receivedData;
    protected int in;
    protected  String file;
    FileInputStream fis;
    FileOutputStream fos;
 
   public Cliente() throws IOException{super("cliente");} 

    public void startClient() //Método para iniciar el cliente
    {
        try
        {            
            int x=1;
            while(x <= 10)
            {
                //BufferedImage bufferedImage = ImageIO.read(new File("C://Users//JCVELMON//Desktop//mem//src//imagenes/"+x+".jpg"));
                //ImageIO.write(bufferedImage, "jpg", cs.getOutputStream());
                //cs.getOutputStream().flush();
                fis = new FileInputStream("C:/Users/Joel_/Desktop/ESCOM/Aplicaciones para Comunicaciones de Red/Exámenes/Primer Parcial/Memorama/src/imagenes"+x+".jpg"); //inFile -> Archivo a copiar
                fos = new FileOutputStream("C:/Users/Joel_/Desktop/ESCOM/Aplicaciones para Comunicaciones de Red/Exámenes/Primer Parcial/Memorama/src/imagenesCliente"+x+".jpg"); //outFile -> Copia del archivo
                FileChannel inChannel = fis.getChannel(); 
                FileChannel outChannel = fos.getChannel(); 
                inChannel.transferTo(0, inChannel.size(), outChannel); 
                x++;
            }
            fis.close(); 
            fos.close();

            //Flujo de datos hacia el servidor
            //salidaServidor = new DataOutputStream(cs.getOutputStream());
            System.out.println("Comienza el juego");
            PlayGame game = new PlayGame();
            game.setVisible(true);
            bos.close();
            bis.close();
            cs.close();//Fin de la conexión

        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}
   


