/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analise;

import analisadorLexico.AnalisadorLexico;
import analisadorLexico.Lexema;
import analisadorSintatico.AnalisadorSintatico;
import java.util.ArrayList;

/**
 *
 * @author cc161255426
 */
public class Analise {
    public static AnalisadorLexico lex = new AnalisadorLexico();
    public static AnalisadorSintatico sint = new AnalisadorSintatico();
    
    public Analise(){
    }
    
    public ArrayList<Object> executeLex(String text){
        return lex.execute(text);
    }
    
    public void executeSint(ArrayList<Object> lex){
        sint.execute(lex);
    }
}
