package com.je.ruskelleditor;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class Main {
    static void main(String[] args) {
        if(args.length < 1) {
            FileLoader.instance.getDirectory(Main::startEditor);
        } else {
            startEditor(new File(args[0]));
        }
    }

    static void startEditor(File file) {
        JFrame frame = new JFrame("Ruskell Editor");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setContentPane(new EditorPanel(file));
        frame.pack();
        frame.setVisible(true);
    }
}
