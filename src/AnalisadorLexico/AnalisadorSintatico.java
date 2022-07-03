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
    //static int RecursiveRegister = 0;
    static int RecursiveVar = 0;
    static int RecursiveConst = 0;

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

//    //Pega o token depois do seguinte
//    public Token terceiro() {
//        if (posicaoAtual + 2 < posicaoFinal) {
//            if (listarTokens.get(posicaoAtual + 2) != null) {
//                return (Token) listarTokens.get(posicaoAtual + 2);
//            }
//        }
//        return null;
//    }
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
            if (atual().getTipo().equals("Identifier")) {
                posicaoAtual = posicaoAtual + 1;
                if (atual().getLexema().equals(";")) {
                    posicaoAtual = posicaoAtual + 1;
                    globalStatement();
                } else {
                    addErro(atual(), "';'");
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
                                        addErro(atual(), "Fim de programa Inesperado");
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
                addErro(atual(), "Identifier");
                if ((seguinte() != null) && seguinte().getTipo().equals("Identifier")) {
                    posicaoAtual = posicaoAtual + 1;
                    posicaoAtual = posicaoAtual + 1;
                    if (atual().getLexema().equals(";")) {
                        posicaoAtual = posicaoAtual + 1;
                        globalStatement();
                    } else {
                        addErro(atual(), "';'");
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
                                            addErro(atual(), "Fim de programa Inesperado");
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
                    while ((atual() != null) && !(atual().getTipo().equals("Identifier")
                            || atual().getLexema().equals(";")
                            || atual().getLexema().equals("var"))) {
                        addErro(atual(), "'Identifier'");
                        posicaoAtual = posicaoAtual + 1;
                    }
                    //posicaoAtual = posicaoAtual + 1;

                    if (atual() != null) {
                        if (atual().getLexema().equals("$")) {
                            addErro(atual(), "Fim de programa Inesperado");
                        } else if (atual().getLexema().equals(";")) {
                            posicaoAtual = posicaoAtual + 1;
                            globalStatement();
                        } else if (atual().getLexema().equals("var")) {
                            globalStatement();

                        } else if (atual().getTipo().equals("Identifier")) {

                            posicaoAtual = posicaoAtual + 1;
                            if (atual().getLexema().equals(";")) {
                                posicaoAtual = posicaoAtual + 1;
                                globalStatement();
                            } else {
                                addErro(atual(), "';'");
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
                                                    addErro(atual(), "Fim de programa Inesperado");
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
                            addErro(atual(), "'Identifier'");
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
                                                addErro(atual(), "Fim de programa Inesperado");
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

                if (atual().getTipo().equals("Identifier")) {
                    posicaoAtual = posicaoAtual + 1;
                    if (atual().getLexema().equals(";")) {
                        posicaoAtual = posicaoAtual + 1;
                        globalStatement();
                    } else {
                        addErro(atual(), "';'");
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
                                            addErro(atual(), "Fim de programa Inesperado");
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
                    addErro(atual(), "Identifier");
                    if ((seguinte() != null) && seguinte().getTipo().equals("Identifier")) {
                        posicaoAtual = posicaoAtual + 1;
                        posicaoAtual = posicaoAtual + 1;
                        if (atual().getLexema().equals(";")) {
                            posicaoAtual = posicaoAtual + 1;
                            globalStatement();
                        } else {
                            addErro(atual(), "';'");
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
                                                addErro(atual(), "Fim de programa Inesperado");
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
                        while ((atual() != null) && !(atual().getTipo().equals("Identifier")
                                || atual().getLexema().equals(";")
                                || atual().getLexema().equals("var"))) {
                            addErro(atual(), "'Identifier'");
                            posicaoAtual = posicaoAtual + 1;
                        }
                        //posicaoAtual = posicaoAtual + 1;

                        if (atual() != null) {
                            if (atual().getLexema().equals("$")) {
                                addErro(atual(), "Fim de programa Inesperado");
                            } else if (atual().getLexema().equals(";")) {
                                posicaoAtual = posicaoAtual + 1;
                                globalStatement();
                            } else if (atual().getLexema().equals("var")) {
                                globalStatement();

                            } else if (atual().getTipo().equals("Identifier")) {

                                posicaoAtual = posicaoAtual + 1;
                                if (atual().getLexema().equals(";")) {
                                    posicaoAtual = posicaoAtual + 1;
                                    globalStatement();
                                } else {
                                    addErro(atual(), "';'");
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
                                                        addErro(atual(), "Fim de programa Inesperado");
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
                                addErro(atual(), "'Identifier'");
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
                                                    addErro(atual(), "Fim de programa Inesperado");
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
                        || atual().getTipo().equals("Identifier")
                        || atual().getLexema().equals(";")
                        || atual().getLexema().equals("var"))) {
                    addErro(atual(), "'program'");
                    posicaoAtual = posicaoAtual + 1;

                }
                if (atual() != null) {
                    switch (atual().getTipo()) {
                        case "Identifier":
                            posicaoAtual = posicaoAtual + 1;
                            if (atual().getLexema().equals(";")) {
                                posicaoAtual = posicaoAtual + 1;
                                globalStatement();
                            } else {
                                addErro(atual(), "';'");
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
                                                    addErro(atual(), "Fim de programa Inesperado");
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
                    switch (atual().getLexema()) {
                        case "$":
                            //addErro(atual(), "Fim de programa Inesperado");
                            break;
                        case "program":
                            posicaoAtual = posicaoAtual + 1;
                            if (atual().getTipo().equals("Identifier")) {
                                posicaoAtual = posicaoAtual + 1;
                                if (atual().getLexema().equals(";")) {
                                    posicaoAtual = posicaoAtual + 1;
                                    globalStatement();
                                } else {
                                    addErro(atual(), "';'");
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
                                                        addErro(atual(), "Fim de programa Inesperado");
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
                                addErro(atual(), "Identifier");
                                if ((seguinte() != null) && seguinte().getTipo().equals("Identifier")) {
                                    posicaoAtual = posicaoAtual + 1;
                                    posicaoAtual = posicaoAtual + 1;
                                    if (atual().getLexema().equals(";")) {
                                        posicaoAtual = posicaoAtual + 1;
                                        globalStatement();
                                    } else {
                                        addErro(atual(), "';'");
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
                                                            addErro(atual(), "Fim de programa Inesperado");
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
                                    while ((atual() != null) && !(atual().getTipo().equals("Identifier")
                                            || atual().getLexema().equals(";")
                                            || atual().getLexema().equals("var"))) {
                                        addErro(atual(), "'Identifier'");
                                        posicaoAtual = posicaoAtual + 1;
                                    }
                                    //posicaoAtual = posicaoAtual + 1;

                                    if (atual() != null) {
                                        if (atual().getLexema().equals("$")) {
                                            addErro(atual(), "Fim de programa Inesperado");
                                        } else if (atual().getLexema().equals(";")) {
                                            posicaoAtual = posicaoAtual + 1;
                                            globalStatement();
                                        } else if (atual().getLexema().equals("var")) {
                                            globalStatement();

                                        } else if (atual().getTipo().equals("Identifier")) {

                                            posicaoAtual = posicaoAtual + 1;
                                            if (atual().getLexema().equals(";")) {
                                                posicaoAtual = posicaoAtual + 1;
                                                globalStatement();
                                            } else {
                                                addErro(atual(), "';'");
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
                                                                    addErro(atual(), "Fim de programa Inesperado");
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
                                            addErro(atual(), "'Identifier'");
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
                                                                addErro(atual(), "Fim de programa Inesperado");
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
        FunctionStatement();
        Main();
    }

    //Declaracao Variaveis
    //<VarStatement>::= 'var' '{' <VarList>
    private void VarStatement() {
        if ((atual() != null) && atual().getLexema().equals("var")) {
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

//    //<VarList>::= <VarDeclaration> <VarList1> | '}'
//    private void VarList() {
//        if (atual() != null && !seguinte().getLexema().equals("}")) {
//            System.out.println("Está aqui----------------------------------------------");
//            VarDeclaration();
//            VarList1();
//        } else if (atual().getLexema().equals("}")) {
//            posicaoAtual = posicaoAtual + 1;
//        } else {
//            addErro(atual(), "'}'");
//        }
//    }
    //<VarList>::= <VarDeclaration> <VarList1> | '}'
    private void VarList() {
        if (atual().getLexema().equals("}")) {
            posicaoAtual = posicaoAtual + 1;
        } else {
            VarDeclaration();
            VarList1();
        }
    }

//    //Testando com VarList por causa de java.lang.StackOverflowError
//    //<VarList1>::= <VarDeclaration> <VarList1> | '}'
//    private void VarList1() {
//        if (RecursiveVar <= 500) {
//            if ((atual() != null)) {
//                //VarDeclaration();
//                if (!atual().getLexema().equals("}")) {
//                    //System.out.println("--------------------------- " + VarType.contains(atual().getLexema()) + " --------------------------");
//                    VarDeclaration();
//                    VarList1();
//                } else if ((atual() != null) && atual().getLexema().equals("}")) {
//                    posicaoAtual = posicaoAtual + 1;
//                } else {
//                    //if (atual() != null) {
//                    addErro(atual(), "'}'");
//                    System.out.println(atual().getLinha());
//                    //}
//                }
//            }
//        }
//        RecursiveVar++;
//    }
    //Testando com VarList por causa de java.lang.StackOverflowError
    //<VarList1>::= <VarDeclaration> <VarList1> | '}'
    private void VarList1() {
        if (RecursiveVar <= 500) {
            if (atual().getLexema().equals("}")) {
                posicaoAtual = posicaoAtual + 1;
            } else {
                VarDeclaration();
                VarList1();
            }
        }
        RecursiveVar++;
    }

    //RecursiveVar
    //<VarDeclaration>::= <VarType> Identifier <VarDeclaration1>
    private void VarDeclaration() {
        if (RecursiveVar <= 500) {
            if (atual() != null) {
                if (VarType.contains(atual().getLexema()) || atual().getTipo().equals("Identifier")) {
                    posicaoAtual = posicaoAtual + 1;
                    if (atual().getTipo().equals("Identifier")) {
                        posicaoAtual = posicaoAtual + 1;
                        VarDeclaration1();
                    } else {
                        addErro(atual(), "'Identifier'");
                    }
                } else {
                    addErro(atual(), "'tipo'");
                }
            }
        }
        RecursiveVar++;
    }

    //<VarDeclaration1>::= ',' Identifier <VarDeclaration1> | ';'
    private void VarDeclaration1() {
        if (atual().getLexema().equals(",")) {
            posicaoAtual = posicaoAtual + 1;
            if (atual().getTipo().equals("Identifier")) {
                posicaoAtual = posicaoAtual + 1;
                VarDeclaration1();
            } else {
                addErro(atual(), "'Identifier'");
            }
        } else if (atual().getLexema().equals(";")) {
            posicaoAtual = posicaoAtual + 1;
        } else {
            addErro(atual(), "';'");
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
            } else {
                addErro(atual(), "'{'");
            }
        } else {
            addErro(atual(), "'const'");

        }
    }

    //<ConstList>::= <ConstDeclaration> <ConstList1>
    private void ConstList() {
        if (atual() != null) {
            ConstDeclaration();
            ConstList1();
        }
    }

    //RecursiveConst
    //<ConstList1> ::= <ConstDeclaration> <ConstList1> | '}'
    private void ConstList1() {
        if (atual() != null) {
            if (RecursiveConst <= 500) {
                if ((atual().getLexema().equals("}"))) {
                    posicaoAtual = posicaoAtual + 1;
                } else {
                    ConstDeclaration();
                    ConstList1();
                }
            }
            RecursiveConst++;
        }
    }

    //Checar <ConstType>!
    //<ConstDeclaration> ::= <ConstType> Identifier '=' <ConstValue> <ConstDeclaration1>
    private void ConstDeclaration() {
        if (RecursiveConst <= 500) {
            if (VarType.contains(atual().getLexema())) {
                posicaoAtual = posicaoAtual + 1;
                if (atual().getTipo().equals("Identifier")) {
                    posicaoAtual = posicaoAtual + 1;
                    if (atual().getLexema().equals("=")) {
                        posicaoAtual = posicaoAtual + 1;
                        ConstValue();
                        ConstDeclaration1();
                    } else {
                        addErro(atual(), "'='");
                    }
                } else {
                    addErro(atual(), "'Identifier'");
                    //System.out.println(" Identifier " + atual().getLinha());
                }
            } else {
                addErro(atual(), "'tipo'");
                //System.out.println(" tipo " + atual().getLinha());
            }
        }
        RecursiveConst++;
    }

    //<ConstDeclaration1> ::= ',' Identifier  '=' <ConstValue> <ConstDeclaration1> | ';'
    private void ConstDeclaration1() {
        if ((atual() != null) && atual().getLexema().equals(",")) {
            posicaoAtual = posicaoAtual + 1;
            if ((atual() != null) && atual().getTipo().equals("Identifier")) {
                posicaoAtual = posicaoAtual + 1;
                if ((atual() != null) && atual().getLexema().equals("=")) {
                    posicaoAtual = posicaoAtual + 1;
                    ConstValue();
                    ConstDeclaration1();
                } else {
                    addErro(atual(), "'='");
                }
            } else {
                addErro(atual(), "'Identifier'");
            }
        } else if (atual().getLexema().equals(";")) {
            posicaoAtual = posicaoAtual + 1;
        } else {
            addErro(atual(), "';'");
        }
    }

    //<ConstValue>::= Decimal | RealNumber | StringLiteral | Char | Boolean
    private void ConstValue() {
        if ((atual() != null) && (atual().getTipo().equals("RealNumber")
                || atual().getTipo().equals("Decimal") || atual().getTipo().equals("StringLiteral")
                || atual().getTipo().equals("Char") || atual().getLexema().equals("boolean")
                || atual().getLexema().equals("true") || atual().getLexema().equals("false"))) {
            posicaoAtual = posicaoAtual + 1;
        } else {
            addErro(atual(), "Valor incorreto para valores");
            while (!(atual().getLexema().equals(",")
                    || atual().getLexema().equals(";")
                    || atual().getLexema().equals("$")
                    || atual().getLexema().equals("}"))) {
                posicaoAtual = posicaoAtual + 1;
            }
            if (atual().getLexema().equals("$")) {
                addErro(atual(), "fim de programa Inesperado");
            }
        }
    }

    //nessa parte aqui olhando pelo modelo base só colocariamos lexema pra 'true' ou 'false',
    //como temos o boolean ele já decide um ou outro (CORRIGIR essa parte)
    //<Value>  ::= Decimal | RealNumber | StringLiteral | Identifier <ValueRegister> | Char | Boolean
    private void Value() {
        if (atual().getTipo().equals("Identifier")) {
            posicaoAtual = posicaoAtual + 1;
            ValueRegister();

        } else if ((atual() != null) && (atual().getTipo().equals("RealNumber")
                || atual().getTipo().equals("Decimal") || atual().getTipo().equals("StringLiteral")
                || atual().getTipo().equals("Char") || atual().getLexema().equals("boolean")
                || atual().getLexema().equals("true") || atual().getLexema().equals("false"))) {
            posicaoAtual = posicaoAtual + 1;
        } else {
            addErro(atual(), "Valor incorreto para valores");
            while (!(atual().getLexema().equals(",")
                    || atual().getLexema().equals(";")
                    || atual().getLexema().equals("$")
                    || atual().getLexema().equals("}"))) {
                posicaoAtual = posicaoAtual + 1;
            }
            if (atual().getLexema().equals("$")) {
                addErro(atual(), "fim de programa Inesperado");
            }
        }
    }

    // Declaracao Register
    // <ValueRegister> ::= '.' Identifier |
    private void ValueRegister() {
        if ((atual() != null) && atual().getLexema().equals(".")) {
            posicaoAtual = posicaoAtual + 1;
            if ((atual() != null) && atual().getTipo().equals("Identifier")) {
                posicaoAtual = posicaoAtual + 1;
            } else {
                addErro(atual(), "'Identifier'");
            }
        } else {
            //addErro(atual(), "'.'"); //Tem que ter o null
        }
    }

    // <RegisterStatementMultiple> ::= <RegisterStatement> |
    private void RegisterStatementMultiple() {
        if ((atual() != null) && !atual().getLexema().equals("procedure")) {
            RegisterStatement();
        }
    }

    //<RegisterStatement> ::= 'register' Identifier '{' <RegisterList>
    private void RegisterStatement() {
        if ((atual() != null) && atual().getLexema().equals("register")) {
            posicaoAtual = posicaoAtual + 1;
            if ((atual() != null) && atual().getTipo().equals("Identifier")) {
                posicaoAtual = posicaoAtual + 1;
                if ((atual() != null) && atual().getLexema().equals("{")) {
                    posicaoAtual = posicaoAtual + 1;
                    RegisterList();
                } else {
                    addErro(atual(), "'{'");
                }
            } else {
                addErro(atual(), "'Identifier'");
            }
        } else {
            addErro(atual(), "'register'");
        }
    }

    //<RegisterList> ::= <RegisterDeclaration> <RegisterList1>
    private void RegisterList() {
        if ((atual() != null)) {
            RegisterDeclaration();
            RegisterList1();
        }
    }

    //Testes evitar o StackOverflowError
    //<RegisterList1> ::= <RegisterDeclaration> <RegisterList1> | '}' <RegisterStatementMultiple>
    private void RegisterList1() {
//        if (RecursiveRegister <= 500) {
        if ((atual() != null) && !atual().getLexema().equals("procedure")) {
            if (!atual().getLexema().equals("}")) {
                RegisterDeclaration();
                RegisterList1();
            } else if ((atual() != null) && atual().getLexema().equals("}")) {
                posicaoAtual = posicaoAtual + 1;
                RegisterStatementMultiple();
            } else {
                addErro(atual(), "'}'");
            }
        }
//        }
//        RecursiveRegister++;
    }

    //Testes evitar o StackOverflowErrors
    //<RegisterDeclaration> ::= <ConstType> Identifier <RegisterDeclaration1>
    private void RegisterDeclaration() {
//        if (RecursiveRegister <= 500) {
        if ((atual() != null) && VarType.contains(atual().getLexema())) {
            posicaoAtual = posicaoAtual + 1;
            if (atual().getTipo().equals("Identifier")) {
                posicaoAtual = posicaoAtual + 1;
                RegisterDeclaration1();
            } else {
                addErro(atual(), "'Identifier'");
            }
        } else {
            addErro(atual(), "'tipo'");
        }
//        }
//        RecursiveRegister++;
    }

    //<RegisterDeclaration1> ::= ',' Identifier <RegisterDeclaration1> | ';'
    private void RegisterDeclaration1() {
        if ((atual() != null) && atual().getLexema().equals(",")) {
            posicaoAtual = posicaoAtual + 1;
            if (atual().getTipo().equals("Identifier")) {
                posicaoAtual = posicaoAtual + 1;
                RegisterDeclaration1();
            } else {
                addErro(atual(), "'Identifier'");
            }
        } else if ((atual() != null) && atual().getLexema().equals(";")) {
            posicaoAtual = posicaoAtual + 1;
        } else {
            addErro(atual(), "';'");
        }
    }

    // Declaração Function e Procedure 
    //<ProcedureStatement> ::= 'procedure' Identifier '(' <ParameterProcedure> '{' <LocalStatement> <ProcedureStatement1> |
    private void ProcedureStatement() {
        if ((atual() != null) && atual().getLexema().equals("procedure")) {
            posicaoAtual = posicaoAtual + 1;
            if ((atual() != null) && atual().getTipo().equals("Identifier")) {
                posicaoAtual = posicaoAtual + 1;
                if ((atual() != null) && atual().getLexema().equals("(")) {
                    posicaoAtual = posicaoAtual + 1;
                    ParameterProcedure();
                    if ((atual() != null) && atual().getLexema().equals("{")) {
                        posicaoAtual = posicaoAtual + 1;
                        LocalStatement();
                        ProcedureStatement1();
                    } else {
                        addErro(atual(), "'{'");
                    }
                } else {
                    addErro(atual(), "'('");
                }
            } else {
                addErro(atual(), "'Identifier'");
            }
        } else {
            if (atual().getLexema().equals("function")) {
                FunctionStatement();
            } else {
                addErro(atual(), "'procedure'");
            }
        }
    }

    //<ProcedureStatement1> ::= '}'  <ProcedureStatement>
    private void ProcedureStatement1() {
        if ((atual() != null) && atual().getLexema().equals("}")) {
            posicaoAtual = posicaoAtual + 1;
            ProcedureStatement();
        } else {
            addErro(atual(), "'}'");
        }
    }

    //<ParameterProcedure> ::= <VarType> Identifier <ParameterListProcedure> | ')'
    private void ParameterProcedure() {
        if (VarType.contains(atual().getLexema())) {
            posicaoAtual = posicaoAtual + 1;
            if ((atual() != null) && atual().getTipo().equals("Identifier")) {
                posicaoAtual = posicaoAtual + 1;
                ParameterListProcedure();
            } else {
                addErro(atual(), "'Identifier'");
            }
        } else if (atual().getLexema().equals(")")) {
            posicaoAtual = posicaoAtual + 1;
        } else {
            addErro(atual(), "')'");
        }
    }

    //<ParameterListProcedure> ::=   ',' <ParameterProcedure> |  ')'  
    private void ParameterListProcedure() {
        if ((atual() != null) && atual().getLexema().equals(",")) {
            posicaoAtual = posicaoAtual + 1;
            ParameterProcedure();
        } else if (atual().getLexema().equals(")")) {
            posicaoAtual = posicaoAtual + 1;
        } else {
            addErro(atual(), "')'");
        }
    }

    //<ParameterFunction> ::= <VarType> Identifier <ParameterListFunction> | ')' ':' <VarType>
    private void ParameterFunction() {
        if ((atual() != null) && VarType.contains(atual().getLexema())) {
            posicaoAtual = posicaoAtual + 1;
            if ((atual() != null) && atual().getTipo().equals("Identifier")) {
                posicaoAtual = posicaoAtual + 1;
                ParameterListFunction();
            } else {
                addErro(atual(), "'Identifier'");
            }
        } else if (atual().getLexema().equals(")")) {
            posicaoAtual = posicaoAtual + 1;
            if (atual().getLexema().equals(":")) {
                posicaoAtual = posicaoAtual + 1;
                if (VarType.contains(atual().getLexema())) {
                    posicaoAtual = posicaoAtual + 1;
                } else {
                    addErro(atual(), "'tipo'");
                }
            } else {
                addErro(atual(), "':'");
            }
        } else {
            addErro(atual(), "')'");
        }
    }

    //<ParameterListFunction> ::=   ',' <ParameterFunction> |  ')' ':' <VarType> 
    private void ParameterListFunction() {
        if ((atual() != null) && atual().getLexema().equals(",")) {
            posicaoAtual = posicaoAtual + 1;
            ParameterFunction();
        } else if (atual().getLexema().equals(")")) {
            posicaoAtual = posicaoAtual + 1;
            if (atual().getLexema().equals(":")) {
                posicaoAtual = posicaoAtual + 1;
                if (VarType.contains(atual().getLexema())) {
                    posicaoAtual = posicaoAtual + 1;
                } else {
                    addErro(atual(), "'tipo'");
                }
            } else {
                addErro(atual(), "':'");
            }
        } else {
            addErro(atual(), "')'");
        }
    }

    //<FunctionStatement>::= 'function' Identifier  '(' <ParameterFunction> '{' <LocalStatement> 'return' <Value>';' <FunctionStatement1> |
    private void FunctionStatement() {
        if ((atual() != null) && atual().getLexema().equals("function")) {
            posicaoAtual = posicaoAtual + 1;
            if (atual().getTipo().equals("Identifier")) {
                posicaoAtual = posicaoAtual + 1;
                if (atual().getLexema().equals("(")) {
                    posicaoAtual = posicaoAtual + 1;
                    ParameterFunction();
                    if (atual().getLexema().equals("{")) {
                        posicaoAtual = posicaoAtual + 1;
                        LocalStatement();
                        System.out.println("Saiu LocalStatement");
                        if (atual().getLexema().equals("return")) {
                            posicaoAtual = posicaoAtual + 1;
                            Value();
                            if (atual().getLexema().equals(";")) {
                                posicaoAtual = posicaoAtual + 1;
                                FunctionStatement1();
                            } else {
                                addErro(atual(), "';'");
                            }
                        } else {
                            addErro(atual(), "'return'");
                        }
                    } else {
                        addErro(atual(), "'{'");
                    }
                } else {
                    addErro(atual(), "'('");
                }
            } else {
                addErro(atual(), "'Identifier'");
            }
        } else {
            //addErro(atual(), "'function'");
        }
    }

    //<FunctionStatement1>::= '}' <FunctionStatement>
    private void FunctionStatement1() {
        if ((atual() != null) && atual().getLexema().equals("}")) {
            posicaoAtual = posicaoAtual + 1;
            FunctionStatement();
        } else {
            addErro(atual(), "'}'");
        }
    }

//    //---------Atribuição
//    //<Assigment> ::= Identifier <AssigmentRegister>
//    private void Assigment() {
//        if (atual().getTipo().equals("Identifier")) {
//            posicaoAtual = posicaoAtual + 1;
//            AssigmentRegister();
//        } else {
//            addErro(atual(), "'Identifier'");
//        }
//    }
//    //<AssigmentRegister> ::= '.' Identifier '=' <AssigmentOperators> ';' | '=' <AssigmentOperators> ';' | '++' ';' | '--' ';'
//    private void AssigmentRegister() {
//        System.out.println("            AssigmentRegister   oooohh  ");
//        if (atual().getLexema().equals(".")) {
//            System.out.println("Encontrou o   .  !!!!!!!!!!");
//            posicaoAtual = posicaoAtual + 1;
//            if (atual().getTipo().equals("Identifier")) {
//                posicaoAtual = posicaoAtual + 1;
//                if (atual().getLexema().equals("=")) {
//                    System.out.println(atual().getLexema() + "    (atual().getLexema().equals(\"=\"))   oia");
//                    posicaoAtual = posicaoAtual + 1;
//                    AssigmentOperators();
//                } else {
//                    addErro(atual(), "'='");
//                }
//            } else {
//                addErro(atual(), "'Identifier'");
//            }
//        } else if (atual().getLexema().equals("=")) {
//            posicaoAtual = posicaoAtual + 1;
//            AssigmentOperators();
//            if (atual().getLexema().equals(";")) {
//                posicaoAtual = posicaoAtual + 1;
//            } else {
//                addErro(atual(), "' ;   '");
//            }
//        } else if (atual().getLexema().equals("++")) {
//            posicaoAtual = posicaoAtual + 1;
//            if (atual().getLexema().equals(";")) {
//                posicaoAtual = posicaoAtual + 1;
//            } else {
//                addErro(atual(), "';'");
//            }
//        } else if (atual().getLexema().equals("--")) {
//            posicaoAtual = posicaoAtual + 1;
//            if (atual().getLexema().equals(";")) {
//                posicaoAtual = posicaoAtual + 1;
//            } else {
//                addErro(atual(), "';'");
//            }
//        }
//    }
//    //Corrigir
//    //<AssigmentOperators> ::= <Value> | <BinaryExpression> | <UnaryExpression>
//    private void AssigmentOperators() {
//        if (atual() != null) {
//            System.out.println(atual().getLexema() + " Atual ");
//            System.out.println(atual().getTipo() + " = Atual Tipo ");
//            System.out.println(seguinte().getLexema() + "<======================== AssigmentOperators");
//            if (seguinte().getLexema().equals("+")
//                    || seguinte().getLexema().equals("-")
//                    || seguinte().getLexema().equals("*")
//                    || seguinte().getLexema().equals("/")
//                    || seguinte().getLexema().equals("++")
//                    || seguinte().getLexema().equals("--")
//                    || seguinte().getLexema().equals("<")
//                    || seguinte().getLexema().equals(">")
//                    || seguinte().getLexema().equals("!=")
//                    || seguinte().getLexema().equals("<=")
//                    || seguinte().getLexema().equals(">=")
//                    || seguinte().getLexema().equals("==")
//                    || seguinte().getLexema().equals("||")
//                    || seguinte().getLexema().equals("&&")) {
//                BinaryExpression();
//            } else if ((atual().getTipo().equals("RealNumber")
//                    || atual().getTipo().equals("Decimal") || atual().getTipo().equals("StringLiteral")
//                    || atual().getTipo().equals("Char") || atual().getTipo().equals("boolean")
//                    || atual().getTipo().equals("Identifier"))) {
//                Value();
//            } else if (atual().getLexema().equals("!")) {
//                UnaryExpression();
//            }
//        }
//    }
//    //! Expressão
//    //<BinaryExpression> ::= <AddendOperator> <BinaryExpressionContin> 
//    private void BinaryExpression() {
//        AddendOperator();
//        BinaryExpressionContin();
//    }
    //
    //<BinaryExpressionContin> ::= '+' <AddendIdent> ';'
    //                | '-' <AddendIdent> ';'
    //                | '*' <AddendIdent> ';'
    //                | '/' <AddendIdent> ';'
    //                | '++' ';'
    //                | '--' ';'
    //                | <RelationalExpression> ';'
    //                | <LogicalExpression> ';'
    //                | ';'
    private void BinaryExpressionContin() {
        System.out.println(atual().getLexema() + "                                   BinaryExpressionContin    ");
        if (atual() != null) {
            if (atual().getLexema().equals("+")
                    || atual().getLexema().equals("-")
                    || atual().getLexema().equals("*")
                    || atual().getLexema().equals("/")) {
                posicaoAtual = posicaoAtual + 1;
                AddendIdent();
                if (atual().getLexema().equals(";")) {
                    posicaoAtual = posicaoAtual + 1;
                } else {
                    addErro(atual(), "';'");
                }
            } else if (atual().getLexema().equals("++")
                    || atual().getLexema().equals("--")) {
                posicaoAtual = posicaoAtual + 1;
                if (atual().getLexema().equals(";")) {
                    posicaoAtual = posicaoAtual + 1;
                } else {
                    addErro(atual(), "';'");
                }
            } else if (atual().getLexema().equals("<")
                    || atual().getLexema().equals(">")
                    || atual().getLexema().equals("!=")
                    || atual().getLexema().equals("<=")
                    || atual().getLexema().equals(">=")
                    || atual().getLexema().equals("==")) {
                RelationalExpression();
                if (atual().getLexema().equals(";")) {
                    posicaoAtual = posicaoAtual + 1;
                } else {
                    addErro(atual(), "';'");
                }
            } else if (atual().getLexema().equals("||")
                    || atual().getLexema().equals("&&")) {
                LogicalExpression();
                if (atual().getLexema().equals(";")) {
                    posicaoAtual = posicaoAtual + 1;
                } else {
                    addErro(atual(), "';'");
                }
            } else if (atual().getLexema().equals(";")) {
                posicaoAtual = posicaoAtual + 1;
            } else {
                addErro(atual(), "'; do BinaryExpressionContin error'");
            }
        }
    }

    //<RelationalExpression> ::= '<' <AddendIdent>
    //                | '>' <AddendIdent>
    //                | '!=' <AddendIdent>
    //                | '<=' <AddendIdent>
    //                | '>=' <AddendIdent>
    //                | '==' <AddendIdent>
    private void RelationalExpression() {
        if (atual() != null) {
            System.out.println(atual().getTipo() + "  e  " + atual().getLexema());
            if (atual().getTipo().equals("OpRelacional")) {
                posicaoAtual = posicaoAtual + 1;
                AddendIdent();
            } else {
                addErro(atual(), "'RelationalExpression'");
            }
        }
    }

    //<LogicalExpression> ::= '||' <AddendIdent> | '&&' <AddendIdent>
    private void LogicalExpression() {
        if (atual() != null) {
            if (atual().getLexema().equals("||")) {
                posicaoAtual = posicaoAtual + 1;
                AddendIdent();
            } else if (atual().getLexema().equals("&&")) {
                posicaoAtual = posicaoAtual + 1;
                AddendIdent();
            } else {
                addErro(atual(), "'&&'");
            }
        }
    }

    //<AddendIdent>::= <AddendOperator> | Identifier
    private void AddendIdent() {
        if (atual() != null) {
            if (atual().getTipo().equals("Identifier")) {
                posicaoAtual = posicaoAtual + 1;
            } else {
                AddendOperator();
            }
        }
    }

    //<AddendOperator> ::= Decimal | RealNumber | Boolean
    private void AddendOperator() {
        System.out.println("=>Entrou AddendOperator<=");
        System.out.println(atual().getTipo());
        System.out.println(atual().getLexema());
        if ((atual() != null) && (atual().getTipo().equals("RealNumber")
                || atual().getTipo().equals("boolean")
                || atual().getLexema().equals("true")
                || atual().getLexema().equals("false")
                || atual().getTipo().equals("Decimal"))) {
            posicaoAtual = posicaoAtual + 1;
        } else {
            addErro(atual(), "'Operador mal formado'");
        }
    }

    //<UnaryExpression> ::= '!' <AddendOperatorUnary> ';'
    private void UnaryExpression() {
        if ((atual() != null) && (atual().getLexema().equals("!"))) {
            posicaoAtual = posicaoAtual + 1;
            AddendOperatorUnary();
            if (atual().getLexema().equals(";")) {
                posicaoAtual = posicaoAtual + 1;
            } else {
                addErro(atual(), "';'");
            }
        } else {
            addErro(atual(), "'!'");
        }
    }

    //Corrigir o Boolean !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //<AddendOperatorUnary> ::= Identifier | Boolean
    private void AddendOperatorUnary() {
        if (atual().getTipo().equals("Identifier")) {
            posicaoAtual = posicaoAtual + 1;
        } else if (atual().getLexema().equals("boolean")
                || (atual().getLexema().equals("true"))
                || (atual().getLexema().equals("false"))) {
            posicaoAtual = posicaoAtual + 1;
        } else {
            addErro(atual(), "'Boolean'");
        }
    }

    //---------Declaracoes Logicas
    //<AssignExpr> ::= <LogicalOrExpression> |
    private void AssignExpr() {
        if (atual() != null) {
            LogicalOrExpression();
        }
    }

    //<LogicalOrExpression> ::= <LogicalAndExpression> <LogicalOrExpression1>
    private void LogicalOrExpression() {
        LogicalAndExpression();
        LogicalOrExpression1();
    }

    //<LogicalOrExpression1> ::= '||' <LogicalAndExpression> <LogicalOrExpression1> |
    private void LogicalOrExpression1() {
        if ((atual() != null)) {
            if (atual().getLexema().equals("||")) {
                posicaoAtual = posicaoAtual + 1;
                LogicalAndExpression();
                LogicalOrExpression1();
            } else {
                //addErro(atual(), "'||'");
            }
        }
    }

    //<LogicalAndExpression> ::= <Condition> <LogicalAndExpression1>
    private void LogicalAndExpression() {
        Condition();
        LogicalAndExpression1();
    }

    //<LogicalAndExpression1> ::= '&&' <Condition> <LogicalAndExpression1> |
    private void LogicalAndExpression1() {
        if (atual() != null) {
            if (atual().getTipo().equals("OpLogico")) {
                posicaoAtual = posicaoAtual + 1;
                Condition();
                LogicalAndExpression1();
            } else {
                //addErro(atual(), "'&&'");
            }
        }
    }

    //---------Chamada de função
    //<FunctionCall> ::= '(' <Argument> ')' ';'
    private void FunctionCall() {
        if (atual().getLexema().equals("(")) {
            posicaoAtual = posicaoAtual + 1;
            Argument();
            if (atual().getLexema().equals(")")) {
                posicaoAtual = posicaoAtual + 1;
                if (atual().getLexema().equals(";")) {
                    posicaoAtual = posicaoAtual + 1;
                } else {
                    addErro(atual(), "';'");
                }
            } else {
                addErro(atual(), "')'");
            }
        } else {
            addErro(atual(), "'('");
        }
    }

    //<Argument> ::= <Value> <ArgumentList> |
    private void Argument() {
        if (atual().getTipo().equals("Identifier") || (atual().getTipo().equals("RealNumber")
                || atual().getTipo().equals("Decimal") || atual().getTipo().equals("StringLiteral")
                || atual().getTipo().equals("Char") || atual().getLexema().equals("boolean")
                || atual().getLexema().equals("true") || atual().getLexema().equals("false"))) {
            Value();
            ArgumentList();
        } else {
        }
    }

    //<ArgumentList> ::= ',' <Argument> |
    private void ArgumentList() {
        if (atual().getLexema().equals(",")) {
            posicaoAtual = posicaoAtual + 1;
            Argument();
        } else {
            //addErro(atual(), "','");//Pode ser vazio
        }
    }

    //---------Chamada de procedure
    //<ProcedureCall> ::= '(' <Argument> ')' ';'
    private void ProcedureCall() {
        if ((atual() != null) && atual().getLexema().equals("(")) {
            posicaoAtual = posicaoAtual + 1;
            Argument();
            if ((atual() != null) && atual().getLexema().equals(")")) {
                posicaoAtual = posicaoAtual + 1;
                if ((atual() != null) && atual().getLexema().equals(";")) {
                    posicaoAtual = posicaoAtual + 1;
                } else {
                    addErro(atual(), "';'");
                }
            } else {
                addErro(atual(), "')'");
            }
        } else {
            addErro(atual(), "'('");
        }
    }

    //---------Declaração Main
    //<Main> ::= 'main' '{' <LocalStatement> '}'
    private void Main() {
        if ((atual() != null) && atual().getLexema().equals("main")) {
            posicaoAtual = posicaoAtual + 1;
            if (atual().getLexema().equals("{")) {
                posicaoAtual = posicaoAtual + 1;
                LocalStatement();
                if (atual().getLexema().equals("}")) {
                    posicaoAtual = posicaoAtual + 1;
                } else {
                    addErro(atual(), "'}'");
                }
            } else {
                addErro(atual(), "'{'");
            }
        } else {
            addErro(atual(), "'main'");
        }
    }

    //---------Blocos
    //<LocalStatement> ::= <VarStatement> <LocalCommands>
    private void LocalStatement() {
        VarStatement();
        LocalCommands();
    }

    //<LocalCommands> ::= <IfDecs> <LocalCommands>
    //              | <WriteDecs> <LocalCommands>
    //              | <ReadDecs> <LocalCommands>
    //              | <WhileDecs> <LocalCommands>
    //              | Identifier <Call> <LocalCommands>
    //              |
    private void LocalCommands() {
        System.out.println("---------------------------------------------- LocalCommands ---------------------");
        if ((atual() != null) && (atual().getLexema().equals("if"))) {
            IfDecs();
            LocalCommands();
        } else if ((atual() != null) && (atual().getLexema().equals("write"))) {
            WriteDecs();
            LocalCommands();
        } else if ((atual() != null) && (atual().getLexema().equals("read"))) {
            ReadDecs();
            LocalCommands();
        } else if ((atual() != null) && (atual().getLexema().equals("while"))) {
            WhileDecs();
            LocalCommands();
        } else if ((atual() != null) && (atual().getTipo().equals("Identifier"))) {
            posicaoAtual = posicaoAtual + 1;
            Call();
            LocalCommands();
        }
    }

    //<Call> ::= <ProcedureCall> | '=' <FAcall> | '.' Identifier '=' <FAcall> | '++' ';' | '--' ';'
    private void Call() {
        if (atual().getLexema().equals("(")) {
            ProcedureCall();
        } else if (atual().getLexema().equals("=")) {
            posicaoAtual = posicaoAtual + 1;
            FAcall();
        } else if (atual().getLexema().equals(".")) {
            posicaoAtual = posicaoAtual + 1;
            if (atual().getTipo().equals("Identifier")) {
                posicaoAtual = posicaoAtual + 1;
                if (atual().getLexema().equals("=")) {
                    posicaoAtual = posicaoAtual + 1;
                    FAcall();
                }
            }
        } else if (atual().getLexema().equals("++")) {
            posicaoAtual = posicaoAtual + 1;
            if (atual().getLexema().equals(";")) {
                posicaoAtual = posicaoAtual + 1;
            } else {
                addErro(atual(), "';'");
            }
        } else if (atual().getLexema().equals("--")) {
            posicaoAtual = posicaoAtual + 1;
            if (atual().getLexema().equals(";")) {
                posicaoAtual = posicaoAtual + 1;
            } else {
                addErro(atual(), "';'");
            }
        }
    }

    //<FAcall>::= Identifier <FAcall1> | <ValueBinary> | <UnaryExpression>
    private void FAcall() {
        if ((atual() != null) && (atual().getTipo().equals("Identifier"))) {
            posicaoAtual = posicaoAtual + 1;
            FAcall1();
        } else if (atual().getTipo().equals("StringLiteral") || (atual().getTipo().equals("Char"))
                || (atual().getTipo().equals("RealNumber")
                || atual().getTipo().equals("boolean")
                || atual().getLexema().equals("true")
                || atual().getLexema().equals("false")
                || atual().getTipo().equals("Decimal"))) {
            ValueBinary();
        } else if (atual().getLexema().equals("!")) {
            UnaryExpression();
        } else {
            addErro(atual(), "'!'");
        }
    }

    //<FAcall1> ::= <FunctionCall> | <BinaryExpressionContin> | '.' Identifier ';'
    private void FAcall1() {
        if (atual().getLexema().equals("(")) {
            FunctionCall();
        } else if (atual().getLexema().equals(".")) {
            posicaoAtual = posicaoAtual + 1;
            if (atual().getTipo().equals("Identifier")) {
                posicaoAtual = posicaoAtual + 1;
                if (atual().getLexema().equals(";")) {
                    posicaoAtual = posicaoAtual + 1;
                } else {
                    addErro(atual(), "';'");
                }
            } else {
                addErro(atual(), "'Identifier'");
            }
        } else {
            BinaryExpressionContin();
        }
    }

    //<ValueBinary>::= StringLiteral ';' | Char ';' | <AddendOperator> <BinaryExpressionContin>
    private void ValueBinary() {
        if (atual().getTipo().equals("StringLiteral")) {
            posicaoAtual = posicaoAtual + 1;
            if (atual().getLexema().equals(";")) {
                posicaoAtual = posicaoAtual + 1;
            } else {
                addErro(atual(), "';'");
            }
        } else if (atual().getTipo().equals("Char")) {
            posicaoAtual = posicaoAtual + 1;
            if (atual().getLexema().equals(";")) {
                posicaoAtual = posicaoAtual + 1;
            } else {
                addErro(atual(), "';'");
            }
        } else {
            AddendOperator();
            BinaryExpressionContin();
        }
    }

    //---------Condicao
    //<Condition> ::= <AddendIdent> <ConditionContin>
    private void Condition() {
        AddendIdent();
        ConditionContin();
    }

    //<ConditionContin> ::= <RelationalExpression> | <LogicalExpression>
    private void ConditionContin() {
        if ((atual() != null)) {
            if (atual().getTipo().equals("OpRelacional")) {
                RelationalExpression();
            } else if (atual().getTipo().equals("OpLogico")) {
                LogicalExpression();
            } else {
                addErro(atual(), "Não achou o OpLogico");
            }
        }
    }

    //---------Declaracao If/Else
    //<IfDecs> ::= 'if' '(' <AssignExpr> ')' '{' <LocalCommands> '}' <ElseDecs>                                                    
    private void IfDecs() {
        if ((atual() != null) && (atual().getLexema().equals("if"))) {
            posicaoAtual = posicaoAtual + 1;
            if ((atual() != null) && (atual().getLexema().equals("("))) {
                posicaoAtual = posicaoAtual + 1;
                AssignExpr();
                if ((atual() != null) && (atual().getLexema().equals(")"))) {
                    posicaoAtual = posicaoAtual + 1;
                    if ((atual() != null) && (atual().getLexema().equals("{"))) {
                        posicaoAtual = posicaoAtual + 1;
                        LocalCommands();
                        //System.out.println(" ---------------------Saiu LocalCommands " + atual().getLexema());
                        if ((atual() != null) && (atual().getLexema().equals("}"))) {
                            posicaoAtual = posicaoAtual + 1;
                            ElseDecs();
                        } else {
                            addErro(atual(), "'}'");
                        }
                    } else {
                        addErro(atual(), "'{'");
                    }
                } else {
                    addErro(atual(), "')'");
                }
            } else {
                addErro(atual(), "'('");
            }
        } else {
            addErro(atual(), "'if'");
        }
    }

    //<ElseDecs>::= 'else' '{' <LocalCommands> '}' |
    private void ElseDecs() {
        if ((atual() != null) && (atual().getLexema().equals("else"))) {
            posicaoAtual = posicaoAtual + 1;
            if ((atual() != null) && (atual().getLexema().equals("{"))) {
                posicaoAtual = posicaoAtual + 1;
                LocalCommands();
                if ((atual() != null) && (atual().getLexema().equals("}"))) {
                    posicaoAtual = posicaoAtual + 1;
                    ElseDecs();
                } else {
                    addErro(atual(), "'}'");
                }
            } else {
                addErro(atual(), "'{'");
            }
        } else {
            //addErro(atual(), "'else'");
        }
    }

    //---------Declaracao while
    //<WhileDecs>::= 'while' '('<AssignExpr>')' '{' <LocalCommands> '}'  
    private void WhileDecs() {
        if ((atual() != null) && (atual().getLexema().equals("while"))) {
            posicaoAtual = posicaoAtual + 1;
            if ((atual() != null) && (atual().getLexema().equals("("))) {
                posicaoAtual = posicaoAtual + 1;
                AssignExpr();
                if ((atual() != null) && (atual().getLexema().equals(")"))) {
                    posicaoAtual = posicaoAtual + 1;
                    if ((atual() != null) && (atual().getLexema().equals("{"))) {
                        posicaoAtual = posicaoAtual + 1;
                        LocalCommands();
                        if ((atual() != null) && (atual().getLexema().equals("}"))) {
                            posicaoAtual = posicaoAtual + 1;
                        } else {
                            addErro(atual(), "'}'");
                        }
                    } else {
                        addErro(atual(), "'{'");
                    }
                } else {
                    addErro(atual(), "')'");
                }
            } else {
                addErro(atual(), "'('");
            }
        } else {
            addErro(atual(), "'while'");
        }
    }

    //---------Declaração Write 
    //<WriteDecs> ::= 'write' '(' <ArgumentsWrite>
    private void WriteDecs() {
        if ((atual() != null) && (atual().getLexema().equals("write"))) {
            posicaoAtual = posicaoAtual + 1;
            if ((atual() != null) && (atual().getLexema().equals("("))) {
                posicaoAtual = posicaoAtual + 1;
                ArgumentsWrite();
            } else {
                addErro(atual(), "'('");
            }
        } else {
            addErro(atual(), "'write'");
        }
    }

    //<ArgumentsWrite> ::= Identifier <RegisterWrite> <ListArgumentsWrite> | <WriteContent> <ListArgumentsWrite>
    private void ArgumentsWrite() {
        if (atual().getTipo().equals("Identifier")) {
            posicaoAtual = posicaoAtual + 1;
            RegisterWrite();
            ListArgumentsWrite();
        } else if (atual() != null) {
            WriteContent();
            ListArgumentsWrite();
        }
    }

    //<WriteContent> ::= Decimal | RealNumber | StringLiteral
    private void WriteContent() {
        if ((atual() != null) && (atual().getTipo().equals("Decimal")
                || atual().getTipo().equals("RealNumber") || atual().getTipo().equals("StringLiteral"))) {
            posicaoAtual = posicaoAtual + 1;
        } else {
            addErro(atual(), "'WriteContent mal formado'");
        }
    }

    //<RegisterWrite> ::= '.' Identifier |
    private void RegisterWrite() {
        if ((atual() != null) && (atual().getLexema().equals("."))) {
            posicaoAtual = posicaoAtual + 1;
            if (atual().getTipo().equals("Identifier")) {
                posicaoAtual = posicaoAtual + 1;
            } else {
                addErro(atual(), "'Identifier'");
            }
        } else {
            //addErro(atual(), "'.'");Pode ser vazia
        }
    }

    //<ListArgumentsWrite> ::= ',' <ArgumentsWrite> | ')' ';'
    private void ListArgumentsWrite() {
        if ((atual() != null) && (atual().getLexema().equals(","))) {
            posicaoAtual = posicaoAtual + 1;
            ArgumentsWrite();
        } else if ((atual() != null) && (atual().getLexema().equals(")"))) {
            posicaoAtual = posicaoAtual + 1;
            if ((atual() != null) && (atual().getLexema().equals(";"))) {
                posicaoAtual = posicaoAtual + 1;
            } else {
                addErro(atual(), "';'");
            }
        } else {
            addErro(atual(), "')'");
        }
    }

    //---------Declaração Read
    //<ReadDecs> ::= 'read' '(' <ArgumentsRead>
    private void ReadDecs() {
        if ((atual() != null) && (atual().getLexema().equals("read"))) {
            posicaoAtual = posicaoAtual + 1;
            if ((atual() != null) && (atual().getLexema().equals("("))) {
                posicaoAtual = posicaoAtual + 1;
                ArgumentsRead();
            } else {
                addErro(atual(), "'('");
            }
        } else {
            addErro(atual(), "'read'");
        }
    }

    //<ArgumentsRead> ::= Identifier <RegisterRead> <ListArgumentsRead>
    private void ArgumentsRead() {
        if (atual().getTipo().equals("Identifier")) {
            posicaoAtual = posicaoAtual + 1;
            RegisterRead();
            ListArgumentsRead();
        } else {
            addErro(atual(), "'Identifier'");
        }
    }

    //<RegisterRead> ::= '.' Identifier |
    private void RegisterRead() {
        if ((atual() != null) && (atual().getLexema().equals("."))) {
            posicaoAtual = posicaoAtual + 1;
            if (atual().getTipo().equals("Identifier")) {
                posicaoAtual = posicaoAtual + 1;
            } else {
                addErro(atual(), "'Identifier'");
            }
        } else {
            //addErro(atual(), "'.'");//Pode ser nulo
        }
    }

    //<ListArgumentsRead> ::= ',' <ArgumentsRead> | ')' ';' 
    private void ListArgumentsRead() {
        if ((atual() != null) && (atual().getLexema().equals(","))) {
            posicaoAtual = posicaoAtual + 1;
            ArgumentsRead();
        } else if ((atual() != null) && (atual().getLexema().equals(")"))) {
            posicaoAtual = posicaoAtual + 1;
            if ((atual() != null) && (atual().getLexema().equals(";"))) {
                posicaoAtual = posicaoAtual + 1;
            } else {
                addErro(atual(), "';'");
            }
        } else {
            addErro(atual(), "')'");
        }
    }
}
