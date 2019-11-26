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
    NAOUTILIZADO("Nao foi utilizado"),
    JADECLARADO("Já foi declarado"),
    NAOFOIDECLARADO("Não foi declarado"),
    NAOFOIINICIALIZADA("Não foi inicializada"),
    PRECISAREAL("Precisa ser real"),
    TIPOSDIFF("Tipos são diferentes");
    
    public String err;
  
    private ErroSem(String err){
        this.err = err;
    }
    
    public String getErro(){
        return err;
    }
}
