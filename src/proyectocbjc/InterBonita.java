/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package proyectocbjc;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author LENOVO
 */
public class InterBonita extends javax.swing.JFrame {
    java.util.List<String> codigosTabla = new java.util.ArrayList<>();
    DefaultTableModel modeloOriginal;
TableRowSorter<DefaultTableModel> sorter;

    /**
     * Creates new form InterBonita
     */
 private javax.swing.JButton botonActivo = null;      // submenú
private javax.swing.JButton botonMenuActivo = null; // menú principal
     private final EscribirCSV gestor = new EscribirCSV();
     
    private void configurarBuscador() {

      final String PLACEHOLDER = "Busca por codigo";
    jTexBuscaCod.setText(PLACEHOLDER);
    jTexBuscaCod.setForeground(java.awt.Color.GRAY);

    // modelo original
    modeloOriginal = gestor.obtenerModelo();
    jTable1.setModel(modeloOriginal);

    // sorter único
    sorter = new TableRowSorter<>(modeloOriginal);
    jTable1.setRowSorter(sorter);

    // placeholder
    jTexBuscaCod.addFocusListener(new java.awt.event.FocusAdapter() {
        @Override
        public void focusGained(java.awt.event.FocusEvent evt) {
            if (jTexBuscaCod.getText().equals(PLACEHOLDER)) {
                jTexBuscaCod.setText("");
                jTexBuscaCod.setForeground(java.awt.Color.BLACK);
            }
        }

        @Override
        public void focusLost(java.awt.event.FocusEvent evt) {
            if (jTexBuscaCod.getText().trim().isEmpty()) {
                jTexBuscaCod.setText(PLACEHOLDER);
                jTexBuscaCod.setForeground(java.awt.Color.GRAY);
                sorter.setRowFilter(null);
            }
        }
    });

    // 🔥 FILTRADO AUTOMÁTICO
    jTexBuscaCod.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
        public void insertUpdate(javax.swing.event.DocumentEvent e) {
            filtrarEnTiempoReal();
        }
        public void removeUpdate(javax.swing.event.DocumentEvent e) {
            filtrarEnTiempoReal();
        }
        public void changedUpdate(javax.swing.event.DocumentEvent e) {
            filtrarEnTiempoReal();
        }
    });
}
    
    
   public InterBonita() {
    initComponents();
    
 
    configurarSpinner();
    configurarTablaHistorial();
 
    jTextFNoMov.setEditable(false);
    jTextFFecha1.setEditable(false);
 
    LocalDate hoy = LocalDate.now();
    jTextFNoMov.setText(gestor.obtenerSiguienteMovimiento());
 
    DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    jTextFFecha1.setText(hoy.format(formato));
 
    configurarTablaMovimientos();
 
    // 🔥 SUBMENÚ MOVIMIENTOS
    configurarBotonNav(jButtonRegisM, () -> {
        mostrarPanelMovimiento(jButtonRegisM, jPanelRegistrarM);
    });
 
    configurarBotonNav(jButtonAjusteStock, () -> {
        mostrarPanelMovimiento(jButtonAjusteStock, jPanelStokA);
        cargarDatosStock("TODOS");
        marcarBotonStockActivo(jButtonProductosT);
    });
 
    // 🔥 AQUÍ ESTÁ EL CAMBIO
    configurarBotonNav(jButtonHistorialM, () -> {
        mostrarPanelMovimiento(jButtonHistorialM, jPanelHistorial);

        java.awt.EventQueue.invokeLater(() -> {
            jTexBuscaCodHistorial.requestFocusInWindow();
        });
    });
 
    // 🔹 Estilo botones submenú
    jButtonRegisM.setContentAreaFilled(false);
    jButtonRegisM.setBorderPainted(false);
    jButtonRegisM.setFocusPainted(false);
 
    jButtonAjusteStock.setContentAreaFilled(false);
    jButtonAjusteStock.setBorderPainted(false);
    jButtonAjusteStock.setFocusPainted(false);
 
    jButtonHistorialM.setContentAreaFilled(false);
    jButtonHistorialM.setBorderPainted(false);
    jButtonHistorialM.setFocusPainted(false);
 
    // 🔥 MENÚ PRINCIPAL
    configurarBotonMenu(btnInicio, () -> {
        jPanelCatalogo.setVisible(false);
        jPanelMovimientos.setVisible(false);
        jPanelInicio.setVisible(true);
    });
 
    configurarBotonMenu(btnCatalogo, () -> {
        jPanelInicio.setVisible(false);
        jPanelMovimientos.setVisible(false);
        jPanelCatalogo.setVisible(true);
    });
 
    configurarBotonMenu(btnMov, () -> {
        jPanelInicio.setVisible(false);
        jPanelCatalogo.setVisible(false);
        jPanelMovimientos.setVisible(true);
        mostrarPanelMovimiento(jButtonRegisM, jPanelRegistrarM);
    });
    configurarBotonMenu(btnMov1, () -> {
        jPanelInicio.setVisible(false);
        jPanelCatalogo.setVisible(false);
        jPanelMovimientos.setVisible(false);
       jPanel3.setVisible(true);
        
    });
 
    configurarBuscador();
    jBtnBuscar1.setVisible(false);
    configurarColumnas();
    configurarAlineacionTabla();
 
    configurarPanelStock();
 
    setLocationRelativeTo(null);
}
    private void configurarBotonNav(javax.swing.JButton boton, Runnable accion) {
    boton.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseEntered(java.awt.event.MouseEvent evt) {
            if (botonActivo != boton) {
                boton.setContentAreaFilled(true);
                boton.setOpaque(true);
                boton.setBackground(new java.awt.Color(232, 240, 254));
            }
        }
        @Override
        public void mouseExited(java.awt.event.MouseEvent evt) {
            if (botonActivo != boton) {
                boton.setContentAreaFilled(false);
                boton.setOpaque(false);
            }
        }
    });
    boton.addActionListener(evt -> {
        setBotonActivo(boton);
        accion.run();
    });
}
    private void configurarBotonMenu(javax.swing.JButton boton, Runnable accion) {

    boton.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseEntered(java.awt.event.MouseEvent evt) {
            if (botonMenuActivo != boton) {
                boton.setContentAreaFilled(true);
                boton.setOpaque(true);
                boton.setBackground(new java.awt.Color(232, 240, 254));
            }
        }

        @Override
        public void mouseExited(java.awt.event.MouseEvent evt) {
            if (botonMenuActivo != boton) {
                boton.setContentAreaFilled(false);
                boton.setOpaque(false);
            }
        }
    });

    boton.addActionListener(evt -> {
        setBotonMenuActivo(boton); // 🔥 ESTE ES EL CAMBIO IMPORTANTE
        accion.run();
    });
}
    public void actualizarTabla() {
    jTable1.setModel(gestor.obtenerModelo());
    configurarColumnas();
    configurarAlineacionTabla();
}
 private void configurarColumnas() {

    if (!(jTable1.getModel() instanceof DefaultTableModel)) return;

    DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();

    // 🔥 USAR EL MISMO SORTER GLOBAL (NO CREAR OTRO)
    sorter = new TableRowSorter<>(modelo);
    jTable1.setRowSorter(sorter);

    // Comparador numérico para columnas 3 a 8
    for (int i = 3; i <= 8; i++) {
        sorter.setComparator(i, (o1, o2) -> {
            try {
                double d1 = Double.parseDouble(
                        o1.toString().replace("$", "").replace(",", "").trim());
                double d2 = Double.parseDouble(
                        o2.toString().replace("$", "").replace(",", "").trim());
                return Double.compare(d1, d2);
            } catch (NumberFormatException e) {
                return o1.toString().compareTo(o2.toString());
            }
        });
    }

    // Anchos de columnas
    javax.swing.table.TableColumnModel columnas = jTable1.getColumnModel();
    int[] anchos = {90, 180, 180, 90, 110, 100, 100, 120, 120, 110};

    for (int i = 0; i < columnas.getColumnCount() && i < anchos.length; i++) {
        columnas.getColumn(i).setPreferredWidth(anchos[i]);
    }

    jTable1.getTableHeader().setReorderingAllowed(false);
    jTable1.getTableHeader().setResizingAllowed(true);
}
 private void configurarAlineacionTabla() {

    // Renderer normal alineado a la derecha (para columnas numéricas sin formato especial)
    javax.swing.table.DefaultTableCellRenderer derecha = new javax.swing.table.DefaultTableCellRenderer();
    derecha.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

    // Renderer especial con formato de dinero $ 0.00 para Costo(3) y Precio(4)
    javax.swing.table.DefaultTableCellRenderer dinero = new javax.swing.table.DefaultTableCellRenderer() {
        @Override
        public void setValue(Object value) {
            if (value != null) {
                try {
                    double d = Double.parseDouble(value.toString().replace("$", "").replace(",", "").trim());
                    setText(String.format("$ %,.2f", d));
                } catch (NumberFormatException e) {
                    setText(value.toString());
                }
            } else {
                setText("");
            }
            setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        }
    };

    // Aplicar renderer de dinero a Costo(3) y Precio(4)
    jTable1.getColumnModel().getColumn(3).setCellRenderer(dinero);
    jTable1.getColumnModel().getColumn(4).setCellRenderer(dinero);

    // Aplicar renderer derecha normal al resto de columnas numéricas
    int[] columnasDerecha = {5, 6, 7, 8};
    for (int i : columnasDerecha) {
        if (i < jTable1.getColumnModel().getColumnCount()) {
            jTable1.getColumnModel().getColumn(i).setCellRenderer(derecha);
        }
    }
}
    private void setBotonActivo(javax.swing.JButton boton) {
    // Desactivar el anterior
    if (botonActivo != null) {
        botonActivo.setContentAreaFilled(false);
        botonActivo.setOpaque(false);
        botonActivo.setBackground(new java.awt.Color(255, 255, 255));
    }
    // Activar el nuevo
    botonActivo = boton;
    boton.setContentAreaFilled(true);
    boton.setOpaque(true);
    boton.setBackground(new java.awt.Color(210, 227, 252)); // azul más oscuro que el hover
}
 private void mostrarPanelMovimiento(javax.swing.JButton boton, javax.swing.JPanel panel) {

    // 🔹 Pintar botón activo
    setBotonActivo(boton);

    // 🔹 Ocultar todos los paneles
    jPanelRegistrarM.setVisible(false);
    jPanelStokA.setVisible(false);
    jPanelHistorial.setVisible(false);

    // 🔹 Mostrar el correcto
    panel.setVisible(true);
}
 private void setBotonMenuActivo(javax.swing.JButton boton) {

    if (botonMenuActivo != null) {
        botonMenuActivo.setContentAreaFilled(false);
        botonMenuActivo.setOpaque(false);
        botonMenuActivo.setBackground(new java.awt.Color(255, 255, 255));
    }

    botonMenuActivo = boton;

    boton.setContentAreaFilled(true);
    boton.setOpaque(true);
    boton.setBackground(new java.awt.Color(210, 227, 252));
}
  private void configurarTablaMovimientos() {

    DefaultTableModel modelo = new DefaultTableModel(
        new String[]{"Cantidad", "Nombre"}, 0   // 🔥 AQUÍ EL CAMBIO
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    jTableMovimientos.setModel(modelo);

    // Tamaños
    jTableMovimientos.getColumnModel().getColumn(0).setPreferredWidth(100); // cantidad
    jTableMovimientos.getColumnModel().getColumn(1).setPreferredWidth(250); // nombre

    // 🔥 Alinear cantidad a la derecha
    javax.swing.table.DefaultTableCellRenderer derecha = new javax.swing.table.DefaultTableCellRenderer();
    derecha.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

    jTableMovimientos.getColumnModel().getColumn(0).setCellRenderer(derecha);
    jTableMovimientos.getColumnModel().getColumn(1).setCellRenderer(derecha);

    // Estilo
    jTableMovimientos.setRowHeight(28);
    jTableMovimientos.setFont(new java.awt.Font("Segoe UI", 0, 14));
    jTableMovimientos.getTableHeader().setFont(new java.awt.Font("Segoe UI", 1, 14));

    jTableMovimientos.setGridColor(new java.awt.Color(200, 200, 200)); // 🔥 líneas visibles
    jTableMovimientos.setShowVerticalLines(true); // 🔥 para que se dividan bien

    jTableMovimientos.getTableHeader().setBackground(new java.awt.Color(232, 240, 254));
}
 private void agregarProductoATabla(String nombre, int cantidad) {

    DefaultTableModel modelo = (DefaultTableModel) jTableMovimientos.getModel();

    for (int i = 0; i < modelo.getRowCount(); i++) {

        String nombreTabla = modelo.getValueAt(i, 1).toString(); // 🔥 columna 1 ahora

        if (nombreTabla.equalsIgnoreCase(nombre)) {

            int cantidadActual = Integer.parseInt(modelo.getValueAt(i, 0).toString()); // 🔥 columna 0
            modelo.setValueAt(cantidadActual + cantidad, i, 0);
            return;
        }
    }

    // 🔥 agregar nuevo
    modelo.addRow(new Object[]{cantidad, nombre});
}
     private void configurarBotonStock(javax.swing.JButton btn,
        java.awt.Color bgColor,      // ya no se usa, se ignora
        java.awt.Color borderColor,  // ya no se usa, se ignora
        java.awt.Color fgColor) {    // ya no se usa, se ignora
 
    // Fondo blanco limpio, igual que los demás paneles
    java.awt.Color bgNormal   = new java.awt.Color(255, 255, 255);
    java.awt.Color bgHover    = new java.awt.Color(248, 249, 250);
    java.awt.Color borderNorm = new java.awt.Color(218, 220, 224); // gris sutil
 
    btn.setOpaque(true);
    btn.setContentAreaFilled(true);
    btn.setFocusPainted(false);
    btn.setBackground(bgNormal);
    btn.setForeground(new java.awt.Color(60, 64, 67));             // mismo gris del menú
    btn.setBorder(javax.swing.BorderFactory.createLineBorder(borderNorm, 1));
    btn.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    btn.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
 
    // Guardar valores para marcarBotonStockActivo
    btn.putClientProperty("bgNormal",     bgNormal);
    btn.putClientProperty("bgHover",      bgHover);
    btn.putClientProperty("borderNormal", borderNorm);
 
    // Efecto hover: fondo gris muy claro, igual que el menú lateral
    btn.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseEntered(java.awt.event.MouseEvent e) {
            Object activo = btn.getClientProperty("esActivo");
            if (!Boolean.TRUE.equals(activo)) {
                btn.setBackground(bgHover);
                btn.setBorder(javax.swing.BorderFactory.createLineBorder(
                        new java.awt.Color(180, 182, 186), 1));
            }
        }
        @Override
        public void mouseExited(java.awt.event.MouseEvent e) {
            Object activo = btn.getClientProperty("esActivo");
            if (!Boolean.TRUE.equals(activo)) {
                btn.setBackground(bgNormal);
                btn.setBorder(javax.swing.BorderFactory.createLineBorder(borderNorm, 1));
            }
        }
    });
}
            private void marcarBotonStockActivo(javax.swing.JButton activo) {
 
    java.awt.Color azulActivo  = new java.awt.Color(232, 240, 254); // #E8F0FE
    java.awt.Color bordeActivo = new java.awt.Color(74, 134, 216);  // azul medio
    java.awt.Color bordeNorm   = new java.awt.Color(218, 220, 224); // gris sutil
    java.awt.Color bgNorm      = new java.awt.Color(255, 255, 255);
 
    for (javax.swing.JButton btn : new javax.swing.JButton[]{
            jButtonProductosT, jButtonProductosA,
            jButtonProductoB,  jButtonProductoS}) {
 
        if (btn == activo) {
            btn.setBackground(azulActivo);
            btn.setBorder(javax.swing.BorderFactory.createLineBorder(bordeActivo, 2));
            btn.putClientProperty("esActivo", Boolean.TRUE);
        } else {
            btn.setBackground(bgNorm);
            btn.setBorder(javax.swing.BorderFactory.createLineBorder(bordeNorm, 1));
            btn.putClientProperty("esActivo", Boolean.FALSE);
        }
    }
}
               private void cargarDatosStock(String filtro) {
    DefaultTableModel modelo = (DefaultTableModel) jTable2.getModel();
    modelo.setRowCount(0);
 
    int todos = 0, agotados = 0, bajo = 0, sobre = 0;
 
    try (BufferedReader br = new BufferedReader(new FileReader("inventario.csv"))) {
        String linea;
        while ((linea = br.readLine()) != null) {
            String[] d = linea.split(",");
            if (d.length == 10) {
                int stockActual = Integer.parseInt(d[5].trim());
                int stockMin    = Integer.parseInt(d[6].trim());
                todos++;
 
                String estadoStock;
                if (stockActual == 0) {
                    estadoStock = "AGOTADO"; agotados++;
                } else if (stockActual < stockMin) {
                    estadoStock = "STOCK BAJO"; bajo++;
                } else if (stockMin > 0 && stockActual > stockMin * 3) {
                    estadoStock = "SOBREINVENTARIO"; sobre++;
                } else {
                    estadoStock = "NORMAL";
                }
 
                boolean incluir = filtro.equals("TODOS")
                    || (filtro.equals("AGOTADO") && estadoStock.equals("AGOTADO"))
                    || (filtro.equals("BAJO")    && estadoStock.equals("STOCK BAJO"))
                    || (filtro.equals("SOBRE")   && estadoStock.equals("SOBREINVENTARIO"));
 
                if (incluir) {
                    modelo.addRow(new Object[]{
                        d[0], d[1],
                        gestor.codigoANombre(d[2]),
                        stockActual, stockMin, estadoStock
                    });
                }
            }
        }
    } catch (IOException e) { e.printStackTrace(); }
 
    // ── ÚNICO BLOQUE QUE CAMBIA: los setText de los 4 botones ──
 
    jButtonProductosT.setText(
        "<html><center>" +
        "<span style='font-size:18pt; font-weight:bold;'>" + todos + "</span>" +
        "<br><span style='font-size:8pt; color:#5F5E5A;'>Todos los productos</span>" +
        "</center></html>");
 
    jButtonProductosA.setText(
        "<html><center>" +
        "<span style='font-size:18pt; font-weight:bold; color:#A32D2D;'>" + agotados + "</span>" +
        "<br><span style='font-size:8pt; color:#5F5E5A;'>Agotados</span>" +
        "</center></html>");
 
    jButtonProductoB.setText(
        "<html><center>" +
        "<span style='font-size:18pt; font-weight:bold; color:#854F0B;'>" + bajo + "</span>" +
        "<br><span style='font-size:8pt; color:#5F5E5A;'>Stock bajo</span>" +
        "</center></html>");
 
    jButtonProductoS.setText(
        "<html><center>" +
        "<span style='font-size:18pt; font-weight:bold; color:#993556;'>" + sobre + "</span>" +
        "<br><span style='font-size:8pt; color:#5F5E5A;'>Sobreinventario</span>" +
        "</center></html>");
}
                private void configurarPanelStock() {

    // estilos de los 4 botones
    configurarBotonStock(jButtonProductosT,
        new java.awt.Color(241, 239, 232),
        new java.awt.Color(211, 209, 199),
        new java.awt.Color(44,  44,  42));
    configurarBotonStock(jButtonProductosA,
        new java.awt.Color(252, 235, 235),
        new java.awt.Color(247, 193, 193),
        new java.awt.Color(163,  45,  45));
    configurarBotonStock(jButtonProductoB,
        new java.awt.Color(250, 238, 218),
        new java.awt.Color(250, 199, 117),
        new java.awt.Color(133,  79,  11));
    configurarBotonStock(jButtonProductoS,
        new java.awt.Color(251, 234, 240),
        new java.awt.Color(244, 192, 209),
        new java.awt.Color(153,  53,  86));

    // configurar tabla jTable2
    DefaultTableModel modeloStock = new DefaultTableModel(
        new String[]{"Código", "Nombre", "Categoría", "Stock actual", "Stock mín.", "Estado"}, 0
    ) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };

    jTable2.setModel(modeloStock);
    jTable2.setRowHeight(26);
    jTable2.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
    jTable2.getTableHeader().setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
    jTable2.getTableHeader().setBackground(new java.awt.Color(232, 240, 254));
    jTable2.setGridColor(new java.awt.Color(220, 220, 220));
    jTable2.setShowVerticalLines(true);
    jTable2.getTableHeader().setReorderingAllowed(false);

    int[] anchos = {90, 200, 160, 95, 85, 110};
    for (int i = 0; i < anchos.length; i++)
        jTable2.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);

    // renderer colores por fila
    javax.swing.table.DefaultTableCellRenderer rendererColor =
        new javax.swing.table.DefaultTableCellRenderer() {
        @Override
        public java.awt.Component getTableCellRendererComponent(
                javax.swing.JTable t, Object v, boolean sel,
                boolean foc, int row, int col) {
            super.getTableCellRendererComponent(t, v, sel, foc, row, col);
            if (!sel) {
                Object estado = t.getModel().getValueAt(row, 5);
                if ("AGOTADO".equals(estado))
                    setBackground(new java.awt.Color(252, 235, 235));
                else if ("STOCK BAJO".equals(estado))
                    setBackground(new java.awt.Color(250, 238, 218));
                else if ("SOBREINVENTARIO".equals(estado))
                    setBackground(new java.awt.Color(251, 234, 240));
                else
                    setBackground(java.awt.Color.WHITE);
            }
            setHorizontalAlignment(col >= 3 && col <= 4
                ? javax.swing.SwingConstants.RIGHT
                : javax.swing.SwingConstants.LEFT);
            return this;
        }
    };
    for (int i = 0; i < 6; i++)
        jTable2.getColumnModel().getColumn(i).setCellRenderer(rendererColor);

    // acciones botones
    jButtonProductosT.addActionListener(e -> {
        cargarDatosStock("TODOS");
        marcarBotonStockActivo(jButtonProductosT);
    });
    jButtonProductosA.addActionListener(e -> {
        cargarDatosStock("AGOTADO");
        marcarBotonStockActivo(jButtonProductosA);
    });
    jButtonProductoB.addActionListener(e -> {
        cargarDatosStock("BAJO");
        marcarBotonStockActivo(jButtonProductoB);
    });
    jButtonProductoS.addActionListener(e -> {
        cargarDatosStock("SOBRE");
        marcarBotonStockActivo(jButtonProductoS);
    });

    // cargar datos iniciales
    cargarDatosStock("TODOS");
    marcarBotonStockActivo(jButtonProductosT);
    }
     
   private void configurarSpinner() {
    javax.swing.JTextField tf =
        ((javax.swing.JSpinner.DefaultEditor) jSpinner1.getEditor()).getTextField();
 
    // Enter → ejecuta Agregar
    tf.addActionListener(e -> jButtonAgregar.doClick());
}
   private void cargarHistorialPorCodigo(String codigo) {
    DefaultTableModel modelo = (DefaultTableModel) jTableHistorial.getModel();
    modelo.setRowCount(0);
    String codBusqueda = codigo.trim().toLowerCase();
    java.util.List<String[]> lista = new java.util.ArrayList<>();
    java.time.format.DateTimeFormatter formato = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");

    try {
        // 1. INVENTARIO EN MEMORIA
        java.util.Map<String, String> nombresProductos = new java.util.HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader("inventario.csv"))) {
            String l;
            while ((l = br.readLine()) != null) {
                String[] p = l.split(",");
                if (p.length >= 2)
                    nombresProductos.put(p[0].trim().toLowerCase(), p[1].trim());
            }
        }

        // 2. ENCABEZADO EN MEMORIA  <-- esto es el fix clave
        java.util.Map<String, String[]> encabezados = new java.util.HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader("encabezado.csv"))) {
            String l;
            while ((l = br.readLine()) != null) {
                // Limitar split a 4 partes por si el motivo tiene comas
                String[] m = l.split(",", 4);
                if (m.length >= 4)
                    encabezados.put(m[0].trim(), new String[]{m[1].trim(), m[2].trim(), m[3].trim()});
            }
        }

        // 3. LEER DETALLE Y CRUZAR
        try (BufferedReader br = new BufferedReader(new FileReader("detalle.csv"))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] d = linea.split(",");
                if (d.length < 3) continue;

                String noMov   = d[0].trim();
                String codArch = d[1].trim().toLowerCase();
                String cantidad = d[2].trim();

                // Debug temporal - bórralo cuando funcione
                System.out.println("Detalle -> noMov:[" + noMov + "] cod:[" + codArch + "] buscando:[" + codBusqueda + "] match:" + codArch.equals(codBusqueda));

                if (!codArch.equals(codBusqueda)) continue;

                String nombreProducto = nombresProductos.getOrDefault(codBusqueda, "Desconocido");
                String[] enc = encabezados.get(noMov);

                if (enc == null) {
                    System.out.println("Sin encabezado para noMov: [" + noMov + "]");
                    continue;
                }

                // enc[0]=fecha, enc[1]=tipo, enc[2]=motivo
                lista.add(new String[]{noMov, enc[0], enc[1], enc[2], nombreProducto, cantidad});
            }
        }

        // 4. ORDENAR
        lista.sort((a, b) -> {
            try {
                java.time.LocalDate f1 = java.time.LocalDate.parse(a[1], formato);
                java.time.LocalDate f2 = java.time.LocalDate.parse(b[1], formato);
                return f2.compareTo(f1);
            } catch (Exception e) {
                return 0;
            }
        });

        // 5. MOSTRAR
        for (String[] fila : lista) {
            modelo.addRow(fila);
        }

    } catch (IOException e) {
        e.printStackTrace();
        javax.swing.JOptionPane.showMessageDialog(this, "Error al leer archivos.");
    }
}
   private void configurarTablaHistorial() {

    DefaultTableModel modelo = new DefaultTableModel(
       new String[]{"No. Mov", "Fecha", "Tipo", "Motivo", "Producto", "Cantidad"}, 0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    jTableHistorial.setModel(modelo);

    jTableHistorial.setRowHeight(28);
    jTableHistorial.setFont(new java.awt.Font("Segoe UI", 0, 14));
    jTableHistorial.getTableHeader().setFont(new java.awt.Font("Segoe UI", 1, 14));

    // 🔥 alinear cantidad
      javax.swing.table.DefaultTableCellRenderer derecha = new javax.swing.table.DefaultTableCellRenderer();
    derecha.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

    jTableHistorial.getColumnModel().getColumn(5).setCellRenderer(derecha);
}
  
      
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        btnInicio = new javax.swing.JButton();
        btnCatalogo = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        JlTitulo = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        btnMov = new javax.swing.JButton();
        btnMov1 = new javax.swing.JButton();
        jPanelContenedor = new javax.swing.JPanel();
        jPanelInicio = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanelCatalogo = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jTexBuscaCod = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jBtnGuardar = new javax.swing.JButton();
        jBtnBuscar = new javax.swing.JButton();
        jBtnBuscar1 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jPanelMovimientos = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jButtonRegisM = new javax.swing.JButton();
        jButtonAjusteStock = new javax.swing.JButton();
        jButtonHistorialM = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        jPanelManejoMovimientos = new javax.swing.JPanel();
        jPanelStokA = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        jPanelBotonesStok = new javax.swing.JPanel();
        jButtonProductosT = new javax.swing.JButton();
        jButtonProductosA = new javax.swing.JButton();
        jButtonProductoB = new javax.swing.JButton();
        jButtonProductoS = new javax.swing.JButton();
        jPanelHistorial = new javax.swing.JPanel();
        jTexBuscaCodHistorial = new javax.swing.JTextField();
        jBtnBuscarHistorial = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTableHistorial = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();
        jPanelRegistrarM = new javax.swing.JPanel();
        jPanelEncabezado = new javax.swing.JPanel();
        jLabelFecha = new javax.swing.JLabel();
        jTextFNoMov = new javax.swing.JTextField();
        jLabelTipo = new javax.swing.JLabel();
        jComboBoxTipo = new javax.swing.JComboBox<>();
        jLabelNMov = new javax.swing.JLabel();
        jTextFFecha1 = new javax.swing.JTextField();
        jLabelMotivo = new javax.swing.JLabel();
        jTextFMotivo = new javax.swing.JTextField();
        jLabelCodig = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        jTextCodBuscar = new javax.swing.JTextField();
        jLabelNombreP = new javax.swing.JLabel();
        jTextFNombreProd = new javax.swing.JTextField();
        jLabelCantidad = new javax.swing.JLabel();
        jSpinner1 = new javax.swing.JSpinner();
        jButtonBuscaCodM = new javax.swing.JButton();
        jButtonAgregar = new javax.swing.JButton();
        jPaneldetalle = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableMovimientos = new javax.swing.JTable();
        jButtonGuardarMov = new javax.swing.JButton();
        jButtonEliminarDeT = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(240, 242, 245));
        setMinimumSize(new java.awt.Dimension(1366, 700));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setMaximumSize(new java.awt.Dimension(220, 728));
        jPanel1.setMinimumSize(new java.awt.Dimension(220, 728));
        jPanel1.setPreferredSize(new java.awt.Dimension(220, 728));

        btnInicio.setBackground(new java.awt.Color(255, 255, 255));
        btnInicio.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnInicio.setForeground(new java.awt.Color(60, 64, 67));
        btnInicio.setText("INICIO");
        btnInicio.setBorderPainted(false);
        btnInicio.setContentAreaFilled(false);
        btnInicio.setFocusPainted(false);
        btnInicio.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        btnInicio.setPreferredSize(new java.awt.Dimension(220, 50));
        btnInicio.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnInicioMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnInicioMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnInicioMouseExited(evt);
            }
        });
        btnInicio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInicioActionPerformed(evt);
            }
        });

        btnCatalogo.setBackground(new java.awt.Color(255, 255, 255));
        btnCatalogo.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnCatalogo.setForeground(new java.awt.Color(60, 64, 67));
        btnCatalogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/revista.png"))); // NOI18N
        btnCatalogo.setText("CATALOGO DE PRODUCTOS");
        btnCatalogo.setBorderPainted(false);
        btnCatalogo.setContentAreaFilled(false);
        btnCatalogo.setFocusPainted(false);
        btnCatalogo.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        btnCatalogo.setPreferredSize(new java.awt.Dimension(220, 50));
        btnCatalogo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCatalogoMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCatalogoMouseExited(evt);
            }
        });
        btnCatalogo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCatalogoActionPerformed(evt);
            }
        });

        JlTitulo.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        JlTitulo.setForeground(new java.awt.Color(0, 0, 0));
        JlTitulo.setText("Sistema de Inventario");

        jLabel1.setForeground(new java.awt.Color(153, 153, 153));
        jLabel1.setText("Gestión y Control");

        btnMov.setBackground(new java.awt.Color(255, 255, 255));
        btnMov.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnMov.setForeground(new java.awt.Color(60, 64, 67));
        btnMov.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/simplificado.png"))); // NOI18N
        btnMov.setText("MOVIMIENTOS INVENTARIO");
        btnMov.setBorderPainted(false);
        btnMov.setContentAreaFilled(false);
        btnMov.setFocusPainted(false);
        btnMov.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        btnMov.setPreferredSize(new java.awt.Dimension(220, 50));
        btnMov.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnMovMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnMovMouseExited(evt);
            }
        });
        btnMov.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMovActionPerformed(evt);
            }
        });

        btnMov1.setBackground(new java.awt.Color(255, 255, 255));
        btnMov1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnMov1.setForeground(new java.awt.Color(60, 64, 67));
        btnMov1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/simplificado.png"))); // NOI18N
        btnMov1.setText("MOVIMIENTOS INVENTARIO");
        btnMov1.setBorderPainted(false);
        btnMov1.setContentAreaFilled(false);
        btnMov1.setFocusPainted(false);
        btnMov1.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        btnMov1.setPreferredSize(new java.awt.Dimension(220, 50));
        btnMov1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnMov1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnMov1MouseExited(evt);
            }
        });
        btnMov1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMov1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnInicio, javax.swing.GroupLayout.DEFAULT_SIZE, 236, Short.MAX_VALUE)
            .addComponent(btnCatalogo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jSeparator1)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(JlTitulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnMov, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnMov1, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(JlTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addGap(32, 32, 32)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(btnInicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCatalogo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnMov, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnMov1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(359, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.LINE_START);

        jPanelContenedor.setBackground(new java.awt.Color(248, 249, 250));
        jPanelContenedor.setMaximumSize(new java.awt.Dimension(1146, 728));
        jPanelContenedor.setMinimumSize(new java.awt.Dimension(1146, 728));
        jPanelContenedor.setPreferredSize(new java.awt.Dimension(1146, 728));
        jPanelContenedor.setLayout(new java.awt.CardLayout());

        javax.swing.GroupLayout jPanelInicioLayout = new javax.swing.GroupLayout(jPanelInicio);
        jPanelInicio.setLayout(jPanelInicioLayout);
        jPanelInicioLayout.setHorizontalGroup(
            jPanelInicioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1146, Short.MAX_VALUE)
        );
        jPanelInicioLayout.setVerticalGroup(
            jPanelInicioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 728, Short.MAX_VALUE)
        );

        jPanelContenedor.add(jPanelInicio, "card2");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1146, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 728, Short.MAX_VALUE)
        );

        jPanelContenedor.add(jPanel2, "card4");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("Catálogo de Productos");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("Gestión completa de artículos e inventario");

        jButton1.setBackground(new java.awt.Color(0, 80, 150));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/agregar.png"))); // NOI18N
        jButton1.setText("Nuevo Producto");
        jButton1.setToolTipText("");
        jButton1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButton1.setOpaque(true);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTexBuscaCod.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTexBuscaCod.setForeground(new java.awt.Color(102, 102, 102));
        jTexBuscaCod.setText("Busca por codigo");
        jTexBuscaCod.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)), javax.swing.BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        jTexBuscaCod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTexBuscaCodActionPerformed(evt);
            }
        });

        jTable1.setBackground(new java.awt.Color(255, 255, 255));
        jTable1.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)), javax.swing.BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jBtnGuardar.setBackground(new java.awt.Color(225, 240, 255));
        jBtnGuardar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jBtnGuardar.setForeground(new java.awt.Color(0, 80, 150));
        jBtnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/editar.png"))); // NOI18N
        jBtnGuardar.setText("EDITAR");
        jBtnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnGuardarActionPerformed(evt);
            }
        });

        jBtnBuscar.setBackground(new java.awt.Color(225, 240, 255));
        jBtnBuscar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jBtnBuscar.setForeground(new java.awt.Color(0, 80, 150));
        jBtnBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/lupa.png"))); // NOI18N
        jBtnBuscar.setText("BUSCAR");
        jBtnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnBuscarActionPerformed(evt);
            }
        });

        jBtnBuscar1.setBackground(new java.awt.Color(240, 240, 240));
        jBtnBuscar1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jBtnBuscar1.setForeground(new java.awt.Color(100, 100, 100));
        jBtnBuscar1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/volver.png"))); // NOI18N
        jBtnBuscar1.setText("ATRAS");
        jBtnBuscar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnBuscar1ActionPerformed(evt);
            }
        });

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/compras-moviles.png"))); // NOI18N

        javax.swing.GroupLayout jPanelCatalogoLayout = new javax.swing.GroupLayout(jPanelCatalogo);
        jPanelCatalogo.setLayout(jPanelCatalogoLayout);
        jPanelCatalogoLayout.setHorizontalGroup(
            jPanelCatalogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCatalogoLayout.createSequentialGroup()
                .addGap(58, 58, 58)
                .addGroup(jPanelCatalogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTexBuscaCod)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1048, Short.MAX_VALUE)
                    .addGroup(jPanelCatalogoLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanelCatalogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jBtnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanelCatalogoLayout.createSequentialGroup()
                                .addComponent(jBtnBuscar1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jBtnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanelCatalogoLayout.createSequentialGroup()
                        .addGroup(jPanelCatalogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 391, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 424, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 376, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(40, 40, 40))
        );
        jPanelCatalogoLayout.setVerticalGroup(
            jPanelCatalogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCatalogoLayout.createSequentialGroup()
                .addGroup(jPanelCatalogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelCatalogoLayout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addGroup(jPanelCatalogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addGap(24, 24, 24))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelCatalogoLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel5)
                        .addGap(18, 18, 18)))
                .addComponent(jTexBuscaCod, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelCatalogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jBtnBuscar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jBtnBuscar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 343, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jBtnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(129, 129, 129))
        );

        jPanelContenedor.add(jPanelCatalogo, "card3");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("Movimientos de Inventario");
        jLabel3.setMaximumSize(new java.awt.Dimension(1026, 60));
        jLabel3.setMinimumSize(new java.awt.Dimension(1026, 60));
        jLabel3.setPreferredSize(new java.awt.Dimension(1026, 60));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(102, 102, 102));
        jLabel6.setText("Registro de entradas, salidas y ajustes de stock");

        jButtonRegisM.setBackground(new java.awt.Color(204, 204, 204));
        jButtonRegisM.setForeground(new java.awt.Color(60, 64, 67));
        jButtonRegisM.setText("Registrar Movimiento");
        jButtonRegisM.setAutoscrolls(true);
        jButtonRegisM.setMaximumSize(new java.awt.Dimension(342, 40));
        jButtonRegisM.setMinimumSize(new java.awt.Dimension(342, 40));
        jButtonRegisM.setPreferredSize(new java.awt.Dimension(342, 40));
        jButtonRegisM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRegisMActionPerformed(evt);
            }
        });

        jButtonAjusteStock.setText("Stock actual de productos");
        jButtonAjusteStock.setMaximumSize(new java.awt.Dimension(342, 40));
        jButtonAjusteStock.setMinimumSize(new java.awt.Dimension(342, 40));
        jButtonAjusteStock.setPreferredSize(new java.awt.Dimension(342, 40));
        jButtonAjusteStock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAjusteStockActionPerformed(evt);
            }
        });

        jButtonHistorialM.setText("Historial");
        jButtonHistorialM.setMaximumSize(new java.awt.Dimension(342, 40));
        jButtonHistorialM.setMinimumSize(new java.awt.Dimension(342, 40));
        jButtonHistorialM.setPreferredSize(new java.awt.Dimension(342, 40));
        jButtonHistorialM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonHistorialMActionPerformed(evt);
            }
        });

        jPanelManejoMovimientos.setMinimumSize(new java.awt.Dimension(1038, 420));
        jPanelManejoMovimientos.setPreferredSize(new java.awt.Dimension(1038, 420));
        jPanelManejoMovimientos.setLayout(new java.awt.CardLayout());

        jPanelStokA.setBackground(new java.awt.Color(255, 255, 255));

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(jTable2);

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setText("Stock actual de productos");
        jLabel7.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 10, 5, 10));

        jPanelBotonesStok.setPreferredSize(new java.awt.Dimension(547, 55));
        jPanelBotonesStok.setLayout(new java.awt.GridLayout(1, 0));

        jButtonProductosT.setText("jButton2");
        jPanelBotonesStok.add(jButtonProductosT);

        jButtonProductosA.setText("jButton3");
        jPanelBotonesStok.add(jButtonProductosA);

        jButtonProductoB.setText("jButton5");
        jPanelBotonesStok.add(jButtonProductoB);

        jButtonProductoS.setText("jButton4");
        jPanelBotonesStok.add(jButtonProductoS);

        javax.swing.GroupLayout jPanelStokALayout = new javax.swing.GroupLayout(jPanelStokA);
        jPanelStokA.setLayout(jPanelStokALayout);
        jPanelStokALayout.setHorizontalGroup(
            jPanelStokALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelBotonesStok, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1038, Short.MAX_VALUE)
            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane3)
        );
        jPanelStokALayout.setVerticalGroup(
            jPanelStokALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelStokALayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                .addComponent(jPanelBotonesStok, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanelManejoMovimientos.add(jPanelStokA, "card3");

        jPanelHistorial.setBackground(new java.awt.Color(255, 255, 255));

        jTexBuscaCodHistorial.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTexBuscaCodHistorial.setForeground(new java.awt.Color(102, 102, 102));
        jTexBuscaCodHistorial.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)), javax.swing.BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        jTexBuscaCodHistorial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTexBuscaCodHistorialActionPerformed(evt);
            }
        });

        jBtnBuscarHistorial.setBackground(new java.awt.Color(225, 240, 255));
        jBtnBuscarHistorial.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jBtnBuscarHistorial.setForeground(new java.awt.Color(0, 80, 150));
        jBtnBuscarHistorial.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/lupa.png"))); // NOI18N
        jBtnBuscarHistorial.setText("BUSCAR");
        jBtnBuscarHistorial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnBuscarHistorialActionPerformed(evt);
            }
        });

        jTableHistorial.setBackground(new java.awt.Color(255, 255, 255));
        jTableHistorial.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)), javax.swing.BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        jTableHistorial.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane4.setViewportView(jTableHistorial);

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(51, 51, 51));
        jLabel8.setText("Ingrese Codigo del Producto");

        javax.swing.GroupLayout jPanelHistorialLayout = new javax.swing.GroupLayout(jPanelHistorial);
        jPanelHistorial.setLayout(jPanelHistorialLayout);
        jPanelHistorialLayout.setHorizontalGroup(
            jPanelHistorialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelHistorialLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelHistorialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelHistorialLayout.createSequentialGroup()
                        .addGroup(jPanelHistorialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanelHistorialLayout.createSequentialGroup()
                                .addComponent(jTexBuscaCodHistorial, javax.swing.GroupLayout.PREFERRED_SIZE, 893, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jBtnBuscarHistorial, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        jPanelHistorialLayout.setVerticalGroup(
            jPanelHistorialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelHistorialLayout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelHistorialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTexBuscaCodHistorial, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBtnBuscarHistorial, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 336, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanelManejoMovimientos.add(jPanelHistorial, "card4");

        jPanelRegistrarM.setBackground(new java.awt.Color(255, 255, 255));
        jPanelRegistrarM.setLayout(new java.awt.GridLayout(1, 0));

        jPanelEncabezado.setForeground(new java.awt.Color(0, 0, 0));

        jLabelFecha.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabelFecha.setForeground(new java.awt.Color(0, 0, 0));
        jLabelFecha.setText("Fecha");

        jTextFNoMov.setEditable(false);
        jTextFNoMov.setBackground(new java.awt.Color(255, 255, 255));
        jTextFNoMov.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextFNoMov.setForeground(new java.awt.Color(0, 0, 0));

        jLabelTipo.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabelTipo.setForeground(new java.awt.Color(0, 0, 0));
        jLabelTipo.setText("Tipo");

        jComboBoxTipo.setBackground(new java.awt.Color(255, 255, 255));
        jComboBoxTipo.setForeground(new java.awt.Color(0, 0, 0));
        jComboBoxTipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ENTRADA", "SALIDA", "AJUSTE", " " }));

        jLabelNMov.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabelNMov.setForeground(new java.awt.Color(0, 0, 0));
        jLabelNMov.setText("No. Movimiento");

        jTextFFecha1.setBackground(new java.awt.Color(255, 255, 255));
        jTextFFecha1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextFFecha1.setForeground(new java.awt.Color(0, 0, 0));
        jTextFFecha1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFFecha1ActionPerformed(evt);
            }
        });

        jLabelMotivo.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabelMotivo.setForeground(new java.awt.Color(0, 0, 0));
        jLabelMotivo.setText("Motivo");

        jTextFMotivo.setBackground(new java.awt.Color(255, 255, 255));
        jTextFMotivo.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextFMotivo.setForeground(new java.awt.Color(0, 0, 0));

        jLabelCodig.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabelCodig.setForeground(new java.awt.Color(0, 0, 0));
        jLabelCodig.setText("Codigo");

        jTextCodBuscar.setBackground(new java.awt.Color(255, 255, 255));
        jTextCodBuscar.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextCodBuscar.setForeground(new java.awt.Color(0, 0, 0));
        jTextCodBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextCodBuscarActionPerformed(evt);
            }
        });

        jLabelNombreP.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabelNombreP.setForeground(new java.awt.Color(0, 0, 0));
        jLabelNombreP.setText("Nombre Producto");

        jTextFNombreProd.setEditable(false);
        jTextFNombreProd.setBackground(new java.awt.Color(255, 255, 255));
        jTextFNombreProd.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextFNombreProd.setForeground(new java.awt.Color(0, 0, 0));

        jLabelCantidad.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabelCantidad.setForeground(new java.awt.Color(0, 0, 0));
        jLabelCantidad.setText("Cantidad");

        jButtonBuscaCodM.setBackground(new java.awt.Color(255, 255, 255));
        jButtonBuscaCodM.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonBuscaCodM.setForeground(new java.awt.Color(66, 66, 66));
        jButtonBuscaCodM.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/lupa.png"))); // NOI18N
        jButtonBuscaCodM.setText("Buscar");
        jButtonBuscaCodM.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(218, 220, 224)));
        jButtonBuscaCodM.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonBuscaCodM.setFocusPainted(false);
        jButtonBuscaCodM.setIconTextGap(8);
        jButtonBuscaCodM.setPreferredSize(new java.awt.Dimension(110, 35));
        jButtonBuscaCodM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBuscaCodMActionPerformed(evt);
            }
        });

        jButtonAgregar.setBackground(new java.awt.Color(255, 255, 255));
        jButtonAgregar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonAgregar.setForeground(new java.awt.Color(66, 66, 66));
        jButtonAgregar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/flecha-correcta.png"))); // NOI18N
        jButtonAgregar.setText("Agregar");
        jButtonAgregar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(218, 220, 224)));
        jButtonAgregar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonAgregar.setFocusPainted(false);
        jButtonAgregar.setIconTextGap(8);
        jButtonAgregar.setPreferredSize(new java.awt.Dimension(110, 35));
        jButtonAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAgregarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelEncabezadoLayout = new javax.swing.GroupLayout(jPanelEncabezado);
        jPanelEncabezado.setLayout(jPanelEncabezadoLayout);
        jPanelEncabezadoLayout.setHorizontalGroup(
            jPanelEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator3)
            .addGroup(jPanelEncabezadoLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanelEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelEncabezadoLayout.createSequentialGroup()
                        .addGroup(jPanelEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelFecha)
                            .addComponent(jTextFFecha1, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(28, 28, 28)
                        .addGroup(jPanelEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBoxTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelTipo))
                        .addGap(18, 18, 18)
                        .addGroup(jPanelEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelEncabezadoLayout.createSequentialGroup()
                                .addComponent(jLabelNMov)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanelEncabezadoLayout.createSequentialGroup()
                                .addComponent(jTextFNoMov, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(jPanelEncabezadoLayout.createSequentialGroup()
                        .addGroup(jPanelEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelMotivo)
                            .addComponent(jTextFMotivo, javax.swing.GroupLayout.PREFERRED_SIZE, 474, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanelEncabezadoLayout.createSequentialGroup()
                        .addGroup(jPanelEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButtonBuscaCodM, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabelCodig)
                            .addComponent(jTextCodBuscar, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE))
                        .addGroup(jPanelEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelEncabezadoLayout.createSequentialGroup()
                                .addGap(23, 23, 23)
                                .addGroup(jPanelEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextFNombreProd, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabelNombreP))
                                .addGroup(jPanelEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanelEncabezadoLayout.createSequentialGroup()
                                        .addGap(34, 34, 34)
                                        .addComponent(jLabelCantidad))
                                    .addGroup(jPanelEncabezadoLayout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelEncabezadoLayout.createSequentialGroup()
                                .addGap(223, 223, 223)
                                .addComponent(jButtonAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(28, Short.MAX_VALUE))))
        );
        jPanelEncabezadoLayout.setVerticalGroup(
            jPanelEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelEncabezadoLayout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(jPanelEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelFecha)
                    .addComponent(jLabelTipo)
                    .addComponent(jLabelNMov))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jTextFFecha1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComboBoxTipo, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFNoMov, javax.swing.GroupLayout.Alignment.LEADING))
                .addGap(31, 31, 31)
                .addComponent(jLabelMotivo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFMotivo, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
                .addGroup(jPanelEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelCodig)
                    .addComponent(jLabelNombreP)
                    .addComponent(jLabelCantidad))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSpinner1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanelEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextCodBuscar)
                        .addComponent(jTextFNombreProd)))
                .addGap(18, 18, 18)
                .addGroup(jPanelEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonBuscaCodM, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(54, 54, 54))
        );

        jPanelRegistrarM.add(jPanelEncabezado);

        jTableMovimientos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Title 1", "Title 2"
            }
        ));
        jScrollPane2.setViewportView(jTableMovimientos);

        jButtonGuardarMov.setBackground(new java.awt.Color(25, 118, 210));
        jButtonGuardarMov.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButtonGuardarMov.setForeground(new java.awt.Color(255, 255, 255));
        jButtonGuardarMov.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/disquete.png"))); // NOI18N
        jButtonGuardarMov.setText("Guardar");
        jButtonGuardarMov.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(25, 118, 210)));
        jButtonGuardarMov.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonGuardarMov.setFocusPainted(false);
        jButtonGuardarMov.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jButtonGuardarMov.setIconTextGap(12);
        jButtonGuardarMov.setPreferredSize(new java.awt.Dimension(140, 45));
        jButtonGuardarMov.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGuardarMovActionPerformed(evt);
            }
        });

        jButtonEliminarDeT.setBackground(new java.awt.Color(220, 53, 69));
        jButtonEliminarDeT.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButtonEliminarDeT.setForeground(new java.awt.Color(255, 255, 255));
        jButtonEliminarDeT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/eliminar.png"))); // NOI18N
        jButtonEliminarDeT.setText("Eliminar");
        jButtonEliminarDeT.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(25, 118, 210)));
        jButtonEliminarDeT.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonEliminarDeT.setFocusPainted(false);
        jButtonEliminarDeT.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jButtonEliminarDeT.setIconTextGap(12);
        jButtonEliminarDeT.setPreferredSize(new java.awt.Dimension(140, 45));
        jButtonEliminarDeT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEliminarDeTActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPaneldetalleLayout = new javax.swing.GroupLayout(jPaneldetalle);
        jPaneldetalle.setLayout(jPaneldetalleLayout);
        jPaneldetalleLayout.setHorizontalGroup(
            jPaneldetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPaneldetalleLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPaneldetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPaneldetalleLayout.createSequentialGroup()
                        .addComponent(jButtonEliminarDeT, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonGuardarMov, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(45, Short.MAX_VALUE))
        );
        jPaneldetalleLayout.setVerticalGroup(
            jPaneldetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPaneldetalleLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addGroup(jPaneldetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonGuardarMov, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonEliminarDeT, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(56, 56, 56))
        );

        jPanelRegistrarM.add(jPaneldetalle);

        jPanelManejoMovimientos.add(jPanelRegistrarM, "card4");

        javax.swing.GroupLayout jPanelMovimientosLayout = new javax.swing.GroupLayout(jPanelMovimientos);
        jPanelMovimientos.setLayout(jPanelMovimientosLayout);
        jPanelMovimientosLayout.setHorizontalGroup(
            jPanelMovimientosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelMovimientosLayout.createSequentialGroup()
                .addGap(58, 58, 58)
                .addGroup(jPanelMovimientosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanelManejoMovimientos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator2)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 371, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanelMovimientosLayout.createSequentialGroup()
                        .addComponent(jButtonRegisM, javax.swing.GroupLayout.PREFERRED_SIZE, 342, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonAjusteStock, javax.swing.GroupLayout.PREFERRED_SIZE, 342, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonHistorialM, javax.swing.GroupLayout.PREFERRED_SIZE, 342, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanelMovimientosLayout.setVerticalGroup(
            jPanelMovimientosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelMovimientosLayout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelMovimientosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonRegisM, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonAjusteStock, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonHistorialM, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanelManejoMovimientos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(92, Short.MAX_VALUE))
        );

        jPanelContenedor.add(jPanelMovimientos, "card5");

        getContentPane().add(jPanelContenedor, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnInicioMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnInicioMouseEntered
        btnInicio.setContentAreaFilled(true);
    btnInicio.setBackground(new java.awt.Color(232, 240, 254));
    btnInicio.setOpaque(true);
    }//GEN-LAST:event_btnInicioMouseEntered

    private void btnInicioMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnInicioMouseExited
          if (botonActivo != btnInicio) {
        btnInicio.setContentAreaFilled(false);
        btnInicio.setOpaque(false);
    }
    }//GEN-LAST:event_btnInicioMouseExited

    private void btnInicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInicioActionPerformed
        // TODO add your handling code here:
         setBotonActivo(btnInicio);
        jPanelCatalogo.setVisible(false);
        jPanelInicio.setVisible(true);
       
        
    }//GEN-LAST:event_btnInicioActionPerformed

    private void btnCatalogoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCatalogoMouseEntered
        // TODO add your handling code here:
        btnCatalogo.setContentAreaFilled(true);
        // Azul claro (el mismo de la imagen que enviaste)
        btnCatalogo.setBackground(new java.awt.Color(232, 240, 254)); 
        btnCatalogo.setOpaque(true);
    }//GEN-LAST:event_btnCatalogoMouseEntered

    private void btnCatalogoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCatalogoMouseExited
        // TODO add your handling code here:
        
        if (botonActivo != btnCatalogo) {
        btnCatalogo.setContentAreaFilled(false);
        btnCatalogo.setOpaque(false);
    }
    }//GEN-LAST:event_btnCatalogoMouseExited

    private void btnCatalogoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCatalogoActionPerformed
        // TODO add your handling code here:
        setBotonActivo(btnCatalogo);
        jPanelInicio.setVisible(false);
        jPanelCatalogo.setVisible(true);
        
    }//GEN-LAST:event_btnCatalogoActionPerformed

    private void btnInicioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnInicioMouseClicked
        // TODO add your handling code here:
        
    }//GEN-LAST:event_btnInicioMouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        Formu dialog = new Formu(this, true);
    
    // Centrarlo automáticamente
    dialog.setLocationRelativeTo(null); 
    
    // ¡Hacerlo visible!
    dialog.setVisible(true);
      
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jBtnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnGuardarActionPerformed
        int filaVista = jTable1.getSelectedRow();

    if (filaVista >= 0) {
        // ✅ Convierte índice visual a índice real del modelo
        int filaModelo = jTable1.convertRowIndexToModel(filaVista);

        String[] datos = new String[10];
        for (int i = 0; i < 10; i++) {
            Object valor = jTable1.getModel().getValueAt(filaModelo, i);
            datos[i] = (valor != null) ? valor.toString() : "";
        }

        Formu dialog = new Formu(this, true, datos);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);

    } else {
        javax.swing.JOptionPane.showMessageDialog(this,
            "Por favor, seleccione un producto de la tabla para editarlo.",
            "Error",
            javax.swing.JOptionPane.WARNING_MESSAGE);
    }
    }//GEN-LAST:event_jBtnGuardarActionPerformed

    private void jBtnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnBuscarActionPerformed
   final String PLACEHOLDER = "Busca por codigo";
    String textoBusqueda = jTexBuscaCod.getText().trim();

    if (textoBusqueda.isEmpty() || textoBusqueda.equalsIgnoreCase(PLACEHOLDER)) {

        jTable1.setModel(modeloOriginal);

        sorter = new TableRowSorter<>(modeloOriginal);
        jTable1.setRowSorter(sorter);

        configurarColumnas();
        configurarAlineacionTabla();
        jBtnBuscar1.setVisible(false);
        return;
    }

    DefaultTableModel modeloBuscado = gestor.buscarPorFiltro(textoBusqueda);

    if (modeloBuscado.getRowCount() > 0) {
        jTable1.setModel(modeloBuscado);

        sorter = new TableRowSorter<>(modeloBuscado);
        jTable1.setRowSorter(sorter);

        configurarColumnas();
        configurarAlineacionTabla();

        jTable1.setRowSelectionInterval(0, 0);
        jBtnBuscar1.setVisible(true);

    } else {
        javax.swing.JOptionPane.showMessageDialog(this,
                "No se encontraron resultados para: " + textoBusqueda);

        jTable1.setModel(modeloOriginal);

        sorter = new TableRowSorter<>(modeloOriginal);
        jTable1.setRowSorter(sorter);

        configurarColumnas();
        configurarAlineacionTabla();

        jBtnBuscar1.setVisible(false);
    }
    }//GEN-LAST:event_jBtnBuscarActionPerformed
private void limpiarMovimiento() {
    DefaultTableModel modelo = (DefaultTableModel) jTableMovimientos.getModel();
    modelo.setRowCount(0);
    codigosTabla.clear();

    jTextFMotivo.setText("");
    jTextCodBuscar.setText("");
    jTextFNombreProd.setText("");
    jSpinner1.setValue(1);

    jTextFNoMov.setText(gestor.obtenerSiguienteMovimiento());
}
    private void jBtnBuscar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnBuscar1ActionPerformed
     jTable1.setModel(gestor.obtenerModelo());

    configurarColumnas();        // 🔥 FALTABA
    configurarAlineacionTabla();

    jBtnBuscar1.setVisible(false);
    }//GEN-LAST:event_jBtnBuscar1ActionPerformed

    private void jTexBuscaCodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTexBuscaCodActionPerformed
        // TODO add your handling code here:
        jBtnBuscar.doClick();
    }//GEN-LAST:event_jTexBuscaCodActionPerformed

    private void btnMovMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMovMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_btnMovMouseEntered

    private void btnMovMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMovMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_btnMovMouseExited

    private void btnMovActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMovActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnMovActionPerformed

    private void jButtonRegisMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRegisMActionPerformed
        // TODO add your handling code here:
        jPanelHistorial.setVisible(false);
      jPanelStokA.setVisible(false);
        jPanelRegistrarM.setVisible(true);
        
    }//GEN-LAST:event_jButtonRegisMActionPerformed

    private void jButtonAjusteStockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAjusteStockActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonAjusteStockActionPerformed

    private void jButtonHistorialMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonHistorialMActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonHistorialMActionPerformed

    private void jTextFFecha1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFFecha1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFFecha1ActionPerformed

    private void jButtonBuscaCodMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBuscaCodMActionPerformed
   String codigo = jTextCodBuscar.getText().trim();
 
    if (codigo.isEmpty()) {
        javax.swing.JOptionPane.showMessageDialog(this, "El código es obligatorio");
        jTextCodBuscar.requestFocus();
        return;
    }
 
    boolean encontrado = false;
 
    try (BufferedReader br = new BufferedReader(new FileReader("inventario.csv"))) {
        String linea;
        while ((linea = br.readLine()) != null) {
            String[] datos = linea.split(",");
            if (datos.length >= 2 && datos[0].equalsIgnoreCase(codigo)) {
                jTextFNombreProd.setText(datos[1]);
                jSpinner1.setValue(1);
                encontrado = true;
 
                javax.swing.JTextField tf =
                    ((javax.swing.JSpinner.DefaultEditor) jSpinner1.getEditor()).getTextField();
 
                javax.swing.SwingUtilities.invokeLater(() -> {
                    tf.requestFocusInWindow();
                    // Forzar que el texto esté "editado" para que Enter siempre dispare
                    tf.setText("1");
                    tf.selectAll();
                });
 
                break;
            }
        }
    } catch (IOException e) {
        javax.swing.JOptionPane.showMessageDialog(this,
            "Error al leer el archivo: " + e.getMessage());
    }
 
    if (!encontrado) {
        javax.swing.JOptionPane.showMessageDialog(this, "Producto no encontrado");
        jTextFNombreProd.setText("");
        jTextCodBuscar.requestFocus();
        jTextCodBuscar.selectAll();
    }
    }//GEN-LAST:event_jButtonBuscaCodMActionPerformed

    private void jButtonAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAgregarActionPerformed
      
    String codigo = jTextCodBuscar.getText().trim();
    String nombre = jTextFNombreProd.getText().trim();
    int cantidadNueva = (int) jSpinner1.getValue();

    // 🔴 VALIDAR CANTIDAD (PRIMERO)
    if (cantidadNueva <= 0) {
        javax.swing.JOptionPane.showMessageDialog(this,
            "La cantidad debe ser mayor a 0");
        return;
    }

    if (codigo.isEmpty()) {
        javax.swing.JOptionPane.showMessageDialog(this, "Busca un producto primero");
        jTextCodBuscar.requestFocus();
        return;
    }

    if (nombre.isEmpty()) {
        javax.swing.JOptionPane.showMessageDialog(this, "Busca un producto primero");
        return;
    }

    // 🔴 VALIDAR STOCK SUFICIENTE EN SALIDA
    String tipo = jComboBoxTipo.getSelectedItem().toString().trim();
    if (tipo.equalsIgnoreCase("SALIDA")) {

        int[] datos = gestor.obtenerStockYEstado(codigo);
        int stockActual = datos[0];

        DefaultTableModel modeloCheck = (DefaultTableModel) jTableMovimientos.getModel();
        int cantidadYaEnTabla = 0;

        for (int i = 0; i < modeloCheck.getRowCount(); i++) {
            if (codigosTabla.get(i).equalsIgnoreCase(codigo)) {
                cantidadYaEnTabla = Integer.parseInt(modeloCheck.getValueAt(i, 0).toString());
                break;
            }
        }

        int totalSalida = cantidadYaEnTabla + cantidadNueva;

        if (totalSalida > stockActual) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "Stock insuficiente para \"" + nombre + "\".\n" +
                "Stock actual: " + stockActual + "   Cantidad solicitada: " + totalSalida,
                "Stock insuficiente",
                javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }
    }

    DefaultTableModel modelo = (DefaultTableModel) jTableMovimientos.getModel();
    boolean encontrado = false;

    for (int i = 0; i < modelo.getRowCount(); i++) {
        if (codigosTabla.get(i).equalsIgnoreCase(codigo)) {
            int cantidadActual = Integer.parseInt(modelo.getValueAt(i, 0).toString());
            modelo.setValueAt(cantidadActual + cantidadNueva, i, 0);
            encontrado = true;
            break;
        }
    }

    if (!encontrado) {
        modelo.addRow(new Object[]{cantidadNueva, nombre});
        codigosTabla.add(codigo);
    }

    // 🔥 limpiar
    jTextCodBuscar.setText("");
    jTextFNombreProd.setText("");
    jSpinner1.setValue(1);
    jTextCodBuscar.requestFocus();
    }//GEN-LAST:event_jButtonAgregarActionPerformed

    private void jButtonGuardarMovActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGuardarMovActionPerformed
         DefaultTableModel modelo = (DefaultTableModel) jTableMovimientos.getModel();

    if (modelo.getRowCount() == 0) {
        javax.swing.JOptionPane.showMessageDialog(this, "No hay productos en la tabla");
        return;
    }

    String motivo = jTextFMotivo.getText().trim();
    String tipo = jComboBoxTipo.getSelectedItem().toString().trim();

    // 🔴 MOTIVO SOLO EN AJUSTE
    if (tipo.equalsIgnoreCase("AJUSTE") && motivo.isEmpty()) {
        javax.swing.JOptionPane.showMessageDialog(this,
            "El motivo es obligatorio para movimientos de tipo AJUSTE.",
            "Campo requerido",
            javax.swing.JOptionPane.WARNING_MESSAGE);
        jTextFMotivo.requestFocus();
        return;
    }

    // 🔴 VALIDACIONES POR PRODUCTO
    for (int i = 0; i < modelo.getRowCount(); i++) {
        int cantidad = Integer.parseInt(modelo.getValueAt(i, 0).toString());
        String nombre = modelo.getValueAt(i, 1).toString();
        String codigo = codigosTabla.get(i);

        if (cantidad <= 0) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "La cantidad para \"" + nombre + "\" debe ser mayor a 0.",
                "Cantidad inválida",
                javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        int[] datos = gestor.obtenerStockYEstado(codigo);
        int stockActual = datos[0];
        int stockMin    = datos[1];
        int estado      = datos[2];

        if (estado == 0 && !tipo.equalsIgnoreCase("AJUSTE")) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "El producto \"" + nombre + "\" está deshabilitado.\n" +
                "Solo se permite movimiento de tipo AJUSTE.",
                "Producto deshabilitado",
                javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (tipo.equalsIgnoreCase("SALIDA") && cantidad > stockActual) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "Stock insuficiente para \"" + nombre + "\".\n" +
                "Stock actual: " + stockActual + "   Cantidad solicitada: " + cantidad,
                "Stock insuficiente",
                javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (tipo.equalsIgnoreCase("ENTRADA")) {
            int stockResultante = stockActual + cantidad;
            if (stockMin > 0 && stockResultante > stockMin * 3) {
                int opcionAdv = javax.swing.JOptionPane.showConfirmDialog(this,
                    "Advertencia: \"" + nombre + "\"\n" +
                    "El stock resultante (" + stockResultante + ") superará 3 veces\n" +
                    "el stock mínimo (" + stockMin + ").\n\n" +
                    "¿Deseas continuar de todas formas?",
                    "Sobreinventario",
                    javax.swing.JOptionPane.YES_NO_OPTION,
                    javax.swing.JOptionPane.WARNING_MESSAGE);
                if (opcionAdv != javax.swing.JOptionPane.YES_OPTION) return;
            }
        }
    }

    int opcion = javax.swing.JOptionPane.showConfirmDialog(
        this,
        "¿Seguro que deseas guardar el movimiento?",
        "Confirmar",
        javax.swing.JOptionPane.YES_NO_OPTION
    );
    if (opcion != javax.swing.JOptionPane.YES_OPTION) return;

    String noMov = jTextFNoMov.getText();
    String fecha = jTextFFecha1.getText();

    try {

        // 🔹 VALIDAR CÓDIGOS EXISTENTES
        if (!gestor.todosLosCodigosExisten(codigosTabla)) {
            javax.swing.JOptionPane.showMessageDialog(this, "Hay códigos inválidos en la tabla");
            return;
        }

        if (!gestor.validarDetalles(codigosTabla, modelo)) {
            javax.swing.JOptionPane.showMessageDialog(this, "Datos inválidos en la tabla");
            return;
        }

        // 🔹 PREPARAR DETALLES EN MEMORIA
        java.util.List<String> detallesTemp = new java.util.ArrayList<>();

        for (int i = 0; i < modelo.getRowCount(); i++) {
            int cantidad = Integer.parseInt(modelo.getValueAt(i, 0).toString());
            String codigo = codigosTabla.get(i);

            detallesTemp.add(noMov + "," + codigo + "," + cantidad);
        }

        // 🔹 GUARDAR TODO JUNTO (SIN DESINCRONIZAR)
        gestor.guardarEncabezado(noMov, fecha, tipo, motivo);
        gestor.guardarDetalleLote(detallesTemp);

        // 🔹 ACTUALIZAR STOCK
        for (int i = 0; i < modelo.getRowCount(); i++) {
            int cantidad = Integer.parseInt(modelo.getValueAt(i, 0).toString());
            String codigo = codigosTabla.get(i);

            if (tipo.equalsIgnoreCase("ENTRADA")) {
                gestor.actualizarStock(codigo, cantidad);
            } else if (tipo.equalsIgnoreCase("SALIDA")) {
                gestor.actualizarStock(codigo, -cantidad);
            } else if (tipo.equalsIgnoreCase("AJUSTE")) {
                gestor.reemplazarStock(codigo, cantidad);
            }
        }

        actualizarTabla();
        javax.swing.JOptionPane.showMessageDialog(this, "Movimiento guardado correctamente");
        limpiarMovimiento();

    } catch (Exception e) {
        e.printStackTrace();
        javax.swing.JOptionPane.showMessageDialog(this, "Error al guardar");
    }

    }//GEN-LAST:event_jButtonGuardarMovActionPerformed

    private void jTextCodBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextCodBuscarActionPerformed
        // TODO add your handling code here:
        jButtonBuscaCodM.doClick();
    }//GEN-LAST:event_jTextCodBuscarActionPerformed

    private void jTexBuscaCodHistorialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTexBuscaCodHistorialActionPerformed
        jBtnBuscarHistorial.doClick();
    }//GEN-LAST:event_jTexBuscaCodHistorialActionPerformed

    private void jBtnBuscarHistorialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnBuscarHistorialActionPerformed
        String codigo = jTexBuscaCodHistorial.getText().trim();

    if (codigo.isEmpty() || codigo.equalsIgnoreCase("Busca por codigo")) {
        javax.swing.JOptionPane.showMessageDialog(this,
            "Ingresa un código de producto",
            "Campo requerido",
            javax.swing.JOptionPane.WARNING_MESSAGE);

        jTexBuscaCodHistorial.requestFocusInWindow();
        return;
    }

    DefaultTableModel modelo = (DefaultTableModel) jTableHistorial.getModel();
    modelo.setRowCount(0);

    cargarHistorialPorCodigo(codigo);

    if (modelo.getRowCount() == 0) {
        javax.swing.JOptionPane.showMessageDialog(this,
            "No hay historial para el código: " + codigo);

        jTexBuscaCodHistorial.requestFocusInWindow();
        jTexBuscaCodHistorial.selectAll();
        
    }
     jTexBuscaCodHistorial.setText("");
    }//GEN-LAST:event_jBtnBuscarHistorialActionPerformed

    private void jButtonEliminarDeTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEliminarDeTActionPerformed
        int fila = jTableMovimientos.getSelectedRow();

    if (fila == -1) {
        javax.swing.JOptionPane.showMessageDialog(this, "Selecciona un producto para eliminar");
        return;
    }

    int opcion = javax.swing.JOptionPane.showConfirmDialog(
        this,
        "¿Deseas eliminar el producto seleccionado?",
        "Confirmar eliminación",
        javax.swing.JOptionPane.YES_NO_OPTION
    );

    if (opcion != javax.swing.JOptionPane.YES_OPTION) return;

    DefaultTableModel modelo = (DefaultTableModel) jTableMovimientos.getModel();
    modelo.removeRow(fila);

    codigosTabla.remove(fila);
    }//GEN-LAST:event_jButtonEliminarDeTActionPerformed

    private void btnMov1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMov1MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_btnMov1MouseEntered

    private void btnMov1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMov1MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_btnMov1MouseExited

    private void btnMov1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMov1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnMov1ActionPerformed
private void filtrarEnTiempoReal() {
    String texto = jTexBuscaCod.getText();

    if (texto.trim().isEmpty() || texto.equals("Busca por codigo")) {
        sorter.setRowFilter(null);
    } else {
        sorter.setRowFilter(javax.swing.RowFilter.regexFilter("(?i)" + texto));
    }
}
    /**
     * @param args the command line arguments
     */
   
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(InterBonita.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(InterBonita.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(InterBonita.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(InterBonita.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new InterBonita().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel JlTitulo;
    private javax.swing.JButton btnCatalogo;
    private javax.swing.JButton btnInicio;
    private javax.swing.JButton btnMov;
    private javax.swing.JButton btnMov1;
    private javax.swing.JButton jBtnBuscar;
    private javax.swing.JButton jBtnBuscar1;
    private javax.swing.JButton jBtnBuscarHistorial;
    private javax.swing.JButton jBtnGuardar;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButtonAgregar;
    private javax.swing.JButton jButtonAjusteStock;
    private javax.swing.JButton jButtonBuscaCodM;
    private javax.swing.JButton jButtonEliminarDeT;
    private javax.swing.JButton jButtonGuardarMov;
    private javax.swing.JButton jButtonHistorialM;
    private javax.swing.JButton jButtonProductoB;
    private javax.swing.JButton jButtonProductoS;
    private javax.swing.JButton jButtonProductosA;
    private javax.swing.JButton jButtonProductosT;
    private javax.swing.JButton jButtonRegisM;
    private javax.swing.JComboBox<String> jComboBoxTipo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabelCantidad;
    private javax.swing.JLabel jLabelCodig;
    private javax.swing.JLabel jLabelFecha;
    private javax.swing.JLabel jLabelMotivo;
    private javax.swing.JLabel jLabelNMov;
    private javax.swing.JLabel jLabelNombreP;
    private javax.swing.JLabel jLabelTipo;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanelBotonesStok;
    private javax.swing.JPanel jPanelCatalogo;
    private javax.swing.JPanel jPanelContenedor;
    private javax.swing.JPanel jPanelEncabezado;
    private javax.swing.JPanel jPanelHistorial;
    private javax.swing.JPanel jPanelInicio;
    private javax.swing.JPanel jPanelManejoMovimientos;
    private javax.swing.JPanel jPanelMovimientos;
    private javax.swing.JPanel jPanelRegistrarM;
    private javax.swing.JPanel jPanelStokA;
    private javax.swing.JPanel jPaneldetalle;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTableHistorial;
    private javax.swing.JTable jTableMovimientos;
    private javax.swing.JTextField jTexBuscaCod;
    private javax.swing.JTextField jTexBuscaCodHistorial;
    private javax.swing.JTextField jTextCodBuscar;
    private javax.swing.JTextField jTextFFecha1;
    private javax.swing.JTextField jTextFMotivo;
    private javax.swing.JTextField jTextFNoMov;
    private javax.swing.JTextField jTextFNombreProd;
    // End of variables declaration//GEN-END:variables
}
