package co.edu.umanizales.automotiveworkshop_api.repository;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Utilidad sencilla para leer/escribir archivos CSV bajo
 * la carpeta del proyecto {@code src/main/resources/data}.
 *
 * Características:
 * - Resuelve la ruta del archivo de datos dentro de la carpeta de recursos.
 * - Si el archivo/carpeta no existe, los crea automáticamente.
 * - Provee métodos para leer todas las líneas y escribir todas las líneas
 *   usando codificación UTF-8.
 *
 * Nota: Esta clase no implementa bloqueo concurrente ni versionado; su uso
 * es didáctico y adecuado para persistencia simple basada en archivos.
 */
public class CsvStorage {
    private static final String DATA_FOLDER = "src/main/resources/data";

    private final String filename;
    private File dataFile;

    public CsvStorage(String filename) {
        this.filename = filename;
        this.dataFile = resolveDataFile();
        ensureFileExists();
    }

    private File resolveDataFile() {
        // Siempre usar la carpeta de datos del proyecto para que los cambios sean visibles en /src/main/resources/data
        return new File(DATA_FOLDER, filename);
    }

    private void ensureFileExists() {
        if (dataFile == null) {
            dataFile = new File(DATA_FOLDER, filename);
        }
        File parent = dataFile.getParentFile();
        if (!parent.exists()) {
            parent.mkdirs();
        }
        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException("Error creando archivo de datos: " + dataFile.getAbsolutePath(), e);
            }
        }
    }

    /**
     * Lee todas las líneas del archivo CSV asociado.
     * @return lista de líneas (incluye encabezado si existe)
     */
    public List<String> readAllLines() {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(dataFile), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error leyendo archivo: " + dataFile.getAbsolutePath(), e);
        }
        return lines;
    }

    /**
     * Escribe todas las líneas en el archivo CSV asociado, reemplazando su contenido.
     * @param lines líneas a escribir (se recomienda incluir la primera línea como encabezado)
     */
    public void writeAllLines(List<String> lines) {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dataFile), StandardCharsets.UTF_8))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error escribiendo archivo: " + dataFile.getAbsolutePath(), e);
        }
    }
}
