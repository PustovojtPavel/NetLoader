package org.example;

import lombok.extern.slf4j.Slf4j;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

@Slf4j
public class Application {

    public static void main(String[] args) {

        ApplicationSettings settings = getApplicationSettings(args);
        if (null == settings) {
            return;
        }


    }

    private static ApplicationSettings getApplicationSettings(String[] args) {

        ApplicationSettings settings = new ApplicationSettings();
        CmdLineParser parser = new CmdLineParser(settings);
        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            parser.printUsage(System.err);
            return null;
        }
        return settings;
    }
}
