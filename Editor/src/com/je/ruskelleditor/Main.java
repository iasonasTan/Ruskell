package com.je.ruskelleditor;

import com.je.core.JeApplication;
import com.je.core.JeLib;
import com.je.io.configuration.Configuration;
import com.je.ruskelleditor.ui.GuiInitializer;
import com.je.ruskelleditor.ui.UIUtils;

import java.io.File;

public final class Main extends JeApplication
{
    static void main(String[] args)
    {
        // Initialize all JeLib and Application utility classes
        JeApplication application = new JeApplication();
        Configuration.init("ruskell_editor");
        GuiInitializer initializer = new GuiInitializer(application).initialize();

        // If there are no arguments given
        if(args.length < 1)
        {
            // Launch a file chooser that starts the GUI when a file is selected
            UIUtils.instance.getFile(f -> initializer.start(f), null);
        }
        // If there is at least one argument
        else
        {
            // Use the first one as file path
            File file = new File(args[0]);
            // If it doesn't exist or is invalid
            if(!file.exists() || !file.isFile())
            {
                // Let the user and the developer know and exit
                JeLib.console().error("Invalid file passed " + file.getAbsolutePath() + ".");
                IO.println("Invalid file passed " + file.getAbsolutePath() + ".");
                System.exit(1);
            }
            // Launch GUI
            initializer.start(file);
        }
    }

    // This private constructor prevents initialization
    private Main(){}
}
