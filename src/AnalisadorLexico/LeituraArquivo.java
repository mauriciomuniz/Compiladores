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
 *
 * @author Mauricio e Alexandre
 */
public class LeituraArquivo {

    private String localFile;
    //private String[] nomeArquivo;
    Pattern patternEndFile = Pattern.compile("\\d+");

    /**
     *
     * @return
     */
    public ArrayList<String> leitura() {

        ArrayList<String> code = new ArrayList<>();
        File access = new File("test/input/");
        if (access == null) {
            System.out.println("Arquivo de input não encontrado");
        } else {
            for (File aux : access.listFiles()) {
                code.add(aux.getName());
            }
        }

        return code;
    }

    /**
     *
     * @param localFile
     * @return
     * @throws FileNotFoundException
     */
    public ArrayList<String> lerArquivo(String localFile) throws FileNotFoundException {

        ArrayList<String> code;
        try ( Scanner scanner = new Scanner(new FileReader("test/input/" + localFile))) {
            this.localFile = localFile;
            //nomeArquivo = this.localFile.split(".txt");
            code = new ArrayList<>();

            while (scanner.hasNextLine()) {
                String aux = scanner.nextLine();
                code.add(aux);
            }
        }
        return code;

    }

    /**
     *
     * @param tokens
     * @param erros
     * @throws IOException
     */
    public void escreverArquivo(ArrayList<Token> tokens, ArrayList<String> erros, String codigo) throws IOException {

        //Verificar número do arquivo
        Matcher numberEndFile = patternEndFile.matcher(codigo);
        numberEndFile.find();
        //System.out.println(numberEndFile.group());

        try ( FileWriter file = new FileWriter("test/output/" + "\\saida" + numberEndFile.group() + ".txt", false)) {
            PrintWriter gravar = new PrintWriter(file);

            tokens.forEach((token) -> {
                gravar.println(token.getLinha() + " " + token.getTipo() + " " + token.getLexema());
            });

            if (erros.isEmpty()) {
                gravar.println("\n Nao existem erros lexicos");
            } else {
                gravar.println("");
                erros.forEach((erro) -> {
                    gravar.println(erro);
                });
            }
        }

    }

    /**
     *
     * @return
     */
    public String getLocalFile() {
        return localFile;
    }

}
