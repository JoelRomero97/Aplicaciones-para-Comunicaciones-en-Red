import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author sophie
 */
public class Servidor {

    public static void clear ()
    {
        try
        {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        }catch (Exception e)
        {
            e.printStackTrace ();
        }
    }
    
    public static void main(String[] args) {
        Tablero juego = null;
        int puerto;
        Scanner sc = new Scanner (System.in);

        clear ();
        
//         <editor-fold desc="Servidor">
        try {
            System.out.println ("Ingresa el puerto: ");
            puerto = sc.nextInt ();
            // Socket de servidor
            //ServerSocket ss = new ServerSocket(4040);
            ServerSocket ss = new ServerSocket(puerto);
            System.out.println("\n\nEsperando jugador...\n");
            while (true) {
                // Aceptamos un cliente y abrimos los flujos de IO
                Socket s = ss.accept();
                System.out.println ("Jugador conectado desde -> " + s.getInetAddress () + ":" + s.getPort());
                ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());
                ObjectInputStream is = new ObjectInputStream(s.getInputStream());
                os.flush();

                Jugada j = (Jugada) is.readObject();

                if (j != null) {
                    if (j.getX() < 0 && j.getY() < 0) {
                        System.out.println("Comienza un juego nuevo");
                        juego = new Tablero(j.getTipo());
                    } else {
                        juego.hazJugada(j.getX(), j.getY(), j.getTipo());
                        if (juego.getEstadoJuego() == -1) {
                            System.out.println("El jugador perdio");
                        }
                    }
                }
                os.writeObject(juego);
                os.flush();
                is.close();
                os.close();
                s.close();
            }

        } catch (ClassNotFoundException ce) {
        } catch (IOException ex) {
        }
        // </editor-fold>
    }
    
}
