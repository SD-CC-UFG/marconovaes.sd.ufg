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

    /*para o nível "A", o desconto é de 3% se o funcionário não tiver
     dependentes e 8% se o funcionário tiver dependentes;
      para o nível "B", o desconto é de 5% se o funcionário não tiver
     dependentes e 10% se o funcionário tiver dependentes;
      para o nível "C", o desconto é de 8% se o funcionário não tiver
     dependentes e 15% se o funcionário tiver dependentes;
      para o nível "D", o desconto é de 10% se o funcionário não tiver
     dependentes e 17% se o funcionário tiver dependentes.*/
    @Override
    public Double pegaSalarioLiquido(Funcionario func) throws RemoteException {
        if (func.getNivel() == 'A' && func.getNrDependentes() == 0) {
            return func.getSalario() * 0.97;
        } else if (func.getNivel() == 'A' && func.getNrDependentes() > 0) {
            return func.getSalario() * 0.92;
        } else if (func.getNivel() == 'B' && func.getNrDependentes() == 0) {
            return func.getSalario() * 0.95;
        } else if (func.getNivel() == 'B' && func.getNrDependentes() > 0) {
            return func.getSalario() * 0.90;
        } else if (func.getNivel() == 'C' && func.getNrDependentes() == 0) {
            return func.getSalario() * 0.92;
        } else if (func.getNivel() == 'C' && func.getNrDependentes() > 0) {
            return func.getSalario() * 0.85;
        } else if (func.getNivel() == 'D' && func.getNrDependentes() == 0) {
            return func.getSalario() * 0.90;
        } else if (func.getNivel() == 'D' && func.getNrDependentes() > 0) {
            return func.getSalario() * 0.83;
        }
        return 0.0;
    }

}
