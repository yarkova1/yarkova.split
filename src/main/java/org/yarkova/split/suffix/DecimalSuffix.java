package org.yarkova.split.suffix;

public class DecimalSuffix extends Suffix{
    private int decimal = 1;

    @Override
    public String nextSuffix() {
        decimal++;
        return String.valueOf(decimal - 1);
    }
}
