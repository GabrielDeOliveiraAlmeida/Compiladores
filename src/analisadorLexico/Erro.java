/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analisadorLexico;

/**
 *
 * @author cc161255426
 */
public class Erro {
     
    private final String lexema;
    private final Classificacao token;
    private final int coluna_inicial;
    private final int coluna_final;
    private final int linha;
    
    public Erro(String lexema,Classificacao token, int coluna_inicial, int coluna_final, int linha) {
        this.lexema = lexema;
        this.token = token;
        this.coluna_inicial = coluna_inicial;
        this.coluna_final = coluna_final;
        this.linha = linha;
    }

    public String getLexema() {
        return lexema;
    }

    public Classificacao getToken() {
        return token;
    }

    public int getColuna_inicial() {
        return coluna_inicial;
    }

    public int getColuna_final() {
        return coluna_final;
    }

    public int getLinha() {
        return linha;
    }
    
}
