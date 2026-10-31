package com.je.ruskelleditor.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public final class FileLoader {
    public static final FileLoader instance = new FileLoader();

    private FileLoader(){}

    public void loadFile(InputStream inputStream, Target taget) throws IOException {
        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while((line = bufferedReader.readLine()) != null) {
                taget.append(line);
            }
        }
    }

    @FunctionalInterface
    public interface Target {
        void append(String line);
    }
}
