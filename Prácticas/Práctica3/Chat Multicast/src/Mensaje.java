
import java.io.Serializable;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author CODMetrichugo
 */
public class Mensaje implements Serializable{
  
  int tipo;
  String mensaje;
  String origen;
  String destino;

  public Mensaje(int tipo,String origen, String destino) {
    this.tipo = tipo;
    this.origen = origen;
    this.destino = destino;
  }
 
  public Mensaje(int tipo,String origen,String destino,String mensaje){
    this.tipo = tipo;
    this.origen = origen;
    this.destino = destino;
    this.mensaje = mensaje;
  }

  Mensaje(int tipo, String usuario) {
    this.tipo = tipo;
    this.origen = usuario;
  }

  public String getDestino() {
    return destino;
  }

  public String getMensaje() {
    return mensaje;
  }

  public String getOrigen() {
    return origen;
  }

  public int getTipo() {
    return tipo;
  }

  public void setDestino(String destino) {
    this.destino = destino;
  }

  public void setMensaje(String mensaje) {
    this.mensaje = mensaje;
  }

  public void setOrigen(String origen) {
    this.origen = origen;
  }

  public void setTipo(int tipo) {
    this.tipo = tipo;
  }
   
}
