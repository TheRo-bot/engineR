package dev.ramar.e2.structures.renderables.hud;

import dev.ramar.e2.interfaces.rendering.Renderable;
import dev.ramar.e2.interfaces.rendering.ViewPort;

import dev.ramar.e2.structures.Vec2;
import dev.ramar.e2.structures.Colour;


import dev.ramar.e2.structures.renderables.hud.elements.*;

import java.util.*;

import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.image.BufferedImage;

import java.awt.FontMetrics;

public abstract class HUD
{

    private static final String INVALID_HUD = "HUD_INVALID";


    public static class HUDManager implements Renderable
    {
        public static final HUDManager STATIC_REF = new HUDManager();

        private static Map<String, List<HUD>> hudMap = java.util.Collections.synchronizedMap(new HashMap<String, List<HUD>>());

        private static List<String> toHide = new ArrayList<>();

        public HUDManager()
        {

        }


        public static void addHUD(HUD h)
        {
            if(! hudMap.containsKey(h.getAreaID()) )
                hudMap.put(h.getAreaID(), new ArrayList<>());

            hudMap.get(h.getAreaID()).add(h);
        }



        public static void removeHUD(HUD h)
        {
            if( hudMap.containsKey(h.getAreaID()) )
            {
                hudMap.get(h.getAreaID()).remove(h);
            }
        }

        public static void hideIDs(String idToHide)
        {
            toHide.add(idToHide);
        }


        public void render(ViewPort vp)
        {
            drawSelf(null, vp);
        }

        public void drawSelf(Vec2 v, ViewPort vp)
        {
            double x = 0,
                   y = 0;

            if( v != null )
            {
                x += v.getX();
                y += v.getY();
            }

            synchronized(hudMap)
            {
                for( String s : hudMap.keySet() )
                {
                    if(! toHide.contains(s) )
                    {
                        List<HUD> huds = hudMap.get(s);
                        for( HUD h : huds )
                            h.getOverlayer().drawSelf(v, vp);
                    }
                }
            }
        }


    }

    private Overlayer overlayer;
    private boolean doRender = false;


    protected void init()
    {

    }

    public HUD()
    {
        overlayer = new Overlayer();
        init();
    }


    public void setRender(boolean b)
    {
        doRender = b;
    }


    public Overlayer getOverlayer()
    {
        return overlayer;
    }


    public boolean doRender()
    {
        return doRender;
    }

    public abstract String getAreaID();

    /* HUD "add" elements, ease of use methods for HUDS
    ------------------------------------------------------
    */

    private Graphics2D graphicsObj = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).createGraphics();

    public RectangleElement addRect(double x, double y, int w, int h, Colour c)
    {
        RectangleElement r = new RectangleElement(x, y, w, h, c, false);
        overlayer.addElement(r);

        return r;
    }


    public TextElement addText(double x, double y, String s, Colour c, Font f)
    {
        double dimX = 0, dimY = 0;

        FontMetrics fm = null;
        if( f != null )
        {
            fm = graphicsObj.getFontMetrics(f);
            dimX = fm.stringWidth(s);
            dimY = fm.getHeight();
        }

        TextElement te = new TextElement(x, y, dimX, dimY, c, s, f);

        te.setMetrics(fm);

        overlayer.addElement(te);

        return te;
    }


    public TextElement addText(double x, double y, double borderX, double borderY, String s, Colour c, Font f)
    {
        TextElement te = new TextElement(x, y, borderX, borderY, c, s, f);
        overlayer.addElement(te);

        return te;
    }



    public DialogueElement addDialogue(double x, double y, double bx, double by, String s, Colour c, Font f)
    {
        DialogueElement de = new DialogueElement(s, x, y, bx, by);
        de.setColour(c);
        de.setFont(f);

        overlayer.addElement(de);

        return de;
    }
}