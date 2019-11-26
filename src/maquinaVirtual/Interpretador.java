/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maquinaVirtual;

import analisadorSemantico.Codigo;
import analisadorSemantico.Instrucao;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;
import java.util.Stack;

/**
 *
 * @author gabriel
 */
public class Interpretador {

    private Stack<Double> pilha;
    private ArrayList<Double> variaveis;
    private ArrayList<Codigo> codigo;
    private int cont;
    private Codigo atual;

    public Interpretador() {

    }

    public ArrayList<Codigo> tratarEntrada(String text) {
        String[] array = text.replace(" ", "\n").split("\n");
        ArrayList<Codigo> areaCodigo = new ArrayList<>();
        
        for (int i = 0; i < array.length-1; i++) {
            Instrucao inst = Instrucao.get(array[i]);
            Instrucao inst2 = Instrucao.get(array[i + 1]);
            if (inst!= null && inst2 == null) {
                areaCodigo.add(new Codigo((inst), array[i + 1]));
            } else if(inst!=null) {
                areaCodigo.add(new Codigo((inst), null));
            }
        }
        Instrucao inst = Instrucao.get(array[array.length - 1]);
        if (inst != null) {
            areaCodigo.add(new Codigo((inst), null));
        }
        
        
        areaCodigo.forEach((c) -> {
            System.out.println(c.getCode().toString() + " --- " + String.valueOf(c.getEnd()));
        });
        
        return areaCodigo;
    }

    public void nextCode() {
        cont++;
        if (cont < codigo.size()) {
            atual = codigo.get(cont);
        } else {
            atual = null;
        }
    }

    public void execute(ArrayList<Codigo> codigo) {
        cont = 0;
        this.codigo = codigo;
        pilha = new Stack<>();
        variaveis = new ArrayList<>();
        atual = codigo.get(cont);
        programa();
    }

    public boolean comparar(Instrucao inst) {
        return atual.getCode().equals(inst);
    }

    public int getEnd() {
        return Integer.parseInt(atual.getEnd());
    }

    private void programa() {
        if (!comparar(Instrucao.INPP)) {
            return;
        }
        instrucoes();
        
        for(int i=0; i<variaveis.size(); i++){
            System.out.println("Memória["+i+"]: " + variaveis.get(i).toString());
        }
    }

    private void instrucoes() {
        Double aux1, aux2;
        while (cont < codigo.size()) {
            nextCode();
            System.out.println("Linha: " + cont + " Instrucao: "+ atual.getCode().toString() );
            
            switch (atual.getCode()) {
                case LEIT:
                    System.out.print("Leitura: ");
                    String ler = new Scanner(System.in).nextLine();
                    pilha.push(Double.parseDouble(ler));
                    break;
                case IMPE:
                    System.out.print("Escrita: ");
                    System.out.println(String.valueOf(pilha.pop()));
                    break;
                case CRCT:
                    pilha.push(Double.parseDouble(atual.getEnd()));
                    break;
                case CRVL:
                    pilha.push(variaveis.get(getEnd()));
                    break;
                case ARMZ:
                    variaveis.set(getEnd(), pilha.pop());
                    break;
                case SOMA:
                    aux1 = pilha.pop();
                    aux2 = pilha.pop();
                    pilha.push(aux1 + aux2);
                    break;
                case SUBT:
                    aux1 = pilha.pop();
                    aux2 = pilha.pop();
                    pilha.push(aux1 - aux2);
                    break;
                case MULT:
                    aux1 = pilha.pop();
                    aux2 = pilha.pop();
                    pilha.push(aux1 * aux2);
                    break;
                case DIVI:
                    aux1 = pilha.pop();
                    aux2 = pilha.pop();
                    pilha.push(aux1 / aux2);
                    break;
                case MODI:
                    aux1 = pilha.pop();
                    aux2 = pilha.pop();
                    pilha.push(aux1 % aux2);
                    break;
                case INVR:
                    aux1 = pilha.pop();
                    pilha.push(-aux1);
                    break;
                case CONJ:
                    aux1 = pilha.pop();
                    aux2 = pilha.pop();
                    if (aux1 == 1 && aux2 == 1) {
                        pilha.push(1d);
                    } else {
                        pilha.push(0d);
                    }
                    break;
                case DISJ:
                    aux1 = pilha.pop();
                    aux2 = pilha.pop();
                    if (aux1 == 1 || aux2 == 1) {
                        pilha.push(1d);
                    } else {
                        pilha.push(0d);
                    }
                    break;
                case NEGA:
                    aux1 = pilha.pop();
                    pilha.push(1d - aux1);
                    break;
                case CMME:
                    aux1 = pilha.pop();
                    aux2 = pilha.pop();
                    if (aux1 < aux2) {
                        pilha.push(1d);
                    } else {
                        pilha.push(0d);
                    }
                    break;
                case CMMA:
                    aux1 = pilha.pop();
                    aux2 = pilha.pop();
                    if (aux1 > aux2) {
                        pilha.push(1d);
                    } else {
                        pilha.push(0d);
                    }
                    break;
                case CMIG:
                    aux1 = pilha.pop();
                    aux2 = pilha.pop();
                    if (Objects.equals(aux1, aux2)) {
                        pilha.push(1d);
                    } else {
                        pilha.push(0d);
                    }
                    break;
                case CMDG:
                    aux1 = pilha.pop();
                    aux2 = pilha.pop();
                    if (!Objects.equals(aux1, aux2)) {
                        pilha.push(1d);
                    } else {
                        pilha.push(0d);
                    }
                    break;
                case CMAG:
                    aux1 = pilha.pop();
                    aux2 = pilha.pop();
                    if (aux1 >= aux2) {
                        pilha.push(1d);
                    } else {
                        pilha.push(0d);
                    }
                    break;
                case CMEG:
                    aux1 = pilha.pop();
                    aux2 = pilha.pop();
                    if (aux1 <= aux2) {
                        pilha.push(1d);
                    } else {
                        pilha.push(0d);
                    }
                    break;
                case DSVS:
                    desvio();
                    break;
                case DSVF:
                    if (pilha.pop() == 0d) {
                        desvio();
                    }
                    break;
                case PARA:
                    return;
                case AMEM:
                    for (int i = 0; i < getEnd(); i++) {
                        variaveis.add(Double.NaN);
                    }
                    break;
                case DMEM:
                    variaveis.set(getEnd(), Double.NaN);
                    break;
                default:
            }
        }
    }

    private void desvio() {
        cont = getEnd();
        atual = codigo.get(cont);
    }

}
