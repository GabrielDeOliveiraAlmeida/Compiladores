/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analisadorSemantico;

import analisadorLexico.Classificacao;
import analisadorLexico.Lexema;
import analisadorSintatico.ErroSint;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.stream.Collectors;
import screens.MainScreenController;

/**
 *
 * @author gabriel
 */
public class AnalisadorSemantico {

    private ArrayList<Object> lexemas;
    private Lexema lexAtual;
    private static int cont;
    private int tam;
    private String grammarCheck;
    private ArrayList<ErroSint> erros;
    private ArrayList<ErroSemantico> errosSem;
    private boolean aux;
    private String expr;
    private ArrayList<Codigo> areaCode;
    private ArrayList<Simbolos> tabelaSimbolos;
    private int escopo;
    private String ultimoTipo;
    private int endereco;
    private String auxTipo;
    private int auxEscopo;
    private boolean parametro;
    private Deque<Lexema> auxLex;
    private Deque<Lexema> posFixa;
    private boolean parenEsq;

    public void execute(ArrayList<Object> lexemas) {
        grammarCheck = "\n Analise incompleta";
        erros = new ArrayList<>();
        errosSem = new ArrayList<>();
        tabelaSimbolos = new ArrayList<>();
        areaCode = new ArrayList<>();
        posFixa = new ArrayDeque<>();
        auxLex = new ArrayDeque<>();
        cont = 0;
        escopo = 0;
        endereco = 0;
        parametro = false;
        tam = lexemas.size();
        this.lexemas = lexemas;
        this.lexemas = lexemas.stream().filter(lex -> lex instanceof Lexema).collect(Collectors.toCollection(ArrayList::new));
        System.out.println("---------Iniciando Analise Semantica -----");
        nextLexema();
        program();
        System.out.println("-----------Fim da Analise Semantica");
        System.out.println("Tabela de Simbolos");
        tabelaSimbolos.forEach((a) -> {
            System.out.println(a.getLex().getLexema() + " -- " + a.getLex().getToken().toString() + "--" + a.getCategoria() + "--"
                    + a.getTipo() + "-- " + String.valueOf(a.getEscopo()) + "--- utilizada: " + a.isUtilizada());
            if (!a.isUtilizada()) {
                addErroSemLex(a.getLex(), ErroSem.NAOUTILIZADO);
            }
        });

        System.out.println("Erros");
        errosSem.forEach((ErroSemantico err) -> {;
            System.out.println(err.toString());
        });
        System.out.println("Area de codigo");
        areaCode.forEach((c) -> {
            System.out.println(c.getCode().toString() + " --- " + String.valueOf(c.getEnd()));
        });

    }

    private void setConsole(String message) {
        MainScreenController.instance.setConsole(message);
    }

    public AnalisadorSemantico() {

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

    private void addErroSemLex(Lexema lex, ErroSem err) {
        errosSem.add(new ErroSemantico(lex, err));
    }

    private void addErroSem(ErroSem err) {
        errosSem.add(new ErroSemantico(lexAtual, err));
    }

    private boolean match(String classe) {
        return classe.matches(Classificacao.RESERVADA.getRegex());
    }

    private void descartar() {
        while (true) {
            if (!hasNext()) {
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

    private boolean compareTipo(String... tipos) {
        for (String str : tipos) {
            if (auxTipo.equals(str)) {
                return true;
            }
        }
        return false;
    }

    private boolean descartarAte(Classificacao... classes) {
        boolean isDescartar = false;
        while (true) {
            for (Classificacao classe : classes) {
                if (checkToken(classe)) {
                    return isDescartar;
                }
            }
            isDescartar = true;
            nextLexema();
            System.out.println("Descartando Token: " + lexAtual.getLexema() + " Classe: " + lexAtual.getToken().toString());
            if (!hasNext()) {
                return isDescartar;
            }
        }
    }

    private void addCode(Instrucao code, String end) {
        areaCode.add(new Codigo(code, end));
    }

    private void addSimbolo(Categoria cat, String tipo, boolean utilizada) {
        tabelaSimbolos.add(new Simbolos(lexAtual, cat, escopo, tipo, endereco++, utilizada));
    }

    private void endProgram() {
        nextLexema();
        if (!hasNext()) {
            grammarCheck = "\n Analise sintática completa";
            if (!checkToken(Classificacao.DELIMITADOR_PONTO)) {
                addErro(Classificacao.DELIMITADOR_PONTO);
            }
        }
        System.out.println("Analise Sintatica Completa");
    }

    private boolean program() {
        if (checkToken(Classificacao.PALAVRA_RESERVADA_PROGRAM)) {
            addCode(Instrucao.INPP, null);
            nextLexema();
            if (checkToken(Classificacao.IDENTIFICADOR)) {
                addSimbolo(Categoria.Programa, "-", true);
                nextLexema();
                System.out.println("Program Ok");
                nextLexema();
                bloco();
                endProgram();
            }
        }
        return false;
    }

    private void bloco() {
        declaracao();
        System.out.println("-------Declaração Ok------------");
        int aux_escopo = escopo;
        if (subRotinas()) {
            if (checkToken(Classificacao.DELIMITADOR_PONTO_VIRGULA)) {
                nextLexema();
            } else {
                addErro(Classificacao.DELIMITADOR_PONTO_VIRGULA);
            }
        }
        escopo = aux_escopo;
        System.out.println("-------Sub-Rotinas Ok------------");
        comandoComposto();
        System.out.println("-------Comando Composto Ok------------");
    }

    private boolean pesquisarSimbolo() {
        return tabelaSimbolos.stream().noneMatch((s) -> (escopo == s.getEscopo() && s.getLex().getLexema().equals(lexAtual.getLexema())));
    }

    private void declaracao() {
        while (tipos()) {
            ultimoTipo = lexAtual.getToken().getRegex();
            nextLexema();
            listaIdentificadores();
            if (checkToken(Classificacao.DELIMITADOR_PONTO_VIRGULA)) {
                nextLexema();
            }
        }
        Lexema aux = lexAtual;
        if (descartarAte(Classificacao.PALAVRA_RESERVADA_BEGIN, Classificacao.PALAVRA_RESERVADA_PROCEDURE, Classificacao.PALAVRA_RESERVADA_INT,
                Classificacao.PALAVRA_RESERVADA_INTEGER, Classificacao.PALAVRA_RESERVADA_VAR,
                Classificacao.PALAVRA_RESERVADA_BOOLEAN, Classificacao.PALAVRA_RESERVADA_CHAR,
                Classificacao.PALAVRA_RESERVADA_REAL)) {
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
            if (pesquisarSimbolo()) {
                if (parametro) {
                    addSimbolo(Categoria.Variavel, ultimoTipo, true);
                } else {
                    addSimbolo(Categoria.Variavel, ultimoTipo, false);
                }
                addCode(Instrucao.AMEM, "1");
            } else {
                addErroSem(ErroSem.JADECLARADO);
            }
            passei_uma_vez_pelo_menos = true;
            nextLexema();
            if (!checkToken(Classificacao.DELIMITADOR_VIRGULA)) {
                return;
            }
            nextLexema();
        }
    }

    //SUB-rotinas
    private boolean subRotinas() {
        if (checkToken(Classificacao.PALAVRA_RESERVADA_PROCEDURE)) {
            escopo++;
            nextLexema();
            if (checkToken(Classificacao.IDENTIFICADOR)) {
                if (pesquisarSimbolo()) {
                    addSimbolo(Categoria.Procedure, "-", false);
                    //addCode(Instrucao.AMEM, 1);
                } else {
                    addErroSem(ErroSem.JADECLARADO);
                }
                nextLexema();
            }
            return subRotinaParametros();
        }
        return false;
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
            if (checkToken(Classificacao.DELIMITADOR_PONTO_VIRGULA)) {
                return true;
            } else {
                addErro(Classificacao.DELIMITADOR_PONTO_VIRGULA);
            }
        }
        return false;
    }

    private void getTipo() {
        int contador = cont;
        descartarAte(Classificacao.INTEIRO, Classificacao.COM_VIRGULA);
        ultimoTipo = lexAtual.getLexema();
        cont = contador;
    }

    private boolean parametros() {
        if (checkLexema("var")) {
            getTipo();
            nextLexema();
            parametro = true;
            listaIdentificadores();
            parametro = false;
            if (checkToken(Classificacao.DELIMITADOR_DOIS_PONTO)) {
                nextLexema();
            }
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

    private boolean writeread() {
        if (checkToken(Classificacao.IDENTIFICADOR)) {
            if (lexAtual.getLexema().equals("write")) {
                addSimbolo(Categoria.WRITE, "-", true);
                procedimento();
                return true;
            } else if (lexAtual.getLexema().equals("read")) {
                addSimbolo(Categoria.READ, "-", true);
                procedimento();
                return true;
            }
            
        }
        return false;
    }

    private boolean comando() {
        System.out.println("Comando ----------- " + lexAtual.getLexema());
        boolean aux = false;
        if (checkToken(Classificacao.PALAVRA_RESERVADA_END)) {
            return false;
        } else if (writeread()) {
            aux = true;
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
        } else {
            descartar();
//            backLexema();;
            System.out.println("Retrocesso do Lexema me levou a : " + lexAtual.getLexema());
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
            Simbolos s = getSimbolo(lexAtual);
            if (s == null) {
                addErroSem(ErroSem.NAOFOIDECLARADO);
                descartar();
                return false;
            } else {
                auxTipo = "qualquer";
                s.setUtilizada(true);
            }
            nextLexema();
            if (checkToken(Classificacao.DELIMITADOR_ABRE_PARENTESE)) {
                nextLexema();
                if (checkToken(Classificacao.DELIMITADOR_FECHA_PARENTESE)) {
                    nextLexema();
                    return true;
                }
                posFixa = new ArrayDeque<>();
                auxLex = new ArrayDeque<>();
                listaExpressoes(s.getCategoria());

                if (checkToken(Classificacao.DELIMITADOR_FECHA_PARENTESE)) {
                    nextLexema();
                    return true;
                } else {
                    addErro(Classificacao.DELIMITADOR_FECHA_PARENTESE);
                }
            } else {
                addErro(Classificacao.DELIMITADOR_ABRE_PARENTESE);
                descartar();
                setCont(cont - 2);
                return true;
            }
        }
        return false;
    }

    private Simbolos getSimbolo(Lexema lexAtual) {
        int esc = escopo;
        do {
            for (Simbolos s : tabelaSimbolos) {
                if (s.getEscopo() == esc && s.getLex().getLexema().equals(lexAtual.getLexema())) {
                    return s;
                }
            }
            esc--;
        } while (esc >= 0);
        return null;
    }

    private void resolverExp() {

        while (!posFixa.isEmpty()) {
            Lexema lex = posFixa.removeFirst();
            if (lex.getToken().equals(Classificacao.IDENTIFICADOR)) {
                Simbolos s = getSimbolo(lex);
                addCode(Instrucao.CRVL, s.getEndereco());

            } else if (lex.getToken().equals(Classificacao.COM_VIRGULA)
                    || lex.getToken().equals(Classificacao.INTEIRO)) {
                Simbolos s = getSimbolo(lex);
                addCode(Instrucao.CRCT, s.getLex().getLexema());
            } else {
                switch (lex.getToken()) {
                    case OPERADOR_SOMA:
                        addCode(Instrucao.SOMA, null);
                        break;
                    case OPERADOR_MENOS:
                        addCode(Instrucao.SUBT, null);
                        break;
                    case OPERADOR_MULTIPLICACAO:
                        addCode(Instrucao.MULT, null);
                        break;
                    case OPERADOR_DIV:
                        addCode(Instrucao.DIVI, null);
                        break;
                    case OPERADOR_MOD:
                        addCode(Instrucao.MODI, null);
                        break;
                }
            }

        }

    }

    private boolean atribuicao() {
        int aux = cont - 1;
        Lexema auxLexema = lexAtual;
//        System.out.println("Atribuicao ---- " + lexAtual.getLexema());

        if (checkToken(Classificacao.IDENTIFICADOR)) {
            Simbolos s = getSimbolo(lexAtual);
            if (s == null) {
                addErroSem(ErroSem.NAOFOIDECLARADO);
                return false;
            } else {
                auxTipo = s.getTipo();
                auxEscopo = s.getEscopo();
            }
            nextLexema();
            if (checkToken(Classificacao.OPERADOR_ATRIBUICAO)) {

                System.out.println("Atribuicao na variavel ");
                nextLexema();
                posFixa = new ArrayDeque<>();
                expressao();
                if (s != null) {
                    s.setUtilizada(true);
                }
                while (!auxLex.isEmpty()) {
                    posFixa.addLast(auxLex.removeLast());
                }
                expr = "";
                if (!posFixa.isEmpty()) {
                    posFixa.forEach(elem -> {
                        expr += elem.getLexema();
                    });
                }
                System.out.println("Fim da atribuicao == " + expr);

                resolverExp();
                addCode(Instrucao.ARMZ, s.getEndereco());
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
            Lexema op = lexAtual;
            resolverExp();
            nextLexema();
            expressaoSimples();
            switch (op.getToken()) {
                case OPERADOR_AND:
                    addCode(Instrucao.DISJ, null);
                    break;
                case OPERADOR_IGUAL:
                    addCode(Instrucao.CMIG, null);
                    break;
                case OPERADOR_MENOR:
                    addCode(Instrucao.CMME, null);
                    break;
                case OPERADOR_MAIOR:
                    addCode(Instrucao.CMMA, null);
                    break;
                case OPERADOR_DIFERENTE:
                    addCode(Instrucao.CMDG, null);
                    break;
                case OPERADOR_MAIOR_IGUAL:
                    addCode(Instrucao.CMAG, null);
                    break;
                case OPERADOR_MENOR_IGUAL:
                    addCode(Instrucao.CMEG, null);
                    break;
            }
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
                return termo();
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
                auxLex.addLast(lexAtual);
                if (checkToken(Classificacao.OPERADOR_AND)) {
                    if (!auxTipo.equals("boolean") || !auxTipo.equals("bool")) {
                        addErroSem(ErroSem.TIPOSDIFF);
                    }
                } else if (checkToken(Classificacao.OPERADOR_DIV)) {
                    if (!auxTipo.equals("real")) {
                        addErroSem(ErroSem.PRECISAREAL);
                    }
                } else if (auxTipo.equals("boolean") || auxTipo.equals("bool")) {
                    addErroSem(ErroSem.TIPOSDIFF);
                }

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
        if (checkToken(Classificacao.PALAVRA_RESERVADA_TRUE) || checkToken(Classificacao.PALAVRA_RESERVADA_FALSE)) {
            if (auxTipo.equals("boolean") || auxTipo.equals("bool")) {
                posFixa.addLast(lexAtual);
            } else {
                addErroSem(ErroSem.TIPOSDIFF);
            }
            return true;
        }
        return false;
    }

    private boolean sinal() {
        if (checkToken(Classificacao.OPERADOR_SOMA) || checkToken(Classificacao.OPERADOR_MENOS)) {
            auxLex.addLast(lexAtual);
            return true;
        }
        return false;
    }

    public boolean variavel() {
        if (checkToken(Classificacao.IDENTIFICADOR)) {
            Simbolos aux = getSimbolo(lexAtual);
            if (aux == null) {
                addErroSem(ErroSem.NAOFOIDECLARADO);
            } else if (!aux.isUtilizada()) {
                addErroSem(ErroSem.NAOFOIINICIALIZADA);
                aux.setUtilizada(true);
            } else if (!aux.getTipo().equals(auxTipo) && !auxTipo.equals("qualquer")) {
                if ((aux.getTipo().equals("int") || aux.getTipo().equals("integer")) && !auxTipo.equals("real")) {
                    addErroSem(ErroSem.TIPOSDIFF);
                }
            }
            posFixa.addLast(lexAtual);
            return true;
        }
        return false;
    }

    private boolean numero() {
        if (checkToken(Classificacao.COM_VIRGULA)) {
            if (auxTipo.equals("real") || auxTipo.equals("qualquer")) {
                addSimbolo(Categoria.Numero, "real", true);
                posFixa.addLast(lexAtual);
                return true;
            }
            addErroSem(ErroSem.TIPOSDIFF);
        } else if (checkToken(Classificacao.INTEIRO)) {
            if (!auxTipo.equals("boolean") || !auxTipo.equals("bool")) {
                addSimbolo(Categoria.Numero, "int", true);
                posFixa.addLast(lexAtual);
                return true;
            }
            addErroSem(ErroSem.TIPOSDIFF);
        }
        return false;
    }

    private boolean exprParenteses() {
        if (checkToken(Classificacao.DELIMITADOR_ABRE_PARENTESE)) {
            nextLexema();
            expressao();
            if (checkToken(Classificacao.DELIMITADOR_FECHA_PARENTESE)) {
                if (!auxLex.isEmpty()) {
                    posFixa.add(auxLex.removeLast());
                }
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
            if (!auxTipo.equals("boolean")) {
                addErroSem(ErroSem.TIPOSDIFF);
            }
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

    private boolean listaExpressoes(String cat) {
        Instrucao aux;
        if(cat.equals("WRITE")){
            aux = Instrucao.IMPE;
        }else if(cat.equals("READ")){
            aux = Instrucao.LEIT;
        }else{
            aux = null;
        }
        if(aux.equals(Instrucao.LEIT))
            addCode(aux, null);
        expressao();
        
        if(aux.equals(Instrucao.IMPE))
            addCode(aux, null);
        
        if(aux != null)
            resolverExp();
        
        while (checkToken(Classificacao.DELIMITADOR_VIRGULA)) {
            nextLexema();
            if(aux.equals(Instrucao.LEIT))
                addCode(aux, null);
            expressao();
            if(aux != null)
                resolverExp();
            if(aux.equals(Instrucao.IMPE))
                addCode(aux, null);
        }
        return true;
    }

}
