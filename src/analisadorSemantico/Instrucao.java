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
public enum Instrucao {
    INPP,
    AMEM,
    DMEM,
    PARA,
    CRCT,
    CRVL,
    DIVI,
    MULT,
    SUBT,
    MODI,
    ARMZ,
    SOMA,
    INVR,
    CONJ,
    DISJ,
    NEGA,
    CMME,
    CMMA,
    CMIG,
    CMDG,
    CMAG,
    CMEG,
    DSVS,
    DSVF,
    LEIT,
    LECH,
    IMPR,
    IMPC,
    IMPE,
    NADA;
    
    
    public static Instrucao get(String text){
        for(Instrucao inst : Instrucao.values()){
            if(inst.toString().equals(text)) return inst;
        }
        return null;
    }
}
