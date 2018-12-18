/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

import Entidade.Carta;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.RMISecurityManager;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class CartaServer extends UnicastRemoteObject
        implements CartaHome {

    public CartaServer() throws RemoteException {
        super();
    }

    public static void main(String args[]) {

        try {
            CartaHome obj = new CartaServer();
            // Bind this object instance to the name "Cliente"
            Registry registry = LocateRegistry.createRegistry(1010);
            registry.bind("CartaServer", obj);
            System.out.println("CartaServer bound in registry");
        } catch (Exception e) {
            System.out.println("CartaServer err: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public String nomeExtenso(Carta carta) throws RemoteException {
        String nomeCarta = "";
        switch (carta.getValor()) {
            case 1:
                nomeCarta = "as ";
                break;
            case 2:
                nomeCarta = "dois ";
                break;
            case 3:
                nomeCarta = "tres ";
                break;
            case 4:
                nomeCarta = "quatro ";
                break;
            case 5:
                nomeCarta = "cinco ";
                break;
            case 6:
                nomeCarta = "seis ";
                break;
            case 7:
                nomeCarta = "sete ";
                break;
            case 8:
                nomeCarta = "oito ";
                break;
            case 9:
                nomeCarta = "nove ";
                break;
            case 10:
                nomeCarta = "dez ";
                break;
            case 11:
                nomeCarta = "valete ";
                break;
            case 12:
                nomeCarta = "dama ";
                break;
            case 13:
                nomeCarta = "rei ";
                break;
        }
        switch (carta.getNaipe()) {
            case 1:
                nomeCarta += "de ouros";
                break;
            case 2:
                nomeCarta += "de paus";
                break;
            case 3:
                nomeCarta += "de copas";
                break;
            case 4:
                nomeCarta += "de espadas";
                break;
        }
        return nomeCarta;
    }

}
