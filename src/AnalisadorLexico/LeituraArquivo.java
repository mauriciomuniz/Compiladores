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

/**
 *
 * @author Mauricio e Alexandre
 */
public class LeituraArquivo {

    private String localFile;
    private String[] nomeArquivo;

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

    public ArrayList<String> lerArquivo(String localFile) throws FileNotFoundException {

        ArrayList<String> code;
        try ( Scanner scanner = new Scanner(new FileReader("test/input/" + localFile))) {
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

    public void escreverArquivo(ArrayList<Token> tokens, ArrayList<String> erros) throws IOException {

        try ( FileWriter file = new FileWriter("test/output/" + this.nomeArquivo[0] + "-lex.txt", false)) {
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

    public String getLocalFile() {
        return localFile;
    }

}
