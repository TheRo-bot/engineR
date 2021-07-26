package dev.ramar.e2.rendering.drawing.stateless;

import dev.ramar.e2.structures.Colour;

import java.awt.Font;
import java.awt.Color;

public abstract class TextDrawer
{

    /*
    This class needs:
     - A FontLoader
       - Some form of "Font" representation
    */

    /*
    This class should function like:
    
    drawText(String, x, y)
    drawChar(char, x, y)
    drawTextBlock(TextBlock, x, y)**
    with modifications:
        withOffset
        withColour
    
    ** a TextBlock is a class which needs to be made to describe
       sections of text that have their own modifications done 
       to draw separately and specially while looking cool and
       nice
    */




    public static class TextMods
    {
        private int size = 12, times = 1;
        private Font font = new Font("Arial Rounded MT Bold", Font.PLAIN, size);
        private Colour colour = new Colour(255, 255, 255, 255);

        private double halign = 0, valign = 0,
                       xOff = 0, yOff = 0,
                       rotZ = 0;

        private FontStyles style = FontStyles.PLAIN;
        private boolean permanent = false,
                        onlyOffset = false;

        public static enum FontStyles
        {
            BOLD(Font.BOLD), 
            CENTER_BASELINE(Font.CENTER_BASELINE),
            HANGING_BASELINE(Font.HANGING_BASELINE),
            ITALIC(Font.ITALIC),
            LAYOUT_LEFT_TO_RIGHT(Font.LAYOUT_LEFT_TO_RIGHT), 
            LAYOUT_NO_LIMIT_CONTEXT(Font.LAYOUT_NO_LIMIT_CONTEXT),
            PLAIN(Font.PLAIN),
            ROMAN_BASELINE(Font.ROMAN_BASELINE), 
            TRUETYPE_FONT(Font.TRUETYPE_FONT), 
            TYPE1_FONT(Font.TYPE1_FONT);

            private int val;

            FontStyles(int val)
            {
                this.val = val;
            }

            public int getVal()
            {
                return val;
            }
        }

        public TextMods()
        {
            this.times = 1;
        }

        public TextMods(int times)
        {
            this.times = times;
        }

        public void reset(int times)
        {
            this.times = times;

            halign = valign = xOff = yOff = 0.0;
            size = 12; 
            font = new Font("Arial Rounded MT Bold", Font.PLAIN, size);
        }


        public boolean isPermanent()
        {
            return permanent;
        }

        public TextMods withPermanence(boolean b)
        {
            permanent = b;
            return this;
        }

        public TextMods withOffsetOnly(boolean b)
        {
            onlyOffset = b;
            return this;
        }

        public boolean isOffsetAllowed()
        {
            return !onlyOffset;
        }

        public TextMods withOffset(double x, double y)
        {
            xOff += x;
            yOff += y;
            return this;
        }

        public TextMods withFont(String fontName, FontStyles fontType)
        {
            font = new Font(fontName, fontType.getVal(), size);
            return this;
        }

        public TextMods withRotation(double theta)
        {
            rotZ = theta;
            return this;
        }

        public TextMods withFont(Font f)
        {
            this.font = f;
            return this;
        }

        public TextMods withColour(int r, int g, int b, int a)
        {
            colour.set(r, g, b, a);
            return this;
        }

        public TextMods withStyle(FontStyles style)
        {
            this.style = style;
            font = font.deriveFont(style.getVal());
            return this;
        }

        public TextMods withSize(int size)
        {
            this.size = size;
            font = font.deriveFont((float)size);
            return this;
        }

        public TextMods withAlignment(double horilign, double vertlign)
        {
            halign = Math.max(-1.0, Math.min(1.0, horilign));
            valign = Math.max(-1.0, Math.min(1.0, vertlign));
            return this;
        }


        public Font getFont()
        {
            return font;
        }

        public Color getColor()
        {
            return colour.convertToColor();
        }

        public double getRotation()
        {
            return rotZ;
        }

        public Colour getColour()
        {
            return colour;
        }

        public double modX(double x)
        {
            return x + xOff;
        }

        public double modY(double y)
        {
            return y + yOff;
        }

        public double getAlignmentHori()
        {
            return halign;
        }


        public double getAlignmentVert()
        {
            return valign;
        }

    }


    private TextMods currMod = null,
                     tempMod = null;

    public TextMods withMod()
    {
        return withMods(1);
    }

    public TextMods withMods(int times)
    {
        TextMods tm = new TextMods(times);
        currMod = tm;
        return tm;
    }

    public void withTempMod(TextMods tm)
    {
        tempMod = tm;
    }

    public void clearTempMod()
    {
        tempMod = null;
    }



    protected TextMods getMod()
    {
        if( tempMod != null )
            return tempMod;


        // withMod/s both recycle currMod so 
        // we don't need to worry about deleting 
        // currMod if times < 0
        TextMods exp = null;
        if( currMod != null )
        {
            if( currMod.times > 0 || currMod.isPermanent() )
                exp = currMod;
            else if( !currMod.isPermanent() )
                currMod.times--;
        }

        return exp;
    }

    public abstract void pos_c(double x, double y, String s);

}