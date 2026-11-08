package com.je.ruskelleditor;

import javax.swing.*;
import java.io.File;
import java.util.List;

import com.je.core.JeLib;
import com.je.core.broadcast.Message;
import com.je.core.broadcast.MessageReceiver;
import com.je.ruskelleditor.ui.EditorPanel;
import com.je.ruskelleditor.ui.GuiInitializer;
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
        JeApplication application = new JeApplication();
        GuiInitializer initializer = new GuiInitializer(application);
        initializer.initialize();
        initializer.start(file);
    }

    private Main() {}
}
