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
    CARACTER_INVALIDO("[@|#|&|¨|!|?|.|,]"),
    INTEIRO("(0|[1-9][0-9]*)"),
    COM_VIRGULA("(0|([1-9][0-9]*))(\\.[0-9]+)?$"),
    IDENTIFICADOR("[a-z|A-Z][a-z|A-Z|0-9|_]*"),
    PALAVRA_RESERVDA("if|then|else|begin|end|while|do|program"
            + "procedure|true|false|char|integer|boolean|const"),
    COMENTARIO("//(.*?)"),
    COMENTARIO_BLOCO("[{]"),
    OPERADORES("\\+|\\-|\\*|\\/|\\=|<>|and|or|not|>|<|<=|>=|:=|mod|in"),
    DELIMITADOR(",|;|:|\\(|\\)|\\[|\\]"),
    BRANCO(" |\t|\r"),
    PULA_LINHA("\n"),
    DESCONHECIDO(Pattern.quote("")),
    
    PALAVRA_RESERVADA_IF("if"),
    PALAVRA_RESERVADA_THEN("then"),
    PALAVRA_RESERVADA_ELSE("else"),
    PALAVRA_RESERVADA_BEGIN("begin"),
    PALAVRA_RESERVADA_END("end"),
    PALAVRA_RESERVADA_WHILE("while"),
    PALAVRA_RESERVADA_DO("do"),
    PALAVRA_RESERVADA_PROGAM("program"),
    PALAVRA_RESERVADA_PROCEDURE("procedure"),
    PALAVRA_RESERVADA_TRUE("true"),
    PALAVRA_RESERVADA_FALSE("false"),
    PALAVRA_RESERVADA_CHAR("char"),
    PALAVRA_RESERVADA_INTEGER("integer"),
    PALAVRA_RESERVADA_BOOLEAN("boolean"),
    PALAVRA_RESERVADA_CONST("const"),
    OPERADOR_MULTIPLICACAO("\\*"),
    OPERADOR_IGUAL("\\="),
    OPERADOR_DIFERENTE("<>"),
    OPERADOR_AND("and"),
    OPERADOR_OR("or"),
    OPERADOR_NOT("not"),
    OPERADOR_MAIOR(">"),
    OPERADOR_MENOR("<"),
    OPERADOR_MAIOR_IGUAL(">="),
    OPERADOR_MENOR_IGUAL("<="),
    OPERADOR_ATRIBUICAO(":="),
    OPERADOR_MOD("mod"),
    OPERADOR_IN("in"),
    OPERADOR_SOMA("\\+"),
    OPERADOR_MENOS("\\-");
    
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
    
}
