package dev.ramar.e2.rendering.drawing.stateless;

import dev.ramar.e2.structures.Colour;



/*
Abstract Class: RectDrawer
 - A Drawing class for rectangles
*/
public abstract class RectDrawer
{
    protected static Colour DEFAULT_COLOR = new Colour(0, 0, 0, 255);


    public RectDrawer()
    {

    }


    /* Inner Class: RectMods
    -==-------------------
     Handles any modifications someone may want to make for a 
     rectangle. editing this class should make changes to all
     implementing RectDrawers (assuming it's within the bounds
     of the current implementation)
    */
    public class RectMods
    {
        private int times;
        private double offX = 0, offY = 0;
        private Colour colour = new Colour(-1, -1, -1, -1);
        private boolean withFill = false,
                        deletable = true;

        public RectMods(int times)
        {
            this.times = times;
        }

        private void reset(int times)
        {
            this.times = times;
            offX = offY = 0;
            colour.set(-1, -1, -1, -1);
            withFill = false;
        }


        public String toString()
        {
            return "RectMods@" + super.toString().split("RectMods@")[1] + 
                   "[times, offset, colour] -> [" + times + ", (" + offX + ", " + offY + "), (" +
                    colour.getRed() + ", " + colour.getGreen() + ", " +
                    colour.getBlue() + ", " + colour.getAlpha() + ")]";
        }
 

        public void doDelete(boolean b)
        {
            deletable = b;
        }

        public boolean isPermanent()
        {
            return deletable;
        }


        public RectMods withOffset(double x, double y)
        {
            offX = x;
            offY = y;
            return this;
        }

        public RectMods withColour(int r, int g, int b, int a)
        {
            if( r < 0 || r > 255 )
                throw new IllegalArgumentException("red is not within 0 and 255");
            if( g < 0 || g > 255 )
                throw new IllegalArgumentException("green is not within 0 and 255");
            if( b < 0 || b > 255 )
                throw new IllegalArgumentException("blue is not within 0 and 255");
            if( a < 0 || a > 255 )
                throw new IllegalArgumentException("alpha is not within 0 and 255");
            
            colour.set(r, g, b, a);
            return this;
        }

        public RectMods withColour(Colour c)
        {
            return withColour(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
        }

        public RectMods withFill()
        {
            withFill = true;
            return this;
        }

        public RectMods withoutFill()
        {
            withFill = false;
            return this;
        }

        public boolean doFill()
        {
            return withFill;
        }


        public double modX(double x)
        {
            return x + offX;
        }


        public double modY(double y)
        {
            return y + offY;
        }


        public boolean hasColour()
        {
            return colour.getRed() != -1 && 
                   colour.getGreen() != -1 &&
                   colour.getBlue() != -1 &&
                   colour.getAlpha() != -1;
        }

        public int getR()
        {
            return colour.getRed();
        }

        public int getG()
        {
            return colour.getGreen();
        }

        public int getB()
        {
            return colour.getBlue();
        }   

        public int getA()
        {
            return colour.getAlpha();
        }
    }

    private RectMods currMods = null;


    public RectMods withMod()
    {
        return withMods(1);
    }   

    public RectMods withMods(int times)
    {
        RectMods rm = null;
        // if no modification exists then make a new one
        if( currMods == null )
            rm = new RectMods(times);
        // if one does, recycle it!
        else
        {
            currMods.reset(times);
            rm = currMods;
        }
        rm = new RectMods(times);
        currMods = rm;
        return rm;
    }

    protected RectMods getMod()
    {
        RectMods exp = null;
        // withMod/s both recycle currMods so 
        // we don't need to worry about deleting 
        // currMods if times < 0
        if( currMods != null )
        {
            currMods.times--;
            if( currMods.times > 0 || currMods.isPermanent() )
                exp = currMods;
        }

        return exp;
    }



    public abstract void pospos(double x1, double y1, double x2, double y2);

    public abstract void poslen(double x, double y, double w, double h);





}