package dev.ramar.e2.rendering;

import dev.ramar.e2.control.MouseManager;
import dev.ramar.e2.control.KeyboardManager;
import dev.ramar.e2.rendering.control.*;

import java.util.*;

public abstract class Window<E extends Viewport, K extends MouseManager, V extends KeyboardManager>
{
    public class Device
    {
        public static double ppmm = java.awt.Toolkit.getDefaultToolkit().getScreenResolution() / 25.4;

        public static double convertToMM(double pixels)
        {
            return pixels / ppmm;
        }


        public static double getDisplayWidth()
        {  
            double out = java.awt.Toolkit.getDefaultToolkit().getScreenSize().getWidth();
            return out;  
        }

        public static double getDisplayHeight()
        {
            double out = java.awt.Toolkit.getDefaultToolkit().getScreenSize().getHeight();
            return out;
        }
    }


    public Window()
    {
        this.viewport = this.createViewport();
        this.mouse = this.createMouseManager();
        this.keys = this.createKeyManager();
    }

    public final E viewport;
    public final K mouse;
    public final V keys;

    protected abstract E createViewport();
    protected abstract K createMouseManager();
    protected abstract V createKeyManager();


    private double res_x = 1920.0,
                   res_y = 1080.0;

    public double getResolutionW()
    {  return this.res_x;  }

    public double getResolutionH()
    {  return this.res_y;  }


    public synchronized void setResolution(double x, double y)
    {
        this.res_x = x;
        this.res_y = y;
    }


    private double size_x = 0.5,
                   size_y = 0.5;

    public synchronized void setSize(double x, double y)
    {
        this.size_x = Math.max(0, Math.min(1.0, x));
        this.size_y = Math.max(0, Math.min(1.0, y));
    } 


    public synchronized double getPixelWidth()
    {  return this.size_x * Device.getDisplayWidth();  }

    public synchronized void setPixelWidth(double pw)
    {
        this.size_x = pw / Device.getDisplayWidth();
    }

    public synchronized double getPixelHeight()
    {  return this.size_y * Device.getDisplayHeight();  }


    public synchronized void setPixelHeight(double ph)
    {
        this.size_y = ph / Device.getDisplayHeight();
    }

    public synchronized void setPixelSize(double w, double h)
    {
        this.setPixelWidth(w);
        this.setPixelHeight(h);
    }
}