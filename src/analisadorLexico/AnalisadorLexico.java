/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analisadorLexico;

import java.util.ArrayList;

/**
 *
 * @author gabriel
 */
public class AnalisadorLexico {

    private char[] textArray;
    private String lexema;
    private String token;
    private String chAtual;
    private int cont;
    private int posInicial;
    private int linha;
    private Classificacao chClass;
    private ArrayList<Lexema> lexemas;
    private ArrayList<Erro> erros;
    private ClassificacaoReservadas classes;
    public AnalisadorLexico() {
        classes = new ClassificacaoReservadas();
    }

    private void init(){
        lexema = "";
        //Array contendo todos os lexemas
        lexemas = new ArrayList<>();
        erros = new ArrayList<>();
        cont =0;
        linha=0;
    }
    public ArrayList<Lexema> execute(String text) {
        
        textArray = (text+" ").toCharArray();
        System.out.println("Iniciando analise léxica"
                + "\n Tamanho = " + textArray.length);
        init();
        nextChar();
        while (cont < textArray.length) {
            System.out.println("Posicao ="+ cont + " Classe = " + chClass + " char = "+ chAtual);
            //Ignorar os caracter em branco
            switch (chClass) {
                case BRANCO:
                    nextChar();
                    break;
                /*
                    Verificar se o número é DOUBLE OU INTEIRO
                 */
                case INTEIRO:
                    posInicial = cont;
                    addChar(); 
                    /*
                    Se caso haja '.' representará um número com virgula, 
                    a segunda verificação serve para ignorar o segundo ponto;
                     */
                    while (chClass.equals(Classificacao.INTEIRO) || (chAtual.equals(".") && (lexema).matches(Classificacao.INTEIRO.getRegex()))) {
                        addChar();
                    }
                    if (lexema.matches(Classificacao.INTEIRO.getRegex())) {
                        if(!verificaTamanho(Classificacao.INTEIRO)) nextChar();
                        else addLexema(Classificacao.INTEIRO);
                    } else {
                        if(!verificaTamanho(Classificacao.COM_VIRGULA)) nextChar();
                        else addLexema(Classificacao.COM_VIRGULA);
                    }
                    break;
                /*
                    Verifica os identificadores e os classifica corretamente.
                 */
                case IDENTIFICADOR:
                    posInicial = cont;
                    addChar();
                    while(chClass.equals(Classificacao.IDENTIFICADOR)) {
                        addChar();
                    } 
                    if (lexema.matches(Classificacao.PALAVRA_RESERVDA.getRegex())) {
                        addLexema(classes.getClasse(lexema));
                    } else {
                        if(!verificaTamanho(Classificacao.IDENTIFICADOR))
                            nextChar();
                        else addLexema(Classificacao.IDENTIFICADOR);
                    }
                    break;
                case PULA_LINHA:
                    pularLinha();
                    break;
                    
                case COMENTARIO:
                    while(!chClass.equals(Classificacao.PULA_LINHA)){
                        nextChar();
                    }
                    break;
                    
                case COMENTARIO_BLOCO:
                    posInicial = cont;
                    while(!chAtual.equals("}")){
                        if(cont >= textArray.length){
                            addErro("'}' Esperado", Classificacao.COMENTARIO_BLOCO);
                            break;
                        }
                        nextChar();
                    }
                    nextChar();
                    break;
                    
                case OPERADORES:
                    posInicial = cont;
                    addChar();
                    if((lexema+chAtual).matches(Classificacao.OPERADORES.getRegex())){
                        addChar();
                    }
                    addLexema(classes.getClasse(lexema));
                    break;
                
                case DELIMITADOR:
                    posInicial = cont;
                    addChar();
                    if((lexema+chAtual).matches(Classificacao.OPERADORES.getRegex())){
                        addChar();
                        addLexema(classes.getClasse(lexema));
                        break;
                    }else{
                        previousChar();
                        System.out.println(chAtual);
                        addLexema(chClass);
                    }
                    
                    break;
                    
                case CARACTER_INVALIDO:
                    posInicial = cont;
                    lexema = chAtual;
                    addErro("'"+chAtual + "' Invalido", Classificacao.CARACTER_INVALIDO);
                    addLexema(chClass);
                    nextChar();
                    break;
                
                default:
                    posInicial = cont;
                    lexema = chAtual;
                    cont++;
                    addLexema(chClass);
                    cont--;
                    nextChar();
            }
        }
        return lexemas;
    }

    //Le o proximo caracter e o classifica para verificações.
    private void nextChar() {
        chAtual = String.valueOf(textArray[cont++]);
        chClass = Classificacao.getOf(chAtual);
    }
    
    private void previousChar(){
        cont--;
        chAtual = String.valueOf(textArray[cont]);
        chClass = Classificacao.getOf(chAtual);
    }

    private void setLexema(){
        lexema = chAtual;
    }
    private void addChar() {
        lexema += chAtual;
        nextChar();
    }

    private void addLexema(Classificacao classe) {
        lexemas.add(new Lexema(lexema, classe, posInicial, cont - 1, linha));
        lexema = "";
    }
    
    private void addErro(String erro, Classificacao classe){
        erros.add(new Erro(erro, classe, posInicial, linha));
    }
    
    private void pularLinha(){
        linha++;
    }
    
    private boolean verificaTamanho(Classificacao classe){
        if(classe.equals(Classificacao.IDENTIFICADOR)){
            if(cont - posInicial>32){
                addErro("IDENTIFICADOR Longo", Classificacao.IDENTIFICADOR);
                return false;
            }
        }else if(classe.equals(Classificacao.INTEIRO)){
            if(cont - posInicial>8){
                addErro("INTEIRO Longo", Classificacao.INTEIRO);
                return false;
            }
        }else if(classe.equals(Classificacao.COM_VIRGULA)){
            if(cont - posInicial > 18){
                addErro("FLOAT Longo", Classificacao.COM_VIRGULA);
                return false;
            }
        }
        return true;
    }
}
