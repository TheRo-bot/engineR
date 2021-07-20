package dev.ramar.e2.rendering.drawing.stateless;

import dev.ramar.e2.rendering.Image;

public abstract class ImageDrawer
{

    public static class ImageMods
    {

        private int times = 0;
        private double offX = 0, offY = 0;
        private boolean deletable = false;
        private double scaleX = 0, scaleY = 0, rotZ = 0;

        public enum HoriOff
        {
            LEFT, MIDDLE, RIGHT
        }


        public enum VertOff
        { 
            TOP, MIDDLE, BOTTOM
        }

        private HoriOff horiPos = HoriOff.MIDDLE;
        private VertOff vertPos = VertOff.MIDDLE;


        public ImageMods()
        {
            times = 1;
        }


        public ImageMods(int times)
        {
            this.times = times;
        }

        

        public void doDelete(boolean b)
        {
            deletable = b;
        }


        public boolean isPermanent()
        {
            return deletable;
        }


        public ImageMods withOffset(double x, double y)
        {
            offX += x;
            offY += y;
            return this;
        }


        public ImageMods withAlignment(int hori, int vert)
        {
            withHoriAlignment(hori);
            withVertAlignment(vert);
            return this;
        }


        public ImageMods withHoriAlignment(int hori)
        {
            horiPos = hori < 0  ? HoriOff.LEFT : 
                      hori == 0 ? HoriOff.MIDDLE :
                                  HoriOff.RIGHT;
            return this;
        }

        public ImageMods withVertAlignment(int vert)
        {
            vertPos = vert < 0  ? VertOff.TOP :
                      vert == 0 ? VertOff.MIDDLE :
                                  VertOff.BOTTOM;
            return this;
        }

        public ImageMods withScale(double xAm, double yAm)
        {
            scaleX = xAm;
            scaleY = yAm;
            return this;
        }

        public ImageMods withRotation(double zAm)
        {
            rotZ = zAm;
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


        public double modScaleX(double x)
        {
            return scaleX + x;
        }


        public double modScaleY(double y)
        {
            return scaleY + y;
        }



        public double modRotZ(double z)
        {
            return z + rotZ;
        }



        public int getHoriAlignment()
        {
            switch(horiPos)
            {
                case LEFT:
                    return -1;
                case RIGHT:
                    return 1;
                default:
                    return 0;
            } 
        }

        public int getVertAlignment()
        {
            switch(vertPos)
            {
                case TOP:
                    return -1;
                case BOTTOM:
                    return 1;
                default:
                    return 0;
            } 
        }

    }


    private ImageMods currMods; 


    public ImageMods withMod()
    {
        return withMods(1);
    }

    public ImageMods withMods(int times)
    {
        if( times < 0 )
            throw new IllegalArgumentException("times must be positive!");

        ImageMods im = new ImageMods(times);
        currMods = im;
        return im;
    }


    protected ImageMods getMod()
    {
        ImageMods exp = null;

        // withMod/s both recycle currMods so 
        // we don't need to worry about deleting 
        // currMods if times < 0
        if( currMods != null )
        {
            if( currMods.times > 0 || currMods.isPermanent() )
                exp = currMods;
            currMods.times--;
        }

        return exp;
    }


    public abstract void pos_c(double x, double y, Image i);

    public abstract void pos_tl(double x, double y, Image i);
}