/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AnalisadorLexico;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 *
 * @author Mauricio e Alexandre
 */
public class Token {

    private final String tipo;
    private final int linha;
    private final int aux;
    private final String lexema;

    public Token(int linha, int aux, String tipo, String lexema) {

        this.tipo = tipo;
        this.linha = linha;
        this.aux = aux;
        this.lexema = lexema;
    }

    public String getTipo() {
        return tipo;
    }

    public String getLinha() {
        //convereter o numero da linha ao formato de dois digitos
        NumberFormat formatter = new DecimalFormat("00");
        String s = formatter.format(linha);

        return s;
    }

    public int getAux() {
        return aux;
    }

    public String getLexema() {
        return lexema;
    }

}
