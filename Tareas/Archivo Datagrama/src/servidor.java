import java.io.*;
import java.net.*;
public class servidor {

    static final int bufferData = 65535;
    static final int puerto = 5001;
    public static void main(String[] args) {
        int vueltas = 0;
        
        String nombre="";
        try{
            
            socDatagrama s = new socDatagrama(puerto);
            //DatagramSocket ds = new DatagramSocket(puerto);
            OutputStream out = null;
            int numTrama = 0;
            while(true){

                datosPaquetes datos = s.recibeDatagrama();
                if(s.revisarDatagrama(datos)){
                    
                    System.out.println("Trama: "+datos.getNumPaquete() + "recibida correctamente");
                    if(datos.getNumPaquete()==-1){
                        System.out.println("Archivo terminado de recibir");
                        vueltas=0;
                        try{
                            out.close();
                        }catch(Exception ex){
                            System.out.println("Error en el archivo");
                        }
                        continue;
                    }

                    if(vueltas == 0){
                        out  = new FileOutputStream( "C:/Users/Joel_/Desktop/ESCOM/Aplicaciones para Comunicaciones de Red/Tareas/Archivo Datagrama/Archivos"+datos.getNombre());
                        nombre = datos.getNombre();
                        out.write(datos.getDatos());
                        numTrama = datos.getNumPaquete();
                        vueltas+=1;
                        
                        s.enivarRespuesta(true, puerto);
                    }else{
                        if(nombre.equals(datos.getNombre())){
                            //if(numTrama == datos.getNumPaquete()){
                            try{
                                out.write(datos.getDatos());
                                s.enivarRespuesta(true, puerto);
                            }catch(Exception ex){
                                System.err.println("Archivo cerrado");
                            }
                            
                            
                            /*}else{
                                System.out.println("Archivo en desorden");
                                s.enivarRespuesta(false,puerto);
                                numTrama-=1;
                            }*/
                        }else{
                            try{
                                s.enivarRespuesta(false, puerto);
                            }catch(NullPointerException ex){
                                System.out.println("error en el envio de la respuesta");
                            }
                        }
                        

                    }
                    numTrama+=1;
                    
                    
                }else{
                    System.out.println("Trama: "+datos.getNumPaquete() + "recibida con error");
                    s.enivarRespuesta(false,puerto);
                }
                
                
                
                
            }
            
            
            
        }catch(Exception ex){
            System.out.println(ex.toString());
        }
        
        
        
    }
}
