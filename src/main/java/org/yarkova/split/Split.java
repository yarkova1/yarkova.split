package org.yarkova.split;

import org.jetbrains.annotations.NotNull;
import org.yarkova.split.suffix.Suffix;

import java.io.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Split {
    private final String outputFilename;
    private final Suffix outputFilenameSuffix;
    private final File inputFile;

    public Split(String outputFilename, Suffix outputFilenameSuffix, File inputFile) {
        this.outputFilename = outputFilename;
        this.outputFilenameSuffix = outputFilenameSuffix;
        this.inputFile = inputFile;
    }

    @NotNull
    public static String extensionIfExist(@NotNull String filename) {
        int dotPosition = filename.lastIndexOf('.');

        if (dotPosition == -1) return "";
        return filename.substring(dotPosition);
    }

    public void splitByLines(int lines) throws IOException {
        if (!inputFile.exists()) throw new FileNotFoundException("Input file " + inputFile + " not found");

        AtomicInteger lineNumber = new AtomicInteger();
        AtomicReference<String> outName = new AtomicReference<>(outputFilename + outputFilenameSuffix.nextSuffix() + extensionIfExist(inputFile.getName()));
        AtomicReference<FileWriter> writer = new AtomicReference<>(new FileWriter(outName.get()));

        BufferedReader reader = new BufferedReader(new FileReader(inputFile));

        reader.lines().forEach(ThrowingConsumer.throwingConsumerWrapper(line -> {
            if (lineNumber.get() < lines) {
                writer.get().write(line.concat(System.lineSeparator()));
                    lineNumber.getAndIncrement();
            } else {
                writer.get().close();
                outName.set(outputFilename + outputFilenameSuffix.nextSuffix() + extensionIfExist(inputFile.getName()));
                writer.set(new FileWriter(outName.get()));
                writer.get().write((line.concat(System.lineSeparator())));
                lineNumber.set(1);
            }
        }));

        reader.close();
        writer.get().close();
    }

    public void splitByCharacters(int chars) throws IOException {
        if (!inputFile.exists()) throw new FileNotFoundException("Input file " + inputFile + " not found");

        int charNumber = 0;

        String outName = outputFilename + outputFilenameSuffix.nextSuffix() + extensionIfExist(inputFile.getName());
        FileWriter writer = new FileWriter(outName);

        FileReader reader = new FileReader(inputFile);
        int character = reader.read();

        while (character != -1) {
            if (charNumber < chars) {
                writer.write((char) character);
                charNumber++;
            } else {
                writer.close();
                outName = outputFilename + outputFilenameSuffix.nextSuffix() + extensionIfExist(inputFile.getName());
                writer = new FileWriter(outName);
                writer.write((char) character);
                charNumber = 1;
            }
            character = reader.read();
        }

        reader.close();
        writer.close();
    }

    public void splitByFiles(int files) throws IOException {
        if (!inputFile.exists()) throw new FileNotFoundException("Input file " + inputFile + " not found");

        int charsInFile = (int) Math.ceil(charsInFile(inputFile.getName()) / (double) files);

        splitByCharacters(charsInFile);
    }

    // возвращает количество символов в строке
    public static int charsInFile(@NotNull String filename) throws IOException {
        FileReader fr = new FileReader(filename);
        int fileTextLen = 0;
        while (fr.read() != -1) {
            fileTextLen++;
        }
        fr.close();

        return fileTextLen;
    }
}
