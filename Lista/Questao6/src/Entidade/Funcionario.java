/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidade;

import java.io.Serializable;

public class Funcionario implements Serializable {
    private String nome;
    private char nivel;
    private Double salario;
    private Integer nrDependentes;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public char getNivel() {
        return nivel;
    }

    public void setNivel(char nivel) {
        this.nivel = nivel;
    }

    public Double getSalario() {
        return salario;
    }

    public void setSalario(Double salario) {
        this.salario = salario;
    }

    public Integer getNrDependentes() {
        return nrDependentes;
    }

    public void setNrDependentes(Integer nrDependentes) {
        this.nrDependentes = nrDependentes;
    }


}
