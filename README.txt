# Compiladores
 MI do Projeto de Compiladores.
 
 Foi utilizado o ambiente de desenvolvimento integrado Apache NetBeans, atravéz do SO Windows 10.

Para executar com o arquivo já compilado:

1 - cd Compiladores/dist

2 - java -jar "Compiladores_Lexico_Mauricio_Alexandre.jar" 


Para exexutar o arquivo zipado:

1 - Descompacte o arquivo

2 - cd Compiladores/src/AnalisadorLexico

3 - Compile o programa 

4 - Execute o Compilador.java


Para Realizar os teste:

=> Checar se existe a pasta input dentro do diretório.
=> Adicione ou modifique os arquivos da pasta input com o nome 
entradaX.txt, onde X = {d+}

Resultados:

=> Os resultados esperados vão estar na pasta output dentro do diretório raiz.

Erros presentes:

=> Possui erro quando o arquivo de entrada possui no seu final de arquivo numeros 
pares de caractere de nova linha ou o "\n". Isso quer dizer, que quando o arquivo 
ter somente 2, 4, 6, ... pulos de de linha sem conter mais nada ele tem erro de 
compilação {"x out of bounds for length x"}.
Obs: Nem todos os arquivos de entrada seguem esse padrão de error, como no arquivo
entrada1.txt apresentou erro em 3 em 3 linhas.