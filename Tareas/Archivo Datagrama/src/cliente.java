
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
        
        if(jfc.showOpenDialog(null)==JFileChooser.APPROVE_OPTION){
            File[] archivos = jfc.getSelectedFiles();
            
            if(archivos.length > 0){
                for(File file:archivos){
                    if(file.isFile()){
                        enviarArchivo(file);
                    }else if(file.isDirectory()){
                        enviarCarpeta(file, file.getAbsolutePath());
                    }

                }
            }else{
                File archivo = jfc.getSelectedFile();
                if(archivo.isFile()){
                        enviarArchivo(archivo);
                    }else if(archivo.isDirectory()){
                        enviarCarpeta(archivo, archivo.getAbsolutePath());
                    }

            }
          
            
            
        }
        
        
    }
    
    public static void enviarArchivo(File archivo){
          try
            {
                DatagramSocket cl = new DatagramSocket();
                //DatagramSocket regreso = new DatagramSocket(puertoRegreso);
                DataInputStream dis = new DataInputStream(new FileInputStream(archivo));
                long tam = dis.available();
                     
                socDatagrama soc = new socDatagrama("127.0.0.1",puerto);
                long enviados=0;
                int i=0,n=0;
                
                while(enviados < tam){
                    //enviar la primera trama que contiene información de control
                    
                    
                    //preparar los datos a mandar
                    byte[] buf = new byte[bufferDatagrama];
                    n = dis.read(buf);
                    datosPaquetes d;
                    
                    d = new datosPaquetes(++i,buf,archivo.getName(),buf.length,archivo.getAbsolutePath());
                    int cont=0;
                    do{
                        if(cont >1){
                            System.out.println("Reenvio de datagrama" + i);
                        }
                        soc.enviaDatagrama(d, puerto);
                        System.out.println("Trama: "+i+" enviada");
                        cont+=1;
                        try{
                            Thread.sleep(100);
                        }catch(InterruptedException t){
                            System.out.println(t.toString());
                        }
                        
                    }while(soc.leeRespuesta(puerto));
                    
                    enviados+=n;
                        
                    
                }
                
                datosPaquetes df = new datosPaquetes(-1,null,"fin de trama",tam,"fin de trama");
                soc.enviaDatagrama(df, puerto);
                
                System.out.println("Archivo enviado");
                
                
                
                
            }catch(Exception ex){
                System.out.println(ex.toString());
            }
        
    
    }
    
    public static void enviarCarpeta(File carpeta, String route){
    
    }    
}
