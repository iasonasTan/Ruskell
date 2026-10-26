package com.je.ruskelleditor;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import com.je.ruskelleditor.ui.EditorPanel;
import com.je.ruskelleditor.utils.FileLoader;
import com.je.io.configuration.Configuration;
import com.je.core.JeApplication;

public final class Main extends JeApplication {
    static void main(String[] args) {
        Configuration.init("ruskell_editor");
        if(args.length < 1) {
            FileLoader.instance.getDirectory(Main::startEditor);
        } else {
            startEditor(new File(args[0]));
        }
    }

    static void startEditor(File file) {
        JFrame frame = new JFrame("Ruskell Editor");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setContentPane(new EditorPanel(file, new JeApplication()));
        frame.pack();
        frame.setVisible(true);
    }

    private Main() {}
}
