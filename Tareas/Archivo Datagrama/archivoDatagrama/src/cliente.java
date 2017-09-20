
import java.io.*;
import java.net.*;
import java.util.Scanner;
import javax.swing.JFileChooser;
//UDP soporta datagramas de 65526 bytes

//armar tramas de 60000 bytes
public class cliente {

    static String host;
    static final int bufferDatagrama = 60000;
    static final int puerto = 5001;
    static final int bufferData = 65535;
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        //System.out.println("Escribe la direccion del servidor");
        //host = scan.nextLine();
        host = "127.0.0.1";
        
        JFileChooser jfc = new JFileChooser();
        jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        jfc.setMultiSelectionEnabled(true);
        
        if(jfc.showOpenDialog(null)==JFileChooser.APPROVE_OPTION){
            File[] archivos = jfc.getSelectedFiles();
            
            //if(archivos.length > 0){
                for(File file:archivos){
                    if(file.isFile()){
                        enviarArchivo(file,"archivos\\");
                    }else if(file.isDirectory()){
                        enviarCarpeta(file, "archivos\\"+file.getName());
                    }

                }
            /*}else{
                File archivo = jfc.getSelectedFile();
                if(archivo.isFile()){
                        enviarArchivo(archivo,"archivos\\");
                    }else if(archivo.isDirectory()){
                        enviarCarpeta(archivo, "archivos\\");
                    }

            }*/
          
            
            
        }
        
        
    }
    
    public static void enviarArchivo(File archivo,String ruta){
        
        enviarDatos env = new enviarDatos();
          try
            {
                env.enviar(archivo, ruta);
                 
            }catch(Exception ex){
                System.out.println(ex.toString());
            }
        
    
    }
    
    public static void enviarCarpeta(File carpeta, String route){
        enviarDatos env = new enviarDatos();
        String nom = carpeta.getName();
        
        File[] archivoc= carpeta.listFiles();
        int y;
        
        for(y=0;y<archivoc.length;y++){
            if(archivoc[y].isDirectory()){     
                enviarCarpeta(archivoc[y],route+"\\"+archivoc[y].getName());
            }else{
                env.enviar(archivoc[y],route+"\\");    
            }
        }
        
        
        
        
    }    
}
