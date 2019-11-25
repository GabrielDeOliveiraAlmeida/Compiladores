/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analisadorSemantico;

/**
 *
 * @author gabriel
 */
public enum ErroSem {
    NAOUTILIZADO("nao foi utilizado"),
    JADECLARADO("Já foi declarado"),
    NAOFOIDECLARADO("Não foi declarado"),
    PRECISAREAL("Precisa ser real"),
    TIPOSDIFF("Tipos são diferentes");
    
    public String err;
  
    private ErroSem(String err){
        this.err = err;
    }
}
