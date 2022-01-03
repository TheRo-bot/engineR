package dev.ramar.e2.rendering.drawing.stateful;

import dev.ramar.e2.rendering.drawing.stateless.LineDrawer.LineMods;
import dev.ramar.e2.rendering.ViewPort;

public class Line extends Shape
{
    private double x1, y1, x2, y2;

    private LineMods mods = new LineMods().withPermanence(true);

    public Line()
    {

    }

    public Line withStartPos(double x1, double y1)
    {
        this.x1 = x1;
        this.y1 = y1;

        return this;
    }

    public Line withEndPos(double x2, double y2)
    {
        this.x2 = x2;
        this.y2 = y2;

        return this;
    }


    public LineMods getMod()
    {
        return mods;
    }


    public void drawAt(double x, double y, ViewPort vp)
    {
        vp.draw.stateless.line.withTempMod(mods);

        mods.withAddToOffset( x,  y);
        vp.draw.stateless.line.pospos(x1, y1, x2, y2);
        mods.withAddToOffset(-x, -y);
        vp.draw.stateless.line.clearTempMod();
    }

}