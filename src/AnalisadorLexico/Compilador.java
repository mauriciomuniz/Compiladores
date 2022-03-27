/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AnalisadorLexico;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
/**
 *
 * @author Mauricio e Alexandre
 */
public class Compilador {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        
        LeituraArquivo arquivo = new LeituraArquivo();
        Automato analiseLexica;
        ArrayList<String> codigos = arquivo.leitura();
          
        for(String codigo : codigos){
            
            analiseLexica = new Automato();           
            ArrayList<String> codigoFonte = arquivo.lerArquivo(codigo); 
            
            analiseLexica.analisadorLexico(codigoFonte);
            arquivo.escreverArquivo(analiseLexica.getListarTokens(), analiseLexica.getListarErros());
            
            System.out.println("Analise lexica concluida");
            if(analiseLexica.getListarErros().isEmpty())
                System.out.println("Nao existem erros lexicos\n");
            else
                System.out.println("Existem erros lexicos\n");
      
        }
    }
    

}
