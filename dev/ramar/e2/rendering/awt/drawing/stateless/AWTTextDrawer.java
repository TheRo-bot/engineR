package dev.ramar.e2.rendering.awt.drawing.stateless;


import dev.ramar.e2.rendering.drawing.stateless.TextDrawer;
import dev.ramar.e2.rendering.drawing.stateless.TextDrawer.TextMods;

import dev.ramar.e2.rendering.ViewPort;
import dev.ramar.e2.rendering.awt.AWTViewPort;

import java.awt.Graphics2D;
import java.awt.FontMetrics;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Paint;

import java.awt.geom.Rectangle2D;


import java.awt.image.AffineTransformOp;
import java.awt.geom.AffineTransform;

public class AWTTextDrawer extends TextDrawer
{

    private AWTViewPort vp = null;

    public AWTTextDrawer()
    {

    }


    public AWTTextDrawer withViewPort(AWTViewPort vp)
    {
        this.vp = vp;
        return this;
    }



    public Graphics2D getViewPortGraphics()
    {
        if( vp == null )
            throw new NullPointerException("Viewport not set. RectDrawer isn't setup to draw right now!");

        return vp.getGraphics();
    }


    public void pos_c(double x, double y, String s)
    {
        double origX = x, origY = y;

        Graphics2D g2d = getViewPortGraphics();
        TextMods mod = getMod();

        double horilignment = 0.0,
               vertlignment = 0.0;
        double rotation = 0.0;
        Paint p = g2d.getPaint();

        if( mod != null )
        {
            horilignment = mod.getAlignmentHori();
            vertlignment = mod.getAlignmentVert();

            g2d.setFont(mod.getFont());
            g2d.setPaint(mod.getColor());
            rotation = mod.getRotation();
            x = mod.modX(x);
            y = mod.modY(y);
        }


        FontMetrics fm = g2d.getFontMetrics();

        Rectangle2D r2d = fm.getStringBounds(s, g2d);

        double swidth  = Math.abs(r2d.getX() - r2d.getWidth()),
               sheight = Math.abs(r2d.getY() - r2d.getHeight());



        // System.out.println(swidth + ", " + sheight);
        // x y by default aim for top left, so override that
        // going half width/height
        // x -= swidth / 2;
        // y -= sheight / 2;

        double drawX = x, drawY = y;

        // offset so things are more lined up
        drawY += sheight * 0.625;

        drawX -= swidth / 2;
        drawY -= sheight / 2;


        drawX += (horilignment) * swidth / 2;
        drawY += (vertlignment) * sheight / 2;

        AffineTransform at = new AffineTransform();
        double transX = x - swidth  / 2 - (horilignment * swidth / 2),
               transY = y - sheight / 2 - (vertlignment * sheight / 2);

        // at.translate( transX,  transY);
        // at.rotate(rotation);
        // at.translate(-transX, -transY);

        // AffineTransform before = g2d.getTransform();

        // g2d.setTransform(at);
        g2d.drawString(s, (int)drawX, (int)drawY);

        // g2d.setTransform(before);
        g2d.setPaint(p);
    }


}