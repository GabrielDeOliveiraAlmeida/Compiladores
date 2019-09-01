/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analisadorLexico;

import java.util.ArrayList;
import java.util.regex.Pattern;

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
    private int posFinal;
    private int linha;
    private Classificacao chClass;
    private ArrayList<Object> lexemas;
    private ClassificacaoReservadas classes;
    private int contCol;

    public AnalisadorLexico() {
        classes = new ClassificacaoReservadas();
    }

    private void init() {
        lexema = "";
        //Array contendo todos os lexemas
        lexemas = new ArrayList<>();
        cont = 0;
        contCol = 0;
        posInicial = 0;
        posFinal = 0;
        linha = 1;
    }

    public ArrayList<Object> execute(String text) {

        textArray = (text + " ").toCharArray();
        System.out.println("Iniciando analise léxica"
                + "\n Tamanho = " + textArray.length);
        init();
        nextChar();

        while (cont < textArray.length) {
            System.out.println("Posicao =" + cont + " Classe = " + chClass + " char = " + chAtual);
            //Ignorar os caracter em branco
            switch (chClass) {
                case BRANCO:
                    nextChar();
                    break;
                /*
                    Verificar se o número é DOUBLE OU INTEIRO
                 */
                case INTEIRO:
                    posInicial = contCol;
                    //addChar();
                    /*
                    Se caso haja '.' representará um número com virgula, 
                    a segunda verificação serve para ignorar o segundo ponto;
                     */
                    boolean aux = true;
                    while (chClass.equals(Classificacao.INTEIRO)) {
                        addChar();
                        
                        if (chAtual.matches(Pattern.quote("."))) {
                            
                            if (((lexema + textArray[cont]).matches(Classificacao.COM_VIRGULA.getRegex()))) {
                                addChar();
                            } else {
                                break;
                            }
                        }
                    }
                    if (lexema.matches(Classificacao.INTEIRO.getRegex())) {
                        addLexema(Classificacao.INTEIRO);
                    } else if (lexema.matches(Classificacao.COM_VIRGULA.getRegex())) {
                        addLexema(Classificacao.COM_VIRGULA);
                    } else {
                        Classificacao classif = Classificacao.checkErr(lexema);
                        if (classif.equals(Classificacao.DESCONHECIDO.getRegex())) {
                            previousChar();
                            break;
                        }
                        addErro(classif);
                    }
                    break;
                /*
                    Verifica os identificadores e os classifica corretamente.
                 */
                case IDENTIFICADOR:
                    posInicial = contCol;
                    addChar();
                    while (chClass.equals(Classificacao.IDENTIFICADOR) || chClass.equals(Classificacao.INTEIRO)) {
                        addChar();
                    }
                    if (lexema.matches(Classificacao.PALAVRA_RESERVDA.getRegex())) {
                        addLexema(classes.getClasse(lexema));
                    } else {
                        Classificacao classif = Classificacao.getOf(lexema);
                        if (classif.equals(Classificacao.IDENTIFICADOR_LONGO)) {
                            addErro(classif);
                        } else {
                            addLexema(classif);
                        }
                    }
                    break;

                case PULA_LINHA:
                    pularLinha();
                    nextChar();
                    break;

                case OPERADORES:
                    posInicial = contCol;
                    addChar();
                    //Tratar comentario
                    if ((lexema + chAtual).matches(Classificacao.COMENTARIO.getRegex())) {;
                        lexema="";
                        while (!chClass.equals(Classificacao.PULA_LINHA) && cont < textArray.length) {
                            nextChar();
                        }
                        break;
                    }
                    //Tratar sinais com dois caracteres
                    if ((lexema + chAtual).matches(Classificacao.OPERADORES.getRegex())) {
                        addChar();
                    }
                    addLexema(classes.getClasse(lexema));
                    break;

                case DELIMITADOR:
                    posInicial = contCol;
                    addChar();
                    if ((lexema + chAtual).matches(Classificacao.OPERADOR_ATRIBUICAO.getRegex())) {
                        addChar();
                    }
                    addLexema(classes.getClasse(lexema));
                    break;

                case CARACTER_INVALIDO:
                    posInicial = contCol;
                    lexema = chAtual;
//                    addErro(chClass);;
                    addLexema(chClass);
                    nextChar();
                    break;

                case COMENTARIO_BLOCO:
                    posInicial = contCol;
                    while (!chAtual.equals("}")) {
                        if (cont >= textArray.length) {
                            addErro(Classificacao.DELIMITADOR_FECHA_CHAVE);
                            cont--;
                            break;
                        }
                        nextChar();
                    }
                    nextChar();
                    break;
//                    
//                default:
//                    posInicial = cont;
//                    lexema = chAtual;
//                    cont++;
//                    addLexema(chClass);
//                    cont--;
//                    nextChar();
            }
        }
        return lexemas;
    }
    //Le o proximo caracter e o classifica para verificações.

    private String getChar() {
        if (cont + 1 < textArray.length) {
            return String.valueOf(textArray[cont + 1]);
        } else {
            return null;
        }
    }

    private void nextChar() {
        chAtual = String.valueOf(textArray[cont++]);
        chClass = Classificacao.getOf(chAtual);
        contCol++;
    }

    private void previousChar() {
        cont--;
        contCol--;
        chAtual = String.valueOf(textArray[cont]);
        chClass = Classificacao.getOf(chAtual);
    }

    private void setLexema() {
        lexema = chAtual;
    }

    private void addChar() {
        lexema += chAtual;
        nextChar();
    }

    private void addLexema(Classificacao classe) {
        lexemas.add(new Lexema(lexema, classe, posInicial, contCol - 1, linha));
        lexema = "";
    }

    private void addErro(Classificacao classe) {
        lexemas.add(new Erro(lexema, classe, posInicial, contCol - 1, linha));
        lexema = "";
    }

    private void pularLinha() {
        linha++;
        contCol = 0;
    }

}
