/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analisadorSintatico;

import analisadorLexico.Classificacao;
import analisadorLexico.Lexema;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 *
 * @author cc161255426
 */
public class AnalisadorSintatico {

    private ArrayList<Object> lexemas;
    private Lexema lexAtual;
    private int cont;
    private int tam;

    public void execute(ArrayList<Object> lexemas) {
        cont = 0;
        tam = lexemas.size();
        this.lexemas = lexemas;
        this.lexemas = lexemas.stream().filter(lex -> lex instanceof Lexema).collect(Collectors.toCollection(ArrayList::new));
        System.out.println("---------Iniciando Analise Sintatica -----");
        nextLexema();
        program();
        bloco();
    }

    public AnalisadorSintatico() {

    }

    private void nextLexema() {
//        while(cont < tam && lexemas.get(cont) instanceof Erro){;
//            cont++;
//        }
        if (cont < tam) {
            lexAtual = (Lexema) lexemas.get(cont++);
            System.out.println("Lexema = " + lexAtual.getLexema());
        }
    }
    
    private boolean hasNext(){
        return cont<tam;
    }

    private boolean checkLexema(String str) {
        return lexAtual.getLexema().equals(str);

    }

    private boolean checkToken(Classificacao classe) {
        return lexAtual.getToken().equals(classe);
    }

    private boolean program() {
        if (checkLexema("program")) {
            nextLexema();
            if (checkToken(Classificacao.IDENTIFICADOR)) {
                nextLexema();
                if (checkToken(Classificacao.DELIMITADOR_PONTO_VIRGULA)) {
                    System.out.println("Program Ok");
                    nextLexema();
                    return true;
                }
            }
        }
        System.out.println("Erro");
        return false;
    }

    private void bloco() {
        declaracao();
        System.out.println("-------Declaração Ok------------");
        subRotinas();
        System.out.println("-------Sub-Rotinas Ok------------");
        comandoComposto();
        System.out.println("-------Comando Composto Ok------------");
    }

    private void declaracao() {
        while (tipos()) {
            nextLexema();
            listaIdentificadores();
            if (checkToken(Classificacao.DELIMITADOR_PONTO_VIRGULA)) {
                System.out.println("Declaração Ok");
                nextLexema();
            }
//            else {
//            System.out.println("Erro Declaração variaveis - espera-se ponto e virgula, taok");
//            }
        }
    }

    private boolean tipos() {
        return checkToken(Classificacao.PALAVRA_RESERVADA_INT) || checkToken(Classificacao.PALAVRA_RESERVADA_CHAR)
                || checkToken(Classificacao.PALAVRA_RESERVADA_INTEGER) || checkToken(Classificacao.PALAVRA_RESERVADA_VAR) || checkToken(Classificacao.PALAVRA_RESERVADA_BOOLEAN);
    }

    private void listaIdentificadores() {
        while (checkToken(Classificacao.IDENTIFICADOR)) {
            nextLexema();
//            if (checkToken(Classificacao.DELIMITADOR_PONTO_VIRGULA)) {
//                System.out.println("Declaração Ok");
//                return;
//            }
            if (!checkToken(Classificacao.DELIMITADOR_VIRGULA)) {
                return;
//                if(tipos()){
//                    System.out.println("Erro Declaração variaveis - espera-se ponto e virgula, taok");
//                }else{
//                    System.out.println("Erro Declaração variaveis - espera-se ponto e virgula, taok");
//                    return;
//                };
            }

            nextLexema();
        }
    }

    //SUB-rotinas
    private void subRotinas() {
        if (checkLexema("procedure")) {
            nextLexema();
            if (checkToken(Classificacao.IDENTIFICADOR)) {
                nextLexema();

                parametrosFormais();
                nextLexema();
                parametrosFormais();
                if (checkToken(Classificacao.DELIMITADOR_PONTO_VIRGULA)) {
                    nextLexema();
                } else {
                    System.out.println("Erro Parametros formais - espera-se ponto e virgula, taok");
                }
                bloco();
            }
        } else {
            System.out.println("Não há procedures");
        }
    }

    private void parametrosFormais() {
        if (checkToken(Classificacao.DELIMITADOR_ABRE_PARENTESE)) {
            nextLexema();
            //enquanto houver parametros
            while (parametros()) {
                nextLexema();
                if (checkToken(Classificacao.DELIMITADOR_PONTO_VIRGULA)) {
                    nextLexema();
                } else if (checkToken(Classificacao.DELIMITADOR_FECHA_PARENTESE)) {
                    return;
                }
            }
        }
    }

    private boolean parametros() {
        if (checkLexema("var")) {
            nextLexema();
            listaIdentificadores();
            nextLexema();
            if (checkToken(Classificacao.DELIMITADOR_DOIS_PONTO)) {
                nextLexema();
                if (!tipos()) {
                    System.out.println("Erro parametros - espera-se tipo da variavel, taok");
                }
            } else {
                System.out.println("Erro Declaração variaveis - espera-se ponto e virgula, taok");
            }
        } else {
            return false;
        }
        return true;

    }

    //----------------------------------------------------------------------------------------------
    //comandos
    private boolean comandoComposto() {
        if (!checkLexema("begin")) {
            System.out.println("Erro bloco composto - espera-se 'begin', taok");
            return false;
        } else {
            nextLexema();
            while (comando()) {
                nextLexema();
                if(checkLexema("end")){
                    if(hasNext()){
                        System.out.println("Erro bloco composto - espera-se 'end', taok");
                        return false;
                    }else{
                        return true;
                    }
                }
            }
            return true;
        }
    }

    private boolean comando() {
        System.out.println("Comando -----------");
        boolean aux = false;
        if(atribuicao()){
            aux = true;
        }else if(comandoComposto()){
            aux = true;
        }
        if(aux && checkToken(Classificacao.DELIMITADOR_PONTO_VIRGULA)){
            return true;
        }
        return false;
    }

    private boolean condicional(){
        return true;
    }
    private boolean repeticao(){
        return true;
    }
    private boolean atribuicao() {
        if (checkToken(Classificacao.IDENTIFICADOR)) {
            nextLexema();
            if (checkToken(Classificacao.OPERADOR_ATRIBUICAO)) {
                System.out.println("Atribuicao na variavel ");
                nextLexema();
                expressao();
                System.out.println("Fim da atribuicao");
            }
            return true;
        }else return false;
    }

    private void expressao() {
        expressaoSimples();
        //nextLexema();
        while (relacao()) {
            nextLexema();
            expressaoSimples();
        }

    }

    private boolean expressaoSimples() {
        if (sinal()) {
            System.out.println(" Tem sinal ");
            nextLexema();
        }
        if (termo()) {
            nextLexema();
            while (sinal() || checkToken(Classificacao.OPERADOR_OR)) {
                nextLexema();
                if (termo()) {
                    nextLexema();
                    return true;
                }else{
                    return false;
                }
            }
            return true;
        } else {
            System.out.println("Erro expressão simples - Expera-se <termo>");
            return false;
        }
    }

    private boolean termo() {
        if (fator()) {
            while (checkToken(Classificacao.OPERADOR_AND) || checkToken(Classificacao.OPERADOR_DIV) || checkToken(Classificacao.OPERADOR_MULTIPLICACAO)) {
                System.out.println(lexAtual.getLexema());
                nextLexema();
                fator();
            }
            return true;
        } else {
            System.out.println("Erro Termo - Espera-se <fator>");
            return false;
        }
    }

    private boolean fator() {
        int contador = cont;
        if (variavel() || numero() || exprParenteses() || notFator() || preDeclaradas()) {
            return true;
        } else {
            System.out.println("Erro fator");
            return false;
        }

    }

    private boolean preDeclaradas(){
        return checkToken(Classificacao.PALAVRA_RESERVADA_TRUE) || checkToken(Classificacao.PALAVRA_RESERVADA_FALSE);
    }
    
    private boolean sinal() {
        return checkToken(Classificacao.OPERADOR_SOMA) || checkToken(Classificacao.OPERADOR_MENOS);
    }

    public boolean variavel() {
        return checkToken(Classificacao.IDENTIFICADOR);
    }

    private boolean numero() {
        return checkToken(Classificacao.COM_VIRGULA) || checkToken(Classificacao.INTEIRO);
    }

    private boolean exprParenteses() {
        if (checkToken(Classificacao.DELIMITADOR_ABRE_PARENTESE)) {
            nextLexema();
            expressao();
            nextLexema();
            if (checkToken(Classificacao.DELIMITADOR_FECHA_PARENTESE)) {
                return true;
            }
            return true;
        } else {
            return false;
        }
    }

    private boolean notFator() {
        if (checkToken(Classificacao.OPERADOR_NOT)) {
            nextLexema();
            fator();
            return true;
        } else {
            return false;
        }
    }

    private boolean relacao() {
        return checkToken(Classificacao.OPERADOR_MENOR_IGUAL) || checkToken(Classificacao.OPERADOR_MENOR) || checkToken(Classificacao.OPERADOR_MAIOR)
                || checkToken(Classificacao.OPERADOR_MAIOR_IGUAL) || checkToken(Classificacao.OPERADOR_IGUAL) || checkToken(Classificacao.OPERADOR_DIFERENTE);
    }

}
