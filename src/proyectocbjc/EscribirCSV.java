package proyectocbjc;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class EscribirCSV {

    private final String archivo = "inventario.csv";

    private String[] getColumnas() {
        return new String[]{"Código", "Nombre", "Categoría", "Costo", "Precio venta", "Stock Actual", "Stock Min", "Tiempo Ent.", "Demanda Anual", "Estado"};
    }

    public DefaultTableModel obtenerModelo() {
        DefaultTableModel modelo = new DefaultTableModel(getColumnas(), 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        File file = new File(archivo);
        if (!file.exists()) return modelo;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length == 10) {
                    datos[2] = codigoANombre(datos[2]);
                    datos[9] = numeroAEstado(datos[9]);
                    modelo.addRow(datos);
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
        return modelo;
    }

    public DefaultTableModel buscarPorFiltro(String filtro) {
        DefaultTableModel modelo = new DefaultTableModel(getColumnas(), 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        String busqueda = filtro.toLowerCase().trim();

        File file = new File(archivo);
        if (!file.exists()) return modelo;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");

                if (datos.length == 10) {
                    String codigo = datos[0].toLowerCase();
                    String nombre = datos[1].toLowerCase();
                    String categoria = codigoANombre(datos[2]).toLowerCase();

                    if (codigo.contains(busqueda) ||
                        nombre.contains(busqueda) ||
                        categoria.contains(busqueda)) {

                        datos[2] = codigoANombre(datos[2]);
                        datos[9] = numeroAEstado(datos[9]);
                        modelo.addRow(datos);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return modelo;
    }

    public void escribir(String id, String nom, String catNom, String cos, String pre, String st, String stMin, String tie, String dem, String estNom) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(archivo, true))) {
            String linea = id + "," + nom + "," + nombreACodigo(catNom) + "," + cos + "," + pre + "," + st + "," + stMin + "," + tie + "," + dem + "," + estadoANumero(estNom);
            pw.println(linea);
        }
    }

    public void actualizarProducto(String id, String nom, String catNom, String cos, String pre, String st, String stMin, String tie, String dem, String estNom) throws IOException {
        List<String> lineas = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.startsWith(id + ",")) {
                    lineas.add(id + "," + nom + "," + nombreACodigo(catNom) + "," + cos + "," + pre + "," + st + "," + stMin + "," + tie + "," + dem + "," + estadoANumero(estNom));
                } else {
                    lineas.add(linea);
                }
            }
        }
        try (PrintWriter pw = new PrintWriter(new FileWriter(archivo))) {
            for (String l : lineas) pw.println(l);
        }
    }

    public String nombreACodigo(String nombre) {
        switch (nombre) {
            case "Computadoras y laptops": return "ELEC01";
            case "Componentes de PC": return "ELEC02";
            case "Periféricos": return "ELEC03";
            case "Monitores": return "ELEC04";
            case "Impresoras y escáneres": return "ELEC05";
            case "Redes y conectividad": return "ELEC06";
            case "Almacenamiento": return "ELEC07";
            case "Accesorios para celulares": return "ELEC08";
            case "Smartphones y tablets": return "ELEC09";
            case "Audio y sonido": return "ELEC10";
            case "Video y entretenimiento": return "ELEC11";
            case "Energía y protección": return "ELEC12";
            case "Cámaras y videovigilancia": return "ELEC13";
            case "Gadgets y wearables": return "ELEC14";
            case "Consumibles": return "ELEC15";
            default: return nombre;
        }
    }

    public String codigoANombre(String codigo) {
        switch (codigo) {
            case "ELEC01": return "Computadoras y laptops";
            case "ELEC02": return "Componentes de PC";
            case "ELEC03": return "Periféricos";
            case "ELEC04": return "Monitores";
            case "ELEC05": return "Impresoras y escáneres";
            case "ELEC06": return "Redes y conectividad";
            case "ELEC07": return "Almacenamiento";
            case "ELEC08": return "Accesorios para celulares";
            case "ELEC09": return "Smartphones y tablets";
            case "ELEC10": return "Audio y sonido";
            case "ELEC11": return "Video y entretenimiento";
            case "ELEC12": return "Energía y protección";
            case "ELEC13": return "Cámaras y videovigilancia";
            case "ELEC14": return "Gadgets y wearables";
            case "ELEC15": return "Consumibles";
            default: return codigo;
        }
    }

    public String estadoANumero(String estado) {
        return estado.equalsIgnoreCase("Activado") ? "1" : "0";
    }

    public String numeroAEstado(String numero) {
        return numero.equals("1") ? "Activado" : "Desactivado";
    }

    public boolean existeCodigo(String id) {
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.startsWith(id + ",")) return true;
            }
        } catch (IOException e) { }
        return false;
    }
    public String obtenerSiguienteMovimiento() {
    File file = new File("encabezado.csv"); // archivo de movimientos

    int ultimo = 0;

    if (!file.exists()) {
        return "0001"; // si no existe empieza en 1
    }

    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
        String linea;

        while ((linea = br.readLine()) != null) {
            String[] datos = linea.split(",");

            if (datos.length > 0) {
                try {
                    int num = Integer.parseInt(datos[0]);
                    if (num > ultimo) {
                        ultimo = num;
                    }
                } catch (NumberFormatException e) {
                    // ignora errores
                }
            }
        }

    } catch (IOException e) {
        e.printStackTrace();
    }

    ultimo++;

    return String.format("%04d", ultimo); // 0001, 0002, etc
}
    public void guardarEncabezado(String noMov, String fecha, String tipo, String motivo) throws IOException {

    File file = new File("encabezado.csv");

    try (PrintWriter pw = new PrintWriter(new FileWriter(file, true))) {
        pw.println(noMov + "," + fecha + "," + tipo + "," + motivo);
    }
}
    public void guardarDetalle(String noMov, String codigo, int cantidad) throws IOException {

    File file = new File("detalle.csv");

    try (PrintWriter pw = new PrintWriter(new FileWriter(file, true))) {
        pw.println(noMov + "," + codigo + "," + cantidad);
    }
}
    public String obtenerCodigoPorNombre(String nombreBuscado) {

    File file = new File(archivo);

    if (!file.exists()) return "";

    try (BufferedReader br = new BufferedReader(new FileReader(file))) {

        String linea;

        while ((linea = br.readLine()) != null) {

            String[] datos = linea.split(",");

            if (datos.length >= 2) {

                String codigo = datos[0];
                String nombre = datos[1];

                if (nombre.equalsIgnoreCase(nombreBuscado)) {
                    return codigo;
                }
            }
        }

    } catch (IOException e) {
        e.printStackTrace();
    }

    return "";
}
    public void actualizarStock(String id, int delta) throws IOException {
    List<String> lineas = new ArrayList<>();

    try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
        String linea;
        while ((linea = br.readLine()) != null) {
            String[] datos = linea.split(",");
            if (datos.length == 10 && datos[0].equalsIgnoreCase(id)) {
                int stockActual = Integer.parseInt(datos[5].trim());
                int nuevoStock = stockActual + delta;
                if (nuevoStock < 0) nuevoStock = 0; // seguridad
                datos[5] = String.valueOf(nuevoStock);
                lineas.add(String.join(",", datos));
            } else {
                lineas.add(linea);
            }
        }
    }

    try (PrintWriter pw = new PrintWriter(new FileWriter(archivo))) {
        for (String l : lineas) pw.println(l);
    }
}
        public int[] obtenerStockYEstado(String id) {
    // retorna [stockActual, stockMin, estado]
    try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
        String linea;
        while ((linea = br.readLine()) != null) {
            String[] datos = linea.split(",");
            if (datos.length == 10 && datos[0].equalsIgnoreCase(id)) {
                int stockActual = Integer.parseInt(datos[5].trim());
                int stockMin    = Integer.parseInt(datos[6].trim());
                int estado      = Integer.parseInt(datos[9].trim()); // 1=activo 0=desactivado
                return new int[]{stockActual, stockMin, estado};
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
    return new int[]{0, 0, 1}; // default seguro
}
           public void reemplazarStock(String id, int nuevoStock) throws IOException {

    if (nuevoStock < 0) {
        nuevoStock = 0;
    }

    List<String> lineas = new ArrayList<>();

    try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
        String linea;
        while ((linea = br.readLine()) != null) {
            String[] datos = linea.split(",");
            if (datos.length == 10 && datos[0].equalsIgnoreCase(id)) {
                datos[5] = String.valueOf(nuevoStock);
                lineas.add(String.join(",", datos));
            } else {
                lineas.add(linea);
            }
        }
    }

    try (PrintWriter pw = new PrintWriter(new FileWriter(archivo))) {
        for (String l : lineas) pw.println(l);
    }
}
            public void guardarDetalleLote(List<String> detalles) throws IOException {

    File file = new File("detalle.csv");

    try (PrintWriter pw = new PrintWriter(new FileWriter(file, true))) {
        for (String det : detalles) {
            pw.println(det);
        }
    }
}
            public boolean todosLosCodigosExisten(List<String> codigos) {

    for (String codigo : codigos) {
        if (!existeCodigo(codigo)) {
            return false;
        }
    }

    return true;
}
            public boolean validarDetalles(List<String> codigos, DefaultTableModel modelo) {

    for (int i = 0; i < modelo.getRowCount(); i++) {

        int cantidad = Integer.parseInt(modelo.getValueAt(i, 0).toString());
        String codigo = codigos.get(i);

        if (cantidad <= 0) return false;

        if (!existeCodigo(codigo)) return false;
    }

    return true;
}
}