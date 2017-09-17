
import java.io.Serializable;

public class datosPaquetes implements Serializable{
    int numPaquete;
    byte[] datos;
    String nombre;
    long tamDatos;
    String ruta;

    public datosPaquetes(int numPaquete, byte[] datos, String nombre, long tamDatos,String ruta) {
        this.numPaquete = numPaquete;
        this.datos = datos;
        this.nombre = nombre;
        this.tamDatos = tamDatos;
        this.ruta = ruta;
        
    }

    public String getRuta() {
        return ruta;
    }

    public int getNumPaquete() {
        return numPaquete;
    }

    public byte[] getDatos() {
        return datos;
    }

    public String getNombre() {
        return nombre;
    }

    public long getTamDatos() {
        return tamDatos;
    }


    
    
    
    
    
    
}
