/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AnalisadorLexico;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Classe que faz a leitura e escrita dos arquivos
 *
 * @author Mauricio e Alexandre
 */
public class LeituraArquivo {

    private String localFile;
    private String[] nomeArquivo;
    Pattern patternEndFile = Pattern.compile("\\d+");

    /**
     * Função de leitura dos arquivos dentro da pasta input
     *
     * @return os arquivos dentro da pasta
     */
    public ArrayList<String> leitura() {

        ArrayList<String> code = new ArrayList<>();
        File search = new File("input/");
        if (search == null) {
            System.out.println("Arquivo de input não encontrado");
        } else {
            for (File aux : search.listFiles()) {
                code.add(aux.getName());
            }
        }

        return code;
    }

    /**
     * Função de pegar as informações de dentro dos arquivos
     *
     * @param localFile
     * @return Arrey contendo as informações de cada linha
     * @throws FileNotFoundException
     */
    public ArrayList<String> lerArquivo(String localFile) throws FileNotFoundException {

        ArrayList<String> code;
        try ( Scanner scanner = new Scanner(new FileReader("input/" + localFile))) {
            this.localFile = localFile;
            nomeArquivo = this.localFile.split(".txt");
            code = new ArrayList<>();

            while (scanner.hasNextLine()) {
                String aux = scanner.nextLine();
                code.add(aux);
            }
        }
        return code;

    }

    /**
     * Função que escreve as informações léxicas no arquivo
     *
     * @param tokens
     * @param erros
     * @throws IOException
     */
    public void escreverArquivo(ArrayList<Token> tokens, ArrayList<String> erros, String codigo) throws IOException {

        // Verificar o número do arquivo
        Matcher numberEndFile = patternEndFile.matcher(codigo);
        numberEndFile.find();
        //System.out.println(numberEndFile.group());

        try ( FileWriter file = new FileWriter("output/" + "\\saida" + numberEndFile.group() + ".txt", false)) {
            PrintWriter gravar = new PrintWriter(file);

            tokens.forEach((token) -> {
                gravar.println(token.getLinha() + " " + token.getTipo() + " " + token.getLexema());
            });

            if (erros.isEmpty()) {
                gravar.println("\n Nao apresentou erros lexicos neste arquivo");
            } else {
                gravar.println("\n Existem estes erros lexicos a seguir: ");
                erros.forEach((erro) -> {
                    gravar.println(erro);
                });
            }
        }
    }

    void escreverArquivoSintatico(ArrayList<String> listarErros) throws IOException {
        try ( FileWriter file = new FileWriter("output/" + "\\saida" + this.nomeArquivo[0] + "-sint.txt", false)) {
            PrintWriter gravar = new PrintWriter(file);

            if (listarErros.isEmpty()) {
                gravar.println("\n Nao existem erros Sintaticos");
            } else {

                for (int i = 0; i < listarErros.size(); i++) {
                    gravar.println(listarErros.get(i));
                }

            }
        }
    }

    /**
     * Pega as informações do nome do arquivo
     *
     * @return nome do arquivo
     */
    public String getLocalFile() {
        return localFile;
    }

}
