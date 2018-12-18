/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

import Entidade.Aluno;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class AlunoServer extends UnicastRemoteObject
        implements AlunoHome {

    public AlunoServer() throws RemoteException {
        super();
    }

    public static void main(String args[]) {

        try {
            AlunoHome obj = new AlunoServer();
            // Bind this object instance to the name "Cliente"
            Registry registry = LocateRegistry.createRegistry(1010);
            registry.bind("AlunoServer", obj);
            System.out.println("AlunoServer bound in registry");
        } catch (Exception e) {
            System.out.println("AlunoServer err: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public boolean verificaAprovacao(Aluno aluno) throws RemoteException {
        Double M = (aluno.getN1() + aluno.getN2()) / 2;
        if (M >= 7) {
            return true;
        } else if (M > 3 && M < 7) {
            if ((M + aluno.getN3()) / 2 >= 5) {
                return true;
            }
        }
        return false;
    }

}
