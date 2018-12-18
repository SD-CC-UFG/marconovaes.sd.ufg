package Cliente;

import Entidade.Funcionario;
import Servidor.FuncionarioHome;
import java.awt.*;
import java.awt.event.*;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import javax.swing.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FuncionarioAplicativo extends JFrame {

    public static void main(String[] args) throws RemoteException, MalformedURLException {
        Funcionario func = new Funcionario();
        func.setNome(JOptionPane.showInputDialog("Digite o nome do funcionario"));
        func.setNivel(JOptionPane.showInputDialog("Digite o nível do funcionario").charAt(0));
        func.setSalario(Double.parseDouble(JOptionPane.showInputDialog("Digite o salario do funcionario")));
        func.setNrDependentes(Integer.parseInt(JOptionPane.showInputDialog("Digite o numero de dependentes do funcionario")));
        FuncionarioHome obj;
        Double salarioLiquido = 0.0;
        try {
            obj = (FuncionarioHome) Naming.lookup("rmi://localhost:1010/FuncionarioServer");
            salarioLiquido = obj.pegaSalarioLiquido(func);
        } catch (NotBoundException ex) {
            System.out.println("FuncionarioAplicativo exception: "
                    + ex.getMessage());
            ex.printStackTrace();
        }

        System.out.println("Nome do funcionário: " + func.getNome() + "\nSalário liquido: " + salarioLiquido);

    }
}
