package dev.ramar.e2;

import dev.ramar.e2.awt.rendering.AWTWindow;

public class EngineR2
{

    public final AWTWindow window;

    public EngineR2()
    {
        this.window = new AWTWindow();
    }

    public EngineR2(AWTWindow window)
    {
        this.window = window;
    }
}