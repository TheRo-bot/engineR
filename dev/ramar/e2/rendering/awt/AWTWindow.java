package dev.ramar.e2.rendering.awt;

import dev.ramar.e2.rendering.Window;

import dev.ramar.e2.control.system.SystemMouseManager;
import dev.ramar.e2.control.system.SystemKeyboardManager;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Cursor;

import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;

public class AWTWindow extends Window<AWTViewport, SystemMouseManager, SystemKeyboardManager>
{
    public AWTWindow()
    {}

    protected AWTViewport createViewport()
    {  return new AWTViewport(this);  }

    protected SystemMouseManager createMouseManager()
    {  return new SystemMouseManager(this);  }

    protected SystemKeyboardManager createKeyManager()
    {  return new SystemKeyboardManager(this);  }


    protected JFrame frame;
    public final Canvas canvas = new Canvas();


    public int getDeviceX()
    {
        if( this.frame != null )
            return this.frame.getX() + 8;

        return 0;
    }
    public int getDeviceY()
    {
        if( this.frame != null )
            return this.frame.getY() + 31;

        return 0;
    }
    private Thread inner = null;

    private double mspf = 0.0;

    public void shutdown()
    {
        this.keys.ignore();
        this.keys.shutdown();
        this.mouse.ignore();
        this.mouse.shutdown();
    }

    public void setup()
    {
        JFrame f = new JFrame();
        f.setSize((int)this.getPixelWidth(), (int)this.getPixelHeight());

        f.setDefaultLookAndFeelDecorated(false);
        f.setLocation(
            (int)(Device.getDisplayWidth() * 0.5 - this.getPixelWidth() * 0.5),
            (int)(Device.getDisplayHeight() * 0.5 - this.getPixelHeight() * 0.5)
        );
        f.add(this.canvas);

        this.mspf = 1000.0 / (Device.getRefreshRate() + 1 );

        this.frame = f;

        this.frame.addWindowListener(new WindowListener()
        {
            public void windowClosed(WindowEvent we)
            {}
            public void windowDeactivated(WindowEvent we) 
            {
                AWTWindow.this.onUnfocus();
            }
            public void windowActivated(WindowEvent we) 
            {
                AWTWindow.this.onFocus();
            }
            public void windowClosing(WindowEvent we) 
            {
                AWTWindow.this.onClose();
            }
            public void windowDeiconified(WindowEvent we) 
            {
                AWTWindow.this.onMinimised();
            }
            public void windowIconified(WindowEvent we) 
            {
                AWTWindow.this.onMaximised();
            }
            public void windowOpened(WindowEvent we) 
            { }
        });

        // this.canvas.addMouseListener(this.mouse.adapter);
        // this.canvas.addMouseMotionListener(this.mouse.adapter);
        // this.canvas.addMouseWheelListener(this.mouse.adapter);

        this.inner = new Thread(() ->
        {
            BufferStrategy bs = this.canvas.getBufferStrategy();
            if( bs == null ) 
            {
                this.canvas.createBufferStrategy(3);
                bs = this.canvas.getBufferStrategy();
            }

            double d = 1.0;
            int fps = 1;
            long lastTime = System.currentTimeMillis();
            while(!this.inner.isInterrupted())
            {
                long nowTime = System.currentTimeMillis();
                double delta = (nowTime - lastTime) / 1000.0;
                d -= delta;
                lastTime = nowTime;
                if( d <= 0 )
                {
                    d = 1.0;
                    System.out.println("FPS: " + fps);
                    fps = 1;
                }

                // render!
                Graphics2D g2d = (Graphics2D)bs.getDrawGraphics();
                long startTime = System.nanoTime();
                if( g2d != null )
                {
                    this.pollSize();
                    this.redraw(g2d);
                    bs.show();
                    g2d.dispose();
                    fps += 1;
                }

                double time = (System.nanoTime() - startTime) / 1000000.0;

                double sleepTime = Math.max(0, this.mspf - time);

                double endTime = System.currentTimeMillis() + sleepTime;
                while(System.currentTimeMillis() < endTime)
                {}
            }
        }, "AWTWindow_RenderThread");
    }

    @Override
    protected void onClose()
    {
        System.out.println("onClose!");
        this.interrupt();
        this.shutdown();
        this.frame.dispose();
        super.onClose();
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
            this.setup();

        this.frame.setResizable(false);
        this.frame.setVisible(true);

        this.frame.requestFocus();
        this.screenTransform.setToScale(
            this.frame.getWidth() / this.getResolutionW(),
            this.frame.getHeight() / this.getResolutionH()
        );

        this.inner.start();
        this.mouse.listen();
        this.keys.listen();
    }

    public void hide()
    {
        this.frame.setVisible(false);
        this.mouse.ignore();
        this.keys.ignore();
    }

    public void minimise()
    {
        this.mouse.ignore();
    }

    public void maximise()
    {
        this.mouse.listen();
    }



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