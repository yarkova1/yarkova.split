package org.yarkova.split;

import org.jetbrains.annotations.NotNull;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.yarkova.split.suffix.DecimalSuffix;
import org.yarkova.split.suffix.StringSuffix;
import org.yarkova.split.suffix.Suffix;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
    private static final int DEFAULT_LINES = 100;

    public static void main(String[] args) {
        ArgsParser parsedStorage = new ArgsParser();
        CmdLineParser cmdLineParser = new CmdLineParser(parsedStorage);
        try {
            cmdLineParser.parseArgument(args);
        } catch (CmdLineException e) {
            System.err.println("Unable parse arguments!");
            System.err.println(e.getLocalizedMessage());
            System.exit(-1);
        }

        File inputFile = parsedStorage.getInputFileArgument();

        // set output filename
        String o = parsedStorage.getOptionO();
        String outputFilename;
        if (o == null) outputFilename = "x";
        else if (o.equals("-")) outputFilename = getNameWithoutExtension(inputFile.getName());
        else outputFilename = o;

        Suffix outputFilenameSuffix = parsedStorage.getOptionD() ? new DecimalSuffix() : new StringSuffix();

        Integer l = parsedStorage.getOptionL();
        Integer c = parsedStorage.getOptionC();
        Integer n = parsedStorage.getOptionN();

        if (l == null && c == null && n == null) {
            l = DEFAULT_LINES;
        } else if ((l == null || l <= 0) && (c == null || c <= 0) && (n == null || n <= 0)) {
            System.err.println("One of pointed option: -l, or -c, or -n must be positive!");
            System.exit(-1);
        }

        Split split = new Split(outputFilename, outputFilenameSuffix, inputFile);

        try {
            if (c != null) {
                split.splitByCharacters(c);
            } else if (n != null) {
                split.splitByFiles(n);
            } else {
                split.splitByLines(l);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Input file with name " + inputFile + " not found!");
            System.exit(-1);
        }
        catch (IOException e) {
            System.err.println(e.getLocalizedMessage());
            System.exit(-1);
        }

    }

    @NotNull
    public static String getNameWithoutExtension(@NotNull String filename) {
        int dotIndex = filename.lastIndexOf('.');
        return (dotIndex == -1) ? filename : filename.substring(0, dotIndex);
    }
}
