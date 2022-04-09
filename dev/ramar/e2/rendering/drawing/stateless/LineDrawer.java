package dev.ramar.e2.rendering.drawing.stateless;

import dev.ramar.e2.structures.Colour;


public abstract class LineDrawer
{




    public static class LineMods
    {

        public static enum CapStyle
        {
            BUTT, 
            ROUND,
            SQUARE;
        }

        public static enum JoinStyle
        {
            BEVEL,
            MITER,
            ROUND;
        }


        private int times = 0;

        private double offX = 0, offY = 0;
        private double thickness = 1;
        private Colour colour = new Colour(0, 0, 0, 0);

        private CapStyle cap = CapStyle.SQUARE;
        private JoinStyle join = JoinStyle.MITER;

        private boolean permanent = false;


        public LineMods()
        {

        }

        public LineMods(int times)
        {
            this.times = times;
        }

        /*
        Accessors/Mutators for: cap
        */

        public CapStyle getCapStyle()
        {   return cap;   }

        public LineMods withCapStyle(CapStyle cs)
        {
            cap = cs;
            return this;
        }


        /*
        Accessors/Mutators for: join
        */

        public JoinStyle getJoinStyle()
        {   return join;   }

        public LineMods withJoinStyle(JoinStyle js)
        {
            join = js;
            return this;
        }


        /*
        Accessors/Mutators for: thickness
        */

        public double getThickness()
        {   return thickness;   }

        public LineMods withThickness(double thickness)
        {
            this.thickness = thickness;
            return this;
        }

        /*
        Accessors/Mutators for: permanent
        */
        public boolean isPermanent()
        {   return permanent;   }


        public LineMods withPermanence(boolean b)
        {
            permanent = b;
            return this;
        }


        /*
        Accessors/Mutators for: times
         - 
        */

        public int getTimes()
        {
            return times;
        }

        public LineMods withTimes(int times)
        {
            this.times = times;
            return this;
        }

        public void decrementTimes()
        {
            times--;
        }

        /*
        Accessors/Mutators for: offX, offY
         - 
        */

        public double getOffX() 
        {   return offX;   }

        public double getOffY() 
        {   return offY;   }


        public LineMods withOffX(double offX)
        {
            this.offX = offX;
            return this;
        }


        public LineMods withOffY(double offY)
        {
            this.offY = offY;
            return this;
        }

        public LineMods withOffset(double offX, double offY)
        {
            this.offX = offX;
            this.offY = offY;
            return this;
        }

        public LineMods withAddToOffset(double offX, double offY)
        {
            this.offX += offX;
            this.offY += offY;
            return this;
        }


        /*
        Accessors / Mutators for: colour 
        */

        public Colour getColour()
        {   return colour;   }

        public LineMods withColour(int r, int g, int b, int a)
        {
            colour.set(r, g, b, a);
            return this;
        }

    }


    private LineMods currentMod,
                     tempMod;

    public LineMods withMod()
    {
        return withMods(1);
    }

    public LineMods withMods(int times)
    {
        LineMods exp = new LineMods(times);
        currentMod = exp;

        return exp;
    }

    public LineMods activeMod()
    {
        if( tempMod != null )
            return tempMod;

        return currentMod;
    }

    public LineMods withTempMod()
    {
        return withTempMod(new LineMods());
    }

    public LineMods withTempMod(LineMods lm)
    {
        tempMod = lm;
        return tempMod;
    }

    public void clearTempMod()
    {
        tempMod = null;
    }


    protected final LineMods getMod()
    {
        if( tempMod != null )
            return tempMod;

        LineMods exp = null;
        if( currentMod != null )
        {
            int times = currentMod.getTimes();

            if( times > 0 )
            {
                currentMod.decrementTimes();
                exp = currentMod;
            }
        }
        return exp;
    }

    public abstract void pospos(double x1, double y1, double x2, double y2);

    public abstract void poslen(double x, double y, double xd, double yd);

    public abstract void pos_linked(double[] xs, double[] ys);


}