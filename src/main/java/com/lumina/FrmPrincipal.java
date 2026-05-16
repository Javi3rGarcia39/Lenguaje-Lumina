package com.lumina;

import com.lumina.error.ErrorHandler;
import com.lumina.lexer.Lexer;
import com.lumina.parser.Parser;
import java.awt.*;
import java.io.*;
import java.nio.file.*;
import java_cup.runtime.Symbol;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class FrmPrincipal extends JFrame {

    // -------------------------------------------------------
    // COLORES -- tema oscuro
    // -------------------------------------------------------
    private static final Color DARK_BG = new Color(30, 30, 30);
    private static final Color DARK_PANEL = new Color(37, 37, 38);
    private static final Color DARK_SIDEBAR = new Color(51, 51, 51);
    private static final Color DARK_EDITOR = new Color(30, 30, 30);
    private static final Color DARK_LINE_NUM = new Color(51, 51, 51);
    private static final Color DARK_TEXT = new Color(212, 212, 212);
    private static final Color DARK_SUBTEXT = new Color(153, 153, 153);
    private static final Color DARK_ACCENT = new Color(0, 122, 204);
    private static final Color DARK_SUCCESS = new Color(78, 201, 176);
    private static final Color DARK_ERROR = new Color(244, 135, 113);
    private static final Color DARK_WARNING = new Color(220, 220, 170);
    private static final Color DARK_BORDER = new Color(60, 60, 60);
    private static final Color DARK_BUTTON = new Color(14, 99, 156);
    private static final Color DARK_TAB_ACTIVE = new Color(30, 30, 30);
    private static final Color DARK_TAB_INACTIVE = new Color(45, 45, 45);

    // -------------------------------------------------------
    // COLORES -- tema claro
    // -------------------------------------------------------
    private static final Color LIGHT_BG = new Color(255, 255, 255);
    private static final Color LIGHT_PANEL = new Color(243, 243, 243);
    private static final Color LIGHT_SIDEBAR = new Color(248, 248, 248);
    private static final Color LIGHT_EDITOR = new Color(255, 255, 255);
    private static final Color LIGHT_LINE_NUM = new Color(237, 237, 237);
    private static final Color LIGHT_TEXT = new Color(30, 30, 30);
    private static final Color LIGHT_SUBTEXT = new Color(100, 100, 100);
    private static final Color LIGHT_ACCENT = new Color(0, 122, 204);
    private static final Color LIGHT_SUCCESS = new Color(0, 128, 0);
    private static final Color LIGHT_ERROR = new Color(205, 49, 49);
    private static final Color LIGHT_WARNING = new Color(128, 128, 0);
    private static final Color LIGHT_BORDER = new Color(220, 220, 220);
    private static final Color LIGHT_BUTTON = new Color(0, 122, 204);
    private static final Color LIGHT_TAB_ACTIVE = new Color(255, 255, 255);
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
    private JTable      tblTokens;
    private JTable      tblErrors;
    private JTable      tblSymbols;
    private JTextArea   txtConsole;
    private JLabel      lblTokenCount;

    // Barra de estado
    private JPanel statusBar;
    private JLabel lblStatus;
    private JLabel lblCursor;
    private JLabel lblLang;
    private JPanel toolbar;

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
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {
        }

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
        toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        toolbar.setPreferredSize(new Dimension(0, 42));

        btnOpen = toolButton("Abrir", "Abrir archivo .lum o .txt");
        btnSave = toolButton("Guardar", "Guardar archivo actual");
        btnAnalyze = toolButton("Analizar", "Ejecutar analisis lexico y sintactico");
        btnClear = toolButton("Limpiar", "Limpiar todos los paneles");

        btnOpen.addActionListener(e -> openFile());
        btnSave.addActionListener(e -> saveFile());
        btnAnalyze.addActionListener(e -> analyze());
        btnClear.addActionListener(e -> clearAll());

        toolbar.add(btnOpen);
        toolbar.add(btnSave);
        toolbar.add(btnAnalyze);
        toolbar.add(btnClear);

        JPanel north = new JPanel(new BorderLayout());
        north.add(titleBar, BorderLayout.NORTH);
        north.add(toolbar, BorderLayout.SOUTH);
        mainPanel.add(north, BorderLayout.NORTH);
    }

    private JButton toolButton(String text, String tooltip) {
        JButton btn = new JButton(text);
        btn.setToolTipText(tooltip);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(true);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(100, 28));
        btn.setBackground(new Color(14, 99, 156));
        btn.setForeground(Color.WHITE);
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
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                updateLineNumbers();
                updateStatus();
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                updateLineNumbers();
                updateStatus();
            }

            public void changedUpdate(javax.swing.event.DocumentEvent e) {
            }
        });

        txtEditor.addCaretListener(e -> updateCursor());

        editorScroll = new JScrollPane(txtEditor);
        editorScroll.setRowHeaderView(txtLineNumbers);
        editorScroll.setBorder(null);
        editorScroll.getVerticalScrollBar().setUnitIncrement(16);

        // Sincronizar scroll de numeros
        editorScroll.getViewport().addChangeListener(e
                -> txtLineNumbers.repaint()
        );

        editorPanel.add(editorHeader, BorderLayout.NORTH);
        editorPanel.add(editorScroll, BorderLayout.CENTER);

        // Panel derecho: resultados
        tabResults = new JTabbedPane();
        tabResults.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabResults.setTabPlacement(JTabbedPane.TOP);

        // --- Tab Tokens ---
        tblTokens = makeTable(new String[]{"#", "TIPO", "VALOR", "LINEA", "COL"});
        setColumnWidths(tblTokens, new int[]{35, 150, 120, 55, 55});
        JPanel tokensPanel = new JPanel(new BorderLayout());
        tokensPanel.add(new JScrollPane(tblTokens), BorderLayout.CENTER);
        lblTokenCount = new JLabel("  Total: 0 tokens");
        lblTokenCount.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblTokenCount.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));
        tokensPanel.add(lblTokenCount, BorderLayout.SOUTH);

        // --- Tab Errores ---
        tblErrors = makeTable(new String[]{"TIPO", "LINEA", "COL", "MENSAJE"});
        setColumnWidths(tblErrors, new int[]{80, 55, 55, 400});

        // --- Tab Tabla Simbolos ---
        tblSymbols = makeTable(new String[]{"NOMBRE", "TIPO", "CLASE", "LINEA", "COL", "INIT"});
        setColumnWidths(tblSymbols, new int[]{120, 80, 80, 55, 55, 60});

        // --- Tab Consola ---
        txtConsole = new JTextArea();
        txtConsole.setFont(new Font("JetBrains Mono", Font.PLAIN, 12));
        txtConsole.setEditable(false);
        txtConsole.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));

        tabResults.addTab("Tokens",         tokensPanel);
        tabResults.addTab("Errores",        new JScrollPane(tblErrors));
        tabResults.addTab("Tabla Simbolos", new JScrollPane(tblSymbols));
        tabResults.addTab("Consola",        new JScrollPane(txtConsole));

        split.setLeftComponent(editorPanel);
        split.setRightComponent(tabResults);

        mainPanel.add(split, BorderLayout.CENTER);
    }

    private JTable makeTable(String[] columns) {
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);
        table.setFont(new Font("JetBrains Mono", Font.PLAIN, 12));
        table.setRowHeight(26);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFillsViewportHeight(true);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setPreferredSize(new Dimension(0, 30));
        table.getTableHeader().setReorderingAllowed(false);
        table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        return table;
    }

    private void setColumnWidths(JTable table, int[] widths) {
        for (int i = 0; i < widths.length && i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }
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
            if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
                return;
            }
            String path = chooser.getSelectedFile().getAbsolutePath();
            if (!path.endsWith(".lum")) {
                path += ".lum";
            }
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

        StringBuilder consoleOut = new StringBuilder();

        consoleOut.append("=== LUMINA COMPILER ===\n\n");

        java.util.List<Symbol> tokenList = new java.util.ArrayList<>();
        try {
            Lexer lexerTokens = new Lexer(new java.io.StringReader(source));
            lexerTokens.setErrorHandler(errorHandler);
            Symbol tok;
            while (true) {
                tok = lexerTokens.next_token();
                if (tok == null || tok.sym == com.lumina.parser.sym.EOF) {
                    break;
                }
                tokenList.add(tok);
            }
        } catch (Exception ex) {
            consoleOut.append("Error en fase lexica: ").append(ex.getMessage()).append("\n");
        }

        // Capturar stderr de CUP -- contiene mensajes de error sintactico
        // que CUP imprime directamente aunque no lance excepcion
        java.io.ByteArrayOutputStream cupErrBytes = new java.io.ByteArrayOutputStream();
        java.io.PrintStream cupErrStream = new java.io.PrintStream(cupErrBytes);
        java.io.PrintStream originalErr = System.err;
        System.setErr(cupErrStream);

        try {
            Lexer lexerParser = new Lexer(new java.io.StringReader(source));
            lexerParser.setErrorHandler(errorHandler);
            Parser parser = new Parser(lexerParser);
            parser.setErrorHandler(errorHandler);
            parser.parse();
            consoleOut.append("Analisis sintactico completado.\n");
        } catch (Exception ex) {
            String msg = ex.getMessage();
            if (msg != null && !msg.isBlank()) {
                consoleOut.append("Error sintactico: ").append(msg).append("\n");
            }
            // Si CUP lanzo excepcion pero report_error no la registro (errorHandler vacio),
            // registrar un error generico con la info disponible
            if (!errorHandler.hasErrors()) {
                errorHandler.syntacticError(
                    msg != null ? msg : "Error de parseo inesperado", 0, 0);
            }
        } finally {
            System.setErr(originalErr);
            // Si CUP imprimio algo en stderr sin lanzar excepcion ni llamar report_error
            String cupOutput = cupErrBytes.toString().trim();
            if (!cupOutput.isEmpty() && !errorHandler.hasErrors()) {
                errorHandler.syntacticError("Error sintactico detectado por el parser", 0, 0);
                consoleOut.append("Salida del parser: ").append(cupOutput).append("\n");
            }
        }

        // -- Poblar tabla Tokens --
        DefaultTableModel tokenModel = (DefaultTableModel) tblTokens.getModel();
        tokenModel.setRowCount(0);
        int i = 1;
        for (Symbol t : tokenList) {
            String typeName = getTokenName(t.sym);
            String value = t.value != null ? t.value.toString() : "";
            tokenModel.addRow(new Object[]{i++, typeName, value, t.left, t.right});
        }
        lblTokenCount.setText("  Total: " + tokenList.size() + " tokens");

        // -- Poblar tabla Errores --
        DefaultTableModel errorModel = (DefaultTableModel) tblErrors.getModel();
        errorModel.setRowCount(0);
        if (errorHandler.hasErrors()) {
            for (ErrorHandler.LuminaError e : errorHandler.getErrors()) {
                errorModel.addRow(new Object[]{e.type.toString(), e.line, e.column, e.message});
            }
            consoleOut.append("\nSe encontraron ")
                    .append(errorHandler.errorCount()).append(" error(es).\n");
            setStatus("Analisis con " + errorHandler.errorCount() + " error(es).", true);
            tabResults.setSelectedIndex(1);
        } else {
            consoleOut.append("\n✓ Compilacion exitosa. Sin errores.\n");
            setStatus("Analisis completado sin errores.", false);
            tabResults.setSelectedIndex(0);
        }

        // -- Poblar tabla Simbolos --
        DefaultTableModel symbolModel = (DefaultTableModel) tblSymbols.getModel();
        symbolModel.setRowCount(0);
        // Se poblara con acciones semanticas

        txtConsole.setText(consoleOut.toString());
        txtConsole.setCaretPosition(0);
    }

    private String getTokenName(int sym) {
        switch (sym) {
            case com.lumina.parser.sym.FUN:
                return "FUN";
            case com.lumina.parser.sym.VAR:
                return "VAR";
            case com.lumina.parser.sym.RETURN:
                return "RETURN";
            case com.lumina.parser.sym.IF:
                return "IF";
            case com.lumina.parser.sym.ELSE:
                return "ELSE";
            case com.lumina.parser.sym.WHILE:
                return "WHILE";
            case com.lumina.parser.sym.FOR:
                return "FOR";
            case com.lumina.parser.sym.PRINT:
                return "PRINT";
            case com.lumina.parser.sym.AND:
                return "AND";
            case com.lumina.parser.sym.OR:
                return "OR";
            case com.lumina.parser.sym.NOT:
                return "NOT";
            case com.lumina.parser.sym.NULL:
                return "NULL";
            case com.lumina.parser.sym.TYPE_INT:
                return "TYPE_INT";
            case com.lumina.parser.sym.TYPE_FLOAT:
                return "TYPE_FLOAT";
            case com.lumina.parser.sym.TYPE_BOOL:
                return "TYPE_BOOL";
            case com.lumina.parser.sym.TYPE_STRING:
                return "TYPE_STRING";
            case com.lumina.parser.sym.INT_LITERAL:
                return "INT_LITERAL";
            case com.lumina.parser.sym.FLOAT_LITERAL:
                return "FLOAT_LITERAL";
            case com.lumina.parser.sym.BOOL_LITERAL:
                return "BOOL_LITERAL";
            case com.lumina.parser.sym.STRING_LITERAL:
                return "STRING_LITERAL";
            case com.lumina.parser.sym.IDENTIFIER:
                return "IDENTIFIER";
            case com.lumina.parser.sym.PLUS:
                return "PLUS";
            case com.lumina.parser.sym.MINUS:
                return "MINUS";
            case com.lumina.parser.sym.STAR:
                return "STAR";
            case com.lumina.parser.sym.SLASH:
                return "SLASH";
            case com.lumina.parser.sym.PERCENT:
                return "PERCENT";
            case com.lumina.parser.sym.EQUAL:
                return "EQUAL";
            case com.lumina.parser.sym.EQUAL_EQUAL:
                return "EQUAL_EQUAL";
            case com.lumina.parser.sym.BANG_EQUAL:
                return "BANG_EQUAL";
            case com.lumina.parser.sym.LESS:
                return "LESS";
            case com.lumina.parser.sym.LESS_EQUAL:
                return "LESS_EQUAL";
            case com.lumina.parser.sym.GREATER:
                return "GREATER";
            case com.lumina.parser.sym.GREATER_EQUAL:
                return "GREATER_EQUAL";
            case com.lumina.parser.sym.PLUS_EQUAL:
                return "PLUS_EQUAL";
            case com.lumina.parser.sym.MINUS_EQUAL:
                return "MINUS_EQUAL";
            case com.lumina.parser.sym.STAR_EQUAL:
                return "STAR_EQUAL";
            case com.lumina.parser.sym.SLASH_EQUAL:
                return "SLASH_EQUAL";
            case com.lumina.parser.sym.ARROW:
                return "ARROW";
            case com.lumina.parser.sym.LEFT_PAREN:
                return "LEFT_PAREN";
            case com.lumina.parser.sym.RIGHT_PAREN:
                return "RIGHT_PAREN";
            case com.lumina.parser.sym.LEFT_BRACE:
                return "LEFT_BRACE";
            case com.lumina.parser.sym.RIGHT_BRACE:
                return "RIGHT_BRACE";
            case com.lumina.parser.sym.LEFT_BRACKET:
                return "LEFT_BRACKET";
            case com.lumina.parser.sym.RIGHT_BRACKET:
                return "RIGHT_BRACKET";
            case com.lumina.parser.sym.COMMA:
                return "COMMA";
            case com.lumina.parser.sym.SEMICOLON:
                return "SEMICOLON";
            case com.lumina.parser.sym.COLON:
                return "COLON";
            case com.lumina.parser.sym.DOT:
                return "DOT";
            case com.lumina.parser.sym.UMINUS:
                return "UMINUS";
            default:
                return "UNKNOWN(" + sym + ")";
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
        ((DefaultTableModel) tblTokens.getModel()).setRowCount(0);
        ((DefaultTableModel) tblErrors.getModel()).setRowCount(0);
        ((DefaultTableModel) tblSymbols.getModel()).setRowCount(0);
        lblTokenCount.setText("  Total: 0 tokens");
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
            int pos = txtEditor.getCaretPosition();
            int line = txtEditor.getLineOfOffset(pos) + 1;
            int col = pos - txtEditor.getLineStartOffset(line - 1) + 1;
            lblCursor.setText("Ln " + line + ", Col " + col);
        } catch (Exception ignored) {
        }
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
        Color bg = darkMode ? DARK_BG : LIGHT_BG;
        Color panel = darkMode ? DARK_PANEL : LIGHT_PANEL;
        Color sidebar = darkMode ? DARK_SIDEBAR : LIGHT_SIDEBAR;
        Color editor = darkMode ? DARK_EDITOR : LIGHT_EDITOR;
        Color lineNum = darkMode ? DARK_LINE_NUM : LIGHT_LINE_NUM;
        Color text = darkMode ? DARK_TEXT : LIGHT_TEXT;
        Color subtext = darkMode ? DARK_SUBTEXT : LIGHT_SUBTEXT;
        Color accent = darkMode ? DARK_ACCENT : LIGHT_ACCENT;
        Color border = darkMode ? DARK_BORDER : LIGHT_BORDER;
        Color button = darkMode ? DARK_BUTTON : LIGHT_BUTTON;
        Color tabActive = darkMode ? DARK_TAB_ACTIVE : LIGHT_TAB_ACTIVE;
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
        toolbar.setBackground(panel);
        for (Component c : toolbar.getComponents()) {
            if (c instanceof JButton btn) {
                btn.setBackground(button);
                btn.setForeground(Color.WHITE);
                btn.setOpaque(true);
                btn.setContentAreaFilled(true);
                btn.setBorderPainted(false);
            }
        }

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
        tabResults.setBackground(bg);
        tabResults.setForeground(text);
        UIManager.put("TabbedPane.selected",  panel);
        UIManager.put("TabbedPane.background", bg);
        UIManager.put("TabbedPane.foreground", text);
        tabResults.updateUI();

        // Estilizar las tres JTables
        for (JTable tbl : new JTable[]{tblTokens, tblErrors, tblSymbols}) {
            tbl.setBackground(bg);
            tbl.setForeground(text);
            tbl.setSelectionBackground(accent);
            tbl.setSelectionForeground(Color.WHITE);
            tbl.setGridColor(border);
            JTableHeader header = tbl.getTableHeader();
            header.setBackground(panel);
            header.setForeground(text);
            header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, accent));

            // Renderer para columnas de texto (izquierda, con padding)
            DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer() {
                { setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 4)); }
            };
            leftRenderer.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
            leftRenderer.setBackground(bg);
            leftRenderer.setForeground(text);

            // Renderer para columnas numericas (derecha, con padding)
            DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer() {
                { setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 10)); }
            };
            rightRenderer.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            rightRenderer.setBackground(bg);
            rightRenderer.setForeground(text);

            for (int col = 0; col < tbl.getColumnCount(); col++) {
                String colName = tbl.getColumnName(col);
                boolean isNumeric = colName.equals("#") || colName.equals("LINEA") || colName.equals("COL");
                tbl.getColumnModel().getColumn(col).setCellRenderer(
                    isNumeric ? rightRenderer : leftRenderer);
            }
            // Scroll panes que envuelven las tablas
            if (tbl.getParent() != null && tbl.getParent().getParent() instanceof JScrollPane sp) {
                sp.setBackground(bg);
                sp.getViewport().setBackground(bg);
                sp.setBorder(BorderFactory.createEmptyBorder());
            }
        }

        // tokensPanel (JPanel que envuelve tblTokens + lblTokenCount)
        lblTokenCount.setBackground(panel);
        lblTokenCount.setForeground(subtext);
        if (tblTokens.getParent() != null
                && tblTokens.getParent().getParent() instanceof JScrollPane sp) {
            sp.setBackground(bg);
            sp.getViewport().setBackground(bg);
            sp.setBorder(BorderFactory.createEmptyBorder());
        }

        // Consola (JTextArea)
        if (txtConsole.getParent() != null
                && txtConsole.getParent().getParent() instanceof JScrollPane sp) {
            sp.setBackground(bg);
            sp.getViewport().setBackground(bg);
            sp.setBorder(BorderFactory.createEmptyBorder());
        }
        txtConsole.setBackground(bg);
        txtConsole.setForeground(text);
        txtConsole.setCaretColor(text);

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