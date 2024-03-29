/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analisadorSemantico;

import analisadorLexico.Lexema;

/**
 *
 * @author gabriel
 */
public class Simbolos<T> {
    private Lexema lex;
    private String cadeia;
    private String token;
    private Categoria categoria;
    private int escopo;
    private boolean utilizada;
    private String tipo;
    private T valor;
    private int endereco;

    public Simbolos() {
    }

    public Simbolos(Lexema lex, Categoria categoria, int escopo, String tipo, int endereco, boolean utilizada) {
        this.lex = lex;
        this.cadeia = lex.getLexema();
        this.token = lex.getToken().toString();
        this.categoria = categoria;
        this.escopo = escopo;
        this.utilizada = utilizada;
        this.tipo = tipo;
        this.endereco = endereco;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setValor(T valor) {
        this.valor = valor;
    }

    public Lexema getLex() {
        return lex;
    }

    public String getCategoria() {
        return categoria.toString();
    }

    public int getEscopo() {
        return escopo;
    }

    public boolean isUtilizada() {
        return utilizada;
    }

    public String getTipo() {
        return tipo;
    }

    public T getValor() {
        return valor;
    }

    public void setUtilizada(boolean utilizada) {
        this.utilizada = utilizada;
    }

    public String getEndereco(){
        return String.valueOf(endereco);
    }
    
}
