/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AnalisadorLexico;

import java.util.ArrayList;

/**
 * 
 * @author Karolyne e Patricia
 */
public class EstruturaLexica {
    
    private final ArrayList<String> palavrasReservadas = new ArrayList<>();
    private final ArrayList<Character> operador = new ArrayList<>();
    private final ArrayList<Character> delimitadores = new ArrayList<>();
    private final ArrayList<Character> simbolos = new ArrayList<>();
    private final ArrayList<Character> letras = new ArrayList<>();

    public EstruturaLexica() {
        
        //lista das palavras reservadas
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

        //lista de operadores aritimeticos, relacionais e lógicos
        operador.add('+');
        operador.add('-');
        operador.add('*');
        operador.add('<');
        operador.add('=');
        operador.add('>');
        operador.add('!');
        operador.add('&');
        operador.add('|');
        
        operadorAritmeticos.add('+');
        
        operador.add('-');
        operador.add('*');
        
        
        //lista de delimitadores
        delimitadores.add('.');
        delimitadores.add(';');
        delimitadores.add(',');
        delimitadores.add('(');
        delimitadores.add(')');
        delimitadores.add('[');
        delimitadores.add(']');
        delimitadores.add('{');
        delimitadores.add('}');
        
        //lista de letras maiúsculas e minúsculas
        for (char i = 'A'; i <= 'Z'; i++){
            this.letras.add((char) i);
        }
        for (char i = 'a'; i <= 'z'; i++){
            this.letras.add((char) i);
        }        
        
        
        //Lista de símbolos
        for(int i = 32; i <= 126; i++){
            if(i != 34){
                this.simbolos.add((char) i);
            }
        }
        
    }
    //verifica se a palavra recebida é uma palavra reservada
    public boolean verificarPalavrasReservada(String string){
        return this.palavrasReservadas.contains(string);
    }
    //verifica se o caracter recebido é um operador
    public boolean verificarOperador(char caractere){
        return (this.operador.contains(caractere));
    }
    //verifica se o caractere recebido é um delimitador
    public boolean verificarDelimitador(char caractere){
        return this.delimitadores.contains(caractere);
    }
    //verifica se o caractere recebido é uma letra
    public boolean verificarLetra(char caractere){
        return this.letras.contains(caractere);
    }
    //verifica se o caracter recebido é um simbolo válido
    public boolean verificarSimbolo(char caractere){
        return this.simbolos.contains(caractere);
    }    
    //verifica se o caractere recebido é um espaço
    public boolean verificarEspaco(char caractere){
        return (Character.isSpaceChar(caractere) || caractere == 9);
    }    
}
