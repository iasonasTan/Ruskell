package com.je.ruskelleditor;

import com.je.core.JeApplication;
import com.je.core.JeLib;
import com.je.io.configuration.Configuration;
import com.je.ruskelleditor.ui.GuiInitializer;
import com.je.ruskelleditor.ui.UIUtils;

import java.io.File;

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
        JeApplication application = new JeApplication();
        GuiInitializer initializer = new GuiInitializer(application);
        initializer.initialize();
        initializer.start(file);
    }

    private Main() {}
}
