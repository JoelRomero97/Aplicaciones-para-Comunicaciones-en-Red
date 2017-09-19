/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.io.IOException;

/**
 *
 * @author JCVELMON
 */
public class main {
   public static void main(String[] args) throws IOException
    {        
        Servidor serv = new Servidor(); //Se crea el servidor
        
        System.out.println("Iniciando servidor\n");
        serv.startServer(); //Se inicia el servidor
    }
}
