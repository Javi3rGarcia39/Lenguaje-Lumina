/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lumina;

import com.lumina.error.ErrorHandler;
import com.lumina.lexer.Lexer;
import com.lumina.parser.Parser;
import com.lumina.symbols.SymbolTable;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;
import java_cup.runtime.Symbol;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.*;

/**
 *
 * @author Javi3Rex39
 */


public class FrmPrincipal extends JFrame {

    // -------------------------------------------------------
    // COLORES -- tema oscuro
    // -------------------------------------------------------
    private static final Color DARK_BG        = new Color(30, 30, 30);
    private static final Color DARK_PANEL     = new Color(37, 37, 38);
    private static final Color DARK_SIDEBAR   = new Color(51, 51, 51);
    private static final Color DARK_EDITOR    = new Color(30, 30, 30);
    private static final Color DARK_LINE_NUM  = new Color(51, 51, 51);
    private static final Color DARK_TEXT      = new Color(212, 212, 212);
    private static final Color DARK_SUBTEXT   = new Color(153, 153, 153);
    private static final Color DARK_ACCENT    = new Color(0, 122, 204);
    private static final Color DARK_SUCCESS   = new Color(78, 201, 176);
    private static final Color DARK_ERROR     = new Color(244, 135, 113);
    private static final Color DARK_WARNING   = new Color(220, 220, 170);
    private static final Color DARK_BORDER    = new Color(60, 60, 60);
    private static final Color DARK_BUTTON    = new Color(14, 99, 156);
    private static final Color DARK_TAB_ACTIVE= new Color(30, 30, 30);
    private static final Color DARK_TAB_INACTIVE = new Color(45, 45, 45);

    // -------------------------------------------------------
    // COLORES -- tema claro
    // -------------------------------------------------------
    private static final Color LIGHT_BG       = new Color(255, 255, 255);
    private static final Color LIGHT_PANEL    = new Color(243, 243, 243);
    private static final Color LIGHT_SIDEBAR  = new Color(248, 248, 248);
    private static final Color LIGHT_EDITOR   = new Color(255, 255, 255);
    private static final Color LIGHT_LINE_NUM = new Color(237, 237, 237);
    private static final Color LIGHT_TEXT     = new Color(30, 30, 30);
    private static final Color LIGHT_SUBTEXT  = new Color(100, 100, 100);
    private static final Color LIGHT_ACCENT   = new Color(0, 122, 204);
    private static final Color LIGHT_SUCCESS  = new Color(0, 128, 0);
    private static final Color LIGHT_ERROR    = new Color(205, 49, 49);
    private static final Color LIGHT_WARNING  = new Color(128, 128, 0);
    private static final Color LIGHT_BORDER   = new Color(220, 220, 220);
    private static final Color LIGHT_BUTTON   = new Color(0, 122, 204);
    private static final Color LIGHT_TAB_ACTIVE   = new Color(255, 255, 255);
    private static final Color LIGHT_TAB_INACTIVE = new Color(236, 236, 236);

    // -------------------------------------------------------
    // ESTADO
    // -------------------------------------------------------
    private boolean darkMode = true;
    private String currentFile = null;

    // -------------------------------------------------------
    // COMPONENTES
    // -------------------------------------------------------
    private JPanel mainPanel;
    private JPanel titleBar;
    private JLabel lblTitle;
    private JLabel lblFile;
    private JToggleButton btnTheme;

    // Editor
    private JPanel editorPanel;
    private JTextArea txtEditor;
    private JTextArea txtLineNumbers;
    private JScrollPane editorScroll;

    // Tabs de resultados
    private JTabbedPane tabResults;
    private JTextArea txtTokens;
    private JTextArea txtErrors;
    private JTextArea txtSymbols;
    private JTextArea txtConsole;

    // Barra de estado
    private JPanel statusBar;
    private JLabel lblStatus;
    private JLabel lblCursor;
    private JLabel lblLang;

    // Botones
    private JButton btnOpen;
    private JButton btnAnalyze;
    private JButton btnClear;
    private JButton btnSave;

    // -------------------------------------------------------
    // CONSTRUCTOR
    // -------------------------------------------------------
    public FrmPrincipal() {
        setTitle("Lumina IDE");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 800);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        buildUI();
        applyTheme();
        setVisible(true);
    }

    // -------------------------------------------------------
    // CONSTRUCCION DE LA UI
    // -------------------------------------------------------
    private void buildUI() {
        mainPanel = new JPanel(new BorderLayout(0, 0));
        setContentPane(mainPanel);

        buildTitleBar();
        buildToolBar();
        buildCenter();
        buildStatusBar();
    }

    private void buildTitleBar() {
        titleBar = new JPanel(new BorderLayout());
        titleBar.setPreferredSize(new Dimension(0, 35));
        titleBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, DARK_BORDER));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 6));
        left.setOpaque(false);

        // Dot decorations al estilo macOS
        JLabel dot1 = dot(new Color(255, 95, 86));
        JLabel dot2 = dot(new Color(255, 189, 46));
        JLabel dot3 = dot(new Color(39, 201, 63));
        left.add(dot1);
        left.add(dot2);
        left.add(dot3);

        lblTitle = new JLabel("  Lumina IDE");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        left.add(lblTitle);

        JPanel center = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 7));
        center.setOpaque(false);
        lblFile = new JLabel("sin archivo");
        lblFile.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        center.add(lblFile);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 6));
        right.setOpaque(false);
        btnTheme = new JToggleButton("☀");
        btnTheme.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnTheme.setFocusPainted(false);
        btnTheme.setBorderPainted(false);
        btnTheme.setContentAreaFilled(false);
        btnTheme.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnTheme.addActionListener(e -> toggleTheme());
        right.add(btnTheme);

        titleBar.add(left, BorderLayout.WEST);
        titleBar.add(center, BorderLayout.CENTER);
        titleBar.add(right, BorderLayout.EAST);

        mainPanel.add(titleBar, BorderLayout.NORTH);
    }

    private JLabel dot(Color color) {
        JLabel d = new JLabel("●");
        d.setForeground(color);
        d.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        return d;
    }

    private void buildToolBar() {
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        toolbar.setPreferredSize(new Dimension(0, 42));

        btnOpen    = toolButton("📂 Abrir",    "Abrir archivo .lum o .txt");
        btnSave    = toolButton("💾 Guardar",  "Guardar archivo actual");
        btnAnalyze = toolButton("▶ Analizar",  "Ejecutar analisis lexico y sintactico");
        btnClear   = toolButton("🗑 Limpiar",  "Limpiar todos los paneles");

        btnOpen.addActionListener(e -> openFile());
        btnSave.addActionListener(e -> saveFile());
        btnAnalyze.addActionListener(e -> analyze());
        btnClear.addActionListener(e -> clearAll());

        toolbar.add(btnOpen);
        toolbar.add(new JSeparator(JSeparator.VERTICAL));
        toolbar.add(btnSave);
        toolbar.add(new JSeparator(JSeparator.VERTICAL));
        toolbar.add(btnAnalyze);
        toolbar.add(btnClear);

        mainPanel.add(toolbar, BorderLayout.AFTER_LINE_ENDS);

        // Lo ponemos debajo del titleBar via un segundo BorderLayout
        JPanel north = new JPanel(new BorderLayout());
        north.add(titleBar, BorderLayout.NORTH);
        north.add(toolbar, BorderLayout.SOUTH);
        mainPanel.add(north, BorderLayout.NORTH);
    }

    private JButton toolButton(String text, String tooltip) {
        JButton btn = new JButton(text);
        btn.setToolTipText(tooltip);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(110, 28));
        return btn;
    }

    private void buildCenter() {
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.setDividerLocation(620);
        split.setDividerSize(4);
        split.setResizeWeight(0.55);
        split.setBorder(null);

        // Panel izquierdo: editor
        editorPanel = new JPanel(new BorderLayout());

        // Encabezado del editor
        JPanel editorHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 6));
        JLabel tabLabel = new JLabel("editor.lum");
        tabLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        editorHeader.add(tabLabel);

        // Editor con numeros de linea
        txtEditor = new JTextArea();
        txtEditor.setFont(new Font("JetBrains Mono", Font.PLAIN, 14));
        txtEditor.setTabSize(4);
        txtEditor.setLineWrap(false);
        txtEditor.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));

        txtLineNumbers = new JTextArea("1");
        txtLineNumbers.setFont(new Font("JetBrains Mono", Font.PLAIN, 14));
        txtLineNumbers.setEditable(false);
        txtLineNumbers.setFocusable(false);
        txtLineNumbers.setPreferredSize(new Dimension(45, 0));
        txtLineNumbers.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        txtLineNumbers.setHighlighter(null);

        // Sincronizar numeros de linea
        txtEditor.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { updateLineNumbers(); updateStatus(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { updateLineNumbers(); updateStatus(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) {}
        });

        txtEditor.addCaretListener(e -> updateCursor());

        editorScroll = new JScrollPane(txtEditor);
        editorScroll.setRowHeaderView(txtLineNumbers);
        editorScroll.setBorder(null);
        editorScroll.getVerticalScrollBar().setUnitIncrement(16);

        // Sincronizar scroll de numeros
        editorScroll.getViewport().addChangeListener(e ->
            txtLineNumbers.repaint()
        );

        editorPanel.add(editorHeader, BorderLayout.NORTH);
        editorPanel.add(editorScroll, BorderLayout.CENTER);

        // Panel derecho: resultados
        tabResults = new JTabbedPane();
        tabResults.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabResults.setTabPlacement(JTabbedPane.TOP);

        txtTokens  = resultArea();
        txtErrors  = resultArea();
        txtSymbols = resultArea();
        txtConsole = resultArea();

        tabResults.addTab("🔤 Tokens",         new JScrollPane(txtTokens));
        tabResults.addTab("⚠ Errores",         new JScrollPane(txtErrors));
        tabResults.addTab("📋 Tabla Simbolos",  new JScrollPane(txtSymbols));
        tabResults.addTab("💻 Consola",         new JScrollPane(txtConsole));

        split.setLeftComponent(editorPanel);
        split.setRightComponent(tabResults);

        mainPanel.add(split, BorderLayout.CENTER);
    }

    private JTextArea resultArea() {
        JTextArea area = new JTextArea();
        area.setFont(new Font("JetBrains Mono", Font.PLAIN, 12));
        area.setEditable(false);
        area.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        return area;
    }

    private void buildStatusBar() {
        statusBar = new JPanel(new BorderLayout());
        statusBar.setPreferredSize(new Dimension(0, 24));
        statusBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, DARK_BORDER));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 3));
        left.setOpaque(false);
        lblStatus = new JLabel("Listo");
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        left.add(lblStatus);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 3));
        right.setOpaque(false);
        lblCursor = new JLabel("Ln 1, Col 1");
        lblCursor.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblLang = new JLabel("Lumina");
        lblLang.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        right.add(lblCursor);
        right.add(new JLabel("|"));
        right.add(lblLang);

        statusBar.add(left, BorderLayout.WEST);
        statusBar.add(right, BorderLayout.EAST);

        mainPanel.add(statusBar, BorderLayout.SOUTH);
    }

    // -------------------------------------------------------
    // LOGICA DE NEGOCIO
    // -------------------------------------------------------
    private void openFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter(
            "Archivos Lumina (*.lum, *.txt)", "lum", "txt"));
        chooser.setCurrentDirectory(new File("."));

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            try {
                String content = new String(Files.readAllBytes(file.toPath()));
                txtEditor.setText(content);
                txtEditor.setCaretPosition(0);
                currentFile = file.getAbsolutePath();
                lblFile.setText(file.getName());
                setStatus("Archivo cargado: " + file.getName(), false);
                clearResults();
            } catch (IOException ex) {
                showError("No se pudo leer el archivo: " + ex.getMessage());
            }
        }
    }

    private void saveFile() {
        if (currentFile == null) {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileFilter(new FileNameExtensionFilter(
                "Archivos Lumina (*.lum)", "lum"));
            if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
            String path = chooser.getSelectedFile().getAbsolutePath();
            if (!path.endsWith(".lum")) path += ".lum";
            currentFile = path;
        }
        try {
            Files.write(Paths.get(currentFile), txtEditor.getText().getBytes());
            lblFile.setText(new File(currentFile).getName());
            setStatus("Archivo guardado.", false);
        } catch (IOException ex) {
            showError("No se pudo guardar: " + ex.getMessage());
        }
    }

    private void analyze() {
        String source = txtEditor.getText().trim();
        if (source.isEmpty()) {
            setStatus("El editor esta vacio.", true);
            return;
        }

        clearResults();
        setStatus("Analizando...", false);

        ErrorHandler errorHandler = new ErrorHandler();
        SymbolTable symbolTable   = new SymbolTable();

        StringBuilder tokensOut  = new StringBuilder();
        StringBuilder errorsOut  = new StringBuilder();
        StringBuilder symbolsOut = new StringBuilder();
        StringBuilder consoleOut = new StringBuilder();

        try {
            // Redirigir stderr para suprimir mensajes internos de JCup
            System.setErr(new java.io.PrintStream(new java.io.OutputStream() {
                public void write(int b) {}
            }));

            Lexer lexer = new Lexer(new java.io.StringReader(source));
            lexer.setErrorHandler(errorHandler);

            // Tokenizar manualmente para mostrar en panel
            Lexer lexerForTokens = new Lexer(new java.io.StringReader(source));
            ErrorHandler ehTokens = new ErrorHandler();
            lexerForTokens.setErrorHandler(ehTokens);

            tokensOut.append(String.format("%-25s %-20s %-8s %s%n",
                "TIPO", "VALOR", "LINEA", "COLUMNA"));
            tokensOut.append("─".repeat(65)).append("\n");

            Symbol tok;
            int tokenCount = 0;
            while (true) {
                tok = lexerForTokens.next_token();
                if (tok == null || tok.sym == com.lumina.parser.sym.EOF) break;
                String typeName = getTokenName(tok.sym);
                String value    = tok.value != null ? tok.value.toString() : "";
                tokensOut.append(String.format("%-25s %-20s %-8d %d%n",
                    typeName, value, tok.left, tok.right));
                tokenCount++;
            }
            tokensOut.append("\n").append("─".repeat(65)).append("\n");
            tokensOut.append("Total de tokens: ").append(tokenCount).append("\n");

            // Parser
            Parser parser = new Parser(lexer);
            parser.setErrorHandler(errorHandler);
            parser.setSymbolTable(symbolTable);
            parser.parse();

            consoleOut.append("=== LUMINA COMPILER ===\n\n");
            consoleOut.append("Analisis completado.\n");

        } catch (Exception ex) {
            consoleOut.append("Error durante el analisis: ").append(ex.getMessage()).append("\n");
        }

        // Errores
        if (errorHandler.hasErrors()) {
            errorsOut.append(String.format("%-12s %-8s %-8s %s%n",
                "TIPO", "LINEA", "COLUMNA", "MENSAJE"));
            errorsOut.append("─".repeat(70)).append("\n");
            for (ErrorHandler.LuminaError e : errorHandler.getErrors()) {
                errorsOut.append(String.format("%-12s %-8d %-8d %s%n",
                    e.type, e.line, e.column, e.message));
            }
            errorsOut.append("\nTotal: ").append(errorHandler.errorCount()).append(" error(es)\n");
            consoleOut.append("\nSe encontraron ")
                      .append(errorHandler.errorCount()).append(" error(es).\n");
            setStatus("Analisis con " + errorHandler.errorCount() + " error(es).", true);
            tabResults.setSelectedIndex(1);
        } else {
            errorsOut.append("✓ Sin errores lexicos ni sintacticos.\n");
            consoleOut.append("\n✓ Compilacion exitosa. Sin errores.\n");
            setStatus("Analisis completado sin errores.", false);
            tabResults.setSelectedIndex(0);
        }

        // Tabla de simbolos
        symbolsOut.append(String.format("%-18s %-10s %-12s %-8s %-8s %s%n",
            "NOMBRE", "TIPO", "CLASE", "LINEA", "COL", "INICIALIZADO"));
        symbolsOut.append("─".repeat(70)).append("\n");
        // La tabla estara vacia hasta que se agreguen acciones semanticas
        symbolsOut.append("(La tabla se poblara con las acciones semanticas)\n");

        txtTokens.setText(tokensOut.toString());
        txtErrors.setText(errorsOut.toString());
        txtSymbols.setText(symbolsOut.toString());
        txtConsole.setText(consoleOut.toString());

        txtTokens.setCaretPosition(0);
        txtErrors.setCaretPosition(0);
        txtSymbols.setCaretPosition(0);
        txtConsole.setCaretPosition(0);
    }

    private String getTokenName(int sym) {
        switch (sym) {
            case com.lumina.parser.sym.FUN:          return "FUN";
            case com.lumina.parser.sym.VAR:          return "VAR";
            case com.lumina.parser.sym.RETURN:       return "RETURN";
            case com.lumina.parser.sym.IF:           return "IF";
            case com.lumina.parser.sym.ELSE:         return "ELSE";
            case com.lumina.parser.sym.WHILE:        return "WHILE";
            case com.lumina.parser.sym.FOR:          return "FOR";
            case com.lumina.parser.sym.PRINT:        return "PRINT";
            case com.lumina.parser.sym.AND:          return "AND";
            case com.lumina.parser.sym.OR:           return "OR";
            case com.lumina.parser.sym.NOT:          return "NOT";
            case com.lumina.parser.sym.NULL:         return "NULL";
            case com.lumina.parser.sym.TYPE_INT:     return "TYPE_INT";
            case com.lumina.parser.sym.TYPE_FLOAT:   return "TYPE_FLOAT";
            case com.lumina.parser.sym.TYPE_BOOL:    return "TYPE_BOOL";
            case com.lumina.parser.sym.TYPE_STRING:  return "TYPE_STRING";
            case com.lumina.parser.sym.INT_LITERAL:  return "INT_LITERAL";
            case com.lumina.parser.sym.FLOAT_LITERAL:return "FLOAT_LITERAL";
            case com.lumina.parser.sym.BOOL_LITERAL: return "BOOL_LITERAL";
            case com.lumina.parser.sym.STRING_LITERAL:return "STRING_LITERAL";
            case com.lumina.parser.sym.IDENTIFIER:   return "IDENTIFIER";
            case com.lumina.parser.sym.PLUS:         return "PLUS";
            case com.lumina.parser.sym.MINUS:        return "MINUS";
            case com.lumina.parser.sym.STAR:         return "STAR";
            case com.lumina.parser.sym.SLASH:        return "SLASH";
            case com.lumina.parser.sym.PERCENT:      return "PERCENT";
            case com.lumina.parser.sym.EQUAL:        return "EQUAL";
            case com.lumina.parser.sym.EQUAL_EQUAL:  return "EQUAL_EQUAL";
            case com.lumina.parser.sym.BANG_EQUAL:   return "BANG_EQUAL";
            case com.lumina.parser.sym.LESS:         return "LESS";
            case com.lumina.parser.sym.LESS_EQUAL:   return "LESS_EQUAL";
            case com.lumina.parser.sym.GREATER:      return "GREATER";
            case com.lumina.parser.sym.GREATER_EQUAL:return "GREATER_EQUAL";
            case com.lumina.parser.sym.PLUS_EQUAL:   return "PLUS_EQUAL";
            case com.lumina.parser.sym.MINUS_EQUAL:  return "MINUS_EQUAL";
            case com.lumina.parser.sym.STAR_EQUAL:   return "STAR_EQUAL";
            case com.lumina.parser.sym.SLASH_EQUAL:  return "SLASH_EQUAL";
            case com.lumina.parser.sym.ARROW:        return "ARROW";
            case com.lumina.parser.sym.LEFT_PAREN:   return "LEFT_PAREN";
            case com.lumina.parser.sym.RIGHT_PAREN:  return "RIGHT_PAREN";
            case com.lumina.parser.sym.LEFT_BRACE:   return "LEFT_BRACE";
            case com.lumina.parser.sym.RIGHT_BRACE:  return "RIGHT_BRACE";
            case com.lumina.parser.sym.LEFT_BRACKET: return "LEFT_BRACKET";
            case com.lumina.parser.sym.RIGHT_BRACKET:return "RIGHT_BRACKET";
            case com.lumina.parser.sym.COMMA:        return "COMMA";
            case com.lumina.parser.sym.SEMICOLON:    return "SEMICOLON";
            case com.lumina.parser.sym.COLON:        return "COLON";
            case com.lumina.parser.sym.DOT:          return "DOT";
            case com.lumina.parser.sym.UMINUS:       return "UMINUS";
            default:                                  return "UNKNOWN(" + sym + ")";
        }
    }

    // -------------------------------------------------------
    // UTILIDADES DE UI
    // -------------------------------------------------------
    private void clearAll() {
        txtEditor.setText("");
        clearResults();
        currentFile = null;
        lblFile.setText("sin archivo");
        setStatus("Listo", false);
    }

    private void clearResults() {
        txtTokens.setText("");
        txtErrors.setText("");
        txtSymbols.setText("");
        txtConsole.setText("");
    }

    private void updateLineNumbers() {
        int lines = txtEditor.getLineCount();
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= lines; i++) {
            sb.append(i).append("\n");
        }
        txtLineNumbers.setText(sb.toString());
    }

    private void updateCursor() {
        try {
            int pos  = txtEditor.getCaretPosition();
            int line = txtEditor.getLineOfOffset(pos) + 1;
            int col  = pos - txtEditor.getLineStartOffset(line - 1) + 1;
            lblCursor.setText("Ln " + line + ", Col " + col);
        } catch (Exception ignored) {}
    }

    private void updateStatus() {
        int lines = txtEditor.getLineCount();
        int chars = txtEditor.getText().length();
        lblStatus.setText("Lineas: " + lines + "  |  Caracteres: " + chars);
    }

    private void setStatus(String msg, boolean isError) {
        lblStatus.setText(msg);
        lblStatus.setForeground(isError
            ? (darkMode ? DARK_ERROR : LIGHT_ERROR)
            : (darkMode ? DARK_SUCCESS : LIGHT_SUCCESS));
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // -------------------------------------------------------
    // TEMAS
    // -------------------------------------------------------
    private void toggleTheme() {
        darkMode = !darkMode;
        applyTheme();
    }

    private void applyTheme() {
        Color bg       = darkMode ? DARK_BG       : LIGHT_BG;
        Color panel    = darkMode ? DARK_PANEL    : LIGHT_PANEL;
        Color sidebar  = darkMode ? DARK_SIDEBAR  : LIGHT_SIDEBAR;
        Color editor   = darkMode ? DARK_EDITOR   : LIGHT_EDITOR;
        Color lineNum  = darkMode ? DARK_LINE_NUM : LIGHT_LINE_NUM;
        Color text     = darkMode ? DARK_TEXT     : LIGHT_TEXT;
        Color subtext  = darkMode ? DARK_SUBTEXT  : LIGHT_SUBTEXT;
        Color accent   = darkMode ? DARK_ACCENT   : LIGHT_ACCENT;
        Color border   = darkMode ? DARK_BORDER   : LIGHT_BORDER;
        Color button   = darkMode ? DARK_BUTTON   : LIGHT_BUTTON;
        Color tabActive   = darkMode ? DARK_TAB_ACTIVE   : LIGHT_TAB_ACTIVE;
        Color tabInactive = darkMode ? DARK_TAB_INACTIVE : LIGHT_TAB_INACTIVE;

        // Main
        mainPanel.setBackground(bg);

        // Title bar
        titleBar.setBackground(panel);
        lblTitle.setForeground(text);
        lblFile.setForeground(subtext);
        btnTheme.setText(darkMode ? "☀" : "🌙");
        btnTheme.setForeground(text);

        // Toolbar
        for (Component c : ((JPanel) ((JPanel) mainPanel.getComponent(0)).getComponent(1)).getComponents()) {
            if (c instanceof JButton btn) {
                btn.setBackground(button);
                btn.setForeground(Color.WHITE);
                btn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(border, 1),
                    BorderFactory.createEmptyBorder(3, 10, 3, 10)
                ));
            }
            if (c instanceof JPanel p) p.setBackground(panel);
        }
        ((JPanel) mainPanel.getComponent(0)).getComponent(1).setBackground(panel);

        // Editor
        editorPanel.setBackground(panel);
        editorPanel.getComponent(0).setBackground(tabActive);
        ((JLabel) ((JPanel) editorPanel.getComponent(0)).getComponent(0)).setForeground(text);
        txtEditor.setBackground(editor);
        txtEditor.setForeground(text);
        txtEditor.setCaretColor(text);
        txtEditor.setSelectionColor(accent);
        txtLineNumbers.setBackground(lineNum);
        txtLineNumbers.setForeground(subtext);
        editorScroll.setBackground(editor);
        editorScroll.getViewport().setBackground(editor);
        editorScroll.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, border));

        // Tabs
        tabResults.setBackground(panel);
        tabResults.setForeground(text);
        for (int i = 0; i < tabResults.getTabCount(); i++) {
            Component tab = tabResults.getComponentAt(i);
            if (tab instanceof JScrollPane sp) {
                sp.setBackground(bg);
                sp.getViewport().setBackground(bg);
                JTextArea area = (JTextArea) sp.getViewport().getView();
                area.setBackground(bg);
                area.setForeground(text);
                area.setCaretColor(text);
            }
        }

        // Status bar
        statusBar.setBackground(accent);
        lblStatus.setForeground(Color.WHITE);
        lblCursor.setForeground(Color.WHITE);
        lblLang.setForeground(Color.WHITE);

        repaint();
        revalidate();
    }

    // -------------------------------------------------------
    // MAIN
    // -------------------------------------------------------
    public static void main(String[] args) {
        SwingUtilities.invokeLater(FrmPrincipal::new);
    }
}
