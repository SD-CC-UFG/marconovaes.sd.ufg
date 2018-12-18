package Cliente;

import Entidade.Aluno;
import Servidor.AlunoHome;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import javax.swing.*;
import java.rmi.RemoteException;

public class AlunoAplicativo {

    public static void main(String[] args) throws RemoteException, MalformedURLException {
        Aluno aluno = new Aluno();
        aluno.setN1(Double.parseDouble(JOptionPane.showInputDialog("Digite a N1")));
        aluno.setN2(Double.parseDouble(JOptionPane.showInputDialog("Digite a N2")));
        aluno.setN3(Double.parseDouble(JOptionPane.showInputDialog("Digite a N3")));
        AlunoHome obj;
        boolean aprovado = false;
        try {
            obj = (AlunoHome) Naming.lookup("rmi://localhost:1010/AlunoServer");
            aprovado = obj.verificaAprovacao(aluno);
        } catch (NotBoundException ex) {
            System.out.println("FuncionarioAplicativo exception: "
                    + ex.getMessage());
            ex.printStackTrace();
        }
        if (aprovado) {
            System.out.println("Aluno aprovado!!");
        } else {
            System.out.println("Aluno reprovado!!");
        }

    }
}
