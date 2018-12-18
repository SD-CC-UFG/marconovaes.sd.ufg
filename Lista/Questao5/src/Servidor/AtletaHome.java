/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

import Entidade.Atleta;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AtletaHome extends Remote {

    String getCategoria(Atleta atleta) throws RemoteException;
}
