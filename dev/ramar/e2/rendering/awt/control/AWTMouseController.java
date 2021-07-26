package dev.ramar.e2.rendering.awt.control;


import dev.ramar.e2.rendering.control.MouseController;
import dev.ramar.e2.rendering.awt.AWTViewPort;
import dev.ramar.e2.rendering.awt.AWTWindow;

import java.awt.PointerInfo;
import java.awt.MouseInfo;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class AWTMouseController extends MouseController
{
    // protected void onPress(int button, double x, double y)

    // protected void onRelease(int button, double x, double y)


    private double convertX(double x)
    {
        double initX = x;
        x -= vp.window.width() / 2;
        x -= vp.getCenterX();

        return x;
    }

    private double convertY(double y)
    {
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
            System.out.println("mouseMoved");
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

    public AWTMouseController()
    {
        super();
    }


    public AWTMouseController withViewPort(AWTViewPort vp)
    {
        this.vp = vp;
        ((AWTWindow)vp.window).getCanvas().addMouseListener(adapter);

        return this;
    }


    public double getMouseX()
    {
        double exp = 0;

        // monitorX
        exp += MouseInfo.getPointerInfo().getLocation().getX();
        // top left offset
        exp -= ((AWTWindow)vp.window).getCanvas().getLocationOnScreen().getX();
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