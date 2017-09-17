import java.net.*;
import java.io.*;
public class socDatagrama {
    
    DatagramSocket cl,ds2;
    String host;
    final int bufferData = 65535;
    public socDatagrama(String host,int puerto){
        //este constructor es para cuando lo ejecuta un cliente
        try{
            cl = new DatagramSocket();
            ds2 = new DatagramSocket(puerto+1);
            this.host = host;
        }catch(SocketException err){
            System.out.println(err.toString());
        }
    }
    
    public socDatagrama(int puerto)throws SocketException{
        cl = new DatagramSocket(puerto);
        ds2 = new DatagramSocket(puerto+1);
    }
    
    public void enviaDatagrama(datosPaquetes d,int puerto){       
                    
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
    
    public boolean leeRespuesta(int port){
        try{
            
            DatagramPacket p1 = new DatagramPacket(new byte[bufferData],bufferData);
            ds2.receive(p1);
            
            String datos = new String(p1.getData(),0,p1.getLength());
            if(datos.equals("siguiente")){
                System.out.println("Enviar siguiente trama");
                return true;
            }else{
                System.out.println("reenviar trama");
                return false;
            }
            
        }catch(Exception ex){
            
        }
        return false;
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
    
    public boolean revisarDatagrama(datosPaquetes datos){
        if(datos.getDatos().length==datos.getTamDatos()){
            return true;
        }else{
            return false;
        }

    }
    
    public void enivarRespuesta(boolean respuesta,int puerto){
        try{
            String mnsj = "";
            if(respuesta){
            //decirle al cliente que mande la siguiente trama
                mnsj = "siguiente";
                System.out.println("Enviar siguiente trama");
            }else{
                //decirle al cliente que vuelva a mandar la trama
                System.out.println("Reenviar trama anterior");
                mnsj = "regreso";
            }
        
            byte[] b = mnsj.getBytes();

            DatagramPacket dp = new DatagramPacket(b,b.length,InetAddress.getByName("127.0.0.1"),puerto+1);
            ds2.send(dp);
            //ds2.close();

        }catch(Exception ex){
            System.out.println(ex.toString());
        }
        
    
    }
    
}
