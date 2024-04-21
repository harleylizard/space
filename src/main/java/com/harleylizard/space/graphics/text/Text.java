package com.harleylizard.space.graphics.text;

import com.harleylizard.space.math.Stringifiable;

public final class Text implements Stringifiable {
    private final Font font;
    private final String string;
    private final int x;
    private final int y;

    public Text(Font font, String string, int x, int y) {
        this.font = font;
        this.string = string;
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

    @Override
    public String stringify() {
        return string;
    }
}
