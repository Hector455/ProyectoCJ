package proyectocbjc;

import java.io.*;
import java.text.DecimalFormat;

public class ManejoDeConfiguracion {

    private static final String ARCHIVO = "ConfiguracionCsv.csv";
    private static final DecimalFormat FMT_DINERO = new DecimalFormat("0.00");

    // ─── Verifica si el archivo existe ───────────────────────────────────────
    public boolean archivoExiste() {
        return new File(ARCHIVO).exists();
    }

    // ─── Lee los 3 valores del archivo ───────────────────────────────────────
    // Retorna double[]{costoPedido, costoMantenimiento, tiempoEntrega}
    // Si el archivo no existe o está dañado retorna {0, 0, 0}
    public double[] leerConfiguracion() {
        if (!archivoExiste()) return new double[]{0, 0, 0};

        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO))) {
            String linea = br.readLine();
            if (linea == null) return new double[]{0, 0, 0};

            String[] partes = linea.split(",");
            if (partes.length < 3) return new double[]{0, 0, 0};

            double costoPedido        = Double.parseDouble(partes[0].trim());
            double costoMantenimiento = Double.parseDouble(partes[1].trim());
            double tiempoEntrega      = Double.parseDouble(partes[2].trim());

            return new double[]{costoPedido, costoMantenimiento, tiempoEntrega};

        } catch (Exception e) {
            e.printStackTrace();
            return new double[]{0, 0, 0};
        }
    }

    // ─── Guarda o sobreescribe el archivo ────────────────────────────────────
    public void guardarConfiguracion(double costoPedido,
                                     double costoMantenimiento,
                                     int    tiempoEntrega) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARCHIVO, false))) {
            pw.println(costoPedido + "," + costoMantenimiento + "," + tiempoEntrega);
        }
    }

    // ─── Formatea un valor monetario para mostrarlo en el JTextField ─────────
    public String formatearDinero(double valor) {
        return FMT_DINERO.format(valor);
    }

    // ─── Validaciones ────────────────────────────────────────────────────────

    // Valida que el texto sea un número mayor que 0 (admite decimales)
    public boolean esDineroValido(String texto) {
        try {
            double v = Double.parseDouble(texto.trim().replace(",", "."));
            return v > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Valida que el texto sea un entero mayor que 0 (sin decimales)
    public boolean esTiempoValido(String texto) {
        try {
            String t = texto.trim();
            // Rechaza cualquier punto decimal
            if (t.contains(".") || t.contains(",")) return false;
            int v = Integer.parseInt(t);
            return v > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}