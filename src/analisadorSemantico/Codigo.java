/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analisadorSemantico;

/**
 *
 * @author gabriel
 */
public class Codigo {
    private final Instrucao code;
    private int end;

    public Codigo(Instrucao code, int end) {
        this.code = code;
        this.end = end;
    }
    
    public Codigo(Instrucao code){
        this.code = code;
    }

    public Instrucao getCode() {
        return code;
    }

    public int getEnd() {
        return end;
    }
    
    
}
