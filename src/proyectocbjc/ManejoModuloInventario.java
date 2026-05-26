/*
 * Módulo de Análisis de Inventario
 * Corregido: carga automática del historial, ABC correcto, indicadores completos
 */
package proyectocbjc;
 
import java.awt.Color;
import java.awt.Font;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.event.*;
import java.io.*;
import java.text.*;
import java.util.*;
 
public class ManejoModuloInventario {
     private javax.swing.JPanel jPanelAnalGrafica;
    private final EscribirCSV gestor = new EscribirCSV();
    private JTextField jTexFiltroCod;
    private JTextField jTexFiltroNom;
    private JComboBox<String> jComboFilttoCat;
    private JTable jTableProductosA;
    private JTable jTableHistorialSalidas;
    private JLabel jLabelConsumoTotal;
    private JLabel jLabelBajoStock;
    private JLabel jLabelReorden;
    private JLabel jLabelSobreInventario;
 
    private DefaultTableModel modeloProductos;
    private DefaultTableModel modeloHistorial;
    private TableRowSorter<DefaultTableModel> sorterProductos;
 
    // Datos en memoria
    private List<String[]> productosData = new ArrayList<>();
    private Map<String, String> grupoABC = new HashMap<>();
    private Map<String, Double> eoqMap = new HashMap<>();
    private Map<String, Double> puntoReordenMap = new HashMap<>();
    private Map<String, Double> costoMap = new HashMap<>();
    private Map<String, String> estadoMap = new HashMap<>();
    private Set<String> productosUnicos = new HashSet<>();
 
    private static final String RUTA_INVENTARIO = "inventario.csv";
    private static final String RUTA_ENCABEZADO = "encabezado.csv";
    private static final String RUTA_DETALLE    = "detalle.csv";
    private static final DecimalFormat FMT_MONEDA = new DecimalFormat("$ #,##0.00");
    private static final DecimalFormat FMT_MONEDA_SIMPLE = new DecimalFormat("#,##0.00");
 
    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------
    public ManejoModuloInventario(
        JTextField jTexFiltroCod,
        JTextField jTexFiltroNom,
        JComboBox<String> jComboFilttoCat,
        JTable jTableProductosA,
        JTable jTableHistorialSalidas,
        JLabel jLabelConsumoTotal,
        JLabel jLabelBajoStock,
        JLabel jLabelReorden,
        JLabel jLabelSobreInventario,
        javax.swing.JPanel jPanelAnalGrafica) {

    this.jTexFiltroCod          = jTexFiltroCod;
    this.jTexFiltroNom          = jTexFiltroNom;
    this.jComboFilttoCat        = jComboFilttoCat;
    this.jTableProductosA       = jTableProductosA;
    this.jTableHistorialSalidas = jTableHistorialSalidas;
    this.jLabelConsumoTotal     = jLabelConsumoTotal;
    this.jLabelBajoStock        = jLabelBajoStock;
    this.jLabelReorden          = jLabelReorden;
    this.jLabelSobreInventario  = jLabelSobreInventario;
    this.jPanelAnalGrafica      = jPanelAnalGrafica;

    configurarLabelsForzado();
}
    
    private void configurarLabelsForzado() {
        JLabel[] labels = {jLabelConsumoTotal, jLabelBajoStock, jLabelReorden, jLabelSobreInventario};
        for (JLabel label : labels) {
            label.setOpaque(true);
            label.setForeground(Color.WHITE);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setVerticalAlignment(SwingConstants.CENTER);
            label.setFont(new Font("Segoe UI", Font.BOLD, 12));
            label.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(10, 5, 10, 5)
            ));
        }
    }
 
    private Color getColor(String tipo, int valor) {
        switch (tipo) {
            case "CONSUMO":
                return new Color(33, 150, 243);
            case "BAJO_STOCK":
                if (valor == 0) return new Color(76, 175, 80);
                else if (valor <= 3) return new Color(255, 193, 7);
                else return new Color(244, 67, 54);
            case "REORDEN":
                if (valor == 0) return new Color(76, 175, 80);
                else if (valor <= 3) return new Color(255, 152, 0);
                else return new Color(244, 67, 54);
            case "SOBREINVENTARIO":
                if (valor == 0) return new Color(76, 175, 80);
                else if (valor <= 3) return new Color(255, 193, 7);
                else return new Color(244, 67, 54);
            default:
                return Color.GRAY;
        }
    }
    
    private String evaluarEstado(int stockActual, int stockMinimo, double puntoReorden) {
    // ✅ PRIMERO verificar punto de reorden (incluye stock=0)
    if (puntoReorden > 0 && stockActual <= puntoReorden) {
        return "PUNTO REORDEN";
    }
    // Luego stock bajo (ya sabemos que stock > puntoReorden si llegamos aquí)
    if (stockMinimo > 0 && stockActual < stockMinimo) {
        return "STOCK BAJO";
    }
    // Sobreinventario
    if (stockMinimo > 0 && stockActual > stockMinimo * 3) {
        return "SOBREINVENTARIO";
    }
    return "NORMAL";
}
    
    private void configurarAlineacionTabla() {
    DefaultTableCellRenderer derecha = new DefaultTableCellRenderer();
    derecha.setHorizontalAlignment(SwingConstants.RIGHT);

    for (int col : new int[]{4, 5, 6, 7}) {
        if (col < jTableProductosA.getColumnCount())
            jTableProductosA.getColumnModel().getColumn(col).setCellRenderer(derecha);
    }

    // Columna Estado (col 8) con colores
    if (jTableProductosA.getColumnCount() > 8) {
        jTableProductosA.getColumnModel().getColumn(8).setCellRenderer(
            new DefaultTableCellRenderer() {
                @Override
                public java.awt.Component getTableCellRendererComponent(
                        JTable table, Object value, boolean isSelected,
                        boolean hasFocus, int row, int column) {
                    super.getTableCellRendererComponent(
                            table, value, isSelected, hasFocus, row, column);
                    setHorizontalAlignment(SwingConstants.CENTER);
                    if (!isSelected) {
                        String val = value != null ? value.toString() : "";
                        switch (val) {
                            case "STOCK BAJO":
                                setBackground(new Color(244, 67, 54));
                                setForeground(Color.WHITE);
                                break;
                            case "PUNTO REORDEN":
                                setBackground(new Color(255, 152, 0));
                                setForeground(Color.WHITE);
                                break;
                            case "SOBREINVENTARIO":
                                setBackground(new Color(255, 193, 7));
                                setForeground(Color.BLACK);
                                break;
                            case "NORMAL":
                                setBackground(new Color(76, 175, 80));
                                setForeground(Color.WHITE);
                                break;
                            default:
                                setBackground(Color.WHITE);
                                setForeground(Color.BLACK);
                        }
                    } else {
                        setBackground(table.getSelectionBackground());
                        setForeground(table.getSelectionForeground());
                    }
                    return this;
                }
            }
        );
    }
}
 
    public void cargarModulo() {
        if (!existenSalidas()) {
            JOptionPane.showMessageDialog(null,
                    "⚠️ NO EXISTEN MOVIMIENTOS DE SALIDA REGISTRADOS\n\n"
                    + "No es posible generar el análisis de inventario porque no hay\n"
                    + "consumos registrados. Para solucionarlo:\n\n"
                    + "1. Vaya a MOVIMIENTOS INVENTARIO\n"
                    + "2. Registre una SALIDA de productos\n"
                    + "3. Vuelva a ingresar a ANALISIS Y REPORTE",
                    "Sin datos de consumo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
 
        inicializarTablas();
        cargarProductos();
        calcularIndicadores();
        llenarFiltros();
        configurarListeners();
        configurarAlineacionTabla();
        configurarGraficaABC();
 
        if (modeloProductos.getRowCount() > 0) {
            jTableProductosA.setRowSelectionInterval(0, 0);
            Object codigoObj = modeloProductos.getValueAt(0, 1);
            if (codigoObj != null) {
                cargarHistorialProducto(codigoObj.toString().trim());
            }
        }
    }
 
    private void inicializarTablas() {
        String[] colsProductos = {
            "Grupo", "Código", "Nombre", "Categoría",
            "Precio", "Stock", "Punto Reorden", "EOQ", "Estado"
        };
        modeloProductos = new DefaultTableModel(colsProductos, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        jTableProductosA.setModel(modeloProductos);
        sorterProductos = new TableRowSorter<>(modeloProductos);
        jTableProductosA.setRowSorter(sorterProductos);
 
        String[] colsHistorial = {
            "No. Movimiento", "Fecha", "Motivo", "Cantidad", "Costo unitario", "Total"
        };
        modeloHistorial = new DefaultTableModel(colsHistorial, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        jTableHistorialSalidas.setModel(modeloHistorial);
    }
 
    private void cargarProductos() {
        productosData.clear();
        grupoABC.clear();
        eoqMap.clear();
        puntoReordenMap.clear();
        costoMap.clear();
        estadoMap.clear();
        productosUnicos.clear();
        modeloProductos.setRowCount(0);
 
        double costoPedido = 0, costoMantenimiento = 0, tiempoEntregaDias = 0;
        try {
            ManejoDeConfiguracion cfg = new ManejoDeConfiguracion();
            double[] config = cfg.leerConfiguracion();
            if (config != null && config.length >= 3) {
                costoPedido = config[0];
                costoMantenimiento = config[1];
                tiempoEntregaDias = config[2];
            }
        } catch (Exception ignored) { }
 
        List<String[]> filas = leerCSV(RUTA_INVENTARIO);
        if (filas.isEmpty()) return;
        
        Map<String, String[]> productosUnicosMap = new LinkedHashMap<>();
        for (String[] fila : filas) {
            if (fila.length >= 10) {
                String codigo = fila[0].trim();
                if (!productosUnicosMap.containsKey(codigo)) {
                    productosUnicosMap.put(codigo, fila);
                }
            }
        }
        
        List<String[]> filasUnicas = new ArrayList<>(productosUnicosMap.values());
 
        for (String[] fila : filasUnicas) {
            String codigo = fila[0].trim();
            double costo = parseDouble(fila[3]);
            costoMap.put(codigo, costo);
        }
 
        Map<String, Double> consumoPorCodigo = new HashMap<>();
        Map<String, Boolean> esSalida = new HashMap<>();
        
        for (String[] fila : leerCSV(RUTA_ENCABEZADO)) {
            if (fila.length < 3) continue;
            esSalida.put(fila[0].trim(), "SALIDA".equalsIgnoreCase(fila[2].trim()));
        }
        
        for (String[] fila : leerCSV(RUTA_DETALLE)) {
            if (fila.length < 3) continue;
            String noMov = fila[0].trim();
            if (!Boolean.TRUE.equals(esSalida.get(noMov))) continue;
            
            String codigo = fila[1].trim();
            double cantidad = parseDouble(fila[2]);
            if (cantidad <= 0) continue;
            
            double costo = costoMap.getOrDefault(codigo, 0.0);
            if (costo == 0.0) {
                costo = buscarCostoEnInventario(codigo);
                if (costo > 0) costoMap.put(codigo, costo);
            }
            consumoPorCodigo.merge(codigo, cantidad * costo, Double::sum);
        }
        
        double consumoTotal = 0;
        for (double v : consumoPorCodigo.values()) consumoTotal += v;
 
        List<String[]> filasValidas = new ArrayList<>(filasUnicas);
        filasValidas.sort((a, b) -> {
            double ca = consumoPorCodigo.getOrDefault(a[0].trim(), 0.0);
            double cb = consumoPorCodigo.getOrDefault(b[0].trim(), 0.0);
            return Double.compare(cb, ca);
        });
 
        double acumulado = 0;
        for (String[] fila : filasValidas) {
            String codigo = fila[0].trim();
            double cp = consumoPorCodigo.getOrDefault(codigo, 0.0);
            if (consumoTotal > 0) {
                acumulado += cp / consumoTotal;
            }
            String grupo;
            if (acumulado <= 0.80) grupo = "A";
            else if (acumulado <= 0.95) grupo = "B";
            else grupo = "C";
            grupoABC.put(codigo, grupo);
        }
 
        for (String[] fila : filasValidas) {
            try {
                String codigo = fila[0].trim();
                String nombre = fila[1].trim();
                String categoriaRaw = fila[2].trim();
                double precio = parseDouble(fila[4]);
                int stockActual = parseInt(fila[5]);
                int stockMinimo = parseInt(fila[6]);
                double demandaAnual = parseDouble(fila[8]);
 
                String categoria = categoriaRaw;
                try {
                    String nombreCat = gestor.codigoANombre(categoriaRaw);
                    if (nombreCat != null && !nombreCat.isEmpty()) categoria = nombreCat;
                } catch (Exception ignored) { }
 
                double eoq = 0;
                if (costoMantenimiento > 0 && demandaAnual > 0 && costoPedido > 0) {
                    eoq = Math.sqrt((2.0 * demandaAnual * costoPedido) / costoMantenimiento);
                }
                eoqMap.put(codigo, eoq);
 
                double puntoReorden = 0;
                if (tiempoEntregaDias > 0 && demandaAnual > 0) {
                    puntoReorden = (demandaAnual / 365.0) * tiempoEntregaDias;
                }
                puntoReordenMap.put(codigo, puntoReorden);
                
                String estado = evaluarEstado(stockActual, stockMinimo, puntoReorden);
                estadoMap.put(codigo, estado);
 
                String grupo = grupoABC.getOrDefault(codigo, "C");
 
                modeloProductos.addRow(new Object[]{
                    grupo, codigo, nombre, categoria,
                    FMT_MONEDA.format(precio), stockActual,
                    String.format("%.2f", puntoReorden), String.format("%.2f", eoq), estado
                });
                productosData.add(fila);
            } catch (Exception ignored) { }
        }
    }
 
    private double buscarCostoEnInventario(String codigoBuscado) {
        for (String[] fila : leerCSV(RUTA_INVENTARIO)) {
            if (fila.length < 10) continue;
            if (fila[0].trim().equals(codigoBuscado)) return parseDouble(fila[3]);
        }
        return 0.0;
    }
 
   
private void calcularIndicadores() {
    double consumoTotal = 0;
    int stockBajo = 0;
    int puntoReordenAlcanzado = 0;
    int sobreInventario = 0;

    // Consumo Total
    Map<String, Boolean> esSalida = new HashMap<>();
    for (String[] fila : leerCSV(RUTA_ENCABEZADO)) {
        if (fila.length < 3) continue;
        esSalida.put(fila[0].trim(), "SALIDA".equalsIgnoreCase(fila[2].trim()));
    }

    for (String[] fila : leerCSV(RUTA_DETALLE)) {
        if (fila.length < 3) continue;
        if (!Boolean.TRUE.equals(esSalida.get(fila[0].trim()))) continue;
        double cantidad = parseDouble(fila[2]);
        if (cantidad <= 0) continue;
        consumoTotal += cantidad * costoMap.getOrDefault(fila[1].trim(), 0.0);
    }

    // Contadores tomados del estadoMap (ya calculado por evaluarEstado)
    for (Map.Entry<String, String> entry : estadoMap.entrySet()) {
        switch (entry.getValue()) {
            case "STOCK BAJO":      stockBajo++;             break;
            case "PUNTO REORDEN":   puntoReordenAlcanzado++; break;
            case "SOBREINVENTARIO": sobreInventario++;        break;
        }
    }

    String consumoFormateado = FMT_MONEDA_SIMPLE.format(consumoTotal);

    jLabelConsumoTotal.setText("<html><center><b>CONSUMO TOTAL</b><br><font size='4'>$" + consumoFormateado + "</font></center></html>");
    jLabelConsumoTotal.setBackground(getColor("CONSUMO", 0));
    jLabelConsumoTotal.setOpaque(true);
    jLabelConsumoTotal.setForeground(Color.WHITE);

    jLabelBajoStock.setText("<html><center><b>STOCK BAJO</b><br><font size='4'>" + stockBajo + "</font></center></html>");
    jLabelBajoStock.setBackground(getColor("BAJO_STOCK", stockBajo));
    jLabelBajoStock.setOpaque(true);
    jLabelBajoStock.setForeground(Color.WHITE);

    jLabelReorden.setText("<html><center><b>PUNTO REORDEN</b><br><font size='4'>" + puntoReordenAlcanzado + "</font></center></html>");
    jLabelReorden.setBackground(getColor("REORDEN", puntoReordenAlcanzado));
    jLabelReorden.setOpaque(true);
    jLabelReorden.setForeground(Color.WHITE);

    jLabelSobreInventario.setText("<html><center><b>SOBREINVENTARIO</b><br><font size='4'>" + sobreInventario + "</font></center></html>");
    jLabelSobreInventario.setBackground(getColor("SOBREINVENTARIO", sobreInventario));
    jLabelSobreInventario.setOpaque(true);
    jLabelSobreInventario.setForeground(Color.WHITE);
}
 
    private void llenarFiltros() {
        boolean tieneTodas = false;
        for (int i = 0; i < jComboFilttoCat.getItemCount(); i++) {
            if ("Todas".equals(jComboFilttoCat.getItemAt(i))) {
                tieneTodas = true;
                break;
            }
        }
        if (!tieneTodas && jComboFilttoCat.getItemCount() > 0) {
            jComboFilttoCat.insertItemAt("Todas", 0);
            jComboFilttoCat.setSelectedIndex(0);
        }
    }
 
    private void configurarListeners() {
        jTexFiltroCod.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) { aplicarFiltros(); }
        });
        jTexFiltroNom.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) { aplicarFiltros(); }
        });
        jComboFilttoCat.addActionListener(e -> aplicarFiltros());
        jTableProductosA.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = jTableProductosA.getSelectedRow();
                if (selectedRow >= 0) {
                    int modelRow = jTableProductosA.convertRowIndexToModel(selectedRow);
                    Object codigoObj = modeloProductos.getValueAt(modelRow, 1);
                    if (codigoObj != null) {
                        cargarHistorialProducto(codigoObj.toString().trim());
                    }
                }
            }
        });
    }
 
    private void aplicarFiltros() {
        String filtroCod = jTexFiltroCod.getText().trim().toLowerCase();
        String filtroNom = jTexFiltroNom.getText().trim().toLowerCase();
        String filtroCat = jComboFilttoCat.getSelectedItem() != null ? jComboFilttoCat.getSelectedItem().toString() : "Todas";
 
        List<RowFilter<DefaultTableModel, Object>> filtros = new ArrayList<>();
        if (!filtroCod.isEmpty()) filtros.add(RowFilter.regexFilter("(?i)" + escapeRegex(filtroCod), 1));
        if (!filtroNom.isEmpty()) filtros.add(RowFilter.regexFilter("(?i)" + escapeRegex(filtroNom), 2));
        if (!"Todas".equals(filtroCat)) filtros.add(RowFilter.regexFilter("(?i)" + escapeRegex(filtroCat), 3));
 
        sorterProductos.setRowFilter(filtros.isEmpty() ? null : RowFilter.andFilter(filtros));
 
        if (jTableProductosA.getRowCount() > 0) {
            jTableProductosA.setRowSelectionInterval(0, 0);
            int modelRow = jTableProductosA.convertRowIndexToModel(0);
            Object codigoObj = modeloProductos.getValueAt(modelRow, 1);
            if (codigoObj != null) cargarHistorialProducto(codigoObj.toString().trim());
        } else {
            modeloHistorial.setRowCount(0);
        }
    }
 
    private String escapeRegex(String input) {
        return input.replaceAll("([\\\\.*+?\\[\\]{}()|^$])", "\\\\$1");
    }
 
    private void cargarHistorialProducto(String codigo) {
        modeloHistorial.setRowCount(0);
        
        Map<String, String[]> encabezados = new HashMap<>();
        for (String[] fila : leerCSV(RUTA_ENCABEZADO)) {
            if (fila.length < 4) continue;
            if (!"SALIDA".equalsIgnoreCase(fila[2].trim())) continue;
            encabezados.put(fila[0].trim(), new String[]{fila[1].trim(), fila[3].trim()});
        }
        
        double costo = costoMap.getOrDefault(codigo, 0.0);
        
        for (String[] fila : leerCSV(RUTA_DETALLE)) {
            if (fila.length < 3) continue;
            if (!fila[1].trim().equalsIgnoreCase(codigo)) continue;
            
            String[] enc = encabezados.get(fila[0].trim());
            if (enc == null) continue;
            
            double cantidad = parseDouble(fila[2]);
            if (cantidad <= 0) continue;
            
            try {
                double total = cantidad * costo;
                modeloHistorial.addRow(new Object[]{
                    fila[0].trim(), enc[0], enc[1], (int) cantidad,
                    FMT_MONEDA.format(costo), FMT_MONEDA.format(total)
                });
            } catch (Exception ignored) { }
        }
    }
 
    private boolean existenSalidas() {
        for (String[] fila : leerCSV(RUTA_ENCABEZADO)) {
            if (fila.length < 3) continue;
            if ("SALIDA".equalsIgnoreCase(fila[2].trim())) return true;
        }
        return false;
    }
 
    private List<String[]> leerCSV(String ruta) {
        List<String[]> resultado = new ArrayList<>();
        File archivo = new File(ruta);
        if (!archivo.exists()) return resultado;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(archivo), "UTF-8"))) {
            String linea;
            boolean primeraLinea = true;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty()) continue;
                if (primeraLinea) {
                    primeraLinea = false;
                    String[] partes = linea.split(",");
                    if (partes.length > 0 && !partes[0].trim().matches("\\d+.*")) continue;
                }
                resultado.add(linea.split(",", -1));
            }
        } catch (Exception ignored) { }
        return resultado;
    }
 
    private double parseDouble(String s) {
        if (s == null) return 0.0;
        try { 
            return Double.parseDouble(s.trim()); 
        }
        catch (NumberFormatException e) { 
            return 0.0; 
        }
    }
 
    private int parseInt(String s) {
        if (s == null) return 0;
        try { 
            return Integer.parseInt(s.trim()); 
        }
        catch (NumberFormatException e) { 
            return 0; 
        }
    }
    
private void configurarGraficaABC() {
    int countA = 0, countB = 0, countC = 0;
    for (String grupo : grupoABC.values()) {
        if ("A".equals(grupo)) countA++;
        else if ("B".equals(grupo)) countB++;
        else countC++;
    }

    // Dataset de barras
    org.jfree.data.category.DefaultCategoryDataset dataset =
        new org.jfree.data.category.DefaultCategoryDataset();
    dataset.addValue(countA, "Productos", "Grupo A");
    dataset.addValue(countB, "Productos", "Grupo B");
    dataset.addValue(countC, "Productos", "Grupo C");

    // Crear gráfica de barras
    org.jfree.chart.JFreeChart chart = org.jfree.chart.ChartFactory.createBarChart(
        "Clasificación ABC",   // título
        "Grupo",               // eje X
        "Cantidad",            // eje Y
        dataset,
        org.jfree.chart.plot.PlotOrientation.VERTICAL,
        false,   // leyenda
        true,    // tooltips
        false
    );

    // Estilo general
    chart.setBackgroundPaint(java.awt.Color.WHITE);
    chart.getTitle().setFont(new Font("Segoe UI", Font.BOLD, 14));

    // Estilo del plot
    org.jfree.chart.plot.CategoryPlot plot =
        (org.jfree.chart.plot.CategoryPlot) chart.getPlot();
    plot.setBackgroundPaint(java.awt.Color.WHITE);
    plot.setRangeGridlinePaint(new java.awt.Color(220, 220, 220));
    plot.setOutlineVisible(false);

    // Estilo de las barras
    org.jfree.chart.renderer.category.BarRenderer renderer =
        (org.jfree.chart.renderer.category.BarRenderer) plot.getRenderer();
    renderer.setSeriesPaint(0, new java.awt.Color(25, 118, 210)); // todas azul por default
    renderer.setSeriesPaint(0, new java.awt.Color(25, 118, 210));

    // Color individual por barra (A=azul, B=naranja, C=verde)
    renderer = new org.jfree.chart.renderer.category.BarRenderer() {
        @Override
        public java.awt.Paint getItemPaint(int row, int col) {
            switch (col) {
                case 0: return new java.awt.Color(25, 118, 210);  // A - azul
                case 1: return new java.awt.Color(255, 152, 0);   // B - naranja
                case 2: return new java.awt.Color(76, 175, 80);   // C - verde
                default: return java.awt.Color.GRAY;
            }
        }
    };
    renderer.setDrawBarOutline(false);
    renderer.setShadowVisible(false);
    renderer.setItemMargin(0.1);

    // Mostrar valores encima de cada barra
    renderer.setDefaultItemLabelGenerator(
        new org.jfree.chart.labels.StandardCategoryItemLabelGenerator());
    renderer.setDefaultItemLabelsVisible(true);
    renderer.setDefaultItemLabelFont(new Font("Segoe UI", Font.BOLD, 13));

    plot.setRenderer(renderer);

    // Estilo de ejes
    org.jfree.chart.axis.CategoryAxis ejeX =
        (org.jfree.chart.axis.CategoryAxis) plot.getDomainAxis();
    ejeX.setTickLabelFont(new Font("Segoe UI", Font.BOLD, 13));
    ejeX.setAxisLineVisible(false);

    org.jfree.chart.axis.NumberAxis ejeY =
        (org.jfree.chart.axis.NumberAxis) plot.getRangeAxis();
    ejeY.setTickLabelFont(new Font("Segoe UI", Font.PLAIN, 12));
    ejeY.setStandardTickUnits(org.jfree.chart.axis.NumberAxis.createIntegerTickUnits());

    // Agregar al panel
    org.jfree.chart.ChartPanel chartPanel =
        new org.jfree.chart.ChartPanel(chart);
    chartPanel.setPreferredSize(jPanelAnalGrafica.getSize());

    jPanelAnalGrafica.removeAll();
    jPanelAnalGrafica.setLayout(new java.awt.BorderLayout());
    jPanelAnalGrafica.add(chartPanel, java.awt.BorderLayout.CENTER);
    jPanelAnalGrafica.revalidate();
    jPanelAnalGrafica.repaint();
}
}