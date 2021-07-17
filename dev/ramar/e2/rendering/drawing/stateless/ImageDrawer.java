package dev.ramar.e2.rendering.drawing.stateless;


public abstract class ImageDrawer
{

    public static class ImageMods
    {

        private int times;
        private double offX = 0, offY = 0;
        private boolean deletable = false;

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
            offX = x;
            offY = y;
            return this;
        }


        public ImageMods withPosDescription(int hori, int vert)
        {
            withHoriDescription(hori);
            withVertDescription(vert);
            return this;
        }


        public ImageMods withHoriDescription(int hori)
        {
            horiPos = hori < 0  ? HoriOff.LEFT : 
                      hori == 0 ? HoriOff.MIDDLE :
                                  HoriOff.RIGHT;
            return this;
        }

        public ImageMods withVertDescription(int vert)
        {
            vertPos = vert < 0  ? VertOff.TOP :
                      vert == 0 ? VertOff.MIDDLE :
                                  VertOff.BOTTOM;
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


        public int getHoriAlignment()
        {
            switch(horiPos)
            {
                case LEFT:
                    return -1;
                case MIDDLE:
                    return 0;
                case RIGHT:
                    return 1;
            } 
        }

        public int getVertAlignment()
        {
            switch(vertPos)
            {
                case TOP:
                    return -1;
                case MIDDLE:
                    return 0;
                case BOTTOM:
                    return 1;
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
            currMods.times--;
            if( currMods.times > 0 || currMods.isPermanent() )
                exp = currMods;
        }

        return exp;
    }


    public abstract void cpos(double x, double y, Image i);

}