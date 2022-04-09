package dev.ramar.e2.rendering.drawing;

import java.awt.BasicStroke;

public enum JoinStyle
{
    Bevel(BasicStroke.JOIN_BEVEL), Miter(BasicStroke.JOIN_MITER),
    Round(BasicStroke.JOIN_ROUND);

    private int id;
    JoinStyle(int id) { this.id = id; }

    public int intify()
    {
        return id;
    }
}