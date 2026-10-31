package com.je.ruskelleditor.ui;

import javax.swing.*;
import java.awt.*;
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

import static com.je.ruskelleditor.ui.ColorManager.DARK_THEME;
import static com.je.ruskelleditor.ui.ColorManager.THEME_SETTINGS;

public class EditorPanel extends JPanel implements MessageReceiver {
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
        setLayout(new BorderLayout());

        JPanel menu = new JPanel();
        menu.setLayout(new BoxLayout(menu, BoxLayout.X_AXIS));
        mColorManager.addComponent(menu);

        ComponentFactory cFactory = new ComponentFactory();
        menu.add(cFactory.createThemeCheckbox());

        add(menu, BorderLayout.NORTH);
        add(new JScrollPane(mTextArea), BorderLayout.CENTER);
    }

    public void loadFromFile(File file) {
        try {
            FileLoader.instance.loadFile(new FileInputStream(file), mTextArea);
        } catch (IOException ioe) {
            Utils.showException(ioe, this);
        }
    }

    private final class ComponentFactory {
        JCheckBox createThemeCheckbox() {
            JCheckBox darkThemeCB = new JCheckBox("Dark Theme");
            darkThemeCB.setSelected(mColorManager.isDarkTheme());
            darkThemeCB.addChangeListener(_ -> {
                boolean state = darkThemeCB.isSelected();
                Bundle bundle = Bundle.builder()
                        .put(DARK_THEME, state)
                        .build();
                Configuration.storeBundle(THEME_SETTINGS, bundle);
                mColorManager.setDarkTheme(state);
            });
            mColorManager.addComponent(darkThemeCB);
            return darkThemeCB;
        }
    }
}
