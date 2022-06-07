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

    public AnalisadorSintatico() {
        VarType = new ArrayList<>();
        VarType.add("integer");
        VarType.add("string");
        VarType.add("real");
        VarType.add("boolean");
        VarType.add("char");
    }

    //<Start> ::= 'program' Identifier ';' <GlobalStatement>
    public void program() {
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
        ProcedureStatement();
        FunctionStatement();
        Main();
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
    }

    //<VarList1>::= <VarDeclaration> <VarList1> | '}'
    private void VarList1() {
    }

    //<VarDeclaration>::= <VarType> Identifier <VarDeclaration1>
    private void VarDeclaration() {
    }

    //<VarDeclaration1>::= ',' Identifier <VarDeclaration1> | ';'
    private void VarDeclaration1() {
    }

    // Declaracao Const
    //<ConstStatement> ::= 'const' '{' <ConstList>
    private void ConstStatement() {
    }

    //<ConstList>::= <ConstDeclaration> <ConstList1>
    private void ConstList() {
    }

    //<ConstList1> ::= <ConstDeclaration> <ConstList1> | '}'
    private void ConstList1() {
    }

    //Checar <ConstType>!
    //<ConstDeclaration> ::= <ConstType> Identifier '=' <Value> <ConstDeclaration1>
    private void ConstDeclaration() {
    }

    //<Value>  ::= Decimal | RealNumber | StringLiteral | Identifier <ValueRegister> | Char | Boolean
    private void Value() {
    }

    // Declaracao Register
    // <ValueRegister> ::= '.' Identifier |
    private void Value() {
    }

    // <RegisterStatementMultiple> ::= <RegisterStatement> |
    private void RegisterStatementMultiple() {
    }

    //<RegisterStatement> ::= 'register' Identifier '{' <RegisterList>
    private void RegisterStatement() {
    }

    //<RegisterList> ::= <RegisterDeclaration> <RegisterList1>
    private void RegisterList() {
    }

    //<RegisterList1> ::= <RegisterDeclaration> <RegisterList1> | '}' <RegisterStatementMultiple>
    private void RegisterList1() {
    }

    //<RegisterDeclaration> ::= <ConstType> Identifier <RegisterDeclaration1>
    private void RegisterDeclaration() {
    }

    //<RegisterDeclaration1> ::= ',' Identifier <RegisterDeclaration1> | ';'
    private void RegisterDeclaration1() {
    }
}
