/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analisadorLexico;

import static analisadorLexico.Classificacao.DELIMITADOR_ABRE_COLCHETE;
import static analisadorLexico.Classificacao.DELIMITADOR_ABRE_PARENTESE;
import static analisadorLexico.Classificacao.DELIMITADOR_DOIS_PONTO;
import static analisadorLexico.Classificacao.DELIMITADOR_FECHA_COLCHETE;
import static analisadorLexico.Classificacao.DELIMITADOR_FECHA_PARENTESE;
import static analisadorLexico.Classificacao.DELIMITADOR_PONTO;
import static analisadorLexico.Classificacao.DELIMITADOR_PONTO_VIRGULA;
import static analisadorLexico.Classificacao.DELIMITADOR_VIRGULA;
import static analisadorLexico.Classificacao.OPERADOR_AND;
import static analisadorLexico.Classificacao.OPERADOR_ATRIBUICAO;
import static analisadorLexico.Classificacao.OPERADOR_DIFERENTE;
import static analisadorLexico.Classificacao.OPERADOR_DIV;
import static analisadorLexico.Classificacao.OPERADOR_DIVISAO;
import static analisadorLexico.Classificacao.OPERADOR_IGUAL;
import static analisadorLexico.Classificacao.OPERADOR_IN;
import static analisadorLexico.Classificacao.OPERADOR_MAIOR;
import static analisadorLexico.Classificacao.OPERADOR_MAIOR_IGUAL;
import static analisadorLexico.Classificacao.OPERADOR_MENOR;
import static analisadorLexico.Classificacao.OPERADOR_MENOR_IGUAL;
import static analisadorLexico.Classificacao.OPERADOR_MENOS;
import static analisadorLexico.Classificacao.OPERADOR_MOD;
import static analisadorLexico.Classificacao.OPERADOR_MULTIPLICACAO;
import static analisadorLexico.Classificacao.OPERADOR_NOT;
import static analisadorLexico.Classificacao.OPERADOR_OR;
import static analisadorLexico.Classificacao.OPERADOR_SOMA;
import static analisadorLexico.Classificacao.PALAVRA_RESERVADA_BEGIN;
import static analisadorLexico.Classificacao.PALAVRA_RESERVADA_BOOLEAN;
import static analisadorLexico.Classificacao.PALAVRA_RESERVADA_CHAR;
import static analisadorLexico.Classificacao.PALAVRA_RESERVADA_CONST;
import static analisadorLexico.Classificacao.PALAVRA_RESERVADA_DO;
import static analisadorLexico.Classificacao.PALAVRA_RESERVADA_ELSE;
import static analisadorLexico.Classificacao.PALAVRA_RESERVADA_END;
import static analisadorLexico.Classificacao.PALAVRA_RESERVADA_FALSE;
import static analisadorLexico.Classificacao.PALAVRA_RESERVADA_IF;
import static analisadorLexico.Classificacao.PALAVRA_RESERVADA_INT;
import static analisadorLexico.Classificacao.PALAVRA_RESERVADA_INTEGER;
import static analisadorLexico.Classificacao.PALAVRA_RESERVADA_PROCEDURE;
import static analisadorLexico.Classificacao.PALAVRA_RESERVADA_PROGRAM;
import static analisadorLexico.Classificacao.PALAVRA_RESERVADA_REAL;
import static analisadorLexico.Classificacao.PALAVRA_RESERVADA_THEN;
import static analisadorLexico.Classificacao.PALAVRA_RESERVADA_TRUE;
import static analisadorLexico.Classificacao.PALAVRA_RESERVADA_VAR;
import static analisadorLexico.Classificacao.PALAVRA_RESERVADA_WHILE;
import java.util.HashMap;

/**
 *
 * @author cc161255426
 */
public class ClassificacaoReservadas {
    public static HashMap<String,Classificacao> tabela = new HashMap<>();
    
    public ClassificacaoReservadas() {
        tabela.put("if", PALAVRA_RESERVADA_IF);
        tabela.put("then", PALAVRA_RESERVADA_THEN);
        tabela.put("else", PALAVRA_RESERVADA_ELSE);
        tabela.put("begin", PALAVRA_RESERVADA_BEGIN);
        tabela.put("end", PALAVRA_RESERVADA_END);
        tabela.put("while", PALAVRA_RESERVADA_WHILE);
        tabela.put("do", PALAVRA_RESERVADA_DO);
        tabela.put("program", PALAVRA_RESERVADA_PROGRAM);
        tabela.put("procedure", PALAVRA_RESERVADA_PROCEDURE);
        tabela.put("true", PALAVRA_RESERVADA_TRUE);
        tabela.put("false", PALAVRA_RESERVADA_FALSE);
        tabela.put("char", PALAVRA_RESERVADA_CHAR);
        tabela.put("integer", PALAVRA_RESERVADA_INTEGER);
        tabela.put("real", PALAVRA_RESERVADA_REAL);
        tabela.put("int", PALAVRA_RESERVADA_INT);
        tabela.put("boolean", PALAVRA_RESERVADA_BOOLEAN);
        tabela.put("var", PALAVRA_RESERVADA_VAR);
        tabela.put("(", DELIMITADOR_ABRE_PARENTESE);
        tabela.put(")", DELIMITADOR_FECHA_PARENTESE);
        tabela.put("[", DELIMITADOR_ABRE_COLCHETE);
        tabela.put("]", DELIMITADOR_FECHA_COLCHETE);
        tabela.put(";", DELIMITADOR_PONTO_VIRGULA);
        tabela.put(".", DELIMITADOR_PONTO);
        tabela.put(":", DELIMITADOR_DOIS_PONTO);
        tabela.put(",", DELIMITADOR_VIRGULA);
        tabela.put("const", PALAVRA_RESERVADA_CONST);
        tabela.put("*", OPERADOR_MULTIPLICACAO);
        tabela.put("/", OPERADOR_DIVISAO);
        tabela.put("=", OPERADOR_IGUAL);
        tabela.put("<>", OPERADOR_DIFERENTE);
        tabela.put("and", OPERADOR_AND);
        tabela.put("or", OPERADOR_OR);
        tabela.put("not", OPERADOR_NOT);
        tabela.put(">", OPERADOR_MAIOR);
        tabela.put("<", OPERADOR_MENOR);
        tabela.put(">=", OPERADOR_MAIOR_IGUAL);
        tabela.put("<=", OPERADOR_MENOR_IGUAL);
        tabela.put(":=", OPERADOR_ATRIBUICAO);
        tabela.put("mod", OPERADOR_MOD);
        tabela.put("div", OPERADOR_DIV);
        tabela.put("in", OPERADOR_IN);
        tabela.put("+", OPERADOR_SOMA);
        tabela.put("-", OPERADOR_MENOS);   
    }
    
    public Classificacao getClasse(String key){
        return tabela.get(key);
    }

       
}
