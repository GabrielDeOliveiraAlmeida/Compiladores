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
    private String end;

    public Codigo(Instrucao code, String end) {
        this.code = code;
        this.end = end;
    }
    
    public Codigo(Instrucao code){
        this.code = code;
    }

    public Instrucao getCode() {
        return code;
    }

    public String getEnd() {
        return end;
    }
    
    
}
