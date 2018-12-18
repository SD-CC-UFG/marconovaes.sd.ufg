/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidade;

import java.io.Serializable;

public class Carta implements Serializable {
    private Integer valor;
    private Integer naipe;
    
    public Carta (Integer valor, Integer naipe){
        this.valor = valor;
        this.naipe = naipe;
    }

    public Integer getValor() {
        return valor;
    }

    public void setValor(Integer valor) {
        this.valor = valor;
    }

    public Integer getNaipe() {
        return naipe;
    }

    public void setNaipe(Integer naipe) {
        this.naipe = naipe;
    }
}
