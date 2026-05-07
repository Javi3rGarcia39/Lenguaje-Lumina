/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.lumina;

import com.lumina.error.ErrorHandler;
import com.lumina.lexer.Lexer;
import com.lumina.parser.Parser;
import com.lumina.symbols.SymbolTable;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) throws Exception {

        String filePath;

        if (args.length > 0) {
            filePath = args[0];
        } else {
            //filePath = "test.lum";
            //filePath = "test_error_lexico.lum";
            filePath = "test_error_sintactico.lum";
        }

        // Verificar que el archivo existe
        if (!Files.exists(Paths.get(filePath))) {
            System.out.println("Error: no se encontro el archivo '" + filePath + "'");
            System.out.println("Uso: java -jar Lumina.jar <archivo.lum>");
            return;
        }

        System.out.println("=== LUMINA COMPILER ===");
        System.out.println("Archivo: " + filePath);
        System.out.println();

        ErrorHandler errorHandler = new ErrorHandler();
        SymbolTable symbolTable = new SymbolTable();

        try {
            Lexer lexer = new Lexer(new FileReader(filePath));
            lexer.setErrorHandler(errorHandler);

            Parser parser = new Parser(lexer);
            parser.setErrorHandler(errorHandler);
            parser.setSymbolTable(symbolTable);

            parser.parse();
            System.out.println("Analisis completado.");

        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
            return;
        } catch (Exception e) {
            System.out.println("Error durante el analisis: " + e.getMessage());
        }

        System.out.println();
        errorHandler.printErrors();

        System.out.println();
        symbolTable.printTable();
    }
}
