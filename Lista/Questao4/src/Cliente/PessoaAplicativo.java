package Cliente;

import Entidade.Pessoa;
import Servidor.PessoaHome;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import javax.swing.*;
import java.rmi.RemoteException;

public class PessoaAplicativo extends JFrame {

    public static void main(String[] args) throws RemoteException, MalformedURLException {
        Pessoa pessoa = new Pessoa();
        pessoa.setSexo(JOptionPane.showInputDialog("Digite o sexo da pessoa"));
        pessoa.setAltura(Double.parseDouble(JOptionPane.showInputDialog("Digite a altura da pessoa")));
        PessoaHome obj;
        Double pesoIdeal = 0.0;
        try {
            obj = (PessoaHome) Naming.lookup("rmi://localhost:1010/PessoaServer");
            pesoIdeal = obj.pesoIdeal(pessoa);
        } catch (NotBoundException ex) {
            System.out.println("FuncionarioAplicativo exception: "
                    + ex.getMessage());
            ex.printStackTrace();
        }

        System.out.println("O peso ideal é: " + pesoIdeal);

    }
}
