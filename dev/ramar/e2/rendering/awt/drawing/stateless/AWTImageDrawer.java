package dev.ramar.e2.rendering.awt.drawing.stateless;


import dev.ramar.e2.rendering.awt.AWTViewPort;

import dev.ramar.e2.rendering.drawing.stateless.ImageDrawer;
import dev.ramar.e2.rendering.drawing.stateless.ImageDrawer.ImageMods;

import dev.ramar.e2.rendering.Image;
import dev.ramar.e2.rendering.awt.AWTImage;

import java.awt.Graphics2D;
import java.awt.image.BufferedImageOp;

import java.awt.image.AffineTransformOp;
import java.awt.geom.AffineTransform;

public class AWTImageDrawer extends ImageDrawer
{

    private AWTViewPort vp;

    public AWTImageDrawer()
    {
    }


    public void withViewPort(AWTViewPort vp)
    {
        if( this.vp == null )
        {
            this.vp = vp;
        }
    }

    public Graphics2D getViewPortGraphics()
    {
        if( vp == null )
            throw new NullPointerException("Viewport not set. RectDrawer isn't setup to draw right now!");

        return vp.getGraphics();
    }


    @Override
    public void pos_c(double x, double y, Image i)
    {
        Graphics2D g2d = getViewPortGraphics();


        ImageMods mod = getMod();


        double horilignment = 0,
               vertlignment = 0;

        double scaleX = 0.0, 
               scaleY = 0.0,
               rotZ = i.getRotZ();

        double width = i.getWidth(),
               height = i.getHeight();

        if( mod != null )
        {
            horilignment = mod.getHoriAlignment();
            vertlignment = mod.getVertAlignment();

            x = mod.modX(x);
            y = mod.modY(y);

            scaleX = mod.modScaleX(scaleX);
            scaleY = mod.modScaleY(scaleY);
            rotZ = mod.modRotZ(rotZ);
        }

        // ideally we also include rotation in these calcs
        // but eh
        width  *= scaleX;
        height *= scaleY;



        double rotAncX = 0, rotAncY = 0;

        // -1 -> 1 to 0 -> 2
        rotAncX = (horilignment + 1) * (width / 2);
        rotAncY = (vertlignment + 1) * (height / 2);

        x -= rotAncX;
        y -= rotAncY;
        // System.out.println(": " + rotAncX + ", " + rotAncY + " | " + horilignment + ", " + vertlignment);

        AffineTransform at = new AffineTransform();

        at.translate(rotAncX, rotAncY);
        at.rotate(Math.toRadians(rotZ));
        at.translate(-rotAncX, -rotAncY);
        at.scale(scaleX, scaleY);



        if( at.getDeterminant() == 0 )
            g2d.drawImage(i.getBufferedImage(), null, (int)x, (int)y);
        else
        {
            AffineTransformOp ato = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
            g2d.drawImage(i.getBufferedImage(), ato, (int)x, (int)y);
        }

    }


    public void pos_tl(double x, double y, Image i)
    {
        pos_c(x + i.getWidth() / 2, y + i.getHeight() / 2, i);
    }

}