package com.je.ruskelleditor;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.function.Consumer;

public final class FileLoader {
    public static final FileLoader instance = new FileLoader();

    private FileLoader(){}

    private JFileChooser createFileChooser(final Consumer<File> consumer) {
        return new JFileChooser(){
            @Override
            public void approveSelection() {
                File f = getSelectedFile();
                consumer.accept(f);
                super.approveSelection();
            }
        };
    }

    public void getDirectory(Consumer<File> consumer) {
        JFileChooser chooser = createFileChooser(consumer);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setDialogTitle("Ruskell Editor - Choose Directory.");
        chooser.showDialog(null, "Select");
    }

    public void loadFile(InputStream inputStream, Target taget) throws IOException {
        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
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
