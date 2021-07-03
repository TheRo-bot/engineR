package dev.ramar.e2.rendering.drawing.stateless;

import dev.ramar.e2.structures.Colour;

public abstract class RectDrawer
{


    public RectDrawer()
    {

    }



    public class RectMods
    {
        private int times;
        private double offX = 0, offY = 0;
        private Colour colour;

        public RectMods(int times)
        {
            this.times = times;
        }


        public RectMods withAnchor(double x, double y)
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
            
            colour = new Colour(r, g, b, a);
            return this;
        }



        public double modX(double x)
        {
            return x + offX;
        }


        public double modY(double y)
        {
            return y + offY;
        }
    }

    private RectMods currMods = null;


    public RectMods withMods()
    {
        return withMods(1);
    }   

    public RectMods withMods(int times)
    {
        RectMods rm = new RectMods(times);
        currMods = rm;
        return rm;
    }

    protected RectMods getMod()
    {
        RectMods exp = null;
        if( currMods != null )
        {
            currMods.times--;
            if( currMods.times <= 0 )
                currMods = null;

        }
        exp = currMods;
        return exp;
    }



    public abstract void pospos(double x1, double y1, double x2, double y2);

    public abstract void poslen(double x, double y, double w, double h);





}