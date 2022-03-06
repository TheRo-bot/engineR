package dev.ramar.e2.rendering.drawing.stateless;

import dev.ramar.e2.structures.Colour;

import dev.ramar.e2.rendering.drawing.stateful.Rect;

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
    public static class RectMods
    {
        private int times;
        private double offX = 0, offY = 0;
        private Colour colour = new Colour(0, 0, 0, 255);
        private boolean withFill = false,
                        permanent = true,
                        onlyOffset = false;

        public RectMods()
        {
            this.times = 1;
        }

        public RectMods(int times)
        {
            this.times = times;
        }

        protected RectMods(RectMods rm)
        {
            set(rm);
        }

        public void set(RectMods rm)
        {
            this.times      = rm.times;
            this.offX       = rm.offX;
            this.offY       = rm.offY;
            this.colour.set  (rm.colour);
            this.withFill   = rm.withFill;
            this.permanent  = rm.permanent;
            this.onlyOffset = rm.onlyOffset;
        }


        public RectMods duplicate()
        {
            return new RectMods(this);
        }

        public void reset(int times)
        {
            this.times = times;
            offX = offY = 0;
            colour.set(-1, -1, -1, -1);
            withFill = false;
        }


        public String toString()
        {
            return "RectMods: Offset(" + offX + ", " + offY +"), Colour(" + colour + "), " 
             + (withFill ? "Filled" : "Border")
             + ", " + (permanent ? "Temporary(" + times + ")" : "Permanent");
        }
 

        public RectMods withPermanence(boolean b)
        {
            permanent = b;
            return this;
        }

        public boolean isPermanent()
        {
            return permanent;
        }

        /*
        Builder: withOnlyOffset
         - a flag to state the only this Mods' offset 
           is to determine the Rect's position
        */
        public RectMods withOffsetOnly(boolean b)
        {
            onlyOffset = b;
            return this;
        }


        public RectMods withOffset(double x, double y)
        {
            offX += x;
            offY += y;
            return this;
        }

        public RectMods withForcedOffset(double x, double y)
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


        public boolean isOffsetAllowed()
        {
            return ! onlyOffset;
        }


    }

    private RectMods currMods = null;
    private RectMods tempMod = null;

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

    public RectMods activeMod()
    {
        if( tempMod != null )
            return tempMod;
        return currMods;
    }

    public RectMods withTempMod()
    {
        return withTempMod(new RectMods());
    }


    public RectMods withTempMod(RectMods rm)
    {
        tempMod = rm;
        return rm;
    }

    public void clearTempMod()
    {
        tempMod = null;
    }


    protected RectMods getMod()
    {
        if( tempMod != null )
            return tempMod;

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

    public abstract void draw(Rect r);

    public abstract void pospos(double x1, double y1, double x2, double y2);

    public abstract void poslen(double x, double y, double w, double h);


    public abstract void unbound_pospos(double x1, double y1, double x2, double y2);

    public abstract void unbound_poslen(double x, double y, double w , double h);




}