package com.je.ruskelleditor.ui;

import javax.swing.*;
import java.awt.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import com.je.ruskelleditor.utils.*;
import java.io.IOException;

final class CodeEditor extends JTextArea implements FileLoader.Target {
    private final java.util.List<String> mKeywords = new ArrayList<>();
    private final ColorManager mColorManager;

    public CodeEditor(ColorManager cm) {
        mColorManager = cm;
        cm.addComponent(this);
        loadKeywords();
        addKeyListener(new EditorKeyListener());
    }

    private void loadKeywords() {
        FileLoader.Target target = line -> {
            String[] keywords = line.split("///");
            if(keywords.length >= 1) {
                mKeywords.add(keywords[0]);
            }
        };
        try {
            FileLoader.instance.loadFile(getClass().getResourceAsStream("/keywords"), target);
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
            var highlighter = new DefaultHighlighter.DefaultHighlightPainter(mColorManager.getHighlightColor());
            try {
                for(Integer[] location: locations) {
                    getHighlighter().addHighlight(location[0], location[1], highlighter);
                }
            } catch (BadLocationException e) {
                // ignore
            }
        }
    }

    private class EditorKeyListener implements KeyListener {
        @Override
        public void keyTyped(KeyEvent keyEvent) {

        }

        @Override
        public void keyPressed(KeyEvent keyEvent) {

        }

        @Override
        public void keyReleased(KeyEvent keyEvent) {
            highlight();
        }
    }
}
