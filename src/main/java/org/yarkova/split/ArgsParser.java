package org.yarkova.split;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;

import java.io.File;

public class ArgsParser {
    @Option(name = "-o", usage = "базовое имя выходного файла")
    private String o;

    @Option(name = "-d", usage = "Целочисленный суффикс к каждому выходному файлу")
    private boolean d;

    @Option(name = "-l", forbids = {"-c", "-n"}, usage = "размер выходных файлов в строчках")
    private Integer l = null;

    @Option(name = "-c", forbids = {"-l", "-n"}, usage = "размер выходных файлов в символах")
    private Integer c = null;

    @Option(name = "-n", forbids = {"-l", "-c"}, usage = "количество выходных файлов")
    private Integer n = null;

    @Argument(required = true, usage = "имя входного файла")
    private File inputFile;

    @Nullable
    public String getOptionO() {
        return o;
    }

    public boolean getOptionD() {
        return d;
    }

    @Nullable
    public Integer getOptionL() {
        return l;
    }

    @Nullable
    public Integer getOptionC() {
        return c;
    }

    @Nullable
    public Integer getOptionN() {
        return n;
    }

    @NotNull
    public File getInputFileArgument() {
        return inputFile;
    }
}
