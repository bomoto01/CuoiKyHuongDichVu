package com.haui.library;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@SpringBootApplication
public class HauiLibraryApplication {

    public static void main(String[] args) {
        loadDotenv();
        SpringApplication.run(HauiLibraryApplication.class, args);
    }

    private static void loadDotenv() {
        String[] candidatePaths = {
            ".env",
            "../.env",
            "backend/.env"
        };
        for (String p : candidatePaths) {
            Path path = Paths.get(p);
            if (Files.exists(path) && Files.isRegularFile(path)) {
                try {
                    List<String> lines = Files.readAllLines(path);
                    for (String line : lines) {
                        line = line.trim();
                        if (line.isEmpty() || line.startsWith("#")) {
                            continue;
                        }
                        int eqIdx = line.indexOf('=');
                        if (eqIdx > 0) {
                            String key = line.substring(0, eqIdx).trim();
                            String value = line.substring(eqIdx + 1).trim();
                            if ((value.startsWith("\"") && value.endsWith("\"")) ||
                                (value.startsWith("'") && value.endsWith("'"))) {
                                value = value.substring(1, value.length() - 1);
                            }
                            if (System.getProperty(key) == null && System.getenv(key) == null) {
                                System.setProperty(key, value);
                            }
                        }
                    }
                    System.out.println("[ENV] Da load bien moi truong tu file: " + path.toAbsolutePath());
                    break;
                } catch (IOException e) {
                    System.err.println("[ENV] Khong the doc file " + p + ": " + e.getMessage());
                }
            }
        }
    }
}
