package dev.ramar.e2.awt.drawing.text;

import dev.ramar.e2.core.drawing.text.TextDrawer;
import dev.ramar.e2.core.drawing.text.TextMods;

import dev.ramar.e2.core.structures.Colour;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.font.LineMetrics;
import java.awt.FontMetrics;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public class AWTTextDrawer extends TextDrawer
{
    public static class Defaults
    {
        public static final Colour COLOUR = new Colour(255, 255, 255, 255);
    }

    private Graphics2D graphics = null;

    public void setGraphics(Graphics2D graphics)
    {
        this.graphics = graphics;
    }


	public void at(double x, double y, String text)
	{
        Graphics2D g2d = this.graphics;
        if( g2d == null )
            return;


        String fontName = "Default";
        int fontSize = 12;
        Colour colour = Defaults.COLOUR;
        TextMods mod = this.getMod();
        double scale = 1.0;

        if( mod != null )
        {
        	fontName = mod.font.get();
        	fontSize = mod.size.get();
        	colour = mod.colour.get();
        	x += mod.offset.getX();
        	y += mod.offset.getY();

        	scale = mod.scale.get();
        }


        AffineTransform origTrans = g2d.getTransform();
        AffineTransform at = new AffineTransform(origTrans);

        Font origFont = g2d.getFont();
        Font currFont = new Font(fontName, Font.PLAIN, fontSize);

        Color origColor = g2d.getColor();
        Color currColor = colour.convertToColor();

        at.translate(x, y);
        at.scale(scale, scale);

        g2d.setTransform(at);
        g2d.setColor(currColor);
        g2d.setFont(currFont);

        FontMetrics fm = g2d.getFontMetrics();
        float w = fm.stringWidth(text);

        // okay this looks bad, because it is.
        // i don't think there's a nice way to always get the *height*
        // of a given font. so for now, i've made it so that if it detects
        // upper-case, it'll use a different calculation that *looks* right
        // compared to fully lower case. don't judge me.


        boolean isAllLower = true;
        for(int ii = 0; isAllLower && ii < text.length(); ii++ )
        	if( Character.isUpperCase(text.charAt(ii)) )
        		isAllLower = false;

        float h = 0.0f;
        if( isAllLower )
        	h = (fm.getAscent() + fm.getLeading()) * 0.5f;
        else
        	h = fm.getAscent() - fm.getLeading() * 0.5f;


        // g2d.drawRect(0, (int)(h * -1), (int)(w), (int)(h));
        g2d.drawString(text, 0, 0);

        g2d.setTransform(origTrans);
        g2d.setColor(origColor);
        g2d.setFont(origFont);

	}
}