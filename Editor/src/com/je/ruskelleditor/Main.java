package com.je.ruskelleditor;

import javax.swing.*;
import java.io.File;

import com.je.core.JeLib;
import com.je.ruskelleditor.ui.EditorPanel;
import com.je.ruskelleditor.ui.UIUtils;
import com.je.io.configuration.Configuration;
import com.je.core.JeApplication;

public final class Main extends JeApplication {
    static void main(String[] args) {
        Configuration.init("ruskell_editor");
        if(args.length < 1) {
            UIUtils.instance.getFile(Main::startEditor, null);
        } else {
            File file = new File(args[0]);
            if(!file.exists() || !file.isFile()) {
                JeLib.console().error("Invalid file passed " + file.getAbsolutePath() + ".");
                System.exit(1);
            }
            startEditor(file);
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
