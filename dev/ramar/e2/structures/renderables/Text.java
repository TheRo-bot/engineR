package dev.ramar.e2.structures.renderables;


import dev.ramar.e2.interfaces.rendering.Renderable;
import dev.ramar.e2.interfaces.rendering.ViewPort;

import dev.ramar.e2.structures.Vec2;
import dev.ramar.e2.structures.Colour;


import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.FontFormatException;

import java.awt.image.BufferedImage;
import java.awt.geom.Rectangle2D;

import java.io.*;
import java.util.*;


public class Text implements Renderable
{
    private static final Graphics2D GRAPHICS_REF = new BufferedImage(1, 1, BufferedImage.TYPE_BYTE_BINARY).createGraphics();

    private Vec2 pos;
    private Vec2 border = new Vec2(0);
    private String text;
    private Font font;
    private FontMetrics metrics;
    private Colour colour;


    public Text(double x, double y, String text, String fontName, int fontSize, Colour c)
    {
        pos = new Vec2(x, y);
        setFont(new Font(fontName, Font.PLAIN, fontSize));
        setText(text);
        colour = c;
    }


    public Text(double x, double y, String text, Font f, Colour c)
    {
        pos = new Vec2(x, y);
        setFont(f);
        setText(text);
        colour = c;
    }


    private void calcBorder()
    {
        String stringBuff = "";

        Rectangle2D rect = metrics.getStringBounds(text, GRAPHICS_REF);
        double w = rect.getWidth();
        double h = rect.getHeight();
        border.set(w, h);

        // int currHeight = metrics.getHeight() / 2;
        // int maxWidth = 0, currWidth = 0;
        // for( int ii = 0; ii < text.length(); ii++ )
        // {
        //     stringBuff += text.charAt(ii);

        //     if( stringBuff.equals("\n") )
        //     {
        //         currHeight += metrics.getHeight() / 2;
        //         if( maxWidth < currWidth )
        //             maxWidth = currWidth;
        //         currWidth = 0;
        //     }

        //     currWidth += metrics.stringWidth(stringBuff);

        //     stringBuff = "";

        // }
        // if( maxWidth < currWidth )
        //     maxWidth = currWidth;

        // border.set(maxWidth, currHeight);
    }


    private void onTextChanged()
    {
        calcBorder();
    }

    public Vec2 getPos()
    {
        return pos;
    }

    public Vec2 getBorder()
    {
        return border;
    }


    private void createFont(String fontName, int fontSize)
    {
        Font exFont = new Font(fontName, Font.PLAIN, fontSize);

        if( exFont == null )
        {

        }
        try
        {
            exFont = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream("resources/fonts/Oswald-Medium.ttf"));
            exFont = exFont.deriveFont((float)fontSize);

        }
        catch(IOException | FontFormatException e) 
        {
            System.out.println("failed! " + e.getMessage());
        }
    }


    private void onFontChanged()
    {
        synchronized(GRAPHICS_REF)
        {
            GRAPHICS_REF.setFont(font);
            metrics = GRAPHICS_REF.getFontMetrics();
        }
    }


    public void setFont(Font f)
    {
        font = f;
        onFontChanged();
    }

    public String getText()
    {
        return text;
    }

    public void setText(String s)
    {
        this.text = s;
        onTextChanged();
    }


    public double getWidth()
    {
        return text == null || metrics == null ? 0 : metrics.stringWidth(text);
    }

    public double getHeight()
    {
        return text == null || metrics == null ? 0 : metrics.getHeight();
    }


    /* Renderable Implementation
    --------------------------------
    */

    public void render(ViewPort vp)
    {
        double x = pos.getX(),
               y = pos.getY();

        Font beforeFont = vp.getFont();
        Colour beforeColour = vp.getColour();

        vp.setColour(colour);
        vp.setFont(font);

        vp.overlayRect(x - 4, y - 4, 8, 8);

        vp.overlayRect(x - border.getX() / 2, y - border.getY() / 2, (int)border.getX(),(int)border.getY());

        vp.overlayAbsText(text, x - border.getX() / 2, y + border.getY() / 2);

        vp.setFont(beforeFont);
        vp.setColour(beforeColour);
    }


    public void drawSelf(Vec2 v, ViewPort vp)
    {
        // double x = pos.getX(),
        //        y = pos.getY();

        // if( v != null )
        // {
        //     x += v.getX();
        //     y += v.getY();
        // }

        // Font beforeFont = vp.getFont();

        // vp.setFont(font);

        // vp.drawText(text, x, y);

        // vp.setFont(beforeFont);

    }

}