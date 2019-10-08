/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analisadorLexico;

import java.util.regex.Pattern;

/**
 * 
 * Adaptado de
 * https://github.com/evernife/Compiladores-Atividades/blob/master/src/main/java/br/com/finalcraft/unesp/compiladores/atividades/logical/lexema/LexemaType.java 
 */
public enum Classificacao {
    CARACTER_INVALIDO("[@|#|&|¨|!|\\?]"),
    INTEIRO("0|[1-9][0-9]{0,10}"),
    COM_VIRGULA("(0|([1-9][0-9]{0,10}))(\\.[0-9]{1,10})?$"),
    IDENTIFICADOR("[_|a-z|A-Z][a-z|A-Z|0-9|_]{0,19}"),
    PALAVRA_RESERVDA("if|then|else|begin|end|while|do|program|"
            + "procedure|true|false|char|integer|int|boolean|const|and|or|not|mod|in|var|div"),
    COMENTARIO(Pattern.quote("//")),
    COMENTARIO_BLOCO("[{]"),
    OPERADORES("\\+|\\-|\\*|\\/|=|<>|>|<|<=|>=|(:=)"),
    DELIMITADOR("\\.|\\,|;|:|\\(|\\)|\\[|\\]"),
    BRANCO(" |\t|\r"),
    PULA_LINHA("\n"),
    
    //ERROS
    INTEIRO_LONGO("0|[1-9][0-9]{10,}"),
    COM_VIRGULA_LONGO_ANTES("(0|([1-9][0-9]{10,}))(\\.[0-9]{1,10})?$"),
    COM_VIRGULA_LONGO_DEPOIS("(0|([1-9][0-9]{1,10}))(\\.[0-9]{10,})?$"),
    COM_VIRGULA_LONGO_ANTES_DEPOIS("(0|([1-9][0-9]{10,}))(\\.[0-9]{10,})?$"),
    IDENTIFICADOR_LONGO("[_|a-z|A-Z][a-z|A-Z|0-9|_]{19,}"),
    
    PALAVRA_RESERVADA_IF("if"),
    PALAVRA_RESERVADA_THEN("then"),
    PALAVRA_RESERVADA_ELSE("else"),
    PALAVRA_RESERVADA_BEGIN("begin"),
    PALAVRA_RESERVADA_END("end"),
    PALAVRA_RESERVADA_WHILE("while"),
    PALAVRA_RESERVADA_DO("do"),
    PALAVRA_RESERVADA_PROGRAM("program"),
    PALAVRA_RESERVADA_PROCEDURE("procedure"),
    PALAVRA_RESERVADA_VAR("var"),
    PALAVRA_RESERVADA_TRUE("true"),
    PALAVRA_RESERVADA_FALSE("false"),
    PALAVRA_RESERVADA_CHAR("char"),
    PALAVRA_RESERVADA_INTEGER("integer"),
    PALAVRA_RESERVADA_INT("int"),
    PALAVRA_RESERVADA_BOOLEAN("boolean"),
    PALAVRA_RESERVADA_CONST("const"),
    DELIMITADOR_ABRE_PARENTESE("\\("),
    DELIMITADOR_FECHA_PARENTESE("\\)"),
    DELIMITADOR_ABRE_COLCHETE(Pattern.quote("[")),
    DELIMITADOR_FECHA_COLCHETE(Pattern.quote("]")),
    DELIMITADOR_ABRE_CHAVE(Pattern.quote("{")),
    DELIMITADOR_FECHA_CHAVE(Pattern.quote("}")),
    OPERADOR_MULTIPLICACAO("\\*"),
    OPERADOR_DIVISAO("\\/"),
    OPERADOR_IGUAL("\\="),
    OPERADOR_DIFERENTE("<>"),
    OPERADOR_AND("and"),
    OPERADOR_OR("or"),
    OPERADOR_NOT("not"),
    OPERADOR_MAIOR(">"),
    OPERADOR_MENOR("<"),
    OPERADOR_MAIOR_IGUAL(">="),
    OPERADOR_MENOR_IGUAL("<="),
    OPERADOR_ATRIBUICAO(Pattern.quote(":=")),
    OPERADOR_MOD("mod"),
    OPERADOR_DIV("div"),
    OPERADOR_IN("in"),
    OPERADOR_SOMA(Pattern.quote("+")),
    OPERADOR_MENOS(Pattern.quote("-")),
    DELIMITADOR_PONTO(Pattern.quote(".")),
    DELIMITADOR_VIRGULA(Pattern.quote(",")),
    DELIMITADOR_PONTO_VIRGULA(Pattern.quote(";")),
    DELIMITADOR_DOIS_PONTO(Pattern.quote(":")),
    
    DESCONHECIDO(Pattern.quote("")),
    EXPRESSAO("");

    
    
    public String regex;
  
    private Classificacao(String regex){
        this.regex = regex;
    }
    
    public String getRegex() {
        return regex;
    }
    
    public static Classificacao getOf(String stringToCheck){
        for (Classificacao classif : Classificacao.values()){
            if (stringToCheck.matches(classif.getRegex())){
                return classif;
            }
        }
        return Classificacao.DESCONHECIDO;
    }
    
    public static Classificacao checkErr(String str){
        if(str.matches(Classificacao.INTEIRO_LONGO.getRegex())) return Classificacao.INTEIRO_LONGO;
        else if(str.matches(Classificacao.COM_VIRGULA_LONGO_ANTES.getRegex())) return Classificacao.COM_VIRGULA_LONGO_ANTES;
        else if(str.matches(Classificacao.COM_VIRGULA_LONGO_DEPOIS.getRegex())) return Classificacao.COM_VIRGULA_LONGO_DEPOIS;
        else if(str.matches(Classificacao.COM_VIRGULA_LONGO_ANTES_DEPOIS.getRegex())) return Classificacao.COM_VIRGULA_LONGO_ANTES_DEPOIS;
        else if(str.matches(Classificacao.IDENTIFICADOR_LONGO.getRegex())) return Classificacao.IDENTIFICADOR_LONGO;
        else return Classificacao.DESCONHECIDO;
    }
    
    public static Classificacao check(String str){
        if(str.matches(Classificacao.INTEIRO.getRegex())) return Classificacao.INTEIRO;
        else if(str.matches(Classificacao.COM_VIRGULA.getRegex())) return Classificacao.COM_VIRGULA;
        else if(str.matches(Classificacao.IDENTIFICADOR.getRegex())) return Classificacao.IDENTIFICADOR;
        else return Classificacao.DESCONHECIDO;
    }
}
