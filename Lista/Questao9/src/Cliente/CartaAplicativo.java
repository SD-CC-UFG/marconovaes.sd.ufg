package Cliente;

import Entidade.Carta;
import Servidor.CartaHome;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import javax.swing.*;
import java.rmi.RemoteException;

public class CartaAplicativo extends JFrame {

    public static void main(String[] args) throws RemoteException, MalformedURLException {
        Carta carta1 = new Carta(1,2);
        Carta carta2 = new Carta(10,1);
        Carta carta3 = new Carta(11,3);
        Carta carta4 = new Carta(5,4);
        Carta carta5 = new Carta(13,2);
        
        
        CartaHome obj;
        String nomeCarta = "";
        try {
            obj = (CartaHome) Naming.lookup("rmi://localhost:1010/CartaServer");
            nomeCarta = obj.nomeExtenso(carta1);
            System.out.println(nomeCarta);
            nomeCarta = obj.nomeExtenso(carta2);
            System.out.println(nomeCarta);
            nomeCarta = obj.nomeExtenso(carta3);
            System.out.println(nomeCarta);
            nomeCarta = obj.nomeExtenso(carta4);
            System.out.println(nomeCarta);
            nomeCarta = obj.nomeExtenso(carta5);
            System.out.println(nomeCarta);
            
        } catch (NotBoundException ex) {
            System.out.println("CartaAplicativo exception: "
                    + ex.getMessage());
            ex.printStackTrace();
        }


    }
}
