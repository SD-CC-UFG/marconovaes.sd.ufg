/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

import Entidade.Pessoa;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PessoaHome extends Remote {

    Double pesoIdeal(Pessoa pessoa) throws RemoteException;
}
