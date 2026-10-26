package com.je.ruskelleditor.ui;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import com.je.ruskelleditor.utils.*;
import java.util.List;

public class EditorPanel extends JPanel {
    private final ColorManager mColorManager = new ColorManager();
    private final CodeEditor mTextArea = new CodeEditor(mColorManager);

    public EditorPanel(File file) {
        setPreferredSize(new Dimension(700, 500));
        loadFromFile(file);
        mTextArea.highlight();
        setLayout(new GridLayout(1,1));
        add(new JScrollPane(mTextArea));
        mColorManager.setDarkTheme(true);
    }

    public void loadFromFile(File file) {
        try {
            FileLoader.instance.loadFile(new FileInputStream(file), mTextArea);
        } catch (IOException ioe) {
            Utils.showException(ioe, this);
        }
    }
}
