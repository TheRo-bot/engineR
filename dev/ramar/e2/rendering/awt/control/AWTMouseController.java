package dev.ramar.e2.rendering.awt.control;


import dev.ramar.e2.rendering.control.MouseController;
import dev.ramar.e2.rendering.awt.AWTViewPort;
import dev.ramar.e2.rendering.awt.AWTWindow;
import dev.ramar.e2.structures.Vec2;

import java.awt.PointerInfo;
import java.awt.MouseInfo;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;


public class AWTMouseController extends MouseController
{
    // protected void onPress(int button, double x, double y)

    // protected void onRelease(int button, double x, double y)


    /*
    Method: convertX
     - Converts <x> from window position (from top left of window being 0, 0)
       to world position (origin is {0, 0}, center of screen when no translations
       have occurred)
    */
    private double convertX(double x)
    {
        double xP = x / vp.window.width();
        x = xP * vp.getLogicalWidth();

        x -= vp.window.width() / 2;
        x -= vp.getCenterX();

        return x;
    }


    private double convertY(double y)
    {
        // convert y from top-left window position to 
        // top left viewport position
        double yP = y / vp.window.height();
        y = yP * vp.getLogicalHeight();
        // convert top left viewport
        y -= vp.window.height() / 2;
        y -= vp.getCenterY();

        return y;
    }


    private AWTViewPort vp;
    private MouseAdapter adapter = new MouseAdapter()
    {

        @Override        
        public void mousePressed(MouseEvent e)
        {
            onPress(e.getButton(), convertX(e.getX()), convertY(e.getY()));
        }

        @Override
        public void mouseMoved(MouseEvent e)
        {
            updatingVec.set(
                convertX(e.getX()),
                convertY(e.getY())
            );
        }

        public void mouseDragged(MouseEvent e)
        {
            updatingVec.set(
                convertX(e.getX()),
                convertY(e.getY())
            );
        }

        @Override        
        public void mouseReleased(MouseEvent e)
        {
            onRelease(e.getButton(), convertX(e.getX()), convertY(e.getY()));
                      // e.getX() - vp.window.width()  + vp.getCenterX() / 2,
                      // e.getY() - vp.window.height() + vp.getCenterY() / 2);
        }


        @Override        
        public void mouseWheelMoved(MouseWheelEvent e)
        {
            System.out.println("mouseWheelMoved " + e);
        }

    };
    public MouseAdapter getMouseAdapter()
    {   return this.adapter;   }

    public AWTMouseController()
    {
        super();
    }


    public AWTMouseController withViewPort(AWTViewPort vp)
    {
        this.vp = vp;
        ((AWTWindow)vp.window).getCanvas().addMouseListener(adapter);
        ((AWTWindow)vp.window).getCanvas().addMouseMotionListener(adapter);

        return this;
    }


    private Vec2 updatingVec = new Vec2();

    public Vec2 getUpdatingVec()
    {   return this.updatingVec;   }

    public double getMouseX()
    {
        double exp = 0;

        // monitorX
        exp += MouseInfo.getPointerInfo().getLocation().getX();
        // top left offset
        exp -= ((AWTWindow)vp.window).getCanvas().getLocationOnScreen().getX();

        // <pW> is a %age of
        // double pW = exp / vp.window.width();
        // System.out.println(pW);
        // typical conversion
        exp = convertX(exp);

        return exp;
    }


    public double getMouseY()
    {
        double exp = 0;

        // monitorY
        exp += MouseInfo.getPointerInfo().getLocation().getY();
        // top left offset
        exp -= ((AWTWindow)vp.window).getCanvas().getLocationOnScreen().getY();
        // typical conversion
        exp = convertY(exp);
        
        return exp;
    }   
}