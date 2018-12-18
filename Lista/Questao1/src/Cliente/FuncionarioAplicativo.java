package Cliente;

import Entidade.Funcionario;
import Servidor.FuncionarioHome;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import javax.swing.*;
import java.rmi.RemoteException;


public class FuncionarioAplicativo extends JFrame {

    public static void main(String[] args) throws RemoteException, MalformedURLException {
        Funcionario func = new Funcionario();
        func.setNome(JOptionPane.showInputDialog("Digite o nome do funcionario"));
        func.setCargo(JOptionPane.showInputDialog("Digite o cargo do funcionario"));
        func.setSalario(Double.parseDouble(JOptionPane.showInputDialog("Digite o salário do funcionario")));
        FuncionarioHome obj;
        try {
            obj = (FuncionarioHome) Naming.lookup("rmi://localhost:1010/FuncionarioServer");
            func = obj.pegaSalarioReajustado(func);
        } catch (NotBoundException ex) {
            System.out.println("FuncionarioAplicativo exception: "
                    + ex.getMessage());
            ex.printStackTrace();
        }
        
        System.out.println("Nome do funcionário: " + func.getNome() + "\nSalário reajustado: " + func.getSalario());

    }
}
