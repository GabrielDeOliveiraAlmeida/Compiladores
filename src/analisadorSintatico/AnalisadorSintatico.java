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
import screens.MainScreenController;

/**
 *
 * @author cc161255426
 */
public class AnalisadorSintatico {

    private ArrayList<Object> lexemas;
    private Lexema lexAtual;
    private int cont;
    private int tam;


    private ArrayList<ErroSint> erros;

    public void execute(ArrayList<Object> lexemas) {
        erros = new ArrayList<>();
        cont = 0;
        tam = lexemas.size();
        this.lexemas = lexemas;
        this.lexemas = lexemas.stream().filter(lex -> lex instanceof Lexema).collect(Collectors.toCollection(ArrayList::new));
        System.out.println("---------Iniciando Analise Sintatica -----");
        nextLexema();
        program();
        setConsole(erros.toString());
    }

    public AnalisadorSintatico() {

    }

    private void setCont(int cont){
        this.cont = cont;
        lexAtual = (Lexema) lexemas.get(this.cont++);
        System.out.println("Retrocesso de Lexema = " + lexAtual.getLexema());
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

    private boolean hasNext() {
        return cont < tam;
    }

    private boolean checkLexema(String str) {
        return lexAtual.getLexema().equals(str);

    }

    private boolean checkToken(Classificacao classe) {
        return lexAtual.getToken().equals(classe);
    }

    private void addErro(Classificacao esperado) {
        System.out.println("Foi encontrado " + lexAtual.getLexema() + " , linha " + lexAtual.getLinha()
                + " e coluna  " + lexAtual.getColuna_inicial() + " - " + lexAtual.getColuna_final()
                + ", espera-se " + esperado.toString());
        erros.add(new ErroSint(lexAtual, esperado));
    }

    private boolean program() {
        if (checkLexema("program")) {
            nextLexema();
            if (checkToken(Classificacao.IDENTIFICADOR)) {
                nextLexema();
                if (checkToken(Classificacao.DELIMITADOR_PONTO_VIRGULA)) {
                    System.out.println("Program Ok");
                    nextLexema();
                    bloco();
                    nextLexema();
                    if (checkToken(Classificacao.DELIMITADOR_PONTO) && !hasNext()) {
                        System.out.println("Analise Sintatica Completa");
                    }
                    return true;
                }
            }
        }
        addErro(Classificacao.PALAVRA_RESERVADA_PROGRAM);
        return false;
    }

    private void bloco() {
        declaracao();
        System.out.println("-------Declaração Ok------------");
        if(subRotinas()){
            System.out.println(lexAtual.getLexema());
            if (checkToken(Classificacao.DELIMITADOR_PONTO_VIRGULA)) {
                nextLexema();
            }else{
                addErro(Classificacao.DELIMITADOR_PONTO_VIRGULA);
            }
        }
        System.out.println("-------Sub-Rotinas Ok------------");
        comandoComposto();
        System.out.println("-------Comando Composto Ok------------");
    }

    private void declaracao() {
//        boolean passei_uma_vez_pelo_menos = false;
        while (tipos()) {
//            passei_uma_vez_pelo_menos = true;
            nextLexema();
            listaIdentificadores();
            if (checkToken(Classificacao.DELIMITADOR_PONTO_VIRGULA)) {
                nextLexema();
            } else {
                addErro(Classificacao.DELIMITADOR_PONTO_VIRGULA);
            }
        }
//        if(!passei_uma_vez_pelo_menos) addErro(Classificacao.PALAVRA_RESERVADA_VAR);
    }

    private boolean tipos() {
        return checkToken(Classificacao.PALAVRA_RESERVADA_INT) || checkToken(Classificacao.PALAVRA_RESERVADA_CHAR)
                || checkToken(Classificacao.PALAVRA_RESERVADA_INTEGER) || checkToken(Classificacao.PALAVRA_RESERVADA_VAR) || checkToken(Classificacao.PALAVRA_RESERVADA_BOOLEAN);
    }

    private void listaIdentificadores() {
        boolean passei_uma_vez_pelo_menos = false;
        while (checkToken(Classificacao.IDENTIFICADOR)) {
            passei_uma_vez_pelo_menos = true;
            nextLexema();
            if (!checkToken(Classificacao.DELIMITADOR_VIRGULA)) {
                return;
            }
            nextLexema();
        }
        if (!passei_uma_vez_pelo_menos) {
            addErro(Classificacao.IDENTIFICADOR);
        }
    }

    //SUB-rotinas
    private boolean subRotinas() {
        if (checkLexema("procedure")) {
            nextLexema();
            if (checkToken(Classificacao.IDENTIFICADOR)) {
                nextLexema();
                parametrosFormais();
                if (checkToken(Classificacao.DELIMITADOR_PONTO_VIRGULA)) {
                    nextLexema();
                    bloco();
                    nextLexema();
                } else {
                    System.out.println("Erro Parametros formais - espera-se ponto e virgula, taok");
                }
                return true;
            }
        } else {
            System.out.println("Não há procedures");
            return false;
        }
        return false;
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
                    nextLexema();
                    return;
                } else {
                    addErro(Classificacao.DELIMITADOR_FECHA_PARENTESE);
                }
            }
        } else {
            addErro(Classificacao.DELIMITADOR_ABRE_PARENTESE);
        }
    }

    private boolean parametros() {
        if (checkLexema("var")) {
            nextLexema();
            listaIdentificadores();
            if (checkToken(Classificacao.DELIMITADOR_DOIS_PONTO)) {
                nextLexema();
                if (!tipos()) {
                    addErro(Classificacao.PALAVRA_RESERVADA_CONST);
                }
            } else {
                addErro(Classificacao.DELIMITADOR_PONTO_VIRGULA);
            }
        } else {
            addErro(Classificacao.PALAVRA_RESERVADA_VAR);
            return false;
        }
        return true;

    }

    //----------------------------------------------------------------------------------------------
    //comandos
    private boolean comandoComposto() {
        System.out.println("ComandoComposto ---- " + lexAtual.getLexema());
        if (!checkLexema("begin")) {
            addErro(Classificacao.PALAVRA_RESERVADA_BEGIN);
            return false;
        } else {
            nextLexema();
            while (comando()) {
                nextLexema();
                if (checkLexema("end")) {
                    return true;
                }
            }
            return true;
        }
    }

    private boolean comando() {
        System.out.println("Comando -----------" );
        boolean aux = false;
        if (atribuicao()) {
            aux = true;
        } else if(condicional()){
            aux = true;
        } else if(repeticao()){
            aux = true;
        } else if(procedimento()){
            aux = true;
        }else if (comandoComposto()) {
            aux = true;
        }
        if (aux && checkToken(Classificacao.DELIMITADOR_PONTO_VIRGULA)) {
            return true;
        }
        return false;
    }

    private boolean condicional() {
//        System.out.println("Condição ---- " + lexAtual.getLexema());
        if (checkToken(Classificacao.PALAVRA_RESERVADA_IF)) {
            nextLexema();
            if (expressao()) {
                if (checkToken(Classificacao.PALAVRA_RESERVADA_THEN)) {
                    nextLexema();
                    if (comando()) {//se Houver else
                        nextLexema();
                        if (checkToken(Classificacao.PALAVRA_RESERVADA_ELSE)) {
                            nextLexema();
                            if (comando()) {
                                return true;
                            }
                        }else{
                            return true;
                        }
                    }
                }else{
                    addErro(Classificacao.PALAVRA_RESERVADA_THEN);
                    return false;
                }
            }else{;
                //Erro <expressão>
            }
        }
        return false;
    }

    private boolean repeticao() {
//        System.out.println("Repetição ---- " + lexAtual.getLexema());
        if(checkToken(Classificacao.PALAVRA_RESERVADA_WHILE)){
            nextLexema();
            if(expressao()){
                if(checkToken(Classificacao.PALAVRA_RESERVADA_DO)){
                    nextLexema();
                    if(comando()){
                        return true;
                    }else return true;
                }else{
                    addErro(Classificacao.PALAVRA_RESERVADA_DO);
                }
            }else{
                System.out.println("Erro falta expressão");
                return false;
            }
        }
        return false;
    }

    private boolean procedimento(){
//        System.out.println("Procedimento ---- " + lexAtual.getLexema() + " --- " + lexAtual.getToken().toString());;
        if(checkToken(Classificacao.IDENTIFICADOR)){
            nextLexema();
            if(checkToken(Classificacao.DELIMITADOR_ABRE_PARENTESE)){
                nextLexema();
                if(checkToken(Classificacao.DELIMITADOR_FECHA_PARENTESE)){
                    nextLexema();
                    return true;
                }else{
                    addErro(Classificacao.DELIMITADOR_FECHA_PARENTESE);
                }
            }else{
                addErro(Classificacao.DELIMITADOR_ABRE_PARENTESE);
            }
        }
        return false;
    }
    
    private boolean atribuicao() {
        int aux = cont-1;
//        System.out.println("Atribuicao ---- " + lexAtual.getLexema());
        if (checkToken(Classificacao.IDENTIFICADOR)) {
            nextLexema();
            if (checkToken(Classificacao.OPERADOR_ATRIBUICAO)) {
                System.out.println("Atribuicao na variavel ");
                nextLexema();
                expressao();
                System.out.println("Fim da atribuicao");
                return true;
            }else{
                setCont(aux);
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean expressao() {
        expressaoSimples();
        //nextLexema();
        while (relacao()) {
            nextLexema();
            expressaoSimples();
        }
        return true;
    }

    private boolean expressaoSimples() {
        if (sinal()) {
            nextLexema();
        }
        if (termo()) {
            nextLexema();
            while (sinal() || checkToken(Classificacao.OPERADOR_OR)) {
                nextLexema();
                if (termo()) {
                    nextLexema();
                    return true;
                } else {
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
            while (checkToken(Classificacao.OPERADOR_AND) || checkToken(Classificacao.OPERADOR_DIV) || 
                    checkToken(Classificacao.OPERADOR_MULTIPLICACAO)) {
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

    private boolean preDeclaradas() {
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
            if (checkToken(Classificacao.DELIMITADOR_FECHA_PARENTESE)) {
                return true;
            }else{
                addErro(Classificacao.DELIMITADOR_FECHA_PARENTESE);
                return false;
            }
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
    
    private void setConsole(String message){
        MainScreenController.instance.setConsole(message);
    }

}
