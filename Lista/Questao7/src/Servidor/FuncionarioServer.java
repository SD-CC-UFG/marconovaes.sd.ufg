/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

import Entidade.Funcionario;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.RMISecurityManager;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class FuncionarioServer extends UnicastRemoteObject
        implements FuncionarioHome {

    public FuncionarioServer() throws RemoteException {
        super();
    }

    public static void main(String args[]) {

        try {
            FuncionarioHome obj = new FuncionarioServer();
            // Bind this object instance to the name "Cliente"
            Registry registry = LocateRegistry.createRegistry(1010);
            registry.bind("FuncionarioServer", obj);
            System.out.println("FuncionarioServer bound in registry");
        } catch (Exception e) {
            System.out.println("FuncionarioServer err: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public boolean verificaAposentadoria(Funcionario func) throws RemoteException {
        if (func.getIdade() >= 65) {
            return true;
        } else if (func.getTempoServico() >= 30) {
            return true;
        } else if (func.getIdade() >= 60 && func.getTempoServico() >= 25) {
            return true;
        }

        return false;
    }

}
