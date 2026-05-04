/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lumina.lexer;

public class Token {

    public final TokenType type;
    public final String value;
    public final int line;
    public final int column;

    public Token(TokenType type, String value, int line, int column) {
        this.type   = type;
        this.value  = value;
        this.line   = line;
        this.column = column;
    }

    @Override
    public String toString() {
        return String.format("Token[%-20s | %-15s | line: %d, col: %d]",
                type, value, line, column);
    }
}