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
    private static int cont;
    private int tam;
    private String grammarCheck;
    private ArrayList<ErroSint> erros;
    private boolean aux;
    private String expr;

    public void execute(ArrayList<Object> lexemas) {
        grammarCheck = "\n Analise incompleta";
        erros = new ArrayList<>();
        cont = 0;
        tam = lexemas.size();
        this.lexemas = lexemas;
        this.lexemas = lexemas.stream().filter(lex -> lex instanceof Lexema).collect(Collectors.toCollection(ArrayList::new));
        System.out.println("---------Iniciando Analise Sintatica -----");
        nextLexema();
        program();
        setConsole(erros.toString() + grammarCheck);
    }
    
    public boolean getErros(){
        return erros.isEmpty();
    }

    private void setConsole(String message) {
        MainScreenController.instance.setConsole(message);
    }

    public AnalisadorSintatico() {

    }

    private void setCont(int cont) {
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
            System.out.println("Next Lexema = " + lexAtual.getLexema());
        }
    }

    private void backLexema() {
        lexAtual = (Lexema) lexemas.get(--cont);
        System.out.println("Back Lexema = " + lexAtual.getLexema());
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

    private boolean match(String classe) {
        return classe.matches(Classificacao.RESERVADA.getRegex());
    }

    private void descartar() {
        while (true) {
            if(!hasNext()){
                return;
            }
            if (checkToken(Classificacao.DELIMITADOR_PONTO_VIRGULA)) {
                nextLexema();
                break;
            }
            if (match(lexAtual.getLexema())) {
                //nextLexema();
                break;
            }
            System.out.println("Descartando Token: " + lexAtual.getLexema() + " Classe: " + lexAtual.getToken().toString());
            nextLexema();
        }
        
        System.out.println("Erro Program -- recuperação da analise sintatica: " + lexAtual.getLexema() + " Classe: " + lexAtual.getToken().toString());
    }
    
    
    private boolean descartarAte(Classificacao... classes){
        boolean isDescartar = false;
        while(true){
            for(Classificacao classe : classes){
                if (checkToken(classe)) {
                    return isDescartar;
                }    
            }
            isDescartar = true;
            nextLexema();
            System.out.println("Descartando Token: " + lexAtual.getLexema() + " Classe: " + lexAtual.getToken().toString());   
            if(!hasNext()) return isDescartar;
        }
    }

    private void endProgram() {
        nextLexema();
        if(!hasNext()){
            grammarCheck = "\n Analise sintática completa";
            if(!checkToken(Classificacao.DELIMITADOR_PONTO)) {
               addErro(Classificacao.DELIMITADOR_PONTO);
            }
        }    
        
        System.out.println("Analise Sintatica Completa");
    }

    private boolean program() {
        if (checkToken(Classificacao.PALAVRA_RESERVADA_PROGRAM)) {
            nextLexema();
            if (checkToken(Classificacao.IDENTIFICADOR)) {
                nextLexema();
                if (checkToken(Classificacao.DELIMITADOR_PONTO_VIRGULA)) {
                    System.out.println("Program Ok");
                    nextLexema();
                    bloco();
                    endProgram();
                } else {
                    addErro(Classificacao.DELIMITADOR_PONTO_VIRGULA);
                    bloco();
                    endProgram();
                }
            }
        } else {
            addErro(Classificacao.PALAVRA_RESERVADA_PROGRAM);
            descartar();
            bloco();
            endProgram();
        }
        return false;
    }

    private void bloco() {
        declaracao();
        System.out.println("-------Declaração Ok------------");
        if (subRotinas()) {
            System.out.println("sbusbusub= " + lexAtual.getLexema());
            if (checkToken(Classificacao.DELIMITADOR_PONTO_VIRGULA)) {
                nextLexema();
            } else {
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
                backLexema();
                descartar();
            }
        }
        Lexema aux = lexAtual;
        if(descartarAte(Classificacao.PALAVRA_RESERVADA_BEGIN, Classificacao.PALAVRA_RESERVADA_PROCEDURE, Classificacao.PALAVRA_RESERVADA_INT, 
                Classificacao.PALAVRA_RESERVADA_INTEGER, Classificacao.PALAVRA_RESERVADA_VAR, 
                Classificacao.PALAVRA_RESERVADA_BOOLEAN, Classificacao.PALAVRA_RESERVADA_CHAR,
                Classificacao.PALAVRA_RESERVADA_REAL)){
            Lexema aux2 = lexAtual;
            lexAtual = aux;
            addErro(Classificacao.PALAVRA_RESERVADA_BEGIN);
            lexAtual = aux2;
            declaracao();
        }
        
//        if(!passei_uma_vez_pelo_menos) addErro(Classificacao.PALAVRA_RESERVADA_VAR);
    }

    private boolean tipos() {
        return checkToken(Classificacao.PALAVRA_RESERVADA_INT) || checkToken(Classificacao.PALAVRA_RESERVADA_CHAR)
                || checkToken(Classificacao.PALAVRA_RESERVADA_INTEGER) || checkToken(Classificacao.PALAVRA_RESERVADA_VAR) || checkToken(Classificacao.PALAVRA_RESERVADA_BOOLEAN)
                || checkToken(Classificacao.PALAVRA_RESERVADA_REAL);
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
        if (checkToken(Classificacao.PALAVRA_RESERVADA_PROCEDURE)) {
            nextLexema();
            if (checkToken(Classificacao.IDENTIFICADOR)) {
                nextLexema();
            } else {
                addErro(Classificacao.IDENTIFICADOR);
                descartar();
                backLexema();
                backLexema();
            }
            return subRotinaParametros();
        } else {
            System.out.println("Não há procedures");
            return false;
        }
    }

    private boolean subRotinaParametros() {
        if (parametrosFormais()) {
            if (checkToken(Classificacao.DELIMITADOR_PONTO_VIRGULA)) {
                nextLexema();
                bloco();
                return true;
            } else {
                addErro(Classificacao.DELIMITADOR_PONTO_VIRGULA);
                return true;
            }
        } else {
            bloco();
            return true;
        }
    }

    private boolean parametrosFormais() {
        if (checkToken(Classificacao.DELIMITADOR_ABRE_PARENTESE)) {
            nextLexema();
            //enquanto houver parametros
            while (parametros()) {
                nextLexema();
                if (checkToken(Classificacao.DELIMITADOR_PONTO_VIRGULA)) {
                    nextLexema();
                } else if (checkToken(Classificacao.DELIMITADOR_FECHA_PARENTESE)) {
                    nextLexema();
                    return true;
                } else {
                    addErro(Classificacao.DELIMITADOR_FECHA_PARENTESE);
                    descartar();
                    return false;
                }
            }
        } else {
            if (checkToken(Classificacao.DELIMITADOR_PONTO_VIRGULA)){
                return true;
            }
            else{
                addErro(Classificacao.DELIMITADOR_PONTO_VIRGULA);
            }
        }
        return false;
    }

    private boolean parametros() {
        if (checkLexema("var")) {
            nextLexema();
            listaIdentificadores();
            if (checkToken(Classificacao.DELIMITADOR_DOIS_PONTO)) {
                nextLexema();
                if (!tipos()) {
                    addErro(Classificacao.PALAVRA_RESERVADA_CONST);
                    backLexema();
                }
            } else {
                addErro(Classificacao.DELIMITADOR_PONTO_VIRGULA);
                descartar();
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
        if (!checkToken(Classificacao.PALAVRA_RESERVADA_BEGIN)) {
            //addErro(Classificacao.PALAVRA_RESERVADA_BEGIN);
//            descartar();
//            bloco();
            return false;
        } else {
            nextLexema();
            comando();
//            if(checkToken(Classificacao.DELIMITADOR_PONTO_VIRGULA)) {;
//                //nextLexema();
//                comandoComposto();
//            }else if(checkToken(Classificacao.PALAVRA_RESERVADA_END)){
//                return false;
//            }else{
//                addErro(Classificacao.DELIMITADOR_PONTO_VIRGULA);
//            }
            System.out.println("Comando Composto possui  --- " + lexAtual.getLexema());
            while (!checkToken(Classificacao.PALAVRA_RESERVADA_END)) {

                if (!checkToken(Classificacao.DELIMITADOR_PONTO_VIRGULA)) {
                    addErro(Classificacao.DELIMITADOR_PONTO_VIRGULA);
                    backLexema();
                }
                nextLexema();
                if (!comando()) {
                    return false;
                }
            }
            nextLexema();
            return true;
        }
    }

    private boolean comando() {
        System.out.println("Comando ----------- " + lexAtual.getLexema());
        boolean aux = false;
        if (checkToken(Classificacao.PALAVRA_RESERVADA_END)) {
            return false;
        } else if (atribuicao()) {
            aux = true;
        } else if (condicional()) {
            aux = true;
        } else if (repeticao()) {
            aux = true;
        } else if (procedimento()) {
            aux = true;
        } else if (comandoComposto()) {
            aux = true;
        }else{
            descartar();
//            backLexema();;
            System.out.println("Retrocesso do Lexema me levou a : "+lexAtual.getLexema());
            aux = true;
        }
//        }else{
//            addErro(Classificacao.DELIMITADOR_PONTO_VIRGULA);
//            return false;
//        }
        return aux;
    }
    
    private boolean condicional() {
//        System.out.println("Condição ---- " + lexAtual.getLexema());
        if (checkToken(Classificacao.PALAVRA_RESERVADA_IF)) {
            nextLexema();
            if (expressao()) {
                System.out.println("Expressao condicional ---- OK ");
                if (checkToken(Classificacao.PALAVRA_RESERVADA_THEN)) {
                    nextLexema();
                    comando();
                    System.out.println("Condicional - " + lexAtual.getLexema());
                    //nextLexema();
                    if (checkToken(Classificacao.PALAVRA_RESERVADA_ELSE)) {//se Houver else
                        nextLexema();
                        if (comando()) {
                            return true;
                        }
                    } else {
                        //backLexema();
//                        if (checkToken(Classificacao.DELIMITADOR_PONTO_VIRGULA)) {;
//                            setCont(cont - 1);
//                        } else {
//                            setCont(cont - 2);
//                        }
                        return true;
                    }

                } else {
                    addErro(Classificacao.PALAVRA_RESERVADA_THEN);
                    descartar();
                    comando();
                    System.out.println("Condicional - " + lexAtual.getLexema());
                    return true;
                }
            } else {;
                //Erro <expressão>
            }
        }
        return false;
    }

    private boolean repeticao() {
//        System.out.println("Repetição ---- " + lexAtual.getLexema());
        if (checkToken(Classificacao.PALAVRA_RESERVADA_WHILE)) {
            nextLexema();
            if (expressao()) {
                System.out.println("Expressão while ----------- OK");
                if (checkToken(Classificacao.PALAVRA_RESERVADA_DO)) {
                    nextLexema();
                    comando();
                    return true;
                } else {
                    addErro(Classificacao.PALAVRA_RESERVADA_DO);
                    descartar();
                    comando();
                    return true;
                }
            } else {
                System.out.println("Erro falta expressão");
                return false;
            }
        }
        return false;
    }

    private boolean procedimento() {
//        System.out.println("Procedimento ---- " + lexAtual.getLexema() + " --- " + lexAtual.getToken().toString());;
        if (checkToken(Classificacao.IDENTIFICADOR)) {
            nextLexema();
            if (checkToken(Classificacao.DELIMITADOR_ABRE_PARENTESE)) {
                nextLexema();
                if (checkToken(Classificacao.DELIMITADOR_FECHA_PARENTESE)){
                    nextLexema();
                    return true;
                }
                listaExpressoes();
                if (checkToken(Classificacao.DELIMITADOR_FECHA_PARENTESE)) {
                    nextLexema();
                    return true;
                } else {
                    addErro(Classificacao.DELIMITADOR_FECHA_PARENTESE);
                }
            } else {
                addErro(Classificacao.DELIMITADOR_ABRE_PARENTESE);
                descartar();
                setCont(cont-2);
                return true;
            }
        }
        return false;
    }

    private boolean atribuicao() {
        int aux = cont - 1;
//        System.out.println("Atribuicao ---- " + lexAtual.getLexema());
        if (checkToken(Classificacao.IDENTIFICADOR)) {
            nextLexema();
            if (checkToken(Classificacao.OPERADOR_ATRIBUICAO)) {
                System.out.println("Atribuicao na variavel ");
                nextLexema();
                expressao();
                System.out.println("Fim da atribuicao == " + expr);
                return true;
            } else {
//                if(checkToken(Classificacao.DELIMITADOR_PONTO_VIRGULA)){
//                    addErro(Classificacao.OPERADOR_ATRIBUICAO);
//                }
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
            while (sinal() || checkToken(Classificacao.OPERADOR_OR)) {
                nextLexema();
                if (termo()) {
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
            nextLexema();
            if (checkToken(Classificacao.OPERADOR_AND) || checkToken(Classificacao.OPERADOR_DIV)
                    || checkToken(Classificacao.OPERADOR_MULTIPLICACAO)) {
                nextLexema();
                termo();
            }
            return true;
        } else {
            System.out.println("Erro Termo - Espera-se <fator>");
            return false;
        }
    }

    private boolean fator() {
        if (variavel() || numero() || exprParenteses() || notFator() || preDeclaradas()) {
            return true;
        } else {
            addErro(Classificacao.EXPRESSAO);
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
            } else {
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

    private boolean listaExpressoes() {
        expressao();
        System.out.println(lexAtual.getLexema());
        while (checkToken(Classificacao.DELIMITADOR_VIRGULA)) {
            nextLexema();
            expressao();
        }
        return true;
    }
}
