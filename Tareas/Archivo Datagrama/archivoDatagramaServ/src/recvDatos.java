
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;


public class recvDatos {
    
    DatagramSocket cl;
    final int bufferData = 65535;
    public recvDatos(int puerto){
        try{
            cl = new DatagramSocket(puerto);
        }catch(Exception ex){
            System.out.println(ex.toString());
        }
    }
    
    public datosPaquetes recibeDatagrama(){
        try {
            
            DatagramPacket dp = new DatagramPacket(new byte[bufferData],bufferData);
            cl.receive(dp);
            ObjectInputStream ois;
            ois = new ObjectInputStream(new ByteArrayInputStream(dp.getData()));
            datosPaquetes datos = (datosPaquetes)ois.readObject();
            return datos;
            
        } catch (Exception ex) {
            System.out.println("Error en la recepcion del objeto");
            return null;
        }
    }
    
}
