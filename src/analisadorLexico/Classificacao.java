/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analisadorLexico;

import java.util.HashMap;
import java.util.regex.Pattern;

/**
 *
 * @author gabriel
 */
public enum Classificacao {
    CARACTER_INVALIDO("[@|#|&|¨|!|?|.]"),
    INTEIRO("0|[1-9][0-9]*"),
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
    DESCONHECIDO(Pattern.quote(""));
    
    
    public String regex;
    public HashMap table;
    
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
