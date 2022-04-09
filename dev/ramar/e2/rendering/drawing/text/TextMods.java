package dev.ramar.e2.rendering.drawing.text;

import dev.ramar.e2.rendering.drawing.Mod;
import dev.ramar.e2.rendering.drawing.mod_helpers.ModHelperOwner;

import dev.ramar.e2.rendering.drawing.mod_helpers.SizeHelper;
import dev.ramar.e2.rendering.drawing.mod_helpers.OffsetHelper;
import dev.ramar.e2.rendering.drawing.mod_helpers.ColourHelper;
import dev.ramar.e2.rendering.drawing.mod_helpers.FontHelper;

import java.awt.Graphics;
import java.awt.FontMetrics;
import java.awt.Font;

import java.awt.image.BufferedImage;

public class TextMods implements Mod, ModHelperOwner
{
    private static final Graphics METRICS_MAKER = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).getGraphics();

    public TextMods() {}

    public final OffsetHelper<TextMods> offset = new OffsetHelper<>(this);

    public final ColourHelper<TextMods> colour = new ColourHelper<>(this);

    public final SizeHelper<TextMods> size = new SizeHelper<>(this, 12);

    public final FontHelper<TextMods> font = new FontHelper<>(this);


    public double getWidthOfText(String s)
    {
        FontMetrics fm = null;

        synchronized(TextMods.METRICS_MAKER)
        {
            TextMods.METRICS_MAKER.setFont(this.font.get());
            fm = TextMods.METRICS_MAKER.getFontMetrics();
        }

        double out = 0;
        
        if( fm != null )
            out = fm.stringWidth(s);

        return out;
    }

}