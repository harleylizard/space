package com.harleylizard.space.input;

import it.unimi.dsi.fastutil.ints.Int2BooleanArrayMap;
import it.unimi.dsi.fastutil.ints.Int2BooleanMap;

import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public final class Mouse {
    private final Int2BooleanMap map = new Int2BooleanArrayMap();

    private double x;
    private double y;

    public void cursorPosCallback(long window, double xpos, double ypos) {
        x = xpos;
        y = ypos;
    }

    public void buttonCallback(long window, int button, int action, int mods) {
        map.put(button, action != GLFW_RELEASE);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public boolean isPressed(int button) {
        return map.get(button);
    }
}
