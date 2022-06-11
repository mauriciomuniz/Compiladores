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
        penultimo = listarTokens.get(listarTokens.size() - 1);
        ultimo = new Token(listarTokens.size(), penultimo.getAux(), "$", "$");
        listarTokens.add(ultimo);
        posicaoFinal = listarTokens.size();

        Start();
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
        if (posicaoAtual < posicaoFinal) {
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

    //<Start> ::= 'program' Identifier ';' <GlobalStatement>
    public void Start() {
        if (atual().getLexema().equals("program")) {
            posicaoAtual = posicaoAtual + 1;
            if (atual().getTipo().equals("Identificador")) {
                posicaoAtual = posicaoAtual + 1;
                if (atual().getLexema().equals(";")) {
                    posicaoAtual = posicaoAtual + 1;
                    globalStatement();
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
            }
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
        }
    }

    //<VarDeclaration>::= <VarType> Identifier <VarDeclaration1>
    private void VarDeclaration() {
        if ((atual() != null) && VarType.contains(atual().getLexema())) {
            posicaoAtual = posicaoAtual + 1;
            if (atual().getTipo().equals("Identificador")) {
                posicaoAtual = posicaoAtual + 1;
                VarDeclaration1();
            }

        }
    }

    //<VarDeclaration1>::= ',' Identifier <VarDeclaration1> | ';'
    private void VarDeclaration1() {
        if (atual().getLexema().equals(",")) {
            posicaoAtual = posicaoAtual + 1;
            if (atual().getTipo().equals("Identificador")) {
                posicaoAtual = posicaoAtual + 1;
                VarDeclaration1();
            }
        } else if (atual().getLexema().equals(";")) {
            posicaoAtual = posicaoAtual + 1;
        }
    }

    // Declaracao Const
    //<ConstStatement> ::= 'const' '{' <ConstList>
    private void ConstStatement() {
        if ((atual() != null) && atual().getLexema().equals("const")) {
            posicaoAtual = posicaoAtual + 1;
            if (atual().getLexema().equals("{")) {
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
        if (atual().getLexema().equals(",")) {
            posicaoAtual = posicaoAtual + 1;
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

    //<LocalStatement> ::= <VarStatement> <LocalCommands>
    private void LocalStatement() {
        VarStatement();
        LocalCommands();
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

    //<LocalCommands> ::= <IfDecs> <LocalCommands>
    //              | <WriteDecs> <LocalCommands>
    //              | <ReadDecs> <LocalCommands>
    //              | <WhileDecs> <LocalCommands>
    //              | <Assigment> <LocalCommands>
    //              | <FunctionCall> <LocalCommands>
    //              | <ProcedureCall> <LocalCommands>
    //              |
    private void LocalCommands() {
    }
}
