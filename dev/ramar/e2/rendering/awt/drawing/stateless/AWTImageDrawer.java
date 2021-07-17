package dev.ramar.e2.rendering.awt.drawing.stateless;


import dev.ramar.e2.rendering.drawing.stateless.ImageDrawer;
import dev.ramar.e2.rendering.drawing.stateless.ImageDrawer.ImageMods;


public class AWTImageDrawer extends ImageDrawer
{

    private AWTViewPort vp;

    public AWTImageDrawer()
    {
    }


    public void withViewPort(AWTViewPort vp)
    {
        if( this.vp == null )
        {
            this.vp = vp;
        }
    }

    public Graphics2D getViewPortGraphics()
    {
        if( vp == null )
            throw new NullPointerException("Viewport not set. RectDrawer isn't setup to draw right now!");

        return ((AWTStatelessDrawer)vp.draw.stateless).getGraphics();
    }


    @Override
    public void cpos(double x, double y, Image i)
    {
        Graphics2D g2d = getViewPortGraphics();

        ImageMods mod = getMod();

        int horilignment = mod.getHoriAlignment(),
            vertlignment = mod.getVertAlignment();

        // left, center, right
             // left ? do nothing
        x += horilignment < 0  ? 0 :
             // middle ? take half width
             horilignment == 0 ? -i.getWidth()/2 :
             // right ? take whole width
                                 -i.getWidth();

            // top ? do nothing
        y += vertlignment < 0  ? 0 :
            // middle ? half height
             vertlignment == 0 ? -i.getHeight()/2 :
            // right? whole height
                                 -i.getHeight();


        x = mod.modX(x);
        y = mod.modY(y);

        g2d.drawImage(i.getBufferedImage(), null, x, y);
    }
}