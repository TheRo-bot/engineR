package dev.ramar.e2.rendering.awt;

import dev.ramar.e2.rendering.*;

import dev.ramar.e2.structures.WindowSettings;


import java.util.*;

import javax.swing.JFrame;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;


public class AWTViewPort extends ViewPort
{

    private JFrame frame;

    public AWTViewPort()
    {
        super(new AWTDrawManager(), new AWTWindow());
        ((AWTDrawManager)draw).withViewPort(this);
    }


    @Override
    public void init(WindowSettings ws)
    {
        System.out.println("init!");
        frame = new JFrame(ws.getTitle());

        frame.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent we)
            {
                onClose();
            }
        });
        frame.setSize(1280, 720);
        frame.setDefaultLookAndFeelDecorated(false);
        // frame.setUndecorated(false);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setExtendedState(JFrame.NORMAL); 

        frame.add(new Canvas());
        frame.createBufferStrategy(3);
        super.init(ws);

        BufferStrategy bs = frame.getBufferStrategy();
        Graphics2D g2d = (Graphics2D)bs.getDrawGraphics();

        g2d.setColor(new Color(150, 150, 150, 255));
        g2d.fillRect(0, 0, 1280, 720);
        g2d.dispose();

        bs.show();
        bs.show();

    }


    private Thread inner;
    private boolean closed = false;

    public void waitForClose()
    {
        if( inner != null )
        {
            try
            {
                while(!closed)
                {
                    Thread.sleep(100);
                }
            }
            catch(InterruptedException e) {}
        }
    }


    @Override
    public void start()
    {
        System.out.println("start!");
        inner = new Thread(() -> 
        {
            try
            {
                while(! inner.isInterrupted() )
                {
                    System.out.println("loop!");
                    Thread.sleep(200);
                }
            }
            catch(InterruptedException e) {}

            onClose();
        });

        inner.start();
        super.start();
    }


    @Override
    public void stop()
    {

        super.stop();
    }


    @Override
    public void interrupt()
    {
        super.interrupt();
    }




    /* Listener Callbacks
    -==---------------------
    */  

    public static class CloseListeners
    {
        public interface CloseListener
        {
            public void onClose();
        }

        private static final List<CloseListener> listeners = new ArrayList<>();

        public static void add(CloseListener cl)
        {
            listeners.add(cl);
        }

        public static void remove(CloseListener cl)
        {
            listeners.remove(cl);
        }


        private static void onClose()
        {
            for( CloseListener cl : listeners )
                cl.onClose();
        }
    }


    private void onClose()
    {
        CloseListeners.onClose();
        closed = true;
    }



}