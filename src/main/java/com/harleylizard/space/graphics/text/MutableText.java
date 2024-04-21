package com.harleylizard.space.graphics.text;

import com.harleylizard.space.math.Stringifiable;

public final class MutableText implements Stringifiable {
    private final Font font;
    private final int x;
    private final int y;

    private String string;

    public MutableText(Font font, int x, int y) {
        this.font = font;
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Font getFont() {
        return font;
    }

    public void set(String string) {
        this.string = string;
    }

    @Override
    public String stringify() {
        return string;
    }
}
