/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

import Entidade.Funcionario;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FuncionarioHome extends Remote {
    boolean verificaAposentadoria(Funcionario func) throws RemoteException;
}
