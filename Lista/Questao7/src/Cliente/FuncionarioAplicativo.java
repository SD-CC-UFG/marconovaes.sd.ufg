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
        func.setIdade(Integer.parseInt(JOptionPane.showInputDialog("Digite a idade do funcionario")));
        func.setTempoServico(Integer.parseInt(JOptionPane.showInputDialog("Digite o tempo de serviço do funcionario")));
        FuncionarioHome obj;
        boolean podeAposentar = false;
        try {
            obj = (FuncionarioHome) Naming.lookup("rmi://localhost:1010/FuncionarioServer");
            podeAposentar = obj.verificaAposentadoria(func);
        } catch (NotBoundException ex) {
            System.out.println("FuncionarioAplicativo exception: "
                    + ex.getMessage());
            ex.printStackTrace();
        }

        if (podeAposentar) {
            System.out.println("Pode aposentar!!");
        } else {
            System.out.println("Não pode aposentar!!");
        }

    }
}
