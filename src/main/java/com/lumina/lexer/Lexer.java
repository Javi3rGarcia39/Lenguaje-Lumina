/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lumina.lexer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.lumina.error.ErrorHandler;

public class Lexer {

    private final String source;
    private final List<Token> tokens;
    private final ErrorHandler errorHandler;

    private int start;      // inicio del token actual
    private int current;    // posicion actual en el source
    private int line;       // linea actual
    private int column;     // columna actual

    // Tabla de palabras reservadas
    private static final Map<String, TokenType> KEYWORDS = new HashMap<>();

    static {
        KEYWORDS.put("fun", TokenType.FUN);
        KEYWORDS.put("var", TokenType.VAR);
        KEYWORDS.put("return", TokenType.RETURN);
        KEYWORDS.put("if", TokenType.IF);
        KEYWORDS.put("else", TokenType.ELSE);
        KEYWORDS.put("while", TokenType.WHILE);
        KEYWORDS.put("for", TokenType.FOR);
        KEYWORDS.put("print", TokenType.PRINT);
        KEYWORDS.put("and", TokenType.AND);
        KEYWORDS.put("or", TokenType.OR);
        KEYWORDS.put("not", TokenType.NOT);
        KEYWORDS.put("null", TokenType.NULL);
        KEYWORDS.put("true", TokenType.BOOL_LITERAL);
        KEYWORDS.put("false", TokenType.BOOL_LITERAL);
        KEYWORDS.put("int", TokenType.INT);
        KEYWORDS.put("float", TokenType.FLOAT);
        KEYWORDS.put("bool", TokenType.BOOL);
        KEYWORDS.put("string", TokenType.STRING);
    }

    public Lexer(String source, ErrorHandler errorHandler) {
        this.source = source;
        this.tokens = new ArrayList<>();
        this.errorHandler = errorHandler;
        this.start = 0;
        this.current = 0;
        this.line = 1;
        this.column = 1;
    }

    // -------------------------------------------------------
    // PUNTO DE ENTRADA
    // -------------------------------------------------------
    public List<Token> tokenize() {
        while (!isAtEnd()) {
            start = current;
            scanToken();
        }
        tokens.add(new Token(TokenType.EOF, "", line, column));
        return tokens;
    }

    // -------------------------------------------------------
    // MOTOR PRINCIPAL
    // -------------------------------------------------------
    private void scanToken() {
        char c = advance();

        switch (c) {
            // Delimitadores simples
            case '(' ->
                addToken(TokenType.LEFT_PAREN);
            case ')' ->
                addToken(TokenType.RIGHT_PAREN);
            case '{' ->
                addToken(TokenType.LEFT_BRACE);
            case '}' ->
                addToken(TokenType.RIGHT_BRACE);
            case '[' ->
                addToken(TokenType.LEFT_BRACKET);
            case ']' ->
                addToken(TokenType.RIGHT_BRACKET);
            case ',' ->
                addToken(TokenType.COMMA);
            case ';' ->
                addToken(TokenType.SEMICOLON);
            case ':' ->
                addToken(TokenType.COLON);
            case '.' ->
                addToken(TokenType.DOT);
            case '%' ->
                addToken(TokenType.PERCENT);

            // Operadores que pueden ser simples o compuestos
            case '+' ->
                addToken(match('=') ? TokenType.PLUS_EQUAL : TokenType.PLUS);
            case '*' ->
                addToken(match('=') ? TokenType.STAR_EQUAL : TokenType.STAR);
            case '/' -> {
                if (match('/')) {
                    // Comentario de linea -- ER: //[^\n]*
                    while (peek() != '\n' && !isAtEnd()) {
                        advance();
                    }
                } else if (match('=')) {
                    addToken(TokenType.SLASH_EQUAL);
                } else {
                    addToken(TokenType.SLASH);
                }
            }
            case '-' -> {
                if (match('>')) {
                    addToken(TokenType.ARROW);         // ->
                } else if (match('=')) {
                    addToken(TokenType.MINUS_EQUAL);   // -=
                } else {
                    addToken(TokenType.MINUS);         // -
                }
            }
            case '=' ->
                addToken(match('=') ? TokenType.EQUAL_EQUAL : TokenType.EQUAL);
            case '!' -> {
                if (match('=')) {
                    addToken(TokenType.BANG_EQUAL);
                } else {
                    errorHandler.lexicalError("Caracter inesperado '!'", line, column);
                    addToken(TokenType.ERROR);
                }
            }
            case '<' ->
                addToken(match('=') ? TokenType.LESS_EQUAL : TokenType.LESS);
            case '>' ->
                addToken(match('=') ? TokenType.GREATER_EQUAL : TokenType.GREATER);

            // Comentario de bloque -- ER: #[^\n]*
            case '#' -> {
                while (peek() != '\n' && !isAtEnd()) {
                    advance();
                }
            }

            // String literal -- ER: "[^"]*"
            case '"' ->
                scanString();

            // Espacios y saltos de linea
            case ' ', '\r', '\t' -> {
                /* ignorar */ }
            case '\n' -> {
                line++;
                column = 1;
            }

            default -> {
                if (Character.isDigit(c)) {
                    // ER: [0-9]+(\.[0-9]+)?
                    scanNumber();
                } else if (Character.isLetter(c) || c == '_') {
                    // ER: [a-zA-Z_][a-zA-Z0-9_]*
                    scanIdentifier();
                } else {
                    errorHandler.lexicalError(
                            "Caracter no reconocido: '" + c + "'", line, column);
                    addToken(TokenType.ERROR);
                }
            }
        }
    }

    // -------------------------------------------------------
    // SCANNERS ESPECIFICOS
    // -------------------------------------------------------
    // ER: "[^"]*"
    private void scanString() {
        int startLine = line;
        int startColumn = column;

        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n') {
                line++;
                column = 1;
            }
            advance();
        }

        if (isAtEnd()) {
            errorHandler.lexicalError(
                    "String sin cerrar", startLine, startColumn);
            addToken(TokenType.ERROR);
            return;
        }

        advance(); // consume el " de cierre

        // valor sin las comillas
        String value = source.substring(start + 1, current - 1);
        tokens.add(new Token(TokenType.STRING_LITERAL, value, startLine, startColumn));
    }

    // ER: [0-9]+(\.[0-9]+)?
    private void scanNumber() {
        while (Character.isDigit(peek())) {
            advance();
        }

        if (peek() == '.' && Character.isDigit(peekNext())) {
            advance(); // consume el punto
            while (Character.isDigit(peek())) {
                advance();
            }
            addToken(TokenType.FLOAT_LITERAL);
        } else {
            addToken(TokenType.INT_LITERAL);
        }
    }

    // ER: [a-zA-Z_][a-zA-Z0-9_]*
    private void scanIdentifier() {
        while (Character.isLetterOrDigit(peek()) || peek() == '_') {
            advance();
        }

        String text = source.substring(start, current);
        TokenType type = KEYWORDS.getOrDefault(text, TokenType.IDENTIFIER);
        addToken(type);
    }

    // -------------------------------------------------------
    // UTILIDADES
    // -------------------------------------------------------
    private char advance() {
        column++;
        return source.charAt(current++);
    }

    private boolean match(char expected) {
        if (isAtEnd()) {
            return false;
        }
        if (source.charAt(current) != expected) {
            return false;
        }
        current++;
        column++;
        return true;
    }

    private char peek() {
        if (isAtEnd()) {
            return '\0';
        }
        return source.charAt(current);
    }

    private char peekNext() {
        if (current + 1 >= source.length()) {
            return '\0';
        }
        return source.charAt(current + 1);
    }

    private boolean isAtEnd() {
        return current >= source.length();
    }

    private void addToken(TokenType type) {
        String value = source.substring(start, current);
        int tokenColumn = column - value.length();
        tokens.add(new Token(type, value, line, tokenColumn));
    }
}
