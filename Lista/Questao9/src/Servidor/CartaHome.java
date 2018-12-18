/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

import Entidade.Carta;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CartaHome extends Remote {
    String nomeExtenso(Carta func) throws RemoteException;
}
