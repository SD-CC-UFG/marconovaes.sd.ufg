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
        pessoa.setNome(JOptionPane.showInputDialog("Digite o nome da pessoa"));
        pessoa.setSexo(JOptionPane.showInputDialog("Digite o sexo da pessoa"));
        pessoa.setIdade(Integer.parseInt(JOptionPane.showInputDialog("Digite a idade da pessoa")));
        PessoaHome obj;
        boolean ehMaior = false;
        try {
            obj = (PessoaHome) Naming.lookup("rmi://localhost:1010/PessoaServer");
            ehMaior = obj.verificaMaioridade(pessoa);
        } catch (NotBoundException ex) {
            System.out.println("FuncionarioAplicativo exception: "
                    + ex.getMessage());
            ex.printStackTrace();
        }
        if (ehMaior) {
            System.out.println("Nome da pessoa: " + pessoa.getNome() + " eh maior de idade");
        } else {
            System.out.println("Nome da pessoa: " + pessoa.getNome() + " eh menor de idade");
        }

    }
}
