package Cliente;

import Entidade.Cliente;
import Servidor.BancoHome;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import javax.swing.*;
import java.rmi.RemoteException;

public class ClienteAplicativo extends JFrame {

    public static void main(String[] args) throws RemoteException, MalformedURLException {
        Cliente func = new Cliente();
        func.setSaldo(Double.parseDouble(JOptionPane.showInputDialog("Digite o saldo")));
        BancoHome obj;
        double valorCredito = 0.0;
        try {
            obj = (BancoHome) Naming.lookup("rmi://localhost:1010/BancoServer");
            valorCredito = obj.calculaCredito(func);
        } catch (NotBoundException ex) {
            System.out.println("BancoAplicativo exception: "
                    + ex.getMessage());
            ex.printStackTrace();
        }

        System.out.println("Saldo médio: " + func.getSaldo() + "\nCredito: " + valorCredito);

    }
}
