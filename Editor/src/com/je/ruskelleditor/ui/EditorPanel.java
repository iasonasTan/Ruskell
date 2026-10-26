package com.je.ruskelleditor.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import com.je.ruskelleditor.utils.*;

public class EditorPanel extends JPanel {
    private final ColorManager mColorManager = new ColorManager();
    private final CodeEditor mTextArea = new CodeEditor(mColorManager);

    public EditorPanel(File file) {
        setPreferredSize(new Dimension(700, 500));
        loadFromFile(file);
        mTextArea.highlight();

        constructGui();

        mColorManager.setDarkTheme(false);
    }

    private void constructGui() {
        setLayout(new BorderLayout());

        JPanel menu = new JPanel();
        menu.setLayout(new BoxLayout(menu, BoxLayout.X_AXIS));
        JCheckBox darkThemeCB = new JCheckBox("Dark Theme");
        darkThemeCB.addChangeListener(ce -> mColorManager.setDarkTheme(darkThemeCB.isSelected()));
        menu.add(darkThemeCB);

        add(menu, BorderLayout.NORTH);
        add(new JScrollPane(mTextArea), BorderLayout.CENTER);

        mColorManager.addComponent(menu);
        mColorManager.addComponent(darkThemeCB);
    }

    public void loadFromFile(File file) {
        try {
            FileLoader.instance.loadFile(new FileInputStream(file), mTextArea);
        } catch (IOException ioe) {
            Utils.showException(ioe, this);
        }
    }
}
