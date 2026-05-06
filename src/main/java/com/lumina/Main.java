/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.lumina;

import com.lumina.error.ErrorHandler;
import com.lumina.lexer.Lexer;
import com.lumina.parser.Parser;
import com.lumina.symbols.SymbolTable;
import java.io.StringReader;

public class Main {

    public static void main(String[] args) throws Exception {

       String source =
    "fun sumar(a: int, b: int) -> int {\n" +
    "    return a + b\n" +
    "}\n" +
    "\n" +
    "fun main() -> int {\n" +
    "    var x: int = 10\n" +
    "    var y: int = 20\n" +
    "    var resultado: int = sumar(x, y)\n" +
    "    print(resultado)\n" +
    "    return 0\n" +
    "}\n";

        ErrorHandler errorHandler = new ErrorHandler();
        SymbolTable symbolTable   = new SymbolTable();

        Lexer lexer   = new Lexer(new StringReader(source));
        lexer.setErrorHandler(errorHandler);

        Parser parser = new Parser(lexer);
        parser.setErrorHandler(errorHandler);
        parser.setSymbolTable(symbolTable);

        try {
            parser.parse();
            System.out.println("Analisis completado.");
        } catch (Exception e) {
            System.out.println("Error durante el analisis: " + e.getMessage());
        }

        System.out.println();
        errorHandler.printErrors();

        System.out.println();
        symbolTable.printTable();
    }
}