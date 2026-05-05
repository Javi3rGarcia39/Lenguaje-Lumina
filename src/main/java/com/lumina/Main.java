/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.lumina;

import com.lumina.error.ErrorHandler;
import com.lumina.lexer.Lexer;
import com.lumina.lexer.Token;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        String source =
            "fun sumar(a: int, b: int) -> int {\n" +
            "    return a + b\n" +
            "}\n" +
            "\n" +
            "fun main() {\n" +
            "    var x: int = 10\n" +
            "    var y: int = 20\n" +
            "    var resultado: int = sumar(x, y)\n" +
            "    print(resultado)\n" +
            "}\n";

        ErrorHandler errorHandler = new ErrorHandler();
        Lexer lexer = new Lexer(source, errorHandler);
        List<Token> tokens = lexer.tokenize();

        System.out.println("=== TOKENS GENERADOS ===\n");
        for (Token token : tokens) {
            System.out.println(token);
        }

        System.out.println("\n=== ERRORES ===\n");
        errorHandler.printErrors();
    }
}