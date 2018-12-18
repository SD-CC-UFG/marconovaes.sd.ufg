package Cliente;

import Entidade.Atleta;
import Servidor.AtletaHome;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import javax.swing.*;
import java.rmi.RemoteException;

public class AtletaAplicativo extends JFrame {

    public static void main(String[] args) throws RemoteException, MalformedURLException {
        Atleta atleta = new Atleta();
        atleta.setIdade(Integer.parseInt(JOptionPane.showInputDialog("Digite a idade do atleta")));
        AtletaHome obj;
        String categoria = null;
        try {
            obj = (AtletaHome) Naming.lookup("rmi://localhost:1010/AtletaServer");
            categoria = obj.getCategoria(atleta);
        } catch (NotBoundException ex) {
            System.out.println("FuncionarioAplicativo exception: "
                    + ex.getMessage());
            ex.printStackTrace();
        }

        System.out.println("A categoria é: " + categoria);

    }
}
