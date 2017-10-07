
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author CODMetrichugo
 */
public class ThreadUsuario extends Thread{
    public static final String MCAST_ADDR = "230.1.1.1";//dir clase D valida, grupo al que nos vamos a unir
    public static final int MCAST_PORT = 12000;
    public static final int DGRAM_BUF_LEN = 512;
    MulticastSocket socket;
    JEditorPane je;
    JComboBox combo;
    Mensaje msj;
    String usuario;
    InetAddress group;
            
  public ThreadUsuario(JEditorPane je, JComboBox combo,String usuario) {
      this.combo = combo;
      this.je = je;
      this.usuario = usuario;
      try {
        socket = new MulticastSocket(MCAST_PORT);
        group = InetAddress.getByName(MCAST_ADDR);
        socket.joinGroup(group);
        System.out.println("Unido al grupo");
      } catch (Exception ex) {
          ex.printStackTrace();
      }
  }
       
    
  public void run(){
    System.out.println("Desde el hilo");
   	try{   
      for(;;){
        byte[] buf = new byte[DGRAM_BUF_LEN];//crea arreglo de bytes 
    	DatagramPacket recv = new DatagramPacket(buf,buf.length);//crea el datagram packet a recibir
    	socket.receive(recv);// ya se tiene el datagram packet
        ByteArrayInputStream baosIn = new ByteArrayInputStream(recv.getData());
        ObjectInputStream ois = new ObjectInputStream(baosIn);
        msj = (Mensaje)ois.readObject();
        if(msj.getTipo()==0 && !msj.getOrigen().equals(usuario)){
          System.out.println("Entro al caso 0");
          combo.addItem(msj.getOrigen());
          if(je.getText().equals("")){
            je.setText("El usuario " + msj.getOrigen().toUpperCase() + " se ha unido al chat\n");
          }else{
            je.setText(je.getText()+"\nEl usuario " + msj.getOrigen().toUpperCase() + " se ha unido al chat\n");
          }
          ByteArrayOutputStream baosOut = new ByteArrayOutputStream();
          ObjectOutputStream oos = new ObjectOutputStream(baosOut);
          msj.setTipo(1);
          msj.setDestino(msj.getOrigen());
          msj.setOrigen(usuario);
          oos.writeObject(msj);
          oos.flush();
          byte []b = baosOut.toByteArray();
          DatagramPacket packetOut = new DatagramPacket(b,b.length,group,MCAST_PORT);
          socket.send(packetOut);
        }else{
          if(msj.getTipo()==1 && !msj.getOrigen().equals(usuario) && usuario.equals(msj.getDestino())){
            System.out.println("Entro al caso 1");
            if(je.getText().equals("")){
              je.setText("El usuario " + msj.getOrigen().toUpperCase() + " se ha unido al chat\n");
            }else{
              je.setText(je.getText()+"\nEl usuario " + msj.getOrigen().toUpperCase() + " se ha unido al chat\n");
            }
            combo.addItem(msj.getOrigen());
          }else{
            if(msj.getTipo()==2 && !msj.getOrigen().equals(usuario)){
              System.out.println("Entro al caso 2");
              if(je.getText().equals("")){
                je.setText(msj.getOrigen()+": " + msj.getMensaje());
              }else{
                je.setText(je.getText()+ "\n"+msj.getOrigen()+": " + msj.getMensaje());
              }
            }else{
              if(msj.getTipo()==3 && !msj.getOrigen().equals(usuario) && msj.getDestino().equals(usuario)){
                if(je.getText().equals("")){
                  je.setText("<Privado>" + msj.getOrigen() + ": "+ msj.getMensaje());
                }else{
                  je.setText(je.getText()+"\n<Privado> " + msj.getOrigen() + ": " + msj.getMensaje());
                }
              }
            }
          }
        }
      }
    }catch(Exception e){e.printStackTrace();}
   
	}//run
  
}
