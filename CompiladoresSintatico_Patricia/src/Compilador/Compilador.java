/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Compilador;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
/**
 *
 * @author Karolyne e Patricia
 */
public class Compilador {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        
        LeituraArquivo arquivo = new LeituraArquivo();
        AnalisadorLexico analiseLexica;
        ArrayList<String> codigos = arquivo.leitura();
        
        AnalisadorSintatico analiseSintatica;
        
        for(String codigo : codigos){
            
            analiseLexica = new AnalisadorLexico();           
            ArrayList<String> codigoFonte = arquivo.lerArquivo(codigo); 
            
            analiseLexica.analisadorLexico(codigoFonte);
            /*arquivo.escreverArquivo(analiseLexica.getListarTokens(), analiseLexica.getListarErros());
            
            System.out.println("Analise lexica concluida");
            if(analiseLexica.getListarErros().isEmpty())
                System.out.println("Nao existem erros lexicos\n");
            else
                System.out.println("Existem erros lexicos\n");
            */
            ArrayList<Token> tokens = new ArrayList<>();
            for (Token token : analiseLexica.getListarTokens()) {
                tokens.add(token);                
            }
            analiseSintatica = new AnalisadorSintatico();
            analiseSintatica.analiseSintatica(tokens);
            
            arquivo.escreverArquivoSintatico(analiseSintatica.getListarErros());
            
            System.out.println("Analise Sintatica concluida");
            if(analiseSintatica.getListarErros().isEmpty())
                System.out.println("Nao existem erros sintaticos\n");
            else
                System.out.println("Existem erros sintaticos\n");
        }
    }
    

}
