import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Cliente {

	private static String host;
    private static int puerto;

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
        // TODO code application logic here
        Scanner sc = new Scanner(System.in);
        int n,m,p;
        clear ();

        System.out.println("\t\t\t\tBuscaminas\n\n");
        System.out.print ("\nIngresa la direccion IP: ");
    	host = sc.nextLine ();
    	System.out.print ("\n\nIngresa el puerto: ");
    	puerto = sc.nextInt ();
    	clear ();
        System.out.println("\n\nQue nivel desea jugar?\n");
        System.out.println("1.Facil");
        System.out.println("2.Intermedio");
        System.out.println("3.Avanzado\n");
        n = sc.nextInt();
        Tablero tab = Tiro(-1, -1, n);

        clear ();
        //System.out.println(tab);
        while (tab.getEstadoJuego() == 0) {
        	clear ();
        	System.out.println(tab);
            System.out.println("\nSeleccione una opcion\n");
            System.out.println("1.Tirar");
            System.out.println("2.Marcar\n");
            n = sc.nextInt()-1;
            System.out.println("Ingrese las coordenadas\n");
            System.out.print ("Coordenada X: ");
            p=sc.nextInt()-1;
            System.out.print ("\nCoordenada Y: ");
            m=sc.nextInt()-1;
            tab = Tiro(m, p, n);
            System.out.println(tab);
        }
        if (tab.getEstadoJuego() == 1) {
            System.out.println("\n\nGANASTE :D");
        } else {
            System.out.println("\n\nPerdiste :(");
        }

    }

    public static Tablero Tiro(int x, int y, int jugada) {
        Tablero t = null;
        try {
        	Scanner sc = new Scanner(System.in);
            // Apertura del socket y los flujos IO
            Socket s = new Socket(InetAddress.getByName(host), puerto);
            ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());
            ObjectInputStream is = new ObjectInputStream(s.getInputStream());

            os.flush();

            Jugada j1 = new Jugada();

            j1.setTipo(jugada);
            j1.setX(x);
            j1.setY(y);
            os.writeObject(j1);
            os.flush();

            t = (Tablero) is.readObject();


            //Cerramos los flujos y el socket
            is.close();
            os.close();
            s.close();
        } catch (UnknownHostException ex) {
        } catch (IOException ex) {
        } catch (ClassNotFoundException ex) {
        }
        return t;
    }
    
}
