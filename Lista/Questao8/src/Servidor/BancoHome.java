/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

import Entidade.Cliente;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface BancoHome extends Remote {
    double calculaCredito(Cliente func) throws RemoteException;
}
