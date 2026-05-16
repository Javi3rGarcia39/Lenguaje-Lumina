package com.lumina.symbols;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class SymbolTable {

    // CLASE INTERNA: simbolo
    public static class Symbol {

        public enum Kind { VARIABLE, PARAMETER, FUNCTION }

        public final String  name;
        public final String  type;   // "int", "float", "bool", "string"
        public final Kind    kind;
        public final int     line;
        public final int     column;
        public boolean       initialized;

        public Symbol(String name, String type, Kind kind,
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
            return String.format("Symbol[%-15s | %-8s | %-10s | line: %d, col: %d | init: %b]",
                    name, type, kind, line, column, initialized);
        }
    }

    // ESTADO
    private final Stack<Map<String, Symbol>> scopes;

    public SymbolTable() {
        this.scopes = new Stack<>();
        enterScope(); // ambito global
    }

    // GESTION DE AMBITOS
    public void enterScope() {
        scopes.push(new HashMap<>());
    }

    public void exitScope() {
        if (scopes.size() > 1) scopes.pop();
    }

    public int currentDepth() {
        return scopes.size();
    }

    // INSERCION
    public boolean define(String name, String type, Symbol.Kind kind,
                          int line, int column) {
        Map<String, Symbol> current = scopes.peek();
        if (current.containsKey(name)) return false;
        current.put(name, new Symbol(name, type, kind, line, column, false));
        return true;
    }

    public void markInitialized(String name) {
        for (int i = scopes.size() - 1; i >= 0; i--) {
            if (scopes.get(i).containsKey(name)) {
                scopes.get(i).get(name).initialized = true;
                return;
            }
        }
    }

    // BUSQUEDA
    public Symbol resolve(String name) {
        for (int i = scopes.size() - 1; i >= 0; i--) {
            Symbol s = scopes.get(i).get(name);
            if (s != null) return s;
        }
        return null;
    }

    public boolean existsInCurrentScope(String name) {
        return scopes.peek().containsKey(name);
    }

    // REPORTE
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