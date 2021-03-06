/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

import Entidade.Pessoa;
import Entidade.Pessoa;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.RMISecurityManager;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class PessoaServer extends UnicastRemoteObject
        implements PessoaHome {

    public PessoaServer() throws RemoteException {
        super();
    }

    public static void main(String args[]) {

        try {
            PessoaHome obj = new PessoaServer();
            // Bind this object instance to the name "Cliente"
            Registry registry = LocateRegistry.createRegistry(1010);
            registry.bind("PessoaServer", obj);
            System.out.println("PessoaServer bound in registry");
        } catch (Exception e) {
            System.out.println("PessoaServer err: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public Double pesoIdeal(Pessoa pessoa) throws RemoteException {
        if (pessoa.getSexo().equals("masculino")) {
            return (72.7 * pessoa.getAltura()) - 58;
        } else {
            return (62.1 * pessoa.getAltura()) - 44.7;
        }
    }

}
