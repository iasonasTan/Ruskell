package com.je.ruskelleditor;

import javax.swing.*;
import java.io.File;
import java.util.List;

import com.je.core.JeLib;
import com.je.core.broadcast.Message;
import com.je.core.broadcast.MessageReceiver;
import com.je.ruskelleditor.ui.EditorPanel;
import com.je.ruskelleditor.ui.UIUtils;
import com.je.io.configuration.Configuration;
import com.je.core.JeApplication;

public final class Main extends JeApplication {
    public static final String ACTION_CHANGE_TITLE = "main.window.title.change";
    public static final String BUDNLEE_WIN_TITLE   = "main.window.bundle.e.win.title";

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
        JFrame frame = new JFrame("Ruskell Editor");
        registerFrameReceiver(frame, application);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setContentPane(new EditorPanel(file, application));
        frame.pack();
        frame.setVisible(true);
    }

    private static void registerFrameReceiver(JFrame frame, JeApplication application) {
        application.registerReceiver(new MessageReceiver() {
            @Override
            public void onReceive(Message message) {
                if(ACTION_CHANGE_TITLE.equals(message.getAction())) {
                    frame.setTitle(message.getBundle().getString(BUDNLEE_WIN_TITLE));
                }
            }

            @Override
            public List<String> getActions() {
                return List.of(ACTION_CHANGE_TITLE);
            }

            @Override
            public void setAction(String s, boolean b) {
                throw new UnsupportedOperationException("Actions are immutable!");
            }
        });
    }

    private Main() {}
}
