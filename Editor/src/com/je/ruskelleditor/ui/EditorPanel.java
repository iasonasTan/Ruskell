package com.je.ruskelleditor.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import com.je.ruskelleditor.utils.*;
import com.je.core.broadcast.MessageReceiver;
import com.je.core.broadcast.Message;
import com.je.io.configuration.Configuration;
import com.je.core.util.Bundle;
import com.je.core.JeApplication;

public class EditorPanel extends JPanel implements MessageReceiver {
    private static final String THEME_SETTINGS = "theme.settings";
    private static final String DARK_THEME     = "dark_theme";

    private final ColorManager mColorManager = new ColorManager();
    private final CodeEditor mTextArea = new CodeEditor(mColorManager);

    public EditorPanel(File file, JeApplication application) {
        setPreferredSize(new Dimension(700, 500));
        loadFromFile(file);
        mTextArea.highlight();
        application.registerReceiver(this);

        constructGui();
    }

    @Override
    public void setAction(String action, boolean enabled) {
        throw new UnsupportedOperationException("Actions are immutable!");
    }

    @Override
    public List<String> getActions() {
        return List.of(getClass().getName());
    }

    @Override
    public void onReceive(Message message) {
        // Everything that has a reference to 'application'
        // can send a message to this class.
    }

    private void constructGui() {
        boolean darkTheme = Configuration.loadBundle(THEME_SETTINGS)
                .getBoolean(DARK_THEME, false);
        setLayout(new BorderLayout());

        JPanel menu = new JPanel();
        menu.setLayout(new BoxLayout(menu, BoxLayout.X_AXIS));
        JCheckBox darkThemeCB = new JCheckBox("Dark Theme");
        darkThemeCB.setSelected(darkTheme);
        darkThemeCB.addChangeListener(ce -> {
            boolean state = darkThemeCB.isSelected();
            Bundle bundle = Bundle.builder()
                    .put(DARK_THEME, state)
                    .build();
            Configuration.storeBundle(THEME_SETTINGS, bundle);
            mColorManager.setDarkTheme(state);
        });
        menu.add(darkThemeCB);

        add(menu, BorderLayout.NORTH);
        add(new JScrollPane(mTextArea), BorderLayout.CENTER);

        mColorManager.addComponent(menu);
        mColorManager.addComponent(darkThemeCB);
        mColorManager.setDarkTheme(darkTheme);
    }

    public void loadFromFile(File file) {
        try {
            FileLoader.instance.loadFile(new FileInputStream(file), mTextArea);
        } catch (IOException ioe) {
            Utils.showException(ioe, this);
        }
    }
}
