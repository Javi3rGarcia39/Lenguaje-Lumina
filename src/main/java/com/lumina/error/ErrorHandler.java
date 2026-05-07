/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lumina.error;

import java.util.ArrayList;
import java.util.List;

public class ErrorHandler {

    public static class LuminaError {

        public enum ErrorType {
            LEXICAL,    // error en el lexer
            SYNTACTIC,  // error en el parser
            SEMANTIC    // error en la tabla de simbolos
        }

        public final ErrorType type;
        public final String    message;
        public final int       line;
        public final int       column;

        public LuminaError(ErrorType type, String message, int line, int column) {
            this.type    = type;
            this.message = message;
            this.line    = line;
            this.column  = column;
        }

        @Override
        public String toString() {
            return String.format("[%s ERROR] linea %d, columna %d: %s",
                    type, line, column, message);
        }
    }

    // ESTADO-------------------------------------------------

    private final List<LuminaError> errors;
    private boolean hasErrors;

    public ErrorHandler() {
        this.errors    = new ArrayList<>();
        this.hasErrors = false;
    }

    // REGISTRO DE ERRORES------------------------------------

    public void lexicalError(String message, int line, int column) {
        register(LuminaError.ErrorType.LEXICAL, message, line, column);
    }

    public void syntacticError(String message, int line, int column) {
        register(LuminaError.ErrorType.SYNTACTIC, message, line, column);
    }

    public void semanticError(String message, int line, int column) {
        register(LuminaError.ErrorType.SEMANTIC, message, line, column);
    }

    private void register(LuminaError.ErrorType type, String message,
                          int line, int column) {
        errors.add(new LuminaError(type, message, line, column));
        hasErrors = true;
    }

    // CONSULTA-----------------------------------------------

    public boolean hasErrors() {
        return hasErrors;
    }

    public List<LuminaError> getErrors() {
        return errors;
    }

    public int errorCount() {
        return errors.size();
    }

    // REPORTE------------------------------------------------

    public void printErrors() {
        if (!hasErrors) {
            System.out.println("Compilacion exitosa. Sin errores.");
            return;
        }

        System.out.println("Se encontraron " + errors.size() + " error(es):\n");
        for (LuminaError error : errors) {
            System.out.println(error);
        }
    }

    public void clear() {
        errors.clear();
        hasErrors = false;
    }
}