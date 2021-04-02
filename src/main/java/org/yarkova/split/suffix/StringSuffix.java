package org.yarkova.split.suffix;

import org.jetbrains.annotations.NotNull;

public class StringSuffix extends Suffix {
    private static final char FROM = 'a';
    private static final char TO = 'z';
    private final StringBuilder chars;

    public StringSuffix() {
        int DEFAULT_SUFFIX_LEN = 2;
        chars = new StringBuilder(String.valueOf(FROM).repeat(DEFAULT_SUFFIX_LEN));

    }

    @NotNull
    @Override
    public String nextSuffix() {
        String result = chars.toString();

        for (int i = chars.length() - 1; i >= 0; i--) {
            if (chars.charAt(i) == TO) {
                chars.setCharAt(i, FROM);

                //  переполнение
                if (i == 0) {
                    chars.insert(0, FROM);
                    break;
                }
            } else {
                chars.setCharAt(i, (char) (chars.charAt(i) + 1));
                break;
            }
        }

        return result;
    }
}
