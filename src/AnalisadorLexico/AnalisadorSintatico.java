//Meta criar o analisador de program, register, var, const.
package AnalisadorLexico;

import java.util.ArrayList;

/**
 * Classe Automato, responsavel pela parte lógica
 *
 * @author Mauricio e Alexandre
 */
public class AnalisadorSintatico {

    private ArrayList<String> VarType;
    private ArrayList<Token> listarTokens;
    private ArrayList<String> listarErros;
    private int posicaoAtual;
    private int posicaoFinal;
    private Token ultimo, penultimo;

    public AnalisadorSintatico() {
        VarType = new ArrayList<>();
        listarErros = new ArrayList<>();
        posicaoAtual = 0;
        VarType.add("integer");
        VarType.add("string");
        VarType.add("real");
        VarType.add("boolean");
        VarType.add("char");
    }

    //A testar....
    public void analiseSintatica(ArrayList<Token> tokens) {
        listarTokens = tokens;
        if (listarTokens.isEmpty()) {
            System.out.println("-------- A lista de Tokens esta vazia -------------");
        } else {
            penultimo = listarTokens.get(listarTokens.size() - 1);
            ultimo = new Token(listarTokens.size(), penultimo.getAux(), "$", "$");
            listarTokens.add(ultimo);
            posicaoFinal = listarTokens.size();

            Start();
        }

    }

    //pega o token atual --------- A testar....
    public Token atual() {
        if (posicaoAtual < posicaoFinal) {
            return (Token) listarTokens.get(posicaoAtual);
        }
        return null;
    }

    //pega o proximo token ------- A testar.... Análise de Erros
    public Token seguinte() {
        if (posicaoAtual + 1 < posicaoFinal) {
            if (listarTokens.get(posicaoAtual + 1) != null) {
                return (Token) listarTokens.get(posicaoAtual + 1);
            }
        }
        return null;
    }

    //pega o token de sincronização ----------- A testar....
    public void sincronizacao(String sinc) {
        while (!(atual().getLexema().equals(sinc))) {
            posicaoAtual = posicaoAtual + 1;
        }
    }

    //Pegar a lista de erros
    public ArrayList<String> getListarErros() {
        return listarErros;
    }

    //Adicionar o erro na lista
    private void addErro(Token token, String erro) {
        listarErros.add("Linha: " + token.getLinha() + " Recebido: " + "'" + token.getLexema() + "'" + " Esperado: " + erro);
    }

    //---------------------------<Start> ::= 'program' Identifier ';' <GlobalStatement>---------------------------------------
    public void Start() {
        if (atual().getLexema().equals("program")) {
            posicaoAtual = posicaoAtual + 1;
            if (atual().getTipo().equals("Identificador")) {
                posicaoAtual = posicaoAtual + 1;
                if (atual().getLexema().equals(";")) {
                    posicaoAtual = posicaoAtual + 1;
                    globalStatement();
                } else {
                    addErro(atual(), "';'");
                    //System.out.println(atual().getLinha());
                    if ((seguinte() != null) && seguinte().getLexema().equals(";")) {
                        posicaoAtual = posicaoAtual + 1;
                        globalStatement();
                    } else {
                        if ((seguinte() != null) && seguinte().getLexema().equals("var")) {
                            globalStatement();
                        } else {
                            posicaoAtual = posicaoAtual + 1;
                            while ((atual() != null) && !(atual().getLexema().equals(";") || (atual().getLexema().equals("var")))) {
                                addErro(atual(), "';'");
                                posicaoAtual = posicaoAtual + 1;
                            }
                            if (atual() != null) {
                                switch (atual().getLexema()) {
                                    case "$":
                                        addErro(atual(), "Fim de programa");
                                        break;
                                    case ";":
                                        posicaoAtual = posicaoAtual + 1;
                                        globalStatement();
                                        break;
                                    default:
                                        globalStatement();
                                        break;
                                }
                            }
                        }
                    }
                }
            } else {
                addErro(atual(), "Identificador");
                //System.out.println(atual().getLinha());     
                if ((seguinte() != null) && seguinte().getTipo().equals("Identificador")) {
                    posicaoAtual = posicaoAtual + 1;
                    posicaoAtual = posicaoAtual + 1;
                    if (atual().getLexema().equals(";")) {
                        posicaoAtual = posicaoAtual + 1;
                        globalStatement();
                    } else {
                        addErro(atual(), "';'");
                        //System.out.println(atual().getLinha());
                        if ((seguinte() != null) && seguinte().getLexema().equals(";")) {
                            posicaoAtual = posicaoAtual + 1;
                            globalStatement();
                        } else {
                            if (seguinte().getLexema().equals("var")) {
                                globalStatement();
                            } else {
                                posicaoAtual = posicaoAtual + 1;
                                while ((atual() != null) && !(atual().getLexema().equals(";") || (atual().getLexema().equals("var")))) {
                                    addErro(atual(), "';'");
                                    posicaoAtual = posicaoAtual + 1;
                                }
                                if (atual() != null) {
                                    switch (atual().getLexema()) {
                                        case "$":
                                            addErro(atual(), "Fim de programa");
                                            break;
                                        case ";":
                                            posicaoAtual = posicaoAtual + 1;
                                            globalStatement();
                                            break;
                                        default:
                                            globalStatement();
                                            break;
                                    }
                                }
                            }
                        }
                    }
                } else {
                    posicaoAtual = posicaoAtual + 1;
                    while ((atual() != null) && !(atual().getTipo().equals("Identificador")
                            || atual().getLexema().equals(";")
                            || atual().getLexema().equals("var"))) {
                        addErro(atual(), "'Identificador'");
                        posicaoAtual = posicaoAtual + 1;
                    }
                    //posicaoAtual = posicaoAtual + 1;

                    if (atual() != null) {
                        if (atual().getLexema().equals("$")) {
                            addErro(atual(), "Fim de programa");
                        } else if (atual().getLexema().equals(";")) {
                            posicaoAtual = posicaoAtual + 1;
                            globalStatement();
                        } else if (atual().getLexema().equals("var")) {
                            globalStatement();

                        } else if (atual().getTipo().equals("Identificador")) {

                            posicaoAtual = posicaoAtual + 1;
                            if (atual().getLexema().equals(";")) {
                                posicaoAtual = posicaoAtual + 1;
                                globalStatement();
                            } else {
                                addErro(atual(), "';'");
                                //System.out.println(atual().getLinha());
                                if ((seguinte() != null) && seguinte().getLexema().equals(";")) {
                                    posicaoAtual = posicaoAtual + 1;
                                    globalStatement();
                                } else {
                                    if ((seguinte() != null) && seguinte().getLexema().equals("var")) {
                                        globalStatement();
                                    } else {
                                        posicaoAtual = posicaoAtual + 1;
                                        while ((atual() != null) && !(atual().getLexema().equals(";") || (atual().getLexema().equals("var")))) {
                                            addErro(atual(), "';'");
                                            posicaoAtual = posicaoAtual + 1;
                                        }
                                        if (atual() != null) {
                                            switch (atual().getLexema()) {
                                                case "$":
                                                    addErro(atual(), "Fim de programa");
                                                    break;
                                                case ";":
                                                    posicaoAtual = posicaoAtual + 1;
                                                    globalStatement();
                                                    break;
                                                default:
                                                    globalStatement();
                                                    break;
                                            }
                                        }
                                    }
                                }
                            }

                        } else {
                            addErro(atual(), "'Identificador'");
                            //System.out.println(atual().getLinha());
                            if ((seguinte() != null) && seguinte().getLexema().equals(";")) {
                                posicaoAtual = posicaoAtual + 1;
                                globalStatement();
                            } else {
                                if (seguinte().getLexema().equals("var")) {
                                    globalStatement();
                                } else {
                                    posicaoAtual = posicaoAtual + 1;
                                    while ((atual() != null) && !(atual().getLexema().equals(";") || (atual().getLexema().equals("var")))) {
                                        addErro(atual(), "';'");
                                        posicaoAtual = posicaoAtual + 1;
                                    }
                                    if (atual() != null) {
                                        switch (atual().getLexema()) {
                                            case "$":
                                                addErro(atual(), "Fim de programa");
                                                break;
                                            case ";":
                                                posicaoAtual = posicaoAtual + 1;
                                                globalStatement();
                                                break;
                                            default:
                                                globalStatement();
                                                break;
                                        }
                                    }
                                }

                            }
                        }
                    }

                }
            }
        } else {
            addErro(atual(), "'program'");
            if ((seguinte() != null) && seguinte().getLexema().equals("program")) {
                posicaoAtual = posicaoAtual + 1;
                posicaoAtual = posicaoAtual + 1;

                if (atual().getTipo().equals("Identificador")) {
                    posicaoAtual = posicaoAtual + 1;
                    if (atual().getLexema().equals(";")) {
                        posicaoAtual = posicaoAtual + 1;
                        globalStatement();
                    } else {
                        addErro(atual(), "';'");
                        //System.out.println(atual().getLinha());
                        if ((seguinte() != null) && seguinte().getLexema().equals(";")) {
                            posicaoAtual = posicaoAtual + 1;
                            globalStatement();
                        } else {
                            if ((seguinte() != null) && seguinte().getLexema().equals("var")) {
                                globalStatement();
                            } else {
                                posicaoAtual = posicaoAtual + 1;
                                while ((atual() != null) && !(atual().getLexema().equals(";") || (atual().getLexema().equals("var")))) {
                                    addErro(atual(), "';'");
                                    posicaoAtual = posicaoAtual + 1;
                                }
                                if (atual() != null) {
                                    switch (atual().getLexema()) {
                                        case "$":
                                            addErro(atual(), "Fim de programa");
                                            break;
                                        case ";":
                                            posicaoAtual = posicaoAtual + 1;
                                            globalStatement();
                                            break;
                                        default:
                                            globalStatement();
                                            break;
                                    }
                                }
                            }
                        }
                    }
                } else {
                    addErro(atual(), "Identificador");
                    //System.out.println(atual().getLinha());     
                    if ((seguinte() != null) && seguinte().getTipo().equals("Identificador")) {
                        posicaoAtual = posicaoAtual + 1;
                        posicaoAtual = posicaoAtual + 1;
                        if (atual().getLexema().equals(";")) {
                            posicaoAtual = posicaoAtual + 1;
                            globalStatement();
                        } else {
                            addErro(atual(), "';'");
                            //System.out.println(atual().getLinha());
                            if ((seguinte() != null) && seguinte().getLexema().equals(";")) {
                                posicaoAtual = posicaoAtual + 1;
                                globalStatement();
                            } else {
                                if (seguinte().getLexema().equals("var")) {
                                    globalStatement();
                                } else {
                                    posicaoAtual = posicaoAtual + 1;
                                    while ((atual() != null) && !(atual().getLexema().equals(";") || (atual().getLexema().equals("var")))) {
                                        addErro(atual(), "';'");
                                        posicaoAtual = posicaoAtual + 1;
                                    }
                                    if (atual() != null) {
                                        switch (atual().getLexema()) {
                                            case "$":
                                                addErro(atual(), "Fim de programa");
                                                break;
                                            case ";":
                                                posicaoAtual = posicaoAtual + 1;
                                                globalStatement();
                                                break;
                                            default:
                                                globalStatement();
                                                break;
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        posicaoAtual = posicaoAtual + 1;
                        while ((atual() != null) && !(atual().getTipo().equals("Identificador")
                                || atual().getLexema().equals(";")
                                || atual().getLexema().equals("var"))) {
                            addErro(atual(), "'Identificador'");
                            posicaoAtual = posicaoAtual + 1;
                        }
                        //posicaoAtual = posicaoAtual + 1;

                        if (atual() != null) {
                            if (atual().getLexema().equals("$")) {
                                addErro(atual(), "Fim de programa");
                            } else if (atual().getLexema().equals(";")) {
                                posicaoAtual = posicaoAtual + 1;
                                globalStatement();
                            } else if (atual().getLexema().equals("var")) {
                                globalStatement();

                            } else if (atual().getTipo().equals("Identificador")) {

                                posicaoAtual = posicaoAtual + 1;
                                if (atual().getLexema().equals(";")) {
                                    posicaoAtual = posicaoAtual + 1;
                                    globalStatement();
                                } else {
                                    addErro(atual(), "';'");
                                    //System.out.println(atual().getLinha());
                                    if ((seguinte() != null) && seguinte().getLexema().equals(";")) {
                                        posicaoAtual = posicaoAtual + 1;
                                        globalStatement();
                                    } else {
                                        if ((seguinte() != null) && seguinte().getLexema().equals("var")) {
                                            globalStatement();
                                        } else {
                                            posicaoAtual = posicaoAtual + 1;
                                            while ((atual() != null) && !(atual().getLexema().equals(";") || (atual().getLexema().equals("var")))) {
                                                addErro(atual(), "';'");
                                                posicaoAtual = posicaoAtual + 1;
                                            }
                                            if (atual() != null) {
                                                switch (atual().getLexema()) {
                                                    case "$":
                                                        addErro(atual(), "Fim de programa");
                                                        break;
                                                    case ";":
                                                        posicaoAtual = posicaoAtual + 1;
                                                        globalStatement();
                                                        break;
                                                    default:
                                                        globalStatement();
                                                        break;
                                                }
                                            }
                                        }
                                    }
                                }

                            } else {
                                addErro(atual(), "'Identificador'");
                                //System.out.println(atual().getLinha());
                                if ((seguinte() != null) && seguinte().getLexema().equals(";")) {
                                    posicaoAtual = posicaoAtual + 1;
                                    globalStatement();
                                } else {
                                    if (seguinte().getLexema().equals("var")) {
                                        globalStatement();
                                    } else {
                                        posicaoAtual = posicaoAtual + 1;
                                        while ((atual() != null) && !(atual().getLexema().equals(";") || (atual().getLexema().equals("var")))) {
                                            addErro(atual(), "';'");
                                            posicaoAtual = posicaoAtual + 1;
                                        }
                                        if (atual() != null) {
                                            switch (atual().getLexema()) {
                                                case "$":
                                                    addErro(atual(), "Fim de programa");
                                                    break;
                                                case ";":
                                                    posicaoAtual = posicaoAtual + 1;
                                                    globalStatement();
                                                    break;
                                                default:
                                                    globalStatement();
                                                    break;
                                            }
                                        }
                                    }

                                }
                            }
                        }

                    }
                }
            } else {
                posicaoAtual = posicaoAtual + 1;
                while ((atual() != null) && !(atual().getLexema().equals("program")
                        || atual().getTipo().equals("Identificador")
                        || atual().getLexema().equals(";")
                        || atual().getLexema().equals("var"))) {
                    addErro(atual(), "'program'");
                    posicaoAtual = posicaoAtual + 1;

                }
                if (atual() != null) {
                    switch (atual().getTipo()) {
                        case "Identificador":
                            posicaoAtual = posicaoAtual + 1;
                            if (atual().getLexema().equals(";")) {
                                posicaoAtual = posicaoAtual + 1;
                                globalStatement();
                            } else {
                                addErro(atual(), "';'");
                                //System.out.println(atual().getLinha());
                                if ((seguinte() != null) && seguinte().getLexema().equals(";")) {
                                    posicaoAtual = posicaoAtual + 1;
                                    globalStatement();
                                } else {
                                    if ((seguinte() != null) && seguinte().getLexema().equals("var")) {
                                        globalStatement();
                                    } else {
                                        posicaoAtual = posicaoAtual + 1;
                                        while ((atual() != null) && !(atual().getLexema().equals(";") || (atual().getLexema().equals("var")))) {
                                            addErro(atual(), "';'");
                                            posicaoAtual = posicaoAtual + 1;
                                        }
                                        if (atual() != null) {
                                            switch (atual().getLexema()) {
                                                case "$":
                                                    addErro(atual(), "Fim de programa");
                                                    break;
                                                case ";":
                                                    posicaoAtual = posicaoAtual + 1;
                                                    globalStatement();
                                                    break;
                                                default:
                                                    globalStatement();
                                                    break;
                                            }
                                        }
                                    }
                                }
                            }
                            break;
                    }
                }

                if (atual() != null) {
                    System.out.println("----------------------------------------------------Entrou aqui--------------------------------------------");
                    switch (atual().getLexema()) {
                        case "$":
                            //addErro(atual(), "Fim de programa");
                            break;
                        case "program":
                            posicaoAtual = posicaoAtual + 1;
                            if (atual().getTipo().equals("Identificador")) {
                                posicaoAtual = posicaoAtual + 1;
                                if (atual().getLexema().equals(";")) {
                                    posicaoAtual = posicaoAtual + 1;
                                    globalStatement();
                                } else {
                                    addErro(atual(), "';'");
                                    //System.out.println(atual().getLinha());
                                    if ((seguinte() != null) && seguinte().getLexema().equals(";")) {
                                        posicaoAtual = posicaoAtual + 1;
                                        globalStatement();
                                    } else {
                                        if ((seguinte() != null) && seguinte().getLexema().equals("var")) {
                                            globalStatement();
                                        } else {
                                            posicaoAtual = posicaoAtual + 1;
                                            while ((atual() != null) && !(atual().getLexema().equals(";") || (atual().getLexema().equals("var")))) {
                                                addErro(atual(), "';'");
                                                posicaoAtual = posicaoAtual + 1;
                                            }
                                            if (atual() != null) {
                                                switch (atual().getLexema()) {
                                                    case "$":
                                                        addErro(atual(), "Fim de programa");
                                                        break;
                                                    case ";":
                                                        posicaoAtual = posicaoAtual + 1;
                                                        globalStatement();
                                                        break;
                                                    default:
                                                        globalStatement();
                                                        break;
                                                }
                                            }
                                        }
                                    }
                                }
                            } else {
                                addErro(atual(), "Identificador");
                                //System.out.println(atual().getLinha());     
                                if ((seguinte() != null) && seguinte().getTipo().equals("Identificador")) {
                                    posicaoAtual = posicaoAtual + 1;
                                    posicaoAtual = posicaoAtual + 1;
                                    if (atual().getLexema().equals(";")) {
                                        posicaoAtual = posicaoAtual + 1;
                                        globalStatement();
                                    } else {
                                        addErro(atual(), "';'");
                                        //System.out.println(atual().getLinha());
                                        if ((seguinte() != null) && seguinte().getLexema().equals(";")) {
                                            posicaoAtual = posicaoAtual + 1;
                                            globalStatement();
                                        } else {
                                            if (seguinte().getLexema().equals("var")) {
                                                globalStatement();
                                            } else {
                                                posicaoAtual = posicaoAtual + 1;
                                                while ((atual() != null) && !(atual().getLexema().equals(";") || (atual().getLexema().equals("var")))) {
                                                    addErro(atual(), "';'");
                                                    posicaoAtual = posicaoAtual + 1;
                                                }
                                                if (atual() != null) {
                                                    switch (atual().getLexema()) {
                                                        case "$":
                                                            addErro(atual(), "Fim de programa");
                                                            break;
                                                        case ";":
                                                            posicaoAtual = posicaoAtual + 1;
                                                            globalStatement();
                                                            break;
                                                        default:
                                                            globalStatement();
                                                            break;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    posicaoAtual = posicaoAtual + 1;
                                    while ((atual() != null) && !(atual().getTipo().equals("Identificador")
                                            || atual().getLexema().equals(";")
                                            || atual().getLexema().equals("var"))) {
                                        addErro(atual(), "'Identificador'");
                                        posicaoAtual = posicaoAtual + 1;
                                    }
                                    //posicaoAtual = posicaoAtual + 1;

                                    if (atual() != null) {
                                        if (atual().getLexema().equals("$")) {
                                            addErro(atual(), "Fim de programa");
                                        } else if (atual().getLexema().equals(";")) {
                                            posicaoAtual = posicaoAtual + 1;
                                            globalStatement();
                                        } else if (atual().getLexema().equals("var")) {
                                            globalStatement();

                                        } else if (atual().getTipo().equals("Identificador")) {

                                            posicaoAtual = posicaoAtual + 1;
                                            if (atual().getLexema().equals(";")) {
                                                posicaoAtual = posicaoAtual + 1;
                                                globalStatement();
                                            } else {
                                                addErro(atual(), "';'");
                                                //System.out.println(atual().getLinha());
                                                if ((seguinte() != null) && seguinte().getLexema().equals(";")) {
                                                    posicaoAtual = posicaoAtual + 1;
                                                    globalStatement();
                                                } else {
                                                    if ((seguinte() != null) && seguinte().getLexema().equals("var")) {
                                                        globalStatement();
                                                    } else {
                                                        posicaoAtual = posicaoAtual + 1;
                                                        while ((atual() != null) && !(atual().getLexema().equals(";") || (atual().getLexema().equals("var")))) {
                                                            addErro(atual(), "';'");
                                                            posicaoAtual = posicaoAtual + 1;
                                                        }
                                                        if (atual() != null) {
                                                            switch (atual().getLexema()) {
                                                                case "$":
                                                                    addErro(atual(), "Fim de programa");
                                                                    break;
                                                                case ";":
                                                                    posicaoAtual = posicaoAtual + 1;
                                                                    globalStatement();
                                                                    break;
                                                                default:
                                                                    globalStatement();
                                                                    break;
                                                            }
                                                        }
                                                    }
                                                }
                                            }

                                        } else {
                                            addErro(atual(), "'Identificador'");
                                            //System.out.println(atual().getLinha());
                                            if ((seguinte() != null) && seguinte().getLexema().equals(";")) {
                                                posicaoAtual = posicaoAtual + 1;
                                                globalStatement();
                                            } else {
                                                if (seguinte().getLexema().equals("var")) {
                                                    globalStatement();
                                                } else {
                                                    posicaoAtual = posicaoAtual + 1;
                                                    while ((atual() != null) && !(atual().getLexema().equals(";") || (atual().getLexema().equals("var")))) {
                                                        addErro(atual(), "';'");
                                                        posicaoAtual = posicaoAtual + 1;
                                                    }
                                                    if (atual() != null) {
                                                        switch (atual().getLexema()) {
                                                            case "$":
                                                                addErro(atual(), "Fim de programa");
                                                                break;
                                                            case ";":
                                                                posicaoAtual = posicaoAtual + 1;
                                                                globalStatement();
                                                                break;
                                                            default:
                                                                globalStatement();
                                                                break;
                                                        }
                                                    }
                                                }

                                            }
                                        }
                                    }

                                }
                            }
                            break;
                        case ";":
                            posicaoAtual = posicaoAtual + 1;
                            globalStatement();
                            break;

                        default:
                            globalStatement();
                            break;
                    }
                }
            }
        }
    }

    //<GlobalStatement> ::= <VarStatement> <ConstStatement> <RegisterStatement><ProcedureStatement><FunctionStatement> <Main>
    public void globalStatement() {
        VarStatement();
        ConstStatement();
        RegisterStatement();
        ProcedureStatement();
        // FunctionStatement();
        // Main();
    }

    //Declaracao Variaveis
    //<VarStatement>::= 'var' '{' <VarList>
    private void VarStatement() {
        if (atual().getLexema().equals("var")) {
            posicaoAtual = posicaoAtual + 1;
            if (atual().getLexema().equals("{")) {
                posicaoAtual = posicaoAtual + 1;
                VarList();
            } else {
                addErro(atual(), "'{'");
            }
        } else {
            addErro(atual(), "'var'");
        }
    }

    //<VarList>::= <VarDeclaration> <VarList1> | '}'
    private void VarList() {
        if ((atual() != null)) {
            posicaoAtual = posicaoAtual + 1;
            VarDeclaration();
            VarList1();
        } else if (atual().getLexema().equals("}")) {
            posicaoAtual = posicaoAtual + 1;
        } else {
            addErro(atual(), "'}'");
        }
    }

    //<VarList1>::= <VarDeclaration> <VarList1> | '}'
    private void VarList1() {
        if ((atual() != null)) {
            posicaoAtual = posicaoAtual + 1;
            VarDeclaration();
            VarList1();
        } else if ((atual() != null) && atual().getLexema().equals("}")) {
            posicaoAtual = posicaoAtual + 1;
        } else {
            addErro(atual(), "'}'");
        }
    }

    //<VarDeclaration>::= <VarType> Identifier <VarDeclaration1>
    private void VarDeclaration() {
        if ((atual() != null) && VarType.contains(atual().getLexema())) {
            posicaoAtual = posicaoAtual + 1;
            if (atual().getTipo().equals("Identificador")) {
                posicaoAtual = posicaoAtual + 1;
                VarDeclaration1();
            } else {
                addErro(atual(), "'Identificador'");
            }
        } else {
            addErro(atual(), "tipo");
        }
    }

    //<VarDeclaration1>::= ',' Identifier <VarDeclaration1> | ';'
    private void VarDeclaration1() {
        if (atual().getLexema().equals(",")) {
            posicaoAtual = posicaoAtual + 1;
            if (atual().getTipo().equals("Identificador")) {
                posicaoAtual = posicaoAtual + 1;
                VarDeclaration1();
            } else {
                addErro(atual(), "Identificador");
            }
        } else if (atual().getLexema().equals(";")) {
            posicaoAtual = posicaoAtual + 1;
        } else {
            addErro(atual(), ";");
        }
    }

    // Declaracao Const
    //<ConstStatement> ::= 'const' '{' <ConstList>
    private void ConstStatement() {
        if ((atual() != null) && atual().getLexema().equals("const")) {
            posicaoAtual = posicaoAtual + 1;
            if (atual() != null && atual().getLexema().equals("{")) {
                posicaoAtual = posicaoAtual + 1;
                ConstList();
            }
        }

    }

    //<ConstList>::= <ConstDeclaration> <ConstList1>
    private void ConstList() {
        if (atual() != null) {
            posicaoAtual = posicaoAtual + 1;
            ConstDeclaration();
            ConstList1();
        }
    }

    //<ConstList1> ::= <ConstDeclaration> <ConstList1> | '}'
    private void ConstList1() {
        if (atual() != null) {
            posicaoAtual = posicaoAtual + 1;
            ConstDeclaration();
            ConstList1();
            if ((atual() != null) && (atual().getLexema().equals("}"))) {
                posicaoAtual = posicaoAtual + 1;

            } else {
                addErro(atual(), "'}'");
                System.out.println(atual().getLinha());
            }
        }

    }

    //Checar <ConstType>!
    //<ConstDeclaration> ::= <ConstType> Identifier '=' <Value> <ConstDeclaration1>
    private void ConstDeclaration() {

        if (VarType.contains(atual().getLexema())) {
            if (atual().getTipo().equals("Identificador")) {
                posicaoAtual = posicaoAtual + 1;
                if (atual().getLexema().equals("=")) {
                    posicaoAtual = posicaoAtual + 1;
                    if (atual().getTipo().equals("Value")) {
                        posicaoAtual = posicaoAtual + 1;
                        ConstDeclaration1();
                    }
                }
            }

        }
    }

    //<ConstDeclaration1> ::= ',' Identifier  '=' <Value> <ConstDeclaration1> | ';'
    private void ConstDeclaration1() {
        if ((atual() != null) && atual().getLexema().equals(",")) {
            posicaoAtual = posicaoAtual + 1;
            if ((atual() != null) && atual().getTipo().equals("Identificador")) {
                posicaoAtual = posicaoAtual + 1;
                if ((atual() != null) && atual().getLexema().equals("=")) {
                    posicaoAtual = posicaoAtual + 1;
                    if ((atual() != null) && atual().getTipo().equals("Value")) {
                        posicaoAtual = posicaoAtual + 1;
                        ConstDeclaration1();
                    }
                }
            }
        } else if (atual().getLexema().equals(";")) {
            posicaoAtual = posicaoAtual + 1;
        }
    }

    //<Value>  ::= Decimal | RealNumber | StringLiteral | Identifier <ValueRegister> | Char | Boolean
    private void Value() {
        if (atual().getTipo().equals("Identificador")) {
            posicaoAtual = posicaoAtual + 1;
            ValueRegister();
        } else if ((atual() != null) && (atual().getTipo().equals("Decimal")
                || atual().getTipo().equals("RealNumber") || atual().getTipo().equals("StringLiteral")
                || atual().getLexema().equals("Char") || atual().getLexema().equals("Boolean"))) {
            posicaoAtual = posicaoAtual + 1;
        }
    }

    /*   
 private void Value() {
        if (atual().getTipo().equals("Identificador")) {
            posicaoAtual = posicaoAtual + 1;
            ValueRegister();
        } else if ((atual() != null) && (atual().getTipo().equals("Decimal")
                || atual().getTipo().equals("RealNumber") || atual().getTipo().equals("StringLiteral")
                || atual().getTipo().equals("Char") || atual().getTipo().equals("Boolean"))) {
            posicaoAtual = posicaoAtual + 1;
        }
    }*/ //nessa parte aqui olhando pelo modelo base só colocariamos lexema pra 'true' ou 'false',
    //como temos o boolean ele já decide um ou outro
    // Declaracao Register
    // <ValueRegister> ::= '.' Identifier |
    private void ValueRegister() {
        if ((atual() != null) && atual().getLexema().equals(".")) {
            posicaoAtual = posicaoAtual + 1;
            if ((atual() != null) && atual().getTipo().equals("Identificador")) {
                posicaoAtual = posicaoAtual + 1;
            }
        }
    }

    // <RegisterStatementMultiple> ::= <RegisterStatement> |
    private void RegisterStatementMultiple() {
        if ((atual() != null)) {
            posicaoAtual = posicaoAtual + 1;
            RegisterStatement();
        }
    }

    //<RegisterStatement> ::= 'register' Identifier '{' <RegisterList>
    private void RegisterStatement() {
        if ((atual() != null) && atual().getLexema().equals("register")) {
            posicaoAtual = posicaoAtual + 1;
            if ((atual() != null) && atual().getTipo().equals("Identificador")) {
                posicaoAtual = posicaoAtual + 1;
                if ((atual() != null) && atual().getLexema().equals("{")) {
                    posicaoAtual = posicaoAtual + 1;
                    RegisterList();
                }
            }
        }
    }

    //<RegisterList> ::= <RegisterDeclaration> <RegisterList1>
    private void RegisterList() {
        if ((atual() != null)) {
            RegisterDeclaration();
            RegisterList1();
        }
    }

    //<RegisterList1> ::= <RegisterDeclaration> <RegisterList1> | '}' <RegisterStatementMultiple>
    private void RegisterList1() {
        if ((atual() != null)) {
            posicaoAtual = posicaoAtual + 1;
            RegisterDeclaration();
            RegisterList1();
        } else if ((atual() != null) && atual().getLexema().equals("}")) {
            posicaoAtual = posicaoAtual + 1;
            RegisterStatementMultiple();
        }
    }

    //<RegisterDeclaration> ::= <ConstType> Identifier <RegisterDeclaration1>
    private void RegisterDeclaration() {
        if ((atual() != null) && VarType.contains(atual().getLexema())) {
            posicaoAtual = posicaoAtual + 1;
            if (atual().getTipo().equals("Identificador")) {
                posicaoAtual = posicaoAtual + 1;
                RegisterDeclaration1();
            }
        }
    }

    //<RegisterDeclaration1> ::= ',' Identifier <RegisterDeclaration1> | ';'
    private void RegisterDeclaration1() {
        if (atual().getLexema().equals(",")) {
            posicaoAtual = posicaoAtual + 1;
            if (atual().getTipo().equals("Identificador")) {
                posicaoAtual = posicaoAtual + 1;
                RegisterDeclaration1();
            }
        } else if (atual().getLexema().equals(";")) {
            posicaoAtual = posicaoAtual + 1;
        }
    }

    // Declaração Function e Procedure 
    //<ProcedureStatement> ::= 'procedure' Identifier '(' <ParameterProcedure> '{' <LocalStatement> <ProcedureStatement1> |
    private void ProcedureStatement() {
        if ((atual() != null) && atual().getLexema().equals("procedure")) {
            posicaoAtual = posicaoAtual + 1;
            if ((atual() != null) && atual().getTipo().equals("Identificador")) {
                posicaoAtual = posicaoAtual + 1;
                if ((atual() != null) && atual().getLexema().equals("(")) {
                    posicaoAtual = posicaoAtual + 1;
                    ParameterProcedure();
                    if ((atual() != null) && atual().getLexema().equals("{")) {
                        posicaoAtual = posicaoAtual + 1;
                        LocalStatement();
                        ProcedureStatement1();
                    }
                }
            }
        }
    }

    //<ProcedureStatement1> ::= '}' | '}' 'procedure' Identifier '(' <ParameterProcedure> '{' <LocalStatement>  <ProcedureStatement1>
    private void ProcedureStatement1() {
        if (atual().getLexema().equals("}")) {
            posicaoAtual = posicaoAtual + 1;
        } else if (atual().getLexema().equals("}")) {
            posicaoAtual = posicaoAtual + 1;
            if (atual().getLexema().equals("procedure")) {
                posicaoAtual = posicaoAtual + 1;
                if (atual().getTipo().equals("Identificador")) {
                    posicaoAtual = posicaoAtual + 1;
                    if (atual().getLexema().equals("(")) {
                        posicaoAtual = posicaoAtual + 1;
                        ParameterProcedure();
                        if (atual().getLexema().equals("{")) {
                            posicaoAtual = posicaoAtual + 1;
                            LocalStatement();
                            ProcedureStatement1();
                        }
                    }
                }
            }
        }
    }

    //<ParameterProcedure> ::= <VarType> Identifier <ParameterListProcedure> | ')'
    private void ParameterProcedure() {
        if (VarType.contains(atual().getLexema())) {
            posicaoAtual = posicaoAtual + 1;
            if (atual().getTipo().equals("Identificador")) {
                posicaoAtual = posicaoAtual + 1;
                ParameterListProcedure();
            }
        } else if (atual().getLexema().equals(")")) {
            posicaoAtual = posicaoAtual + 1;
        }
    }

    //<ParameterListProcedure> ::=   ',' <ParameterProcedure> |  ')'  
    private void ParameterListProcedure() {
        if (atual().getLexema().equals(",")) {
            posicaoAtual = posicaoAtual + 1;
            ParameterProcedure();
        } else if (atual().getLexema().equals(")")) {
            posicaoAtual = posicaoAtual + 1;
        }
    }

    //<ParameterFunction> ::= <VarType> Identifier <ParameterListFunction> | ')' ':' <VarType>
    private void ParameterFunction() {
    }

    //<ParameterListFunction> ::=   ',' <ParameterFunction> |  ')' ':' <VarType> 
    private void ParameterListFunction() {
    }

    //<FunctionStatement>::= 'function' Identifier  '(' <ParameterFunction> '{' <LocalStatement> 'return' <Value>';' <FunctionStatement1> |
    private void FunctionStatement() {
    }

    //<FunctionStatement1>::= '}' | '}' 'function' Identifier  '(' <ParameterFunction>  '{' <LocalStatement> 'return' <Value>';' <FunctionStatement1> 
    private void FunctionStatement1() {
    }
    //---------Atribuição
    //<Assigment> ::= Identifier <AssigmentRegister>

    private void Assigment() {
    }

    //<AssigmentRegister> ::= '.' Identifier '=' <AssigmentOperators> ';' | '=' <AssigmentOperators> ';' | '++' ';' | '--' ';'
    private void AssigmentRegister() {
    }

    //<AssigmentOperators> ::= <Value> | <BinaryExpression> | <UnaryExpression>
    private void AssigmentOperators() {
    }

    //! Expressão
    //<BinaryExpression> ::= <AddendOperator> <BinaryExpressionContin> 
    private void BinaryExpression() {
    }

    //<BinaryExpressionContin> ::= '+' <AddendOperator> 
    //                    | '-' <AddendOperator>
    //                    | '*' <AddendOperator>
    //                    | '/' <AddendOperator>
    //                    | '++'
    //                    | '--'
    //                    | <RelationalExpression>
    //                    | <LogicalExpression>
    private void BinaryExpressionContin() {
    }

    //<RelationalExpression> ::= '<' <AddendOperator>
    //                    | '>' <AddendOperator>
    //                    | '!=' <AddendOperator>
    //                    | '<=' <AddendOperator>
    //                    | '>=' <AddendOperator>
    //                    | '==' <AddendOperator>
    private void RelationalExpression() {
    }

    //<LogicalExpression> ::= '||' <AddendOperator> | '&&' <AddendOperator>
    private void LogicalExpression() {
    }

    //<AddendOperator> ::= Identifier | Decimal | RealNumber | Boolean
    private void AddendOperator() {
    }

    //<UnaryExpression> ::= '!' <AddendOperatorUnary>
    private void UnaryExpression() {
    }

    //<AddendOperatorUnary> ::= Identifier | Boolean
    private void AddendOperatorUnary() {
    }

    //---------Declaracoes Logicas
    //<AssignExpr> ::= <LogicalOrExpression> |
    private void AssignExpr() {
    }

    //<LogicalOrExpression> ::= <LogicalAndExpression> <LogicalOrExpression1>
    private void LogicalOrExpression() {
    }

    //<LogicalOrExpression1> ::= '||' <LogicalAndExpression> <LogicalOrExpression1> |
    private void LogicalOrExpression1() {
    }

    //<LogicalAndExpression> ::= <Condition> <LogicalAndExpression1>
    private void LogicalAndExpression() {
    }

    //<LogicalAndExpression1> ::= '&&' <Condition> <LogicalAndExpression1> |
    private void LogicalAndExpression1() {
    }

    //---------Chamada de função
    //<FunctionCall> ::= Identifier '=' Identifier '(' <Argument> ')' ';'
    private void FunctionCall() {
    }

    //<Argument> ::= <Value> <ArgumentList> |
    private void Argument() {
    }

    //<ArgumentList> ::= ',' <Argument> |
    private void ArgumentList() {
    }

    //---------Chamada de procedure
    //<ProcedureCall> ::= Identifier '(' <Argument> ')' ';'
    private void ProcedureCall() {
    }

    //---------Declaração Main
    //<Main> ::= 'main' '{' <LocalStatement> '}'
    private void Main() {
    }

    //---------Blocos
    //<LocalStatement> ::= <VarStatement> <LocalCommands>
    private void LocalStatement() {
        VarStatement();
        // LocalCommands();
    }

    //<LocalCommands> ::= <IfDecs> <LocalCommands>
    //              | <WriteDecs> <LocalCommands>
    //              | <ReadDecs> <LocalCommands>
    //              | <WhileDecs> <LocalCommands>
    //              | <Assigment> <LocalCommands>
    //              | <FunctionCall> <LocalCommands>
    //              | <ProcedureCall> <LocalCommands>
    //              |
    private void LocalCommands() {
        if ((atual() != null) && (atual().getLexema().equals("IfDecs"))) {
            IfDecs();
        } else if ((atual() != null) && (atual().getLexema().equals("WriteDecs"))) {
            //WriteDecs();
        } else if ((atual() != null) && (atual().getLexema().equals("ReadDecs"))) {
            //ReadDecs();
        } else if ((atual() != null) && (atual().getLexema().equals("WhileDecs"))) {
            //WhileDecs();
        } else if ((atual() != null) && (atual().getTipo().equals("Assigment"))) {
            //Assigment();
        } else if ((atual() != null) && (atual().getTipo().equals("FunctionCall"))) {
            //FunctionCall();
        } else if ((atual() != null) && (atual().getTipo().equals("ProcedureCall"))) {
            //ProcedureCall();
        }
    }

    //---------Condicao
    //<Condition> ::= <AddendOperator> <ConditionContin>
    private void Condition() {
    }

    //<ConditionContin> ::= <RelationalExpression> | <LogicalExpression>
    private void ConditionContin() {
    }

    //---------Declaracao If/Else
    //<IfDecs> ::= 'if' '(' <AssignExpr> ')' '{' <LocalCommands> '}' <ElseDecs>                                                    
    private void IfDecs() {
        if ((atual() != null) && (atual().getLexema().equals("if"))) {
            posicaoAtual = posicaoAtual + 1;
            if ((atual() != null) && (atual().getLexema().equals("("))) {
                posicaoAtual = posicaoAtual + 1;
                //AssingExpr();
                if ((atual() != null) && (atual().getLexema().equals(")"))) {
                    posicaoAtual = posicaoAtual + 1;
                    if ((atual() != null) && (atual().getLexema().equals("{"))) {
                        posicaoAtual = posicaoAtual + 1;
                        LocalCommands();
                        if ((atual() != null) && (atual().getLexema().equals("}"))) {
                            posicaoAtual = posicaoAtual + 1;
                            ElseDecs();
                        }

                    }
                }
            }
        }
    }

    //<ElseDecs>::= 'else' '{' <LocalCommands> '}' |
    private void ElseDecs() {
        if ((atual() != null) && (atual().getLexema().equals("else"))) {
            posicaoAtual = posicaoAtual + 1;
            if ((atual() != null) && (atual().getLexema().equals("{"))) {
                posicaoAtual = posicaoAtual + 1;
                if ((atual() != null) && (atual().getLexema().equals("{"))) {
                    posicaoAtual = posicaoAtual + 1;
                    LocalCommands();
                    if ((atual() != null) && (atual().getLexema().equals("}"))) {
                        posicaoAtual = posicaoAtual + 1;
                        ElseDecs();
                    }
                }
            }
        }
    }

    //---------Declaracao while
    //<WhileDecs>::= 'while' '('<AssignExpr>')' '{' <LocalCommands> '}'  
    private void WhileDecs() {
        if ((atual() != null) && (atual().getLexema().equals("while"))) {
            posicaoAtual = posicaoAtual + 1;
        }
    }

    //---------Declaração Write 
    //<WriteDecs> ::= 'print' '(' <ArgumentsWrite>
    private void WriteDecs() {
    }

    //<ArgumentsWrite> ::= Identifier <RegisterWrite> <ListArgumentsWrite> | <WriteContent> <ListArgumentsWrite>
    private void ArgumentsWrite() {
    }

    //<WriteContent> ::= Decimal | RealNumber | StringLiteral
    private void WriteContent() {
    }

    //<RegisterWrite> ::= '.' Identifier |
    private void RegisterWrite() {
    }

    //<ListArgumentsWrite> ::= ',' <ArgumentsWrite> | ')' ';'
    private void ListArgumentsWrite() {
    }

    //---------Declaração Read
    //<ReadDecs> ::= 'read' '(' <ArgumentsRead>
    private void ReadDecs() {
    }

    //<ArgumentsRead> ::= Identifier <RegisterRead> <ListArgumentsRead>
    private void ArgumentsRead() {
    }

    //<RegisterRead> ::= '.' Identifier |
    private void RegisterRead() {
    }

    //<ListArgumentsRead> ::= ',' <ArgumentsRead> | ')' ';' 
    private void ListArgumentsRead() {
    }
}
