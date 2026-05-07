/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lumina.symbols;

import com.lumina.lexer.TokenType;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class SymbolTable {

    // CLASE INTERNA -- representa un simbolo en la tabla-----

    public static class Symbol {

        public enum Kind {
            VARIABLE,
            PARAMETER,
            FUNCTION
        }

        public final String    name;
        public final TokenType type;
        public final Kind      kind;
        public final int       line;
        public final int       column;
        public boolean         initialized;

        public Symbol(String name, TokenType type, Kind kind,
                      int line, int column, boolean initialized) {
            this.name        = name;
            this.type        = type;
            this.kind        = kind;
            this.line        = line;
            this.column      = column;
            this.initialized = initialized;
        }

        @Override
        public String toString() {
            return String.format("Symbol[%-15s | %-10s | %-10s | line: %d, col: %d | init: %b]",
                    name, type, kind, line, column, initialized);
        }
    }

    // ESTADO -- pila de ambitos (scopes)---------------------

    private final Stack<Map<String, Symbol>> scopes;

    public SymbolTable() {
        this.scopes = new Stack<>();
        enterScope(); // ambito global
    }

    // GESTION DE AMBITOS-------------------------------------

    // Abre un nuevo ambito (al entrar a una funcion o bloque)
    public void enterScope() {
        scopes.push(new HashMap<>());
    }

    // Cierra el ambito actual (al salir de una funcion o bloque)
    public void exitScope() {
        if (scopes.size() > 1) {
            scopes.pop();
        }
    }

    public int currentDepth() {
        return scopes.size();
    }

    // INSERCION----------------------------------------------

    // Retorna true si se inserto correctamente
    // Retorna false si ya existia en el ambito actual
    public boolean define(String name, TokenType type, Symbol.Kind kind,
                          int line, int column) {
        Map<String, Symbol> current = scopes.peek();
        if (current.containsKey(name)) {
            return false; // ya declarado en este ambito
        }
        current.put(name, new Symbol(name, type, kind, line, column, false));
        return true;
    }

    // Marca un simbolo como inicializado
    public void markInitialized(String name) {
        for (int i = scopes.size() - 1; i >= 0; i--) {
            if (scopes.get(i).containsKey(name)) {
                scopes.get(i).get(name).initialized = true;
                return;
            }
        }
    }

    // BUSQUEDA-----------------------------------------------

    // Busca desde el ambito actual hacia el global
    public Symbol resolve(String name) {
        for (int i = scopes.size() - 1; i >= 0; i--) {
            Symbol symbol = scopes.get(i).get(name);
            if (symbol != null) {
                return symbol;
            }
        }
        return null; // no encontrado
    }

    // Verifica si existe en el ambito actual unicamente
    public boolean existsInCurrentScope(String name) {
        return scopes.peek().containsKey(name);
    }

    // REPORTE------------------------------------------------

    public void printTable() {
        System.out.println("=== TABLA DE SIMBOLOS ===\n");
        for (int i = scopes.size() - 1; i >= 0; i--) {
            System.out.println("-- Ambito " + i + (i == 0 ? " (global)" : " (local)") + " --");
            if (scopes.get(i).isEmpty()) {
                System.out.println("   (vacio)");
            } else {
                for (Symbol s : scopes.get(i).values()) {
                    System.out.println("   " + s);
                }
            }
        }
        System.out.println();
    }
}