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
 * Classe que faz a chamada da leitura, escrita e análise léxica
 *
 * @author Mauricio e Alexandre
 */
public class Compilador {

    public static void main(String[] args) throws FileNotFoundException, IOException {

        LeituraArquivo arquivo = new LeituraArquivo();// Criação do leitor
        Automato analiseLexica;// Criação do Automato
        ArrayList<String> codigos = arquivo.leitura();// Leitura do arquivo

        for (String codigo : codigos) {

            System.out.println(codigo);// Mostra qual Entrada está lidando

            analiseLexica = new Automato();
            ArrayList<String> codigoFonte = arquivo.lerArquivo(codigo);

            analiseLexica.analisadorLexico(codigoFonte);
            arquivo.escreverArquivo(analiseLexica.getListarTokens(), analiseLexica.getListarErros(), codigo);

            // Resultados da análise
            System.out.println("Analise lexica foi concluida");
            if (analiseLexica.getListarErros().isEmpty()) {
                System.out.println("Nao existem erros lexicos no arquivo\n");
            } else {
                System.out.println("Existem " + analiseLexica.getListarErros().size() + " erros lexicos no arquivo\n");

            }
        }
    }

}
