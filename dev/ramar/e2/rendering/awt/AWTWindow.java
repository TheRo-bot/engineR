package dev.ramar.e2.rendering.awt;

import dev.ramar.e2.rendering.Window;

import dev.ramar.e2.control.awt.AWTMouseManager;
import dev.ramar.e2.control.awt.AWTKeyboardManager;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Cursor;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;

public class AWTWindow extends Window<AWTViewport, AWTMouseManager, AWTKeyboardManager>
{
    public AWTWindow()
    {}

    protected AWTViewport createViewport()
    {  return new AWTViewport(this);  }

    protected AWTMouseManager createMouseManager()
    {  return new AWTMouseManager(this);  }

    protected AWTKeyboardManager createKeyManager()
    {  return new AWTKeyboardManager();  }


    protected JFrame frame;
    public final Canvas canvas = new Canvas();



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
        this.canvas.addMouseListener(this.mouse.adapter);
        this.canvas.addMouseMotionListener(this.mouse.adapter);
        this.canvas.addMouseWheelListener(this.mouse.adapter);

        this.inner = new Thread(() ->
        {
            try
            {
                double d = 1.0;

                BufferStrategy bs = this.canvas.getBufferStrategy();
                if( bs == null ) 
                {
                    this.canvas.createBufferStrategy(3);
                    bs = this.canvas.getBufferStrategy();
                }

                long lastTime = System.currentTimeMillis();
                while(true)
                {
                    long nowTime = System.currentTimeMillis();
                    double delta = (nowTime - lastTime) / 1000.0;
                    lastTime = nowTime;
                    d -= delta;
                    if( d <= 0 )
                    {
                        d = 1.0;
                        System.out.println("yuh");
                    }
                    Graphics2D g2d = (Graphics2D)bs.getDrawGraphics();
                    if( g2d != null )
                    {
                        this.pollSize();
                        this.redraw(g2d);
                        bs.show();
                        g2d.dispose();
                    }
                    Thread.sleep(1);
                }
            }
            catch(InterruptedException e) {}

        });
    }


    private AffineTransform screenTransform = new AffineTransform();

    public void pollSize()
    {
        double frameW = this.canvas.getWidth(),
               frameH = this.canvas.getHeight();

        double screenW = this.getPixelWidth(),
               screenH = this.getPixelHeight();
        if( Math.abs(frameW - screenW) > 0.1 || Math.abs(frameH - screenH) > 0.1 )
        {
            this.setPixelSize(frameW, frameH);
            synchronized(this.screenTransform)
            {
                this.screenTransform.setToScale(frameW / this.getResolutionW(), frameH / this.getResolutionH());
            }
        }
    }


    public void show()
    {
        if( this.frame == null )
            this.initialise();

        this.frame.setResizable(false);
        this.frame.setVisible(true);

        this.frame.requestFocus();
        this.screenTransform.setToScale(
            this.frame.getWidth() / this.getResolutionW(),
            this.frame.getHeight() / this.getResolutionH()
        );

        this.inner.start();
    }

    public void hide()
    {
        this.frame.setVisible(false);
    }

    public void minimise()
    {}

    public void maximise()
    {}



    public void interrupt()
    {
        if( this.inner != null )
            this.inner.interrupt();
    }

    public void waitForClose()
    {
        if( this.inner != null )
        {
            try
            {
                this.inner.join();
            }
            catch(InterruptedException e) {}
        }
    }

    public void stop()
    {
        this.interrupt();
        this.waitForClose();
    }

    public void redraw(Graphics2D g2d)
    {
        if( g2d != null )
        {
            AffineTransform at = g2d.getTransform();
            synchronized(this.screenTransform)
            {
                at.concatenate(this.screenTransform);
            }
            g2d.setTransform(at);
            long start = System.currentTimeMillis();
            this.viewport.drawTo(g2d);
        }
    }
}