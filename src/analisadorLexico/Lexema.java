/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analisadorLexico;

import analisadorLexico.Classificacao;
import java.util.ArrayList;

/**
 *
 * @author cc161255426
 */
public class Lexema {
    
    
    private String lexema;
    private Classificacao token;
    private int coluna_inicial;
    private int coluna_final;
    private int linha;
    
    public Lexema(String lexema,Classificacao token, int coluna_inicial, int coluna_final, int linha) {
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
    
    
    
}
