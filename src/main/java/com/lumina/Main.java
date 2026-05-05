/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.lumina;

import com.lumina.error.ErrorHandler;
import com.lumina.lexer.Lexer;
import com.lumina.lexer.Token;
import com.lumina.lexer.TokenType;
import com.lumina.symbols.SymbolTable;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        String source
                = "fun sumar(a: int, b: int) -> int {\n"
                + "    return a + b\n"
                + "}\n"
                + "\n"
                + "fun main() {\n"
                + "    var x: int = 10\n"
                + "    var y: int = 20\n"
                + "    var resultado: int = sumar(x, y)\n"
                + "    print(resultado)\n"
                + "}\n";

        ErrorHandler errorHandler = new ErrorHandler();
        Lexer lexer = new Lexer(source, errorHandler);
        List<Token> tokens = lexer.tokenize();

        System.out.println("=== TOKENS GENERADOS ===\n");
        for (Token token : tokens) {
            System.out.println(token);
        }

        System.out.println("\n=== ERRORES ===\n");
        errorHandler.printErrors();

        System.out.println("\n=== TABLA DE SIMBOLOS (prueba manual) ===\n");
        SymbolTable table = new SymbolTable();
        table.define("x", TokenType.INT, SymbolTable.Symbol.Kind.VARIABLE, 6, 9);
        table.define("y", TokenType.INT, SymbolTable.Symbol.Kind.VARIABLE, 7, 9);
        table.define("suma", TokenType.FLOAT, SymbolTable.Symbol.Kind.FUNCTION, 1, 1);
        table.markInitialized("x");
        table.printTable();
    }
}
