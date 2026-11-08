package com.je.ruskelleditor.ui;

import com.je.core.JeApplication;
import com.je.core.broadcast.Message;
import com.je.core.broadcast.MessageReceiver;

import javax.swing.*;
import java.io.File;
import java.util.List;

public class GuiInitializer {
    static final String ACTION_CHANGE_TITLE = "main.window.title.change";
    static final String BUDNLEE_WIN_TITLE   = "main.window.bundle.e.win.title";
    static final String THEME_SETTINGS      = "theme.settings";
    static final String DARK_THEME          = "dark_theme";
    static final String OUTPUT_FILE_PATH    = ".temp/program.hs";
    static final String FONT_SETTINGS       = "font.settings";
    static final String BUNDLEE_FONT_SIZE   = "font_size";

    private final JeApplication mApplication;
    private JFrame mFrame;

    public GuiInitializer(JeApplication application) {
        this.mApplication = application;
    }

    public void initialize() {
        mFrame = new JFrame("Ruskell Editor");
        mFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        registerFrameReceiver(mFrame, mApplication);
        ColorManager.initialize(mApplication);
        FontManager.initialize();
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

    public void start(File file) {
        mFrame.setContentPane(new EditorPanel(file, mApplication));
        mFrame.pack();
        mFrame.setVisible(true);
    }
}
