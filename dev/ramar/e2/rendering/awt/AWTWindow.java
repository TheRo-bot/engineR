package dev.ramar.e2.rendering.awt;

import dev.ramar.e2.rendering.*;

import dev.ramar.e2.rendering.awt.control.*;


import dev.ramar.e2.structures.WindowSettings;

import dev.ramar.e2.rendering.Window.FullscreenState;


import java.util.*;

import javax.swing.JFrame;
import java.awt.Frame;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.GraphicsDevice;
import java.awt.Toolkit;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;

import java.awt.Component;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowFocusListener;

public class AWTWindow extends Window
{
    private AWTViewPort vp;

    private JFrame frame;
    private Canvas canvas;

    private int screenW = 1280, screenH = 720;
    private String windowName;

    private Thread shutdownHook = new Thread(() ->
    {
        System.out.println("Runtime shutdown! closing..");
        onClose();
    });

    public AWTWindow()
    {
        super(new AWTKeyController(), new AWTMouseController());
        canvas = new Canvas();

        Runtime.getRuntime().addShutdownHook(shutdownHook);
    }

    public AWTKeyController getAWTKeys()
    {
        return (AWTKeyController)keys;
    }

    public AWTMouseController getAWTMouse()
    {
        return (AWTMouseController)mouse;
    }

    public Canvas getCanvas()
    {
        return canvas;
    }

    public JFrame getFrame()
    {
        return frame;
    }



    public double getPixelsPerMM()
    {
        double inch = Toolkit.getDefaultToolkit().getScreenResolution();
        return inch * 25.4;
    }


    public AWTWindow withViewPort(AWTViewPort vp)
    {
        this.vp = vp;
        
        return this;
    }


    public boolean isRenderable()
    {
        return ready;
    }
    private boolean ready = false;

    private void setupFullscreenState(FullscreenState fss, JFrame jf)
    {
        switch(fss)
        {
            case FULLSCREEN: 
                jf.addWindowFocusListener(new WindowFocusListener()
                    {
                        public void windowGainedFocus(WindowEvent we)
                        {
                            frame.setState(Frame.NORMAL);
                        }
                        public void windowLostFocus(WindowEvent we)
                        {
                            frame.setState(Frame.ICONIFIED);
                        }
                    });
            case WINDOWED_BORDERLESS:
                jf.setExtendedState(JFrame.MAXIMIZED_BOTH);
                jf.setUndecorated(true);
                break;

            case WINDOWED:
                jf.setExtendedState(JFrame.NORMAL);
                jf.setUndecorated(false);
                break;
        }
    }

    public void setupFrame(JFrame jf)
    {
        synchronized(this)
        {
            jf.setSize(screenW, screenH);
            jf.setTitle(windowName);
            jf.add(canvas);

            setupFullscreenState(fullscreenState, jf);

            jf.addComponentListener(new ComponentAdapter()
            {
                public void componentResized(ComponentEvent e)
                {
                    Component c = (Component)e.getSource();
                    onResize(c.getWidth(), c.getHeight());
                }
            });

            jf.addWindowListener(new WindowAdapter()
            {
                @Override
                public void windowClosing(WindowEvent we)
                {
                    onClose();
                    close();
                    jf.dispose();
                }
            });

            jf.setResizable(false);
            jf.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

            jf.setVisible(true);
            getAWTKeys().withViewPort(vp);
            getAWTMouse().withViewPort(vp);

            canvas.createBufferStrategy(3);
        }
    }



    public void init()
    {
        ready = false;
        if( frame != null )
        {
            frame.dispose();
            frame = null;
        }

        frame = new JFrame();
        setupFrame(frame);
        frame.toFront();
        frame.requestFocus();
        ready = true;
    }


    public void resize(int w, int h)
    {
        screenW = w;
        screenH = h;

        if( frame != null )
        {
            frame.setSize(w, h);
            onResize(w, h);
        }
    }

    public void setTitle(String s)
    {
        windowName = s;
        if( frame != null )
            frame.setTitle(s);
    }


    public void setFullscreenState(FullscreenState fs)
    {
        if( fullscreenState != fs )
        {
            fullscreenState = fs;

            if( frame != null )
            {
                synchronized(this)
                {
                    init();
                }
            }
        }
    }

    public FullscreenState getFullscreenState()
    {
        return fullscreenState;
    }

    public int width()
    {
        return frame.getWidth();
    }

    public int height()
    {
        return frame.getHeight();
    }


    public Graphics2D getDrawGraphics()
    {
        Graphics2D exp = null;

        if( getBufferStrategy() != null )
            exp = (Graphics2D) getBufferStrategy().getDrawGraphics();

        return exp;
    }


    public BufferStrategy getBufferStrategy()
    {  
        if( canvas.getBufferStrategy() == null )
            canvas.createBufferStrategy(3);

        return canvas.getBufferStrategy();
    }


    public void close()
    {
        clearListeners();
        try
        {
            Runtime.getRuntime().removeShutdownHook(shutdownHook);
        }
        // if we're already shutting down (^C) we can't remove a shutdown hook
        catch(IllegalStateException e ) {}
    }



}