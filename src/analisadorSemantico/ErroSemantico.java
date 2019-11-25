/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analisadorSemantico;

import analisadorLexico.Lexema;

/**
 *
 * @author gabriel
 */
public class ErroSemantico {
    private final Lexema lexema;
    private final ErroSem esperado;
    
    public ErroSemantico(Lexema lex, ErroSem esperado){
        this.lexema = lex;
        this.esperado = esperado;
    }

    @Override
    public String toString() {
        return "ErroSint{ " + " lexema = " + lexema.getLexema() + " linha = " + lexema.getLinha() + " , esperado = " + esperado.getErro() + " } \n";
    }

}
