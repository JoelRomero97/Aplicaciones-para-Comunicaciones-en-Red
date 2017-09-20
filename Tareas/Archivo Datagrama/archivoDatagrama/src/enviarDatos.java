
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;


    
public class enviarDatos {
    static String host;
    static final int bufferDatagrama = 60000;
    static final int puerto = 5001;
    static final int bufferData = 65535;
    DatagramSocket cl;
    public enviarDatos(){
        try {
            cl = new DatagramSocket();
        } catch (SocketException ex) {
            System.out.println(ex.toString());
        }
    }
    
    public void enviar(File f,String route){
        try
            {
                //DatagramSocket regreso = new DatagramSocket(puertoRegreso);
                DataInputStream dis = new DataInputStream(new FileInputStream(f));
                long tam = dis.available();
                     
                //socDatagrama soc = new socDatagrama("127.0.0.1",puerto);
                long enviados=0;
                int i=0,n=0;
                
                while(enviados < tam){
                    //enviar la primera trama que contiene información de control
                    
                    
                    //preparar los datos a mandar
                    byte[] buf = new byte[bufferDatagrama];
                    n = dis.read(buf);
                    datosPaquetes d;
                    
                    d = new datosPaquetes(++i,buf,f.getName(),tam,route);
                    int cont=0;
                    
                        enviaDatagrama(d, puerto);
                        System.out.println("Trama: "+i+" enviada");
                        cont+=1;
                        try{
                            Thread.sleep(100);
                        }catch(InterruptedException t){
                            System.out.println(t.toString());
                        }
                        
                    
                    enviados+=n;
                        
                    
                }
                
                byte[] b = {0x02};
                datosPaquetes df = new datosPaquetes(-1,b,f.getName(),b.length,route);
                enviaDatagrama(df, puerto);
                
                System.out.println("Archivo enviado");
                
                
                
                
            }catch(Exception ex){
                System.out.println(ex.toString());
            }
    }
    
    private void enviaDatagrama(datosPaquetes d,int puerto){       
                    
        try{
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);

            oos.writeObject(d);
            oos.flush();

            byte[] mnsj = baos.toByteArray();
                //enviar el archivo

            DatagramPacket dp = new DatagramPacket(mnsj,mnsj.length,InetAddress.getByName(""),puerto);
            cl.send(dp);
        }catch(IOException err){
            System.out.println("Error en la escritura del objeto");
        }
    }
    
}
