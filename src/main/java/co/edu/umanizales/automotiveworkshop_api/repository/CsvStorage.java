package co.edu.umanizales.automotiveworkshop_api.repository;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple CSV storage utility that always reads/writes files
 * under src/main/resources/data, trying to use ClassPathResource
 * during development. If the classpath resource is not writable
 * (e.g., running from a packaged JAR), falls back to a relative
 * path under the project directory.
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
        // Always use the project resources data folder so changes are visible in /src/main/resources/data
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
                throw new RuntimeException("Error creating data file: " + dataFile.getAbsolutePath(), e);
            }
        }
    }

    public List<String> readAllLines() {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(dataFile), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading file: " + dataFile.getAbsolutePath(), e);
        }
        return lines;
    }

    public void writeAllLines(List<String> lines) {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dataFile), StandardCharsets.UTF_8))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error writing file: " + dataFile.getAbsolutePath(), e);
        }
    }
}
