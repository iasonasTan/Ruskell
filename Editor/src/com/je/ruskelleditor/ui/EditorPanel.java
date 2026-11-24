package com.je.ruskelleditor.ui;

import com.je.core.JeApplication;
import com.je.core.broadcast.Message;
import com.je.core.broadcast.MessageReceiver;
import com.je.core.util.Bundle;
import com.je.io.configuration.Configuration;
import com.je.ruskelleditor.utils.FileLoader;
import com.je.ruskelleditor.ui.FontManager;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import static com.je.ruskelleditor.ui.GuiInitializer.*;

class EditorPanel extends JPanel implements MessageReceiver {
    private final CodeEditor mTextArea = new CodeEditor();
    private File mOpenFile;
    private final JeApplication mApplication;

    public EditorPanel(File file, JeApplication application) {
        mApplication = application;
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
        return List.of(DARK_THEME);
    }

    @Override
    public void onReceive(Message message) {
        if(message.getAction().equals(DARK_THEME)) {
            mTextArea.highlight();
        }
    }

    private void constructGui() {
        setLayout(new BorderLayout());

        JPanel menu = new JPanel();
        menu.setLayout(new BoxLayout(menu, BoxLayout.X_AXIS));
        ColorManager.instance().addComponent(menu);
        FontManager.instance().addComponent(mTextArea);

        ComponentFactory cFactory = new ComponentFactory();
        menu.add(cFactory.createThemeCheckbox());
        menu.add(cFactory.createLoadButton());
        menu.add(cFactory.createRunButton());
        menu.add(cFactory.createFontDecreaseButton());
        menu.add(cFactory.createFontIncreaseButton());

        add(menu, BorderLayout.NORTH);
        add(new JScrollPane(mTextArea), BorderLayout.CENTER);
    }

    public void loadFromFile(File file) {
        mTextArea.setText("");
        String windowTitle = "Ruskell Editor";
        try {
            FileLoader.instance.loadFile(new FileInputStream(file), mTextArea);
            mTextArea.highlight();
            mOpenFile = file;
            windowTitle += " - " + file.getName();
        } catch (IOException ioe) {
            UIUtils.showException(ioe, this);
            mTextArea.append("[COULD NOT LOAD FILE]");
            mOpenFile = null;
            windowTitle += " - " + "[ERROR LOADING]";
        }
        Message message = Message.newBuilder()
                    .setAction(ACTION_CHANGE_TITLE)
                    .putExtra(BUDNLEE_WIN_TITLE, windowTitle)
                    .build();
        mApplication.broadcastMessage(message);
    }

    private final class ComponentFactory {
        JCheckBox createThemeCheckbox() {
            JCheckBox darkThemeCB = new JCheckBox("Тёмная тема");
            darkThemeCB.setSelected(ColorManager.instance().isDarkTheme());
            darkThemeCB.addChangeListener(_ -> {
                boolean state = darkThemeCB.isSelected();
                Bundle bundle = Bundle.builder()
                        .put(DARK_THEME, state)
                        .build();
                Configuration.storeBundle(THEME_SETTINGS, bundle);
                ColorManager.instance().setDarkTheme(state);
            });
            ColorManager.instance().addComponent(darkThemeCB);
            FontManager.instance().addComponent(darkThemeCB);
            return darkThemeCB;
        }

        JButton createLoadButton() {
            JButton button = new JButton("Открыть");
            button.addActionListener(_ ->
                    UIUtils.instance.getFile(EditorPanel.this::loadFromFile, EditorPanel.this)
            );
            ColorManager.instance().addComponent(button);
            FontManager.instance().addComponent(button);
            return button;
        }

        JButton createRunButton() {
            JButton button = new JButton("Запустить");
            button.addActionListener(_ -> TerminalPanel.show(mOpenFile));
            ColorManager.instance().addComponent(button);
            FontManager.instance().addComponent(button);
            return button;
        }

        JButton createFontIncreaseButton() {
            JButton button = new JButton("+");
            button.addActionListener(_ -> {
                FontManager.instance().increaseFontSize();
            });
            ColorManager.instance().addComponent(button);
            FontManager.instance().addComponent(button);
            return button;
        }

        JButton createFontDecreaseButton() {
            JButton button = new JButton("-");
            button.addActionListener(_ -> {
                FontManager.instance().decreaseFontSize();
            });
            ColorManager.instance().addComponent(button);
            FontManager.instance().addComponent(button);
            return button;
        }
    }
}
