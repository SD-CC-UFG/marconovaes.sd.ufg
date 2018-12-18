/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidade;

import java.io.Serializable;

public class Funcionario implements Serializable {
    private Integer idade;
    private Integer tempoServico;

    public Integer getIdade() {
        return idade;
    }

    public void setIdade(Integer idade) {
        this.idade = idade;
    }

    public Integer getTempoServico() {
        return tempoServico;
    }

    public void setTempoServico(Integer tempoServico) {
        this.tempoServico = tempoServico;
    }

}
