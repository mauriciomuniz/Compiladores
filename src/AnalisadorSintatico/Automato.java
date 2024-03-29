package AnalisadorSintatico;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Classe Automato, responsavel pela parte lógica
 *
 * @author Mauricio e Alexandre
 */
public class Automato {

    private final ArrayList<Token> listarTokens; //lista de tokens corretoa
    private final ArrayList<String> listarErros; //lista de erros
    private ArrayList<String> codigo; // entrada
    private static final char EOF = '\0';
    private int linha, aux; //linha e coluna 
    private boolean linhaVazia;
    private final EstruturaLexica token;

    /**
     * Construtor da classe Automato
     */
    public Automato() {
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
        char a = proximoChar();
        while (a != EOF) {
            verificaAutomato(a);
            a = proximoChar();
        }
    }

    private char proximoChar() {
        if (!codigo.isEmpty()) { // Verifica se o arquivo está vazio

            char c[] = codigo.get(linha).toCharArray(); //Adição da linha atual do codigo em um vetor de caracteres

            /*
            *  Verifica se a linha acabou,
            * se a posição do último elento é aigual a posição atual de colunas
             */
            if (c.length == aux) {
                linhaVazia = false;
                return ' ';
            } // Percorre a linha na posição atual da coluna
            else if (c.length > aux) {
                linhaVazia = false;
                return c[aux]; // Retorna o elemento da posição atual da coluna
            } else if (codigo.size() > (linha + 1)) { // Verifica se ainda existem linhas para percorrer
                linha++;
                c = codigo.get(linha).toCharArray(); // Cria uma nova linha de sequencia de caracteres
                aux = 0; // reinicia a posição das colunas
                if (c.length == 0) { // Checa se a linha está vazia
                    this.linhaVazia = true;
                    return ' ';
                }
                return c[aux]; // Retorna o elemento da posição atual da coluna
            } else {
                return EOF; // Alcançou o fim de arquivo
            }
        } else {
            return EOF; // Alcançou o fim de arquivo
        }
    }

    private void verificaAutomato(char a) {
        String lexema = "";
        if (!this.linhaVazia) {
            //verifica se o caracter é espaço, se for é desconsiderado
            if (token.verificarEspaco(a)) {
                aux++;
            } else if (token.verificarLetra(a)) {//verifica se é uma letra e direciona pra palavra reservada e identificador 
                palavraReservadaId(lexema, a);
            } else if (Character.isDigit(a)) {//verifica se é um digito e direciona pra numero
                numero(lexema, a);
            } else if (token.verificarOperador(a)) {//verifica se é um operador e envia para operadores e trata
                operador(lexema, a);
            } else if (token.verificarDelimitador(a)) { //verifica se é um delimitador e envia para delimitadores 
                delimitador(lexema, a);
            } else if (a == '%') {//verifica se é uma '%' e envia para comentário de linha e trata
                comentarioLinha(lexema, a);
            } else if (a == '/') {//verifica se é uma '/' e envia para checar se é comentário de bloco o Op. Aritm.
                barraSimples(lexema, a);
            } else if (a == '"') {//verifica se é uma '"' e envia para cadeia de caracteres 
                cadeiaDeCaractere(lexema, a);
            } else if (a == '\'') {//verifica se é uma '"' e envia para cadeia de caracteres 
                caractere(lexema, a);
            } else {//qualquer simbolo diferente aos da tabela é considerado um erro de palavra invalida
                this.palavraInvalida(lexema, a);
            }

        } else {
            linhaVazia = false;
            linha++;
        }
    }

    /**
     * Identificador deve receber apenas letras, digitos ou o simbolo '_'
     * Palavra recervada compara se o que foi recebido é igual a uma das
     * palavras salvas no array
     *
     * @param lexema
     * @param a caractere que direciona ao automato de palavraReservadaId é uma
     * letra.
     */
    private void palavraReservadaId(String lexema, char a) {

        int linhaInicial = linha;
        int auxPalavraReservadaId = aux;
        Token tokenaux;

        lexema = lexema + a;
        this.aux++;
        a = this.proximoChar();
        //verifica se os proximos caracteres podem formar uma palavra reservada ou um identificador
        while (a == '_' || Character.isLetterOrDigit(a)) {
            lexema = lexema + a;
            aux++;
            a = this.proximoChar();
        }//enquanto for letra || '_' || digito continua no laço e adiciona o caractere no lexema
        //compara a palavre recebida com a lista de palavras reservadas
        if (token.verificarPalavrasReservada(lexema)) {
            tokenaux = new Token(linhaInicial + 1, auxPalavraReservadaId + 1, "PalavraReservada", lexema);
        }//se não considera a palavra um identificador 
        else {
            tokenaux = new Token(linhaInicial + 1, auxPalavraReservadaId + 1, "Identifier", lexema);
        }//adiciona o token a lista de tokens corretos
        listarTokens.add(tokenaux);

    }

    /**
     * Apenas adiciona o Simbolo incorreto a lista de erros
     *
     * @param lexema
     * @param a caractere que direciona ao automato de palavraInvalida é
     * qualquer simbolo que não pertença a nenhuma lista
     */
    private void palavraInvalida(String lexema, char a) {

        int linhaInicial = this.linha;
        int auxiliarSimbolo = this.aux;
        Token tokenAuxiliar;
        lexema = lexema + a;
        this.aux++;
        //adiciona o simbolo invalido a lista de erros 
        if (!token.verificarSimboloSem39(a) || !token.verificarSimboloSem34(a)) {
            this.addListaErro("SimboloIncorreto", lexema, linhaInicial);
        } else {//adiciona o simbolo válido a lista de tokens
            tokenAuxiliar = new Token(linhaInicial + 1, auxiliarSimbolo + 1, "Simbolo", lexema);
            listarTokens.add(tokenAuxiliar);
        }
    }

    /**
     * Delimitador adiciona o token na lista
     *
     * @param lexema
     * @param a caractere que direciona ao automato delimitador é identificado
     * pela lista de delimitadores
     */
    private void delimitador(String lexema, char a) {

        int linhaInicial = this.linha;
        int auxiliarDelimitador = this.aux;
        Token tokenAuxiliar;
        //adiciona o delimitador a lista de tokens corretos
        lexema = lexema + a;
        this.aux++;
        tokenAuxiliar = new Token(linhaInicial + 1, auxiliarDelimitador + 1, "Delimitador", lexema);
        listarTokens.add(tokenAuxiliar);
    }

    /**
     * Operador identifica que o simbolo é um identificador direciona para o
     * tipo de operador referente a ele (entre Aritmetico, lógico e relacional)
     *
     * @param lexema
     * @param a caractere que direciona ao automato operador é identificado pela
     * lista de operadores
     */
    private void operador(String lexema, char a) {
        //envia para automato de operadorAritmetico
        switch (a) {
            //envia para o automato de operadorRelacional
            case '+':
            case '-':
            case '*':
                operadorAritmetico(lexema, a);
                break;
            //envia ao automato de operadorLogico
            case '<':
            case '>':
            case '=':
            case '!':
                operadorRelacional(lexema, a);
                break;
            default:
                operadorLogico(lexema, a);
                break;
        }
    }

    /**
     * O método operadorAritmetico verifica o primeiro caractere recebido se for
     * adição verifica se é uma soma comum ou incremento ( '+' || '++') se for
     * subtração verifica se é uma subtração comum ou decremento ou número
     * negativo( '-' || '--' || "-nro") "-nro" se o que antecede ele não for um
     * número ou um identificador caso contrário ele se torna um operador
     * aritmético '*' operador aritmético de multiplicação
     *
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
            a = this.proximoChar();
            //incremento
            if (a == '+') {
                lexema = lexema + a;
                this.aux++;
            }
        } else if (a == '-') {
            a = this.proximoChar();
            //pega o último token da lista
            tokenAnterior = listarTokens.get(listarTokens.size() - 1);
            //desconsidera os espaços
            if (Character.isSpaceChar(a)) {
                do {
                    this.aux++;
                    a = this.proximoChar();
                } while (token.verificarEspaco(a));
                if (Character.isDigit(a) && linhaInicial == linha) {
                    // compara se o último token é um numero ou identificador
                    if (!(tokenAnterior.getTipo().equals("Numero") || tokenAnterior.getTipo().equals("Identifier"))) {
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
                if (!(tokenAnterior.getTipo().equals("Numero") || tokenAnterior.getTipo().equals("Identifier"))) {
                    //se não for um numero ou um identificador é enviado para o método de número
                    this.numero(lexema, a);
                    return;
                }
            }

        }
        //adiciona a lista de token o operador aritmetico
        tokenAuxiliar = new Token(linhaInicial + 1, auxiliarOpAritmetico + 1, "OpAritmetico", lexema);
        listarTokens.add(tokenAuxiliar);
    }

    /**
     * O método comentarioLinha verifica quando é um comentário
     *
     * @param lexema
     * @param a caractere recebido '%'
     */
    private void comentarioLinha(String lexema, char a) {
        int linhaInicial = this.linha;
        int auxiliarComentario = this.aux;
        Token tokenAuxiliar;

        if (a == '%') {
            lexema = lexema + a;
            this.aux++;
            a = this.proximoChar();
            //percorre a linha toda até o final desconsiderando essa linha
            while (linha == linhaInicial && a != EOF) {
                lexema = lexema + a;
                this.aux++;
                a = this.proximoChar();
            }
            /*tokenAuxiliar = new Token(linhaInicial + 1, auxiliarComentario + 1, "CoM", lexema);
                this.listarTokens.add(tokenAuxiliar);*/ //adiciona o comentário para a lista de tokens
        }
    }

    /**
     * O método barraSimples verifica quando é um operador aritmético ou quando
     * recebe um / e em seguido um # é enviado para o método que trata
     * comentário de bloco
     *
     * @param lexema
     * @param a caractere recebido '/'
     */
    private void barraSimples(String lexema, char a) {
        int linhaInicial = this.linha;
        int auxiliarComentario = this.aux;
        Token tokenAuxiliar;

        lexema = lexema + a;
        this.aux++;
        a = this.proximoChar();

        switch (a) {
            //se receber o '#' é encaminhado para o método comentário de bloco
            case '#':
                this.comentarioBloco(lexema, a, linhaInicial);
                return;
            //Caso não seja é considerado um operador aritmético
            default:
                tokenAuxiliar = new Token(linhaInicial + 1, auxiliarComentario + 1, "OpAritmetico", lexema);
                this.listarTokens.add(tokenAuxiliar);
                break;
        }
    }

    /**
     * O método comentarioBloco verifica se o comentário foi fechado com '# /'
     * caso não tenha sido informa erro de comentário mal formado Se estiver
     * correto desconsidera o bloco
     *
     * @param lexema recebe o lexema '/'
     * @param a o caractere '#'
     * @param linhaInicialComent a linha inicial que é a mesma linha inicial do
     * comentário de linha, onde iniciou o automato
     */
    private void comentarioBloco(String lexema, char a, int linhaInicialComent) {
        int linhaInicial = linhaInicialComent;

        //desconsidera tudo até encontrat o '#' ou o fim do arquivo
        do {
            lexema = lexema + a;
            this.aux++;
            a = this.proximoChar();
        } while (a != '#' && a != EOF);

        if (a == '#') {
            lexema = lexema + a;
            this.aux++;
            a = this.proximoChar();

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
            this.addListaErro("ComentarioMF", lexema, linhaInicial);
        }

    }

    /**
     * O método operadorRelacional verifica as operadores relacionais em uma
     * operação Caso receba '>', '<' ou '=' espera um operador de '=' ou já
     * adiciona o token na lista. Dessa forma não existe erro de operador
     * relacional mal formado Se receber o operador '!' ele verifica se tem um
     * '=' em seguida e caracteriza ele omo operador relacional senão é
     * classificado como operador logico.
     *
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

            //chama o proximoChar caractere
            a = this.proximoChar();
            //verifica o '=' e adiciona ao lexema
            if (a == '=') {
                lexema = lexema + a;
                this.aux++;
            }
            //adiciona o token a lista seja ele '>', ">=", '<', "<=", '=', "==".
            tokenAuxiliar = new Token(linhaInicial + 1, auxiliarOperador + 1, "OpRelacional", lexema);
            listarTokens.add(tokenAuxiliar);

        }//verifica o operador ! 
        else if (a == '!') {
            //chama o proximoChar
            a = this.proximoChar();
            //se for '=' é um operador relacional
            if (a == '=') {
                lexema = lexema + a;
                this.aux++;
                //adiciona o operador relacional a lista "!="
                tokenAuxiliar = new Token(linhaInicial + 1, auxiliarOperador + 1, "OpRelacional", lexema);
                listarTokens.add(tokenAuxiliar);
            }//senão adiciona o operador lógico a lista  
            else {
                tokenAuxiliar = new Token(linhaInicial + 1, auxiliarOperador + 1, "OpLogico", lexema);
                listarTokens.add(tokenAuxiliar);
            }
        }
    }

    /**
     * O método de operadorLogico recebe um operador de '&' ou '|' e espera
     * outro igual caso não receba um igual o operador lógivo é considerado mal
     * formado
     *
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
            a = this.proximoChar();
            //verifica se o operador é outro '&'
            if (a == '&') {
                lexema = lexema + a;
                this.aux++;
                //adiciona o operador a lista de tokens
                tokenAuxiliar = new Token(linhaInicial + 1, auxiliarOperador + 1, "OpLogico", lexema);
                listarTokens.add(tokenAuxiliar);
            }// caso não seja adiciona a lista como operador logico mal formado
            else {
                this.addListaErro("OpLogicoMF", lexema, linhaInicial);
            }
        } //ou se é o operador '|'
        else if (a == '|') {
            a = this.proximoChar();
            //verifica se o operador é outro '|'
            if (a == '|') {
                lexema = lexema + a;
                this.aux++;
                //adiciona o operador a lista de tokens
                tokenAuxiliar = new Token(linhaInicial + 1, auxiliarOperador + 1, "OpLogico", lexema);
                listarTokens.add(tokenAuxiliar);
            }// caso não seja adiciona a lista como operador logico mal formado 
            else {
                this.addListaErro("OpLogicoMF", lexema, linhaInicial);
            }
        }
    }

    /**
     * Ao receber um digito ele é encaminhado para o automato de numero que pode
     * ser no formato de numero negativo (tratado no autômato de
     * operadorAritmetico) pode ou não conter ponto, caso tenha ponto deve vir
     * logo após o ponto um ou mais de um digito
     *
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
            a = this.proximoChar();
            //continua no laço enquanto receber digitos
        } while (Character.isDigit(a));
        //caso receba algo que não é digito verifica se é um ponto
        if (a == '.') {
            //se for um ponto ele adiciona ao lexema
            lexema = lexema + a;
            this.aux++;
            a = this.proximoChar();
            //verifica se o proximoChar é um digito
            if (!Character.isDigit(a)) {
                //se não for um digito é considerado incorreto pois sempre após um ponto deve ter pelo menos um digito
                erro = true;
            }//enquanto ouver digitos após o ponto adiciona ao lexema 
            while (Character.isDigit(a)) {
                lexema = lexema + a;
                this.aux++;
                a = this.proximoChar();
            }
            if (!erro) {
                tokenAuxiliar = new Token(linhaInicial + 1, auxiliarNumero + 1, "RealNumber", lexema);
                listarTokens.add(tokenAuxiliar);
                return;
            }//se houver erro de numero malç formado é adicionado a lista de erros
            else {
                addListaErro("NumeroMF", lexema, linhaInicial);
            }
        }
        tokenAuxiliar = new Token(linhaInicial + 1, auxiliarNumero + 1, "Decimal", lexema);
        listarTokens.add(tokenAuxiliar);
        //se houver erro de numero mal formado é adicionado a lista de erros

    }

    /**
     * A cadeia de caractere é formada a partir do primeiro " e busca o ultimo "
     * caso não seja fechado é notificado o erro, aceita simbolos válidos da
     * lista de símbolos menos o 34, digitos, letras
     *
     * @param lexema
     * @param a '"'
     */
    private void cadeiaDeCaractere(String lexema, char a) {

        int linhaInicial = this.linha;
        int auxiliarCadeiaCaractere = this.aux;
        Token tokenAuxiliar;

        lexema = lexema + a;
        this.aux++;
        a = this.proximoChar();
        //recebe o primeiro " e consome o que tem dentro da linha até encontrar o ultimo "
        while (a != '"' && linhaInicial == linha) {
            if (Character.isLetterOrDigit(a) || token.verificarSimboloSem34(a)) {
                lexema = lexema + a;
                this.aux++;
                a = this.proximoChar();
            } //se não for essas opções adiciona o erro na lista
            else {
                this.addListaErro("StringLiteralMF", lexema, linhaInicial);
                return;
            }

        }//verifica se esta na mesma linha, ou se encerrarou o comentário
        if (a == '"' && linhaInicial == linha) {
            lexema = lexema + a;
            this.aux++;
            tokenAuxiliar = new Token(linhaInicial + 1, auxiliarCadeiaCaractere + 1, "StringLiteral", lexema);
            this.listarTokens.add(tokenAuxiliar);
        }// adiciona o erro a lista 
        else {
            this.addListaErro("StringLiteralMF", lexema, linhaInicial);
        }

    }

    /**
     * O caractere é formado a partir do primeiro ' e busca o ultimo ' caso não
     * seja fechado é notificado o erro, aceita simbolos válidos da lista de
     * símbolos menos o 39, digitos, letras.
     *
     * @param lexema
     * @param a '''
     */
    private void caractere(String lexema, char a) {

        int linhaInicial = this.linha;
        int auxiliarCadeiaCaractere = this.aux;
        Token tokenAuxiliar;

        lexema = lexema + a;
        this.aux++;
        a = this.proximoChar();
        //recebe o primeiro ' e consome o que tem dentro da linha até encontrar o ultimo '

        if (Character.isLetterOrDigit(a) || token.verificarSimboloSem39(a)) {
            lexema = lexema + a;
            this.aux++;
            a = this.proximoChar();
        } //se não for essas opções adiciona o erro na lista
        else {
            this.addListaErro("CaractereMF", lexema, linhaInicial);
            return;
        }

        if (a == '\'' && linhaInicial == linha) {
            lexema = lexema + a;
            this.aux++;
            tokenAuxiliar = new Token(linhaInicial + 1, auxiliarCadeiaCaractere + 1, "Char", lexema);
            this.listarTokens.add(tokenAuxiliar);
        }// adiciona o erro a lista 
        else {
            this.addListaErro("CaractereMF", lexema, linhaInicial);
        }

    }

    /**
     * Adiciona o erro numa lista e faz a identificação do tipo de erro o lexema
     * e a linha onde aconteceu o erro
     *
     * @param tipo tipo do erro
     * @param erro lexema
     * @param linha linha do erro
     */
    private void addListaErro(String tipo, String erro, int linha) {
        NumberFormat formatter = new DecimalFormat("00");
        String s = formatter.format(linha + 1);
        listarErros.add(s + " " + tipo + " " + erro + " ");
    }

    /**
     * Função que pega a lista de erro
     *
     * @return lista de erros
     */
    public ArrayList<String> getListarErros() {
        return listarErros;
    }
//

    /**
     * Função que pega a lista de Tokens
     *
     * @return lista de tokens válidos
     */
    public ArrayList<Token> getListarTokens() {
        return listarTokens;
    }
}
