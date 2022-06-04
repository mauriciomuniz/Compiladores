
package Compilador;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

/**
 *
 * @author Karolyne e Patricia
 */
public class AnalisadorLexico {

    private ArrayList<Token> listarTokens; //lista de tokens corretoa
    private ArrayList<String> listarErros; //lista de erros
    private ArrayList<String> codigo; // entrada
    private static final char EOF = '\0'; 
    private int linha, aux; //linha e coluna 
    private boolean linhaVazia; 
    private final EstruturaLexica token; 

    public AnalisadorLexico() {
        this.listarTokens = new ArrayList<>();
        this.listarErros = new ArrayList<>();
        this.codigo = new ArrayList<>();
        this.linha = 0;
        this.aux = 0;
        this.linhaVazia = false;
        this.token = new EstruturaLexica();
    }

    void analisadorLexico(ArrayList<String> codigoFonte) {
        this.codigo = codigoFonte;
        char a = proximo();
        while (a != EOF) {
            verificaAutomato(a);
            a = proximo();
        }
    }

    private char proximo() {
        if (!codigo.isEmpty()) { //condição que verifica se o arquivo está vazio

            char c[] = codigo.get(linha).toCharArray(); //adiciona a linha atual do codigo em um vetor de caracteres
            
            // condição que verifica se a linha acabou, ou seja, se a posição do último elento é aigual a posição atual de colunas
            if (c.length == aux) { 
                linhaVazia = false;
                return ' ';
            } 
            // condição que percorre a linha na posição atual da coluna
            else if (c.length > aux) { 
                linhaVazia = false;
                return c[aux]; // retorna o elemento da posição atual da coluna
            } else if (codigo.size() > (linha + 1)) { // condição verifica se ainda existem linhas para percorrer
                linha++; // incrementa as linhas 
                c = codigo.get(linha).toCharArray(); // cria uma nova linha de sequencia de caracteres
                aux = 0; // reinicia a posição das colunas
                if (c.length == 0) { // condição que verifica se a linha é vazia
                    this.linhaVazia = true;
                    return ' ';
                } 
                return c[aux]; // retorna o elemento da posição atual da coluna
            } else { 
                return EOF; //fim de arquivo
            }
        } else {
            return EOF; //fim de arquivo
        }
    }

    private void verificaAutomato(char a) {
        String lexema = "";
        if (!this.linhaVazia) {
            //verifica se o caracter é espaço e desconsidera
            if (token.verificarEspaco(a)) {
                aux++;
            }//verifica se é uma letra e direciona pra palavra reservada e identificador 
            else if (token.verificarLetra(a)) {
                palavraReservadaId(lexema, a);
            }//verifica se é um digito e direciona pra numero 
            else if (Character.isDigit(a)) {
                numero(lexema, a);
            }//verifica se é um operador e envia para operadores e trata
            else if (token.verificarOperador(a)) {
                operador(lexema, a);
            }//verifica se é um delimitador e envia para delimitadores 
            else if (token.verificarDelimitador(a)) {
                delimitador(lexema, a);
            }//verifica se é uma '/' e envia para comentário de linha e trata
            else if (a == '/') {
                comentarioLinha(lexema, a);
            }//verifica se é uma '"' e envia para cadeia de caracteres 
            else if (a == '"') {
                cadeiaDeCaractere(lexema, a);
            }//qualquer simbolo diferente aos da tabela é considerado um erro de palavra invalida 
            else {
                this.palavraInvalida(lexema, a);
            }

        } else {
            linhaVazia = false;
            linha++;
        }
    }
/**
 * Identificador deve receber apenas letras, digitos ou o simbolo '_'
 * Palavra recervada compara se o que foi recebido é igual a uma das palavras salvas no array  
 * @param lexema 
 * @param a caractere que direciona ao automato de palavraReservadaId é uma letra.
 */
    private void palavraReservadaId(String lexema, char a) {

        int linhaInicial = linha;
        int auxPalavraReservadaId = aux;
        Token tokenaux;

        lexema = lexema + a;
        this.aux++;
        a = this.proximo();
        //verifica se os proximos caracteres podem formar uma palavra reservada ou um identificador
        while (a == '_' || Character.isLetterOrDigit(a)) {
            lexema = lexema + a;
            aux++;
            a = this.proximo();
        }//enquanto for letra || '_' || digito continua no laço e adiciona o caractere no lexema
        //compara a palavre recebida com a lista de palavras reservadas
        if (token.verificarPalavrasReservada(lexema)) {
            tokenaux = new Token(linhaInicial + 1, auxPalavraReservadaId + 1, "PRE", lexema);
        }//se não considera a palavra um identificador 
        else {
            tokenaux = new Token(linhaInicial + 1, auxPalavraReservadaId + 1, "IDE", lexema);
        }//adiciona o token a lista de tokens corretos
        listarTokens.add(tokenaux);

    }
/**
 * Apenas adiciona o Simbolo incorreto a lista de erros
 * @param lexema
 * @param a caractere que direciona ao automato de palavraInvalida é qualquer simbolo que não pertença a nenhuma lista
 */
    private void palavraInvalida(String lexema, char a) {

        int linhaInicial = this.linha;
        //adiciona o simbolo invalido a lista de erros 
        lexema = lexema + a;
        this.aux++;
        this.addErro("SIB", lexema, linhaInicial);

    }
/**
 * Delimitador adiciona o token na lista
 * @param lexema
 * @param a caractere que direciona ao automato delimitador é identificado pela lista de delimitadores
 */
    private void delimitador(String lexema, char a) {

        int linhaInicial = this.linha;
        int auxiliarDelimitador = this.aux;
        Token tokenAuxiliar;
        //adiciona o delimitador a lista de tokens corretos
        lexema = lexema + a;
        this.aux++;
        tokenAuxiliar = new Token(linhaInicial + 1, auxiliarDelimitador + 1, "DEL", lexema);
        listarTokens.add(tokenAuxiliar);
    }
/**
 * Operador identifica que o simbolo é um identificador 
 * direciona para o tipo de operador referente a ele (entre Aritmetico, lógico e relacional) 
 * @param lexema
 * @param a caractere que direciona ao automato operador é identificado pela lista de operadores
 */
    private void operador(String lexema, char a) {
        //envia para automato de operadorAritmetico
        if (a == '+' || a == '-' || a == '*') {
            operadorAritmetico(lexema, a);
        }//envia para o automato de operadorRelacional 
        else if (a == '<' || a == '>' || a == '='|| a == '!'){
            operadorRelacional(lexema, a);
        }//envia ao automato de operadorLogico 
        else {
            operadorLogico(lexema, a);
        }
    }
/**
 * O método operadorAritmetico verifica o primeiro caractere recebido 
 se for adição verifica se é uma soma comum ou incremento ( '+' || '++')
 se for subtração verifica se é uma subtração comum ou decremento ou número negativo( '-' || '--' || "-nro")
 "-nro" se o que antecede ele não for um número ou um identificador
 caso contrário ele se torna um operador aritmético
 '*' operador aritmético de multiplicação
 * @param lexema
 * @param a caractere recebido '+' || '-' || '*'
 */
    private void operadorAritmetico(String lexema, char a) {

        int linhaInicial = this.linha;
        int auxiliarOpAritmetico = this.aux;
        Token tokenAuxiliar;
        Token tokenAnterior;
        
        lexema = lexema + a;
        this.aux++;

        if (a == '+') {
            //adição
            a = this.proximo();
            //incremento
            if (a == '+') { 
                lexema = lexema + a;
                this.aux++;
            }
        } else if (a == '-') {
            a = this.proximo();
            //pega o último token da lista
            tokenAnterior = listarTokens.get(listarTokens.size() -1); 
            //desconsidera os espaços
            if (Character.isSpaceChar(a)) {
                do {
                    this.aux++;
                    a = this.proximo();
                } while (token.verificarEspaco(a));
                if (Character.isDigit(a) && linhaInicial == linha) {
                    // compara se o último token é um numero ou identificador
                    if (!(tokenAnterior.getTipo().equals("NRO") || tokenAnterior.getTipo().equals("IDE"))) { 
                        //se não for um numero ou um identificador é enviado para o método de número
                        this.numero(lexema, a);
                        return;
                    }
                }
            } //decremento
            else if (a == '-') {
                lexema = lexema + a;
                this.aux++;
            } else if (Character.isDigit(a)) {
                // compara se o último token é um numero ou identificador
                if (!(tokenAnterior.getTipo().equals("NRO") || tokenAnterior.getTipo().equals("IDE"))) {
                    //se não for um numero ou um identificador é enviado para o método de número
                    this.numero(lexema, a);
                    return;
                }
            }

        }
        //adiciona a lista de token o operador aritmetico
        tokenAuxiliar = new Token(linhaInicial + 1, auxiliarOpAritmetico + 1, "ART", lexema);
        listarTokens.add(tokenAuxiliar);
    }
/**
 * O método comentarioLinha verifica quando é um comentário ou um operador aritmético
 * quando recebe um / e em seguida outro / é considerado comentário de linha correto
 * quando recebe um / e em seguido um * é enviado para o método que trata comentário de bloco
 * quando recebe / e nenhuma das opções acima são válidas é considerado um operador aritmética
 * @param lexema
 * @param a caractere recebido '/'
  */
    private void comentarioLinha(String lexema, char a) {
        int linhaInicial = this.linha;
        int auxiliarComentario = this.aux;
        Token tokenAuxiliar;

        lexema = lexema + a;
        this.aux++;
        a = this.proximo();

        switch (a) {
            //Se receber o segundo / é conciderado comentário de linha 
            case '/':
                lexema = lexema + a;
                this.aux++;
                a = this.proximo();
                //percorre a linha toda até o final descunsiderando essa linha
                while (linha == linhaInicial && a != EOF) {
                    lexema = lexema + a;
                    this.aux++;
                    a = this.proximo();
                }
                /*tokenAuxiliar = new Token(linhaInicial + 1, auxiliarComentario + 1, "CoM", lexema);
                this.listarTokens.add(tokenAuxiliar);*/ //adiciona o comentário para a lista de tokens
                break;
            //se receber o '*' é encaminhado para o método comentário de bloco
            case '*':
                this.comentarioBloco(lexema, a, linhaInicial);
                return;
            //Caso não seja nenhum dos dois é considerado um operador aritmético
            default:
                tokenAuxiliar = new Token(linhaInicial + 1, auxiliarComentario + 1, "ART", lexema);
                this.listarTokens.add(tokenAuxiliar);
                break;
        }
    }
    /**
     * O método comentarioBloco verifica se o comentário foi fechado com '* /'
     * caso não tenha sido informa erro de comentário mal formado
     * Se estiver correto desconsidera o bloco
     * @param lexema recebe o lexema '/'
     * @param a o caractere '*'
     * @param linhaInicialComent a linha inicial que é a mesma linha inicial do comentário de linha, onde iniciou o automato
     */
    private void comentarioBloco(String lexema, char a, int linhaInicialComent) {
        int linhaInicial = linhaInicialComent;
        
        //desconsidera tudo até encontrat o '*' ou o fim do arquivo
        do {
            lexema = lexema + a;
            this.aux++;
            a = this.proximo();
        } while (a != '*' && a != EOF);
        
        if (a == '*') {
            lexema = lexema + a;
            this.aux++;
            a = this.proximo();

            switch (a) {
                //verifica se o caractere seguinte é um '/' e finaliza o comentário de bloco desconsiderando o bloco
                case '/':
                    lexema = lexema + a;
                    this.aux++;
                    /*tokenAuxiliar = new Token(linhaInicial + 1, auxiliarComentario + 1, "CoM", lexema);
                    this.listarTokens.add(tokenAuxiliar);*/ //adiciona o comentário para a lista de tokens
                    break;
                //se não ele retorna ao comentário pois ele não chegou ao fim
                default:
                    this.comentarioBloco(lexema, a, linhaInicial);
                    break;
            }
            
        } //se chegar ao fim do arquivo e não achar o fechamento ele notifica o erro de comentário.
        else {
            this.addErro("CoMF", lexema, linhaInicial);
        }

    }
/**
 * O método operadorRelacional verifica as operadores relacionais em uma operação 
 * Caso receba '>', '<' ou '=' espera um operador de '=' ou já adiciona o token na lista
 * Dessa forma não existe erro de operador relacional mal formado
 * Se receber o operador '!' ele verifica se tem um '=' em seguida e caracteriza ele omo operador relacional
 * senão é classificado como operador logico
 * @param lexema
 * @param a caractere recebido '>' || '<' || '=' || '!'
 */
    private void operadorRelacional(String lexema, char a) {

        int linhaInicial = this.linha;
        int auxiliarOperador = this.aux;
        Token tokenAuxiliar;

        lexema = lexema + a;
        this.aux++;
        //operadores que direciona ao automato
        if (a == '<' || a == '>' || a == '=') {
            
            //chama o proximo caractere
            a = this.proximo();
            //verifica o '=' e adiciona ao lexema
            if (a == '=') {
                lexema = lexema + a;
                this.aux++;
            }
            //adiciona o token a lista seja ele '>', ">=", '<', "<=", '=', "==".
            tokenAuxiliar = new Token(linhaInicial + 1, auxiliarOperador + 1, "REL", lexema);
            listarTokens.add(tokenAuxiliar);

        }//verifica o operador ! 
        else if (a == '!') {
            //chama o proximo
            a = this.proximo();
            //se for '=' é um operador relacional
            if (a == '=') {
                lexema = lexema + a;
                this.aux++;
                //adiciona o operador relacional a lista "!="
                tokenAuxiliar = new Token(linhaInicial + 1, auxiliarOperador + 1, "REL", lexema);
                listarTokens.add(tokenAuxiliar);
            }//senão adiciona o operador lógico a lista  
            else {
                tokenAuxiliar = new Token(linhaInicial + 1, auxiliarOperador + 1, "LOG", lexema);
                listarTokens.add(tokenAuxiliar);
            }
        } 
    }
/**
 * O método de operadorLogico recebe um operador de '&' ou '|' e espera outro igual
 * caso não receba um igual o operador lógivo é considerado mal formado
 * @param lexema
 * @param a caractere recebido '&' || '|' 
 */    
    private void operadorLogico(String lexema, char a) {

        int linhaInicial = this.linha;
        int auxiliarOperador = this.aux;
        Token tokenAuxiliar;

        lexema = lexema + a;
        this.aux++;
        //verifica qual operador recebido se é '&'
        if (a == '&') {
            a = this.proximo();
            //verifica se o operador é outro '&'
            if (a == '&') {
                lexema = lexema + a;
                this.aux++;
                //adiciona o operador a lista de tokens
                tokenAuxiliar = new Token(linhaInicial + 1, auxiliarOperador + 1, "LOG", lexema);
                listarTokens.add(tokenAuxiliar);
            }// caso não seja adiciona a lista como operador logico mal formado
            else {
                this.addErro("LOGMF", lexema, linhaInicial);
            }
        } //ou se é o operador '|'
        else if (a == '|') {
            a = this.proximo();
            //verifica se o operador é outro '|'
            if (a == '|') {
                lexema = lexema + a;
                this.aux++;
                //adiciona o operador a lista de tokens
                tokenAuxiliar = new Token(linhaInicial + 1, auxiliarOperador + 1, "LOG", lexema);
                listarTokens.add(tokenAuxiliar);
            }// caso não seja adiciona a lista como operador logico mal formado 
            else {
                this.addErro("LOGMF", lexema, linhaInicial);
            }
        }
    }
/**
 * Ao receber um digito ele é encaminhado para o automato de numero
 * que pode ser no formato de numero negativo (tratado no autômato de operadorAritmetico)
 * pode ou não conter ponto, caso tenha ponto deve vir logo após o ponto um ou mais digitos
 * @param lexema
 * @param a digito
 */
    private void numero(String lexema, char a) {

        int linhaInicial = linha;
        int auxiliarNumero = aux;
        Token tokenAuxiliar;
        boolean erro = false;
        //é direcionado ao automato ao receber um digito
       do {
            lexema = lexema + a;
            this.aux++;
            a = this.proximo();
            //continua no laço enquanto receber digitos
        } while (Character.isDigit(a));
        //caso receba algo que não é digito verifica se é um ponto
        if (a == '.') {
            //se for um ponto ele adiciona ao lexema
            lexema = lexema + a;
            this.aux++;
            a = this.proximo();
            //verifica se o proximo é um digito
            if (!Character.isDigit(a)) {
                //se não for um digito é considerado incorreto pois sempre após um ponte deve ter pelo menos um digito
                erro = true;
            }//enquanto ouver digitos após o ponto adiciona ao lexema 
            while (Character.isDigit(a)) {
                lexema = lexema + a;
                this.aux++;
                a = this.proximo();
            }
            if (!erro) {
                tokenAuxiliar = new Token(linhaInicial + 1, auxiliarNumero + 1, "NROREAL", lexema);
                listarTokens.add(tokenAuxiliar);
                return;
            }//se houver erro de numero malç formado é adicionado a lista de erros
            else {
                addErro("NMF", lexema, linhaInicial);
            }
        }
        tokenAuxiliar = new Token(linhaInicial + 1, auxiliarNumero + 1, "NROINT", lexema);
        listarTokens.add(tokenAuxiliar);
        //se não houver erros adiciona o numero a lista de tokens

    }
/**
 * A cadeia de caractere é formada a partir do primeiro '"' e busca o ultimo '"'
 * caso não seja fechado ele notifica o erro
 * aceita simbolos válidos da lista, digitos, letras, e '\"'
 * @param lexema
 * @param a '"'
 */
    private void cadeiaDeCaractere(String lexema, char a) {

        int linhaInicial = this.linha;
        int auxiliarCadeiaCaractere = this.aux;
        Token tokenAuxiliar;

        lexema = lexema + a;
        this.aux++;
        a = this.proximo();
        //recebe o primeiro '"' e consome o que tem dentro da linha até encontrar o ultimo '"'
        while (a != '"' && linhaInicial == linha) {
            //caso receba um '\' pode receber um um '"' e continuar na cadeia de caractere sem finalizar
            if (a == ((char) 92)) {
                this.aux++;
                lexema = lexema + a;
                a = this.proximo();
                //verifica se é o '"' e retorna ao automato esperando o ultimo '"'
                if (a == '"') {
                    this.cadeiaDeCaractere(lexema, a);
                    return;
                }
            }//consome a linha enquanto houver simblos validos ou letras ou digitos 
            else if (Character.isLetterOrDigit(a) || token.verificarSimbolo(a)) {
                lexema = lexema + a;
                this.aux++;
                a = this.proximo();
            } //se não for essas opções adiciona o erro na lista
            else {
                this.addErro("CMF", lexema, linhaInicial);
                return;
            }

        }//verifica se esta na mesma linha, ou se encerrarou o comentário
        if (a == '"' && linhaInicial == linha) {
            lexema = lexema + a;
            this.aux++;
            tokenAuxiliar = new Token(linhaInicial + 1, auxiliarCadeiaCaractere + 1, "CDC", lexema);
            this.listarTokens.add(tokenAuxiliar);
        }// adiciona o erro a lista 
        else {
            this.addErro("CMF", lexema, linhaInicial);
        }

    }
/**
 * adiciona o erro numa lista identificando o tipo de erro o lexema e a linha onde está o erro
 * @param tipo tipo do erro 
 * @param erro lexema
 * @param linha linha do erro
 */
    private void addErro(String tipo, String erro, int linha) {
        NumberFormat formatter = new DecimalFormat("00");
        String s = formatter.format(linha + 1);
        listarErros.add(s + " " + tipo + " " + erro + " ");
    }
//retorna a lista de erros
    public ArrayList<String> getListarErros() {
        return listarErros;
    }
//retorna a lista de tokens válidos
    public ArrayList<Token> getListarTokens() {
        return listarTokens;
    }
}
