package com.lumina.lexer;

import com.lumina.error.ErrorHandler;
import java_cup.runtime.Symbol;
import com.lumina.parser.sym;

%%

%class Lexer
%public
%unicode
%cup
%line
%column
%char

%{
    private ErrorHandler errorHandler;

    public void setErrorHandler(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    private Symbol token(int type) {
        return new Symbol(type, yyline + 1, yycolumn + 1, yytext());
    }

    private Symbol token(int type, Object value) {
        return new Symbol(type, yyline + 1, yycolumn + 1, value);
    }
%}

// Expresiones regulares
LineTerminator  = \r|\n|\r\n
WhiteSpace      = {LineTerminator} | [ \t\f]
Comment         = "#" [^\r\n]*

Digit           = [0-9]
Letter          = [a-zA-Z_]
AlphaNum        = [a-zA-Z0-9_]

IntLiteral      = {Digit}+
FloatLiteral    = {Digit}+ "." {Digit}+
StringLiteral   = \" [^\"\r\n]* \"
Identifier      = {Letter}{AlphaNum}*

%%

//Palabras reservadas
"fun"       { return token(sym.FUN); }
"var"       { return token(sym.VAR); }
"return"    { return token(sym.RETURN); }
"if"        { return token(sym.IF); }
"else"      { return token(sym.ELSE); }
"while"     { return token(sym.WHILE); }
"for"       { return token(sym.FOR); }
"print"     { return token(sym.PRINT); }
"and"       { return token(sym.AND); }
"or"        { return token(sym.OR); }
"not"       { return token(sym.NOT); }
"null"      { return token(sym.NULL); }
"true"      { return token(sym.BOOL_LITERAL, true); }
"false"     { return token(sym.BOOL_LITERAL, false); }
"int"       { return token(sym.TYPE_INT); }
"float"     { return token(sym.TYPE_FLOAT); }
"bool"      { return token(sym.TYPE_BOOL); }
"string"    { return token(sym.TYPE_STRING); }

//Literales
{IntLiteral}    { return token(sym.INT_LITERAL,    Integer.parseInt(yytext())); }
{FloatLiteral}  { return token(sym.FLOAT_LITERAL,  Double.parseDouble(yytext())); }
{StringLiteral} { return token(sym.STRING_LITERAL, yytext().substring(1, yytext().length() - 1)); }

//Identificadores
{Identifier}    { return token(sym.IDENTIFIER, yytext()); }

//Operadores aritmeticos
"+"     { return token(sym.PLUS); }
"-"     { return token(sym.MINUS); }
"*"     { return token(sym.STAR); }
"/"     { return token(sym.SLASH); }
"%"     { return token(sym.PERCENT); }

//Operadores de asignacion
"+="    { return token(sym.PLUS_EQUAL); }
"-="    { return token(sym.MINUS_EQUAL); }
"*="    { return token(sym.STAR_EQUAL); }
"/="    { return token(sym.SLASH_EQUAL); }
"="     { return token(sym.EQUAL); }

//Operadores relacionales
"=="    { return token(sym.EQUAL_EQUAL); }
"!="    { return token(sym.BANG_EQUAL); }
"<="    { return token(sym.LESS_EQUAL); }
">="    { return token(sym.GREATER_EQUAL); }
"<"     { return token(sym.LESS); }
">"     { return token(sym.GREATER); }

//Flecha de retorno
"->"    { return token(sym.ARROW); }

//Delimitadores
"("     { return token(sym.LEFT_PAREN); }
")"     { return token(sym.RIGHT_PAREN); }
"{"     { return token(sym.LEFT_BRACE); }
"}"     { return token(sym.RIGHT_BRACE); }
"["     { return token(sym.LEFT_BRACKET); }
"]"     { return token(sym.RIGHT_BRACKET); }
","     { return token(sym.COMMA); }
";"     { return token(sym.SEMICOLON); }
":"     { return token(sym.COLON); }
"."     { return token(sym.DOT); }

//Comentarios y espacios
{Comment}       { /* ignorar */ }
{WhiteSpace}    { /* ignorar */ }

//Error lexico
[^]  {
    if (errorHandler != null) {
        errorHandler.lexicalError(
            "Caracter no reconocido: '" + yytext() + "'",
            yyline + 1,
            yycolumn + 1
        );
    }
}