/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AnalisadorSintatico;

import java.util.ArrayList;

/**
 * Classe Estrutura Lexica
 *
 * @author Mauricio e Alexandre
 */
public class EstruturaLexica {

    private final ArrayList<String> palavrasReservadas = new ArrayList<>();
    private final ArrayList<Character> operador = new ArrayList<>();
    /* 
    private final ArrayList<Character> operadorAritmetico = new ArrayList<>();
    private final ArrayList<Character> operadorRelacional = new ArrayList<>();
    private final ArrayList<Character> operadorLogico = new ArrayList<>();
     */
    private final ArrayList<Character> delimitadores = new ArrayList<>();
    private final ArrayList<Character> simboloS34 = new ArrayList<>();
    private final ArrayList<Character> simboloS39 = new ArrayList<>();
    private final ArrayList<Character> letras = new ArrayList<>();

    /**
     * Construtor da classe
     */
    public EstruturaLexica() {

        //Lista das palavras reservadas
        palavrasReservadas.add("program");
        palavrasReservadas.add("var");
        palavrasReservadas.add("const");
        palavrasReservadas.add("register");
        palavrasReservadas.add("function");
        palavrasReservadas.add("procedure");
        palavrasReservadas.add("return");
        palavrasReservadas.add("main");
        palavrasReservadas.add("if");
        palavrasReservadas.add("else");
        palavrasReservadas.add("while");
        palavrasReservadas.add("read");
        palavrasReservadas.add("write");
        palavrasReservadas.add("integer");
        palavrasReservadas.add("real");
        palavrasReservadas.add("boolean");
        palavrasReservadas.add("char");
        palavrasReservadas.add("string");
        palavrasReservadas.add("true");
        palavrasReservadas.add("false");

        //Lista de operadores aritimeticos, relacionais e lógicos
        operador.add('+');
        operador.add('-');
        operador.add('*');
        operador.add('<');
        operador.add('=');
        operador.add('>');
        operador.add('!');
        operador.add('&');
        operador.add('|');

        /* 
        //Lista de operadores aritimeticos
        operadorAritmetico.add('+');
        operadorAritmetico.add('-');
        operadorAritmetico.add('*');        
        //Lista de operadores Relacionais 
        operadorRelacional.add('<');
        operadorRelacional.add('=');
        operadorRelacional.add('>');
        //Lista de operadores Lógicos
        operadorLogico.add('!');
        operadorLogico.add('&');
        operadorLogico.add('|');
         */
        //Lista de delimitadores
        delimitadores.add(';');
        delimitadores.add(',');
        delimitadores.add('(');
        delimitadores.add(')');
        delimitadores.add('{');
        delimitadores.add('}');
        delimitadores.add('[');
        delimitadores.add(']');
        delimitadores.add('.');
        delimitadores.add(':');

        //Lista de letras maiúsculas e minúsculas
        for (char i = 'A'; i <= 'Z'; i++) {
            this.letras.add((char) i);
        }
        for (char i = 'a'; i <= 'z'; i++) {
            this.letras.add((char) i);
        }

        //Lista de símbolos com a exceção das " de numero 34
        for (int i = 32; i <= 126; i++) {
            if (i != 34) {
                this.simboloS34.add((char) i);
            }
        }
        //Lista de símbolos com a exceção das " de numero 39
        for (int i = 32; i <= 126; i++) {
            if (i != 39) {
                this.simboloS39.add((char) i);
            }
        }

    }

    /**
     * Verifica se a palavra recebida é uma palavra reservada
     *
     * @param string
     * @return
     */
    public boolean verificarPalavrasReservada(String string) {
        return this.palavrasReservadas.contains(string);
    }

    /**
     * Verifica se o caracter recebido é um operador
     *
     * @param caractere
     * @return
     */
    public boolean verificarOperador(char caractere) {
        return (this.operador.contains(caractere));
    }

    /*
   /**
     * Verifica se o caracter recebido é um operador Aritmetico
     *
     * @param caractere
     * @return
    
    public boolean verificarOperadorAritmetico(char caractere){
        return (this.operadorAritmetico.contains(caractere));
    }
    
   /**
     * Verifica se o caracter recebido é um operador Relacional
     *
     * @param caractere
     * @return
    
    public boolean verificarOperadorRelacional(char caractere){
        return (this.operadorRelacional.contains(caractere));
    }
    
   /**
     * Verifica se o caracter recebido é um operador Logico
     *
     * @param caractere
     * @return
    
    public boolean verificarOperadorLogico(char caractere){
        return (this.operadorLogico.contains(caractere));
    }    
     */
    /**
     * Verifica se o caractere recebido é um delimitador
     *
     * @param caractere
     * @return
     */
    public boolean verificarDelimitador(char caractere) {
        return this.delimitadores.contains(caractere);
    }

    /**
     * Verifica se o caractere recebido é uma letra
     *
     * @param caractere
     * @return
     */
    public boolean verificarLetra(char caractere) {
        return this.letras.contains(caractere);
    }

    /**
     * Verifica se o caracter recebido é um simbolo válido
     *
     * @param caractere
     * @return
     */
    public boolean verificarSimboloSem34(char caractere) {
        return this.simboloS34.contains(caractere);
    }

    /**
     * Verifica se o caracter recebido é um simbolo válido
     *
     * @param caractere
     * @return
     */
    public boolean verificarSimboloSem39(char caractere) {
        return this.simboloS39.contains(caractere);
    }

    /**
     * Verifica se o caractere recebido é um espaço
     *
     * @param caractere
     * @return
     */
    public boolean verificarEspaco(char caractere) {
        return (Character.isSpaceChar(caractere) || caractere == 9);
    }
}
