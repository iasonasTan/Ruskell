package com.je.ruskelleditor;

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
import java.util.List;

public class EditorPanel extends JPanel {
    private final CodeEditor mTextArea = new CodeEditor();

    public EditorPanel(File file) {
        setPreferredSize(new Dimension(700, 500));
        loadFromFile(file);
        mTextArea.highlight();
        setLayout(new GridLayout(1,1));
        add(new JScrollPane(mTextArea));
    }

    public void loadFromFile(File file) {
        try {
            FileLoader.instance.loadFile(new FileInputStream(file), mTextArea);
        } catch (IOException ioe) {
            Utils.showException(ioe, this);
        }
    }

    private static final class CodeEditor extends JTextArea implements FileLoader.Target {
        private final java.util.List<String> mKeywords = new ArrayList<>();

        public CodeEditor() {
            loadKeywords();
            addKeyListener(new KeyListener() {
                @Override public void keyTyped(KeyEvent keyEvent) {}
                @Override public void keyPressed(KeyEvent keyEvent) {}

                @Override
                public void keyReleased(KeyEvent keyEvent) {
                    highlight();
                }
            });
        }

        private void loadKeywords() {
            FileLoader.Target target = line -> mKeywords.add(line.replace("\n", ""));
            try {
                FileLoader.instance.loadFile(getClass().getResourceAsStream("/keywords.txt"), target);
            } catch (IOException e) {
                Utils.showException(e, this);
            }
        }

        @Override
        public void append(String text) {
            super.append(text+'\n');
        }

        public void highlight() {
            getHighlighter().removeAllHighlights();
            for(String keyword: mKeywords) {
                List<Integer[]> locations = Utils.findWord(getText(), keyword);
                var highlighter = new DefaultHighlighter.DefaultHighlightPainter(Color.BLUE);
                try {
                    for(Integer[] location: locations) {
                        getHighlighter().addHighlight(location[0], location[1], highlighter);
                    }
                } catch (BadLocationException e) {
                    // ignore
                }
            }
        }
    }
}
