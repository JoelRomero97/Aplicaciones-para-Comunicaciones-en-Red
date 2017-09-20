import java.io.*;
import java.net.*;
import java.util.ArrayList;
public class servidor {

    static final int bufferData = 65535;
    static final int puerto = 5001;
    public static void main(String[] args) {
        int vueltas = 0;
        
        String nombre="";
        try{
            ArrayList <byte []> lista = null;
            recvDatos recv = new recvDatos(puerto);
            //DatagramSocket ds = new DatagramSocket(puerto);
            DataOutputStream out = null;
            int numTrama = 0;
            byte[] datosRec=null;
            while(true){

                datosPaquetes datos = recv.recibeDatagrama();
                    
                  
                    System.out.println("Trama: "+datos.getNumPaquete() + "recibida correctamente");
                    
                    if(datos.getNumPaquete()==-1){
                        System.out.println("Archivo terminado de recibir");
                        vueltas=0;
                        try
                        {
                            File folder = new File(datos.getRuta()+"\\");
                            if(!folder.exists())
                            {
                                folder.mkdir();
                            }
                            
                            out  = new DataOutputStream(new FileOutputStream(datos.getRuta()+"\\"+datos.getNombre()));
                            for(int i=0;i<lista.size();++i){
                                out.write(lista.get(i));
                            }
                            out.close();
                            
                        }catch(Exception ex){
                            System.out.println(ex.toString());
                        }
                        
                        
                        continue;
                    }

                    if(vueltas == 0){
                        lista = new ArrayList<>();
                        nombre = datos.getNombre();
                        lista.add(datos.getDatos());
                        numTrama = datos.getNumPaquete();
                        vueltas+=1;
                        
                    }else{
                        if(nombre.equals(datos.getNombre())){
                            lista.add(datos.getDatos());
                                          
                        }                        

                    }
                    numTrama+=1;
                    
                
                
                
                
            }
            
            
            
        }catch(Exception ex){
            System.out.println(ex.toString());
        }
        
        
        
    }
    
}
