package dev.ramar.e2.rendering.drawing.stateless;

import dev.ramar.e2.rendering.Image;

public abstract class ImageDrawer
{

    public static class ImageMods
    {

        private int times = 0;
        private boolean deletable = false;
        // affine transformations
        private double offX = 0, offY = 0,
                       scaleX = 0, scaleY = 0,
                       rotZ = 0;
        // alignment
        private double hAlign = 0, vAlign = 0;

        public enum HoriOff
        {
            LEFT, MIDDLE, RIGHT
        }


        public enum VertOff
        { 
            TOP, MIDDLE, BOTTOM
        }


        public ImageMods()
        {
            times = 1;
        }


        public ImageMods(int times)
        {
            this.times = times;
        }

        public ImageMods reset(int times)
        {
            hAlign = vAlign =
            offX = offY =
            scaleX = scaleY =
            rotZ = 0.0;
            this.times = times;

            deletable = false;
            
            return this;
        }

        public String toString()
        {
            return "ImageMods: Scale(" + scaleX + ", " + scaleY + "), "
                 + "Rotation(" + rotZ + "), " + "Translation(" + offX + ", " + offY + "), "
                 + "Alignment(" + hAlign + ", " + vAlign + "), "
                 + "Times(" + times + "), " + (deletable ? "Temporary(" + times + ")" : "Permanent"); 
        }

        



        public boolean isPermanent()
        {
            return deletable;
        }


        public ImageMods withDelete(boolean b)
        {
            deletable = b;
            return this;
        }


        public ImageMods withOffset(double x, double y)
        {
            offX += x;
            offY += y;
            return this;
        }

        public ImageMods withOffsetOnly(double x, double y)
        {
            offX = x;
            offY = y;
            return this;
        }


        public ImageMods withAlignment(double hori, double vert)
        {
            withHoriAlignment(hori);
            withVertAlignment(vert);
            return this;
        }


        public ImageMods withHoriAlignment(double hori)
        {
            hAlign = Math.max(-1, Math.min(1.0, hori));
            return this;
        }

        public ImageMods withVertAlignment(double vert)
        {
            vAlign = Math.max(-1, Math.min(1.0, vert));
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


        public double getHoriAlignment()
        {
            return hAlign;
        }

        public double getVertAlignment()
        {
            return vAlign;
        }

    }


    private ImageMods currMods, tempMod; 

    public ImageMods withMod()
    {
        return withMods(1);
    }

    public ImageMods withMods(int times)
    {
        if( times < 0 )
            throw new IllegalArgumentException("times must be positive!");

        ImageMods im = null;
        if( currMods != null )
            im = currMods.reset(times);
        else
            im = new ImageMods(times);


        currMods = im;
        return im;
    }


    public ImageMods withTempMod(ImageMods im)
    {
        tempMod = im;
        return im; 
    }


    public void clearTempMod()
    {
        tempMod = null;
    }



    protected ImageMods getMod()
    {
        if( tempMod != null )
            return tempMod;
        
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