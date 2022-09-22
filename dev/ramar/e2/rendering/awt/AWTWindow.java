package dev.ramar.e2.rendering.awt;

import dev.ramar.e2.rendering.Window;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Graphics2D;

import java.awt.image.BufferStrategy;

public class AWTWindow extends Window
{
    public AWTWindow()
    {
        this.viewport = new AWTViewport(this);
    }

    protected JFrame frame;
    protected EngineRCanvas canvas = new EngineRCanvas();
    
    protected AWTViewport viewport;


    private class EngineRCanvas extends JPanel
    {
        public EngineRCanvas()
        {
        }

        @Override
        public void paintComponent(Graphics g)
        {
            AWTWindow.this.redraw((Graphics2D)g);
        }
    }


    private Thread inner = null;

    public void initialise()
    {
        JFrame f = new JFrame();
        f.setSize((int)this.getPixelWidth(), (int)this.getPixelHeight());

        f.setDefaultLookAndFeelDecorated(false);
        f.setLocation(
            (int)(Device.getDisplayWidth() * 0.5 - this.getPixelWidth() * 0.5),
            (int)(Device.getDisplayHeight() * 0.5 - this.getPixelHeight() * 0.5)
        );
        f.add(this.canvas);

        this.frame = f;
    }


    public void show()
    {
        if( this.frame == null )
            this.initialise();

        this.frame.setVisible(true);

        this.frame.requestFocus();
    }

    public void hide()
    {
        this.frame.setVisible(false);
    }

    public void minimise()
    {}

    public void maximise()
    {}

    public void redraw(Graphics2D g2d)
    {
        long start = System.currentTimeMillis();
        if( g2d != null )
        {
            this.viewport.drawTo(g2d);
        }
    }
}