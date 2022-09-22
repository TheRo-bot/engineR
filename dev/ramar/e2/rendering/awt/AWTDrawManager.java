package dev.ramar.e2.rendering.awt;

import dev.ramar.e2.rendering.DrawManager;

public class AWTDrawManager extends DrawManager
{
    public AWTDrawManager()
    {}

    private Graphics2D graphics = null;

    public void setGraphics(Graphics2D g2d)
    {
        this.graphics = g2d;
    }



}