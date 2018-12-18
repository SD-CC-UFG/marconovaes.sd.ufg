/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

import Entidade.Atleta;
import Entidade.Atleta;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.RMISecurityManager;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class AtletaServer extends UnicastRemoteObject
        implements AtletaHome {

    public AtletaServer() throws RemoteException {
        super();
    }

    public static void main(String args[]) {

        try {
            AtletaHome obj = new AtletaServer();
            // Bind this object instance to the name "Cliente"
            Registry registry = LocateRegistry.createRegistry(1010);
            registry.bind("AtletaServer", obj);
            System.out.println("AtletaServer bound in registry");
        } catch (Exception e) {
            System.out.println("AtletaServer err: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public String getCategoria(Atleta atleta) throws RemoteException {
        if (atleta.getIdade() >= 5 && atleta.getIdade() <= 7) {
            return "infantil A";
        }
        else if (atleta.getIdade() >= 8 && atleta.getIdade() <= 10) {
            return "infantil B";
        }
        else if (atleta.getIdade() >= 11 && atleta.getIdade() <= 13) {
            return "juvenil A";
        }
        else if (atleta.getIdade() >= 14 && atleta.getIdade() <= 17) {
            return "juvenil B";
        }
        else if (atleta.getIdade() >= 18) {
            return "adulto";
        }
        else {
            return "sem categoria";
        }
    }

}
