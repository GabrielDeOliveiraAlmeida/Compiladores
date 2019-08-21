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
    private String erro;
    private Classificacao classe;
    private int coluna;
    private int linha;
    
    
    public Erro(String erro, Classificacao classe, int coluna, int linha){
        this.erro = erro;
        this.classe = classe;
        this.coluna = coluna;
        this.linha = linha;
    }
    
}
