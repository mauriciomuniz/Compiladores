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
            if (listarTokens.getTipo().equals("Identificador")                       {
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
}
