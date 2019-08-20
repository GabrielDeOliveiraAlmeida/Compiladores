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

    public AnalisadorLexico() {

    }

    private void init(){
        lexema = "";
        lexemas = new ArrayList<>();
        cont =0;
        linha=0;
    }
    public ArrayList<Lexema> execute(String text) {
        //Array contendo todos os lexemas
        
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
                    if (lexema.matches(Classificacao.COM_VIRGULA.getRegex())) {
                        addLexema(Classificacao.COM_VIRGULA);
                    } else {
                        addLexema(Classificacao.INTEIRO);
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
                        addLexema(Classificacao.PALAVRA_RESERVDA);
                    } else {
                        addLexema(Classificacao.IDENTIFICADOR);
                    }
                    break;
                case PULA_LINHA:
                    linha++;
                    break;
                case COMENTARIO:
                    while(!chClass.equals(Classificacao.PULA_LINHA)){
                        nextChar();
                    }
                    break;
                case COMENTARIO_BLOCO:
                    while(!chAtual.equals("}")){
                        if(cont >= textArray.length){
                            break;
                        }
                        nextChar();
                    }
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

    private void addChar() {
        lexema += chAtual;
        nextChar();
    }

    private void addLexema(Classificacao classe) {
        lexemas.add(new Lexema(lexema, classe, posInicial, cont - 1, linha));
        lexema = "";
    }
    
}
