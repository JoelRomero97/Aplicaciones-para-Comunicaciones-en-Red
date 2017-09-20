import java.net.*;
import java.io.*;
public class leeAnuncio {
    
    public static void main(String[] args) {
        try{
            String dir = "235.1.1.1";
            InetAddress gpo = null;
            int pto = 8000;
            MulticastSocket s = new MulticastSocket(pto);
            s.setReuseAddress(true);
            s.setTimeToLive(255);
            
            try{
                gpo = InetAddress.getByName(dir);
            }catch(UnknownHostException u){
                System.err.println("Direccion multicast no valida");
                s.close();
                System.exit(1);
            }
            
            s.joinGroup(gpo);
            System.out.println("Servicio iniciado y unido al grupo: "+dir+"\n Comienza recepción de anuncios");
            String anuncio = "anuncio";
            
            byte[] b = anuncio.getBytes();
            for(;;){
                DatagramPacket p = new DatagramPacket(new byte[65535],65535);
                s.receive(p);
                
                String mensaje = new String(p.getData(),0,p.getLength());
                
                if(mensaje.compareTo(anuncio)==0){
                    System.out.println("servidor encontrado en "+p.getAddress());
                }
                
                try{
                    Thread.sleep(5000);
                }catch(InterruptedException ex){
                    
                }
                
            }
            
            
            
        }catch(Exception ex){
            
        }
        
        
    }
    
}