/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analise;

import analisadorLexico.AnalisadorLexico;
import analisadorSemantico.AnalisadorSemantico;
import analisadorSemantico.Codigo;
import analisadorSintatico.AnalisadorSintatico;
import java.util.ArrayList;
import maquinaVirtual.Interpretador;

/**
 *
 * @author cc161255426
 */
public class Analise {
    public static AnalisadorLexico lex = new AnalisadorLexico();
    public static AnalisadorSintatico sint = new AnalisadorSintatico();
    public static AnalisadorSemantico sem = new AnalisadorSemantico();
    public static Interpretador vm = new Interpretador();
    public Analise(){
    }
    
    public ArrayList<Object> executeLex(String text){
        return lex.execute(text);
    }
    
    public void executeSint(ArrayList<Object> lex){
        sint.execute(lex);
    }
    
    public void executeSem(ArrayList<Object> lex){
        sem.execute(lex);
    }
    
    public void executeInter(ArrayList<Codigo> cod){
        vm.execute(cod);
    }
    
    public ArrayList<Codigo> tratarEntrada(String text){
        return vm.tratarEntrada(text);
    }
}
