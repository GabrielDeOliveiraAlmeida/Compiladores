/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analisadorSintatico;

import analisadorLexico.Classificacao;
import analisadorLexico.Lexema;

/**
 *
 * @author gabriel
 */
public class ErroSint {
    private final Lexema lexema;
    private final Classificacao esperado;
    
    public ErroSint(Lexema lex, Classificacao esperado){
        this.lexema = lex;
        this.esperado = esperado;
    }
    
    
    
    

            
    
    
}
