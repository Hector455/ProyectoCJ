/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package proyectocbjc;

/**
 *
 * @author LENOVO
 */
public class Formu extends javax.swing.JDialog {

    /**
     * Creates new form Formu
     */
    private String estadoAnterior = "";
   private InterBonita padre;
    private boolean modoEdicion = false;
    
    public Formu(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
    this.padre = (InterBonita) parent;
    this.modoEdicion = false;
    initComponents();
    configurarCampos();

    jComboCatego1.setSelectedItem("Activado");
    jComboCatego1.setEnabled(false);
    jPanel1.setFocusable(true);

    // ✅ El focus se pide DESPUÉS de que la ventana esté lista
    java.awt.EventQueue.invokeLater(() -> {
        jTexCodigo1.requestFocusInWindow();
    });

    }
    public Formu(java.awt.Frame parent, boolean modal, String[] datos) {
    super(parent, modal);
        this.padre = (InterBonita) parent;
        this.modoEdicion = true; // ACTIVAMOS EL MODO EDICIÓN
        initComponents();
        configurarCampos();
         this.estadoAnterior = datos[9];


        // Rellenar campos y bloquear el ID
        jTexCodigo1.setText(datos[0]);
        jTexCodigo1.setEditable(false); // NO PERMITE EDITAR EL ID
        jTexNombre.setText(datos[1]);
        jComboCatego.setSelectedItem(datos[2]);
        jTexCosto.setText(datos[3]);
        jTexPrecio.setText(datos[4]);
        jTexStockActual.setText(datos[5]);
        jTexStockMin.setText(datos[6]); // Ajustado a tu variable real
        jTexTiempoEntrega.setText(datos[7]);
        jTexEstimaDemanda.setText(datos[8]);
        jComboCatego1.setSelectedItem(datos[9]);
      
}
    private void validarSoloNumeros(java.awt.event.KeyEvent evt) {
    char c = evt.getKeyChar();
    if (!Character.isDigit(c) && c != java.awt.event.KeyEvent.VK_BACK_SPACE) {
        evt.consume(); 
    }
}

// Bloquea letras pero permite un punto decimal para costos/precios
private void validarNumerosConPunto(java.awt.event.KeyEvent evt, javax.swing.JTextField campo) {
    char c = evt.getKeyChar();
    if (!Character.isDigit(c) && c != '.' && c != java.awt.event.KeyEvent.VK_BACK_SPACE) {
        evt.consume();
    }
    if (c == '.' && campo.getText().contains(".")) {
        evt.consume();
    }
}
private boolean esPlaceholder(javax.swing.JTextField campo, String texto) {
    return campo.getText().trim().equals(texto);
}


// Método para validar que no haya campos vacíos o textos de ejemplo
private boolean validarCampos() {

    // 🔹 Código
    String codigo = jTexCodigo1.getText().trim();
    if (codigo.isEmpty() || codigo.equals("Ej: 001")) {
        javax.swing.JOptionPane.showMessageDialog(this, "El campo 'Código' no puede estar vacío.");
        jTexCodigo1.requestFocus();
        return false;
    }
    if (!codigo.matches("[a-zA-Z0-9]+")) {
        javax.swing.JOptionPane.showMessageDialog(this, "El código solo debe contener letras y números.");
        jTexCodigo1.requestFocus();
        return false;
    }

    // 🔹 Nombre
    String nombre = jTexNombre.getText().trim();
    if (nombre.isEmpty() || nombre.equals("Nombre del Producto")) {
        javax.swing.JOptionPane.showMessageDialog(this, "El campo 'Nombre' no puede estar vacío.");
        jTexNombre.requestFocus();
        return false;
    }

    // 🔹 Categoría
    if (jComboCatego.getSelectedIndex() == 0) {
        javax.swing.JOptionPane.showMessageDialog(this, "El campo 'Categoría' no puede estar vacío.");
        return false;
    }

    // 🔹 Costo
    String costoRaw = jTexCosto.getText().trim();
    boolean costoEsPlaceholder = costoRaw.isEmpty() || costoRaw.equals("$ 0");
    double costo = -1;
    if (!costoEsPlaceholder) {
        try {
            costo = Double.parseDouble(costoRaw.replace("$", "").replace(",", "").trim());
        } catch (NumberFormatException e) {
            costo = -1;
        }
    }
    if (costoEsPlaceholder) {
        javax.swing.JOptionPane.showMessageDialog(this, "El campo 'Costo' no puede estar vacío.");
        jTexCosto.requestFocus();
        return false;
    }
    if (costo <= 0) {
        javax.swing.JOptionPane.showMessageDialog(this, "El campo 'Costo' no puede ser 0 o negativo.");
        jTexCosto.requestFocus();
        return false;
    }

    // 🔹 Precio
    String precioRaw = jTexPrecio.getText().trim();
    boolean precioEsPlaceholder = precioRaw.isEmpty() || precioRaw.equals("$ 0");
    double precio = -1;
    if (!precioEsPlaceholder) {
        try {
            precio = Double.parseDouble(precioRaw.replace("$", "").replace(",", "").trim());
        } catch (NumberFormatException e) {
            precio = -1;
        }
    }
    if (precioEsPlaceholder) {
        javax.swing.JOptionPane.showMessageDialog(this, "El campo 'Precio' no puede estar vacío.");
        jTexPrecio.requestFocus();
        return false;
    }
    if (precio <= 0) {
        javax.swing.JOptionPane.showMessageDialog(this, "El campo 'Precio' no puede ser 0 o negativo.");
        jTexPrecio.requestFocus();
        return false;
    }

    // ⚠️ Advertencia costo >= precio
    if (costo >= precio) {
        int conf = javax.swing.JOptionPane.showConfirmDialog(this,
                "El costo es mayor o igual al precio de venta. ¿Deseas continuar?",
                "Advertencia",
                javax.swing.JOptionPane.YES_NO_OPTION);
        if (conf != javax.swing.JOptionPane.YES_OPTION) return false;
    }

    // 🔹 Stock Actual — se permite 0, solo no negativo ni texto
    String stActualRaw = jTexStockActual.getText().trim();
    boolean stActualEsPlaceholder = stActualRaw.isEmpty() || stActualRaw.equals("0");
    if (!stActualEsPlaceholder) {
        double stockActual;
        try {
            stockActual = Double.parseDouble(stActualRaw);
        } catch (NumberFormatException e) {
            javax.swing.JOptionPane.showMessageDialog(this, "El campo 'Stock Actual' no puede estar vacío.");
            jTexStockActual.requestFocus();
            return false;
        }
        if (stockActual < 0) {
            javax.swing.JOptionPane.showMessageDialog(this, "El campo 'Stock Actual' no puede ser negativo.");
            jTexStockActual.requestFocus();
            return false;
        }
    }

    // 🔹 Stock Mínimo
    String stMinRaw = jTexStockMin.getText().trim();
    boolean stMinEsPlaceholder = stMinRaw.isEmpty() || stMinRaw.equals("0");
    double stockMin = -1;
    if (!stMinEsPlaceholder) {
        try {
            stockMin = Double.parseDouble(stMinRaw);
        } catch (NumberFormatException e) {
            stockMin = -1;
        }
    }
    if (stMinEsPlaceholder) {
        javax.swing.JOptionPane.showMessageDialog(this, "El campo 'Stock Mínimo' no puede estar vacío.");
        jTexStockMin.requestFocus();
        return false;
    }
    if (stockMin <= 0) {
        javax.swing.JOptionPane.showMessageDialog(this, "El campo 'Stock Mínimo' no puede ser 0 o negativo.");
        jTexStockMin.requestFocus();
        return false;
    }

    // 🔹 Tiempo de Entrega
    String tiempoRaw = jTexTiempoEntrega.getText().trim();
    boolean tiempoEsPlaceholder = tiempoRaw.isEmpty() || tiempoRaw.equals("0");
    double tiempo = -1;
    if (!tiempoEsPlaceholder) {
        try {
            tiempo = Double.parseDouble(tiempoRaw);
        } catch (NumberFormatException e) {
            tiempo = -1;
        }
    }
    if (tiempoEsPlaceholder) {
        javax.swing.JOptionPane.showMessageDialog(this, "El campo 'Tiempo de Entrega' no puede estar vacío.");
        jTexTiempoEntrega.requestFocus();
        return false;
    }
    if (tiempo < 1) {
        javax.swing.JOptionPane.showMessageDialog(this, "El campo 'Tiempo de Entrega' no puede ser 0, negativo o menor a 1 día.");
        jTexTiempoEntrega.requestFocus();
        return false;
    }

    // 🔹 Estimación de Demanda
    String demandaRaw = jTexEstimaDemanda.getText().trim();
    boolean demandaEsPlaceholder = demandaRaw.isEmpty() || demandaRaw.equals("0");
    double demanda = -1;
    if (!demandaEsPlaceholder) {
        try {
            demanda = Double.parseDouble(demandaRaw);
        } catch (NumberFormatException e) {
            demanda = -1;
        }
    }
    if (demandaEsPlaceholder) {
        javax.swing.JOptionPane.showMessageDialog(this, "El campo 'Estimación de Demanda' no puede estar vacío.");
        jTexEstimaDemanda.requestFocus();
        return false;
    }
    if (demanda < 1) {
        javax.swing.JOptionPane.showMessageDialog(this, "El campo 'Estimación de Demanda' no puede ser 0, negativo o menor a 1.");
        jTexEstimaDemanda.requestFocus();
        return false;
    }

    return true;
}

// Método para las reglas de negocio (precios y valores 0)


// Método para la lógica de guardado

   private void configurarCampos() {
   // 1. Agregamos jTexStockMin al arreglo de campos
    javax.swing.JTextField[] campos = {
        jTexCodigo1, jTexNombre, jTexCosto, jTexPrecio, 
        jTexStockActual, jTexStockMin, jTexTiempoEntrega, jTexEstimaDemanda
    };
    
    // 2. Definimos los textos iniciales que deben borrarse al hacer clic
    String[] textos = {"Ej: 001", "Nombre del Producto", "$ 0", "$ 0", "0", "0", "0", "0"};
    
    for (int i = 0; i < campos.length; i++) {
        final int index = i;
        campos[i].addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                // Si el texto actual es el "placeholder", lo borramos y ponemos negro
                if (campos[index].getText().equals(textos[index])) {
                    campos[index].setText("");
                    campos[index].setForeground(java.awt.Color.BLACK);
                }
            }
            
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                // Opcional: Si el usuario dejó el campo vacío, regresa el texto gris
                if (campos[index].getText().isEmpty()) {
                    campos[index].setText(textos[index]);
                    campos[index].setForeground(java.awt.Color.GRAY);
                }
            }
        });
        // Ponemos el color inicial gris para que se vea como sugerencia
        campos[i].setForeground(java.awt.Color.GRAY);
    }
}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField16 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jTexNombre = new javax.swing.JTextField();
        jTexCodigo1 = new javax.swing.JTextField();
        jTexCosto = new javax.swing.JTextField();
        jComboCatego = new javax.swing.JComboBox<>();
        jTexStockActual = new javax.swing.JTextField();
        jTexTiempoEntrega = new javax.swing.JTextField();
        jTexPrecio = new javax.swing.JTextField();
        jTexStockMin = new javax.swing.JTextField();
        jTexEstimaDemanda = new javax.swing.JTextField();
        jComboCatego1 = new javax.swing.JComboBox<>();
        jBtnGuardar = new javax.swing.JButton();
        jBtnCancelar = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();

        jTextField16.setBackground(new java.awt.Color(255, 255, 255));
        jTextField16.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jTextField16.setForeground(new java.awt.Color(51, 51, 51));
        jTextField16.setText("Código del Producto *");
        jTextField16.setMaximumSize(new java.awt.Dimension(255, 25));
        jTextField16.setMinimumSize(new java.awt.Dimension(255, 25));
        jTextField16.setPreferredSize(new java.awt.Dimension(255, 25));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("Nombre o Descripción *");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setBackground(new java.awt.Color(0, 0, 0));
        setMinimumSize(new java.awt.Dimension(1366, 728));

        jPanel2.setBackground(new java.awt.Color(0, 0, 0));
        jPanel2.setLayout(new java.awt.GridBagLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setMaximumSize(new java.awt.Dimension(600, 600));
        jPanel1.setMinimumSize(new java.awt.Dimension(600, 600));
        jPanel1.setPreferredSize(new java.awt.Dimension(600, 600));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Nuevo Producto");

        jTexNombre.setText("Nombre del Producto");
        jTexNombre.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)), javax.swing.BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        jTexNombre.setMaximumSize(new java.awt.Dimension(255, 40));
        jTexNombre.setMinimumSize(new java.awt.Dimension(255, 40));
        jTexNombre.setPreferredSize(new java.awt.Dimension(255, 40));

        jTexCodigo1.setText("Ej: 001");
        jTexCodigo1.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)), javax.swing.BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        jTexCodigo1.setMaximumSize(new java.awt.Dimension(255, 40));
        jTexCodigo1.setMinimumSize(new java.awt.Dimension(255, 40));
        jTexCodigo1.setPreferredSize(new java.awt.Dimension(255, 40));
        jTexCodigo1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTexCodigo1ActionPerformed(evt);
            }
        });

        jTexCosto.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTexCosto.setText("$ 0");
        jTexCosto.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)), javax.swing.BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        jTexCosto.setMaximumSize(new java.awt.Dimension(255, 40));
        jTexCosto.setMinimumSize(new java.awt.Dimension(255, 40));
        jTexCosto.setPreferredSize(new java.awt.Dimension(255, 40));

        jComboCatego.setBackground(new java.awt.Color(255, 255, 255));
        jComboCatego.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboCatego.setForeground(new java.awt.Color(51, 51, 51));
        jComboCatego.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "\" \"", "Computadoras y laptops", "Componentes de PC", "Periféricos", "Monitores", "Impresoras y escáneres", "Redes y conectividad", "Almacenamiento", "Accesorios para celulares", "Smartphones y tablets", "Audio y sonido", "Video y entretenimiento", "Energía y protección", "Cámaras y videovigilancia", "Gadgets y wearables", "Consumibles" }));
        jComboCatego.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jComboCatego.setMinimumSize(new java.awt.Dimension(255, 40));
        jComboCatego.setPreferredSize(new java.awt.Dimension(255, 40));

        jTexStockActual.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTexStockActual.setText("0");
        jTexStockActual.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)), javax.swing.BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        jTexStockActual.setMaximumSize(new java.awt.Dimension(255, 40));
        jTexStockActual.setMinimumSize(new java.awt.Dimension(255, 40));
        jTexStockActual.setPreferredSize(new java.awt.Dimension(255, 40));

        jTexTiempoEntrega.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTexTiempoEntrega.setText("0");
        jTexTiempoEntrega.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)), javax.swing.BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        jTexTiempoEntrega.setMaximumSize(new java.awt.Dimension(255, 40));
        jTexTiempoEntrega.setMinimumSize(new java.awt.Dimension(255, 40));
        jTexTiempoEntrega.setPreferredSize(new java.awt.Dimension(255, 40));

        jTexPrecio.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTexPrecio.setText("$ 0");
        jTexPrecio.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)), javax.swing.BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        jTexPrecio.setMaximumSize(new java.awt.Dimension(255, 40));
        jTexPrecio.setMinimumSize(new java.awt.Dimension(255, 40));
        jTexPrecio.setPreferredSize(new java.awt.Dimension(255, 40));

        jTexStockMin.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTexStockMin.setText("0");
        jTexStockMin.setBorder(javax.swing.BorderFactory.createCompoundBorder(null, javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)), javax.swing.BorderFactory.createEmptyBorder(5, 10, 5, 10))));
        jTexStockMin.setMaximumSize(new java.awt.Dimension(255, 40));
        jTexStockMin.setMinimumSize(new java.awt.Dimension(255, 40));
        jTexStockMin.setPreferredSize(new java.awt.Dimension(255, 40));

        jTexEstimaDemanda.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTexEstimaDemanda.setText("0");
        jTexEstimaDemanda.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)), javax.swing.BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        jTexEstimaDemanda.setMaximumSize(new java.awt.Dimension(255, 40));
        jTexEstimaDemanda.setMinimumSize(new java.awt.Dimension(255, 40));
        jTexEstimaDemanda.setNextFocusableComponent(jBtnGuardar);
        jTexEstimaDemanda.setPreferredSize(new java.awt.Dimension(255, 40));

        jComboCatego1.setBackground(new java.awt.Color(255, 255, 255));
        jComboCatego1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboCatego1.setForeground(new java.awt.Color(51, 51, 51));
        jComboCatego1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Activado", "Desactivado" }));
        jComboCatego1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jComboCatego1.setMinimumSize(new java.awt.Dimension(255, 40));
        jComboCatego1.setPreferredSize(new java.awt.Dimension(255, 40));

        jBtnGuardar.setBackground(new java.awt.Color(0, 80, 150));
        jBtnGuardar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jBtnGuardar.setForeground(new java.awt.Color(255, 255, 255));
        jBtnGuardar.setText("GUARDAR");
        jBtnGuardar.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 20, 8, 20));
        jBtnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnGuardarActionPerformed(evt);
            }
        });

        jBtnCancelar.setBackground(new java.awt.Color(240, 240, 240));
        jBtnCancelar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jBtnCancelar.setForeground(new java.awt.Color(60, 64, 67));
        jBtnCancelar.setText("CANCELAR");
        jBtnCancelar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(200, 200, 200)));
        jBtnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnCancelarActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("Código del Producto *");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("Categoría * ");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("Nombre o Descripción *");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setText("Costo *");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setText("Stock Mínimo *");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 0, 0));
        jLabel8.setText("Stock Actual *");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 0, 0));
        jLabel9.setText("Precio de Venta * ");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(0, 0, 0));
        jLabel10.setText("Tiempo de Entrega (días) *");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 0, 0));
        jLabel11.setText("Estimación de Demanda Actual *");

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(0, 0, 0));
        jLabel12.setText("Estado");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jBtnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jBtnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(148, 148, 148))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jTexCodigo1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jComboCatego, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jTexEstimaDemanda, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jTexPrecio, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jTexStockMin, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTexNombre, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTexCosto, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTexStockActual, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTexTiempoEntrega, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jComboCatego1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
                .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTexCodigo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTexNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(19, 19, 19)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboCatego, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTexCosto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTexPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTexStockActual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTexStockMin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTexTiempoEntrega, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTexEstimaDemanda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboCatego1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jBtnGuardar, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                    .addComponent(jBtnCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel2.add(jPanel1, new java.awt.GridBagConstraints());

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTexCodigo1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTexCodigo1ActionPerformed
        // TODO add your handling code here:
        jTexNombre.requestFocus();
        
    }//GEN-LAST:event_jTexCodigo1ActionPerformed

    private void jBtnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnGuardarActionPerformed

   
    // 🔴 SOLO UNA VALIDACIÓN COMPLETA
    if (!validarCampos()) return;

    // 🔵 Guardar
    guardarOActualizar();
    }//GEN-LAST:event_jBtnGuardarActionPerformed
private void guardarOActualizar() {
    EscribirCSV gestor = new EscribirCSV();

    String id = jTexCodigo1.getText().trim();
    if (id.equals("Ej: 001")) id = "";
    String nom = jTexNombre.getText().replace(",", " ").trim();
    if (nom.equals("Nombre del Producto")) nom = "";
    String cat = jComboCatego.getSelectedItem().toString();
    String cos = jTexCosto.getText().replace("$ ", "").replace(",", "").trim();
    String pre = jTexPrecio.getText().replace("$ ", "").replace(",", "").trim();
    String st = jTexStockActual.getText().trim();
    String stMin = jTexStockMin.getText().trim();
    String tie = jTexTiempoEntrega.getText().trim();
    String dem = jTexEstimaDemanda.getText().trim();
    String est = jComboCatego1.getSelectedItem().toString();

    try {
        if (modoEdicion) {
            if (!est.equals(estadoAnterior)) {
                int conf = javax.swing.JOptionPane.showConfirmDialog(this,
                        "¿Seguro que deseas cambiar el estado a " + est + "?",
                        "Confirmar cambio de estado",
                        javax.swing.JOptionPane.YES_NO_OPTION);
                if (conf != javax.swing.JOptionPane.YES_OPTION) return;
            }
            gestor.actualizarProducto(id, nom, cat, cos, pre, st, stMin, tie, dem, est);
            javax.swing.JOptionPane.showMessageDialog(this, "Producto actualizado correctamente.");
        } else {
            if (gestor.existeCodigo(id)) {
                javax.swing.JOptionPane.showMessageDialog(this, "Error: El código ya existe.");
                return;
            }
            gestor.escribir(id, nom, cat, cos, pre, st, stMin, tie, dem, est);
            javax.swing.JOptionPane.showMessageDialog(this, "Producto guardado correctamente.");
        }

        if (padre != null) padre.actualizarTabla();
        this.dispose();

    } catch (java.io.IOException e) {
        javax.swing.JOptionPane.showMessageDialog(this,
            "No se pudo guardar. El archivo está siendo usado por otra aplicación.\n" +
            "Cierra el archivo y vuelve a intentarlo.",
            "Error al guardar",
            javax.swing.JOptionPane.ERROR_MESSAGE);
    }
}
    private void jBtnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnCancelarActionPerformed
        int opcion = javax.swing.JOptionPane.showConfirmDialog(
        this, 
        "¿Estás seguro de que deseas cancelar? Los cambios no se guardarán.", 
        "Confirmar cancelación", 
        javax.swing.JOptionPane.YES_NO_OPTION, 
        javax.swing.JOptionPane.WARNING_MESSAGE
    );

    // Si el usuario presiona "Sí" (que es la opción 0), cerramos la ventana
    if (opcion == javax.swing.JOptionPane.YES_OPTION) {
        this.dispose();
    }
    }//GEN-LAST:event_jBtnCancelarActionPerformed

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
            java.util.logging.Logger.getLogger(Formu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Formu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Formu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Formu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Formu dialog = new Formu(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBtnCancelar;
    private javax.swing.JButton jBtnGuardar;
    private javax.swing.JComboBox<String> jComboCatego;
    private javax.swing.JComboBox<String> jComboCatego1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField jTexCodigo1;
    private javax.swing.JTextField jTexCosto;
    private javax.swing.JTextField jTexEstimaDemanda;
    private javax.swing.JTextField jTexNombre;
    private javax.swing.JTextField jTexPrecio;
    private javax.swing.JTextField jTexStockActual;
    private javax.swing.JTextField jTexStockMin;
    private javax.swing.JTextField jTexTiempoEntrega;
    private javax.swing.JTextField jTextField16;
    // End of variables declaration//GEN-END:variables
}
