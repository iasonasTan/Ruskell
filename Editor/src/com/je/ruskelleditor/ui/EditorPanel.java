package com.je.ruskelleditor.ui;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import com.je.core.broadcast.MessageReceiver;
import com.je.core.broadcast.Message;
import com.je.io.configuration.Configuration;
import com.je.core.util.Bundle;
import com.je.core.JeApplication;
import com.je.ruskelleditor.utils.FileLoader;

import static com.je.ruskelleditor.ui.ColorManager.DARK_THEME;
import static com.je.ruskelleditor.ui.ColorManager.THEME_SETTINGS;

public class EditorPanel extends JPanel implements MessageReceiver {
    private final CodeEditor mTextArea = new CodeEditor();

    public EditorPanel(File file, JeApplication application) {
        setPreferredSize(new Dimension(700, 500));
        loadFromFile(file);
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
        setLayout(new BorderLayout());

        JPanel menu = new JPanel();
        menu.setLayout(new BoxLayout(menu, BoxLayout.X_AXIS));
        ColorManager.instance.addComponent(menu);

        ComponentFactory cFactory = new ComponentFactory();
        menu.add(cFactory.createThemeCheckbox());
        menu.add(cFactory.createLoadButton());

        add(menu, BorderLayout.NORTH);
        add(new JScrollPane(mTextArea), BorderLayout.CENTER);
    }

    public void loadFromFile(File file) {
        try {
            mTextArea.setText("");
            FileLoader.instance.loadFile(new FileInputStream(file), mTextArea);
            mTextArea.highlight();
        } catch (IOException ioe) {
            UIUtils.showException(ioe, this);
        }
    }

    private final class ComponentFactory {
        JCheckBox createThemeCheckbox() {
            JCheckBox darkThemeCB = new JCheckBox("Dark Theme");
            darkThemeCB.setSelected(ColorManager.instance.isDarkTheme());
            darkThemeCB.addChangeListener(_ -> {
                boolean state = darkThemeCB.isSelected();
                Bundle bundle = Bundle.builder()
                        .put(DARK_THEME, state)
                        .build();
                Configuration.storeBundle(THEME_SETTINGS, bundle);
                ColorManager.instance.setDarkTheme(state);
            });
            ColorManager.instance.addComponent(darkThemeCB);
            return darkThemeCB;
        }

        JButton createLoadButton() {
            JButton button = new JButton("Open");
            button.addActionListener(_ ->
                    UIUtils.instance.getDirectory(EditorPanel.this::loadFromFile, EditorPanel.this)
            );
            ColorManager.instance.addComponent(button);
            return button;
        }
    }
}
