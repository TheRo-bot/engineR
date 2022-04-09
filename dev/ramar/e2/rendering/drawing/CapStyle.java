package dev.ramar.e2.rendering.drawing;

import java.awt.BasicStroke;

public enum CapStyle
{
    Butt(BasicStroke.CAP_BUTT), Round(BasicStroke.CAP_ROUND),
    Square(BasicStroke.CAP_SQUARE);

    private int id;
    CapStyle(int id) { this.id = id; }

    public int intify()
    {
        return id;
    }
}