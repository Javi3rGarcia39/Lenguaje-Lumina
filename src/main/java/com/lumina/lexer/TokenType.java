/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lumina.lexer;

public enum TokenType {

    // Tipos de datos
    INT, FLOAT, BOOL, STRING,

    // Literales
    INT_LITERAL,        // [0-9]+
    FLOAT_LITERAL,      // [0-9]+\.[0-9]+
    BOOL_LITERAL,       // true|false
    STRING_LITERAL,     // "[^"]*"

    // Identificadores
    IDENTIFIER,         // [a-zA-Z_][a-zA-Z0-9_]*

    // Palabras reservadas
    FUN,                // fun
    VAR,                // var
    RETURN,             // return
    IF,                 // if
    ELSE,               // else
    WHILE,              // while
    FOR,                // for
    PRINT,              // print
    AND,                // and
    OR,                 // or
    NOT,                // not
    NULL,               // null

    // Operadores aritméticos
    PLUS,               // +
    MINUS,              // -
    STAR,               // *
    SLASH,              // /
    PERCENT,            // %

    // Operadores relacionales
    EQUAL_EQUAL,        // ==
    BANG_EQUAL,         // !=
    LESS,               // 
    LESS_EQUAL,         // <=
    GREATER,            // >
    GREATER_EQUAL,      // >=

    // Operadores de asignación
    EQUAL,              // =
    PLUS_EQUAL,         // +=
    MINUS_EQUAL,        // -=
    STAR_EQUAL,         // *=
    SLASH_EQUAL,        // /=

    // Delimitadores
    LEFT_PAREN,         // (
    RIGHT_PAREN,        // )
    LEFT_BRACE,         // {
    RIGHT_BRACE,        // }
    LEFT_BRACKET,       // [
    RIGHT_BRACKET,      // ]
    COMMA,              // ,
    SEMICOLON,          // ;
    COLON,              // :
    ARROW,              // ->
    DOT,                // .

    // Especiales
    EOF,                // fin de archivo
    ERROR               // token inválido
}