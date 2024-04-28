package com.harleylizard.space.graphics;

public final class Validator {

    private Validator() {}

    public static void assertValid(String string) {
        if (string.length() != 51) {
            throw new RuntimeException("-sad face- :(");
        }
    }
}
