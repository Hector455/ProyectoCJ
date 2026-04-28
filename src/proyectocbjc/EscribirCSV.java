package proyectocbjc;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class EscribirCSV {

    private final String archivo = "inventario.csv";

    // Columnas estándar para la interfaz
    private String[] getColumnas() {
        return new String[]{"Código", "Nombre", "Categoría", "Costo", "Precio venta", "Stock Actual", "Stock Min", "Tiempo Ent.", "Demanda Anual", "Estado"};
    }

    // 1. Obtener modelo para la JTable (Lectura y conversión para mostrar al usuario)
    public DefaultTableModel obtenerModelo() {
        DefaultTableModel modelo = new DefaultTableModel(getColumnas(), 0);
        
        File file = new File(archivo);
        if (!file.exists()) return modelo;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length == 10) {
                    // Convertimos códigos técnicos a nombres legibles para la tabla
                    datos[2] = codigoANombre(datos[2]);
                    datos[9] = numeroAEstado(datos[9]);
                    modelo.addRow(datos);
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
        return modelo;
    }

    // 2. Búsqueda Flexible
   public DefaultTableModel buscarPorFiltro(String filtro) {
    DefaultTableModel modelo = new DefaultTableModel(getColumnas(), 0);
    String busqueda = filtro.toLowerCase().trim();

    File file = new File(archivo);
    if (!file.exists()) return modelo;

    try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
        String linea;
        while ((linea = br.readLine()) != null) {
            String[] datos = linea.split(",");

            if (datos.length == 10) {

                // Datos reales del archivo
                String codigo = datos[0].toLowerCase();
                String nombre = datos[1].toLowerCase();
                String categoria = codigoANombre(datos[2]).toLowerCase();

                // 🔥 AQUÍ BUSCA EN TODO
                if (codigo.contains(busqueda) || 
                    nombre.contains(busqueda) || 
                    categoria.contains(busqueda)) {

                    // Convertir para mostrar en tabla
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

    // 3. Guardar nuevo producto (Convierte nombre de categoría a código técnico antes de guardar)
   public void escribir(String id, String nom, String catNom, String cos, String pre, String st, String stMin, String tie, String dem, String estNom) throws IOException {
    try (PrintWriter pw = new PrintWriter(new FileWriter(archivo, true))) {
        String linea = id + "," + nom + "," + nombreACodigo(catNom) + "," + cos + "," + pre + "," + st + "," + stMin + "," + tie + "," + dem + "," + estadoANumero(estNom);
        pw.println(linea);
    }
}

    // 4. Actualizar producto existente
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

    // --- MÉTODOS DE CONVERSIÓN TÉCNICA ---

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
}