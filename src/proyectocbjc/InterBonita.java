/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package proyectocbjc;

import javax.swing.table.DefaultTableModel;

/**
 *
 * @author LENOVO
 */
public class InterBonita extends javax.swing.JFrame {

    /**
     * Creates new form InterBonita
     */
    private javax.swing.JButton botonActivo = null;
     private final EscribirCSV gestor = new EscribirCSV();
     private void configurarBuscador() {
    final String PLACEHOLDER = "Busca por codigo";
    jTexBuscaCod.setText(PLACEHOLDER);
    jTexBuscaCod.setForeground(java.awt.Color.GRAY);

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
            }
        }
    });
}
    public InterBonita() {
       
      initComponents();
    configurarBuscador();
    jBtnBuscar1.setVisible(false);

    // ✅ PRIMERO cargar el modelo real
    jTable1.setModel(gestor.obtenerModelo());

    // ✅ DESPUÉS configurar columnas (ya tiene el modelo correcto)
    configurarColumnas();
    configurarAlineacionTabla();

    // Centrar ventana
    java.awt.GraphicsEnvironment ge = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment();
    java.awt.GraphicsDevice gd = ge.getDefaultScreenDevice();
    java.awt.Rectangle rect = gd.getDefaultConfiguration().getBounds();
    java.awt.Insets insets = java.awt.Toolkit.getDefaultToolkit().getScreenInsets(gd.getDefaultConfiguration());
    int x = (rect.width - getWidth()) / 2;
    int y = (rect.height - insets.top - insets.bottom - getHeight()) / 2;
    setLocation(x, y);
    }
    public void actualizarTabla() {
    jTable1.setModel(gestor.obtenerModelo());
    configurarColumnas();
    configurarAlineacionTabla();
}
   private void configurarColumnas() {
    // ✅ Verificar que el modelo sea el correcto antes de crear el sorter
    if (!(jTable1.getModel() instanceof javax.swing.table.DefaultTableModel)) return;

    javax.swing.table.DefaultTableModel modelo = 
        (javax.swing.table.DefaultTableModel) jTable1.getModel();

    javax.swing.table.TableRowSorter<javax.swing.table.DefaultTableModel> sorter =
        new javax.swing.table.TableRowSorter<>(modelo);
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(240, 242, 245));
        setMaximumSize(new java.awt.Dimension(1366, 700));
        setMinimumSize(new java.awt.Dimension(1366, 700));
        setPreferredSize(new java.awt.Dimension(1366, 700));

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
                .addComponent(btnMov, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                .addContainerGap(415, Short.MAX_VALUE))
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

    // 1. Validar campo vacío
    if (textoBusqueda.isEmpty() || textoBusqueda.equalsIgnoreCase(PLACEHOLDER)) {
        javax.swing.JOptionPane.showMessageDialog(this,
                "Ingresa un código, nombre o categoría para buscar.",
                "Campo vacío",
                javax.swing.JOptionPane.WARNING_MESSAGE);

        jTable1.setModel(gestor.obtenerModelo());
        configurarColumnas();          // 🔥 AQUÍ
        configurarAlineacionTabla();   // 🔥 AQUÍ
        jBtnBuscar1.setVisible(false);
        return;
    }

    // 2. Buscar
    DefaultTableModel modeloBuscado = gestor.buscarPorFiltro(textoBusqueda);

    if (modeloBuscado.getRowCount() > 0) {
        jTable1.setModel(modeloBuscado);

        configurarColumnas();          // 🔥 IMPORTANTE
        configurarAlineacionTabla();   // 🔥 IMPORTANTE

        jTable1.setRowSelectionInterval(0, 0);
        jBtnBuscar1.setVisible(true);

    } else {
        javax.swing.JOptionPane.showMessageDialog(this,
                "No se encontraron resultados para: " + textoBusqueda,
                "Sin resultados",
                javax.swing.JOptionPane.INFORMATION_MESSAGE);

        jTable1.setModel(gestor.obtenerModelo());

        configurarColumnas();          // 🔥 TAMBIÉN AQUÍ
        configurarAlineacionTabla();

        jBtnBuscar1.setVisible(false);
    }
    }//GEN-LAST:event_jBtnBuscarActionPerformed

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
    private javax.swing.JButton jBtnBuscar;
    private javax.swing.JButton jBtnBuscar1;
    private javax.swing.JButton jBtnGuardar;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanelCatalogo;
    private javax.swing.JPanel jPanelContenedor;
    private javax.swing.JPanel jPanelInicio;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTexBuscaCod;
    // End of variables declaration//GEN-END:variables
}
