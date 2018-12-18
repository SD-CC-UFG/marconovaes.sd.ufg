/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

import Entidade.Cliente;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.RMISecurityManager;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class BancoServer extends UnicastRemoteObject
        implements BancoHome {

    public BancoServer() throws RemoteException {
        super();
    }

    public static void main(String args[]) {

        try {
            BancoHome obj = new BancoServer();
            // Bind this object instance to the name "Cliente"
            Registry registry = LocateRegistry.createRegistry(1010);
            registry.bind("BancoServer", obj);
            System.out.println("BancoServer bound in registry");
        } catch (Exception e) {
            System.out.println("BancoServer err: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public double calculaCredito(Cliente func) throws RemoteException {
       if(func.getSaldo()>=0 && func.getSaldo()<=200){
           return 0.0;
       }
       else if(func.getSaldo()>=201 && func.getSaldo()<=200){
           return func.getSaldo() * 0.2;
       }
       else if(func.getSaldo()>=401 && func.getSaldo()<=600){
           return func.getSaldo() * 0.3;
       }
       else if(func.getSaldo()>=601){
           return func.getSaldo() * 0.4;
       }
       return 0.0;
    }

}
