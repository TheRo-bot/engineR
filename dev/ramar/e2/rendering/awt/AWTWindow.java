package dev.ramar.e2.rendering.awt;

import dev.ramar.e2.rendering.*;

import dev.ramar.e2.rendering.awt.control.*;


import dev.ramar.e2.structures.WindowSettings;

import java.util.*;

import javax.swing.JFrame;

import java.awt.Dimension;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;

import java.awt.Component;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class AWTWindow extends Window
{

    private AWTViewPort vp;

    private JFrame frame;
    private Canvas canvas;

    public AWTWindow()
    {
        super(new AWTKeyController(), new AWTMouseController());
        Runtime.getRuntime().addShutdownHook(new Thread()
        {
            public void run()
            {
                System.out.println("shutdown! closing..");
                onClose();
            }
        });
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


    public AWTWindow withViewPort(AWTViewPort vp)
    {
        this.vp = vp;

        return this;
    }

    public void init(WindowSettings ws)
    {
        frame = new JFrame(ws.getTitle());


        /*
        Used for:
         - Resizing
        */
        frame.addComponentListener(new ComponentAdapter()
        {
            public void componentResized(ComponentEvent e)
            {
                Component c = (Component)e.getSource();
                onResize(c.getWidth(), c.getHeight());

            }
        });

        // setup the close action
        /*
        Used for:
         - Closing
        */
        frame.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent we)
            {
                onClose();
            }
        });

        if( ws.fullscreen() )
        {
            frame.setSize(ws.screenW, ws.screenH);
            frame.setResizable(false);   
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
            frame.setUndecorated(true);

            Dimension d = new Dimension(ws.screenW, ws.screenH);
        }
        else
        {
            frame.setSize(ws.screenW, ws.screenH);
            frame.setDefaultLookAndFeelDecorated(false);
            frame.setExtendedState(JFrame.NORMAL); 
        }

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        canvas = new Canvas();
        getAWTKeys().withViewPort(vp);
        getAWTMouse().withViewPort(vp);

        
        frame.add(canvas);
        canvas.createBufferStrategy(3);
        Graphics2D g2d = (Graphics2D)canvas.getBufferStrategy().getDrawGraphics();

        g2d.setColor(new Color(0, 0, 0, 255));

        g2d.fillRect(0, 0, ws.screenW, ws.screenH);


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

        exp = (Graphics2D) getBufferStrategy().getDrawGraphics();

        return exp;
    }


    public BufferStrategy getBufferStrategy()
    {  
        if( canvas.getBufferStrategy() == null )
            canvas.createBufferStrategy(3);

        return canvas.getBufferStrategy();
    }









}