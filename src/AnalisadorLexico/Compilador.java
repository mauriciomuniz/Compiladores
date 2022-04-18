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
        Automato analiseLexica;// Definição do tipo Automato
        ArrayList<String> codigos = arquivo.leitura();// Leitura do arquivo

        for (String codigo : codigos) {

            System.out.println(codigo);// Mostra qual Entrada está lidando

            analiseLexica = new Automato();// Criação de um novo Automato
            ArrayList<String> codigoFonte = arquivo.lerArquivo(codigo);

            analiseLexica.analisadorLexico(codigoFonte);// Chamada do Analisador Léxico
            arquivo.escreverArquivo(analiseLexica.getListarTokens(), analiseLexica.getListarErros(), codigo);// Escrita dos tokens no arquivo

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
