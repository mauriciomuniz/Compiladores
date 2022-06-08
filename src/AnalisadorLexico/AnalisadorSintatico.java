//Meta criar o analisador de program, register, var, const.
package AnalisadorLexico;
package AnalisadorLexico.Token;

import java.util.ArrayList;

/**
 * Classe Automato, responsavel pela parte lógica
 *
 * @author Mauricio e Alexandre
 */
public class AnalisadorSintatico {

    private ArrayList<String> VarType;
    private ArrayList<Token> listarTokens;
    private int posicaoAtual;
    private int posicaoFinal;

    public AnalisadorSintatico() {
        VarType = new ArrayList<>();
        VarType.add("integer");
        VarType.add("string");
        VarType.add("real");
        VarType.add("boolean");
        VarType.add("char");
    }
    
    //pega o token atual --------- testado
    public Token atual() {
        if (posicaoAtual < posicaoFinal) {
            return (Token) listarTokens.get(posicaoAtual);
        }
        return null;
    }

    //<Start> ::= 'program' Identifier ';' <GlobalStatement>
    public void Start() {
        if (listarTokens.getLexema().equals("program")) {
            if (listarTokens.getTipo().equals("Identificador")) {
                if (listarTokens.getLexema().equals(";")) {
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
       // ProcedureStatement();
       // FunctionStatement();
       // Main();
    }

    //Declaracao Variaveis
    //<VarStatement>::= 'var' '{' <VarList>
    private void VarStatement() {
        if (listarTokens.getLexema().equals("var")) {
            if (listarTokens.getLexema().equals("{")) {
                VarList();
            }
        }
    }

    //<VarList>::= <VarDeclaration> <VarList1> | '}'
    private void VarList() {
        if ((listarTokens != null)) {
            VarDeclaration();
            VarList1();
        } else if (listarTokens.getLexema().equals("}")) {
        }
    }

    //<VarList1>::= <VarDeclaration> <VarList1> | '}'
    private void VarList1() {
        if ((listarTokens != null)) {
            VarDeclaration();
            VarList1();
        } else if (listarTokens.getLexema().equals("}")) {
        }
    }

    //<VarDeclaration>::= <VarType> Identifier <VarDeclaration1>
    private void VarDeclaration() {
        if (listarTokens.contains(atual().getLexema())) {
            if (listarTokens.getTipo().equals("Identificador")) {
                VarDeclaration1();
            }

        }
    }

    //<VarDeclaration1>::= ',' Identifier <VarDeclaration1> | ';'
    private void VarDeclaration1() {
        if (listarTokens.getLexema().equals(",")) {
            if (listarTokens.getTipo().equals("Identificador")) {
                VarDeclaration1();
            }
        } else if (listarTokens.getLexema().equals(";")) {

        }
    }

    // Declaracao Const
    //<ConstStatement> ::= 'const' '{' <ConstList>
    private void ConstStatement() {
        if (listarTokens.getLexema().equals("const")) {
            if (listarTokens.getLexema().equals("{")) {
                ConstList();
            }
        }

    }

    //<ConstList>::= <ConstDeclaration> <ConstList1>
    private void ConstList() {
        if (listarTokens() != null) {
            ConstDeclaration();
            ConstList1();
        }
    }

    //<ConstList1> ::= <ConstDeclaration> <ConstList1> | '}'
    private void ConstList1() {
        if (listarTokens() != null) {
            ConstDeclaration();
            ConstList1();
        }

    }

    //Checar <ConstType>!
    //<ConstDeclaration> ::= <ConstType> Identifier '=' <Value> <ConstDeclaration1>
    private void ConstDeclaration() {
        
        if (listarTokens.contains(atual().getLexema())) {
            if (listarTokens.getTipo().equals("Identificador")) {
                if (listarTokens.getLexema().equals("=")){
                    if (listarTokens.getTipo().equals("Value")){
                        ConstDeclaration1();
                        }
                    }
                }
                
            }
        }
        
    

    //<ConstDeclaration1> ::= ',' Identifier  '=' <Value> <ConstDeclaration1> | ';'
    private void ConstDeclaration1() {
         if (listarTokens.getLexema().equals(",")) {
            if (listarTokens.getTipo().equals("Identificador")) {
                if (listarTokens.getLexema().equals("=")){
                    if (listarTokens.getTipo().equals("Value")){
                        ConstDeclaration1();
                    }
                }
            }
        } else if (listarTokens.getLexema().equals(";")) {
        }
    }

    //<Value>  ::= Decimal | RealNumber | StringLiteral | Identifier <ValueRegister> | Char | Boolean
    private void Value() {
        if (listarTokens.getTipo().equals("Identificador")) {
            ValueRegister();
        } else if ((listarTokens != null) && (listarTokens.getTipo().equals("Decimal")
                || listarTokens.getTipo().equals("RealNumber") || listarTokens.getTipo().equals("StringLiteral")
                || listarTokens.getLexema().equals("Char") || listarTokens.getLexema().equals("Boolean"))) {
        }
    }

    // Declaracao Register
    // <ValueRegister> ::= '.' Identifier |
    private void ValueRegister() {
        if ((listarTokens != null) && listarTokens.getLexema().equals(".")) {
            if ((listarTokens != null) && listarTokens.getTipo().equals("Identificador")) {
            }
        }
    }

    // <RegisterStatementMultiple> ::= <RegisterStatement> |
    private void RegisterStatementMultiple() {
        if ((listarTokens != null)) {
            RegisterStatement();
        }
    }

    //<RegisterStatement> ::= 'register' Identifier '{' <RegisterList>
    private void RegisterStatement() {
        if (listarTokens.getLexema().equals("register")) {
            if (listarTokens.getTipo().equals("Identificador")) {
                if (listarTokens.getLexema().equals("{")) {
                    RegisterList();
                }
            }
        }
    }

    //<RegisterList> ::= <RegisterDeclaration> <RegisterList1>
    private void RegisterList() {
        RegisterDeclaration();
        RegisterList1();
    }

    //<RegisterList1> ::= <RegisterDeclaration> <RegisterList1> | '}' <RegisterStatementMultiple>
    private void RegisterList1() {
        if ((listarTokens != null)) {
            RegisterDeclaration();
            RegisterList1();
        } else if (listarTokens.getLexema().equals("}")) {
            RegisterStatementMultiple();
        }
    }

    //<RegisterDeclaration> ::= <ConstType> Identifier <RegisterDeclaration1>
    private void RegisterDeclaration() {
        if (listarTokens.contains(atual().getLexema())) {
            if (listarTokens.getTipo().equals("Identificador")) {
                RegisterDeclaration1();
            }
        }
    }

    //<RegisterDeclaration1> ::= ',' Identifier <RegisterDeclaration1> | ';'
    private void RegisterDeclaration1() {
        if (listarTokens.getLexema().equals(",")) {
            if (listarTokens.getTipo().equals("Identificador")) {
                RegisterDeclaration1();
            }
        } else if (listarTokens.getLexema().equals(";")) {
        }
    }
}
