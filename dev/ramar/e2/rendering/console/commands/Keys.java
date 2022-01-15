package dev.ramar.e2.rendering.console.commands;

import dev.ramar.e2.EngineR2;
import dev.ramar.e2.rendering.ViewPort;

import dev.ramar.e2.rendering.console.*;
import dev.ramar.e2.rendering.console.parsers.*;

import dev.ramar.e2.rendering.Drawable;

import dev.ramar.e2.rendering.control.Stealer;
import dev.ramar.e2.rendering.control.Stealable;

import dev.ramar.e2.rendering.drawing.stateless.TextDrawer.TextMods;
import dev.ramar.e2.rendering.drawing.stateless.RectDrawer.RectMods;

import java.util.Map;
import java.util.HashMap;

import java.util.Set;
import java.util.TreeSet;

public class Keys implements Command
{
    private static final ObjectParser PARSER = new StringSplitter(" ");

    private EngineR2 er;

    public Keys(EngineR2 instance)
    {
        er = instance;
    }

    private Drawable drawer = new Drawable()
    {
        private int xc = 0, w = 100,
                    yc = 0, h = 20;

        public void drawAt(double x, double y, ViewPort vp)
        {
            xc = Math.min(vp.getLogicalWidth(), vp.window.width()) - w;
            yc = Math.min(vp.getLogicalHeight(), vp.window.height()) - 80;
            int xOff = 0;
            vp.window.keys.copyPressed(toDisplay);

            int evenOff = 0;
            int step = 16;

            TextMods tm = new TextMods()
                .withSize(12)
                .withColour(255, 255, 255, 255)
                .withOffset(xc, yc)
            ;
            synchronized(Keys.this)
            {

                // draw the main rect
                RectMods rm = new RectMods()
                    .withOffset(xc, yc)
                    .withColour(100, 100, 100, 150)
                    .withFill()
                ;
                double rectWidth = Math.max(tm.getWidthOfText("keylog"), step * toDisplay.size());

                vp.draw.stateless.rect.withTempMod(rm);
                vp.draw.stateless.rect.poslen(-rectWidth / 2 - 2, 
                                              -h/2 - 12,
                                              rectWidth + 4,
                                              h + 12);
                rm.withColour(125, 125, 125, 150);

                vp.draw.stateless.rect.poslen(-(step * toDisplay.size()) / 2, -h/4, step * toDisplay.size(), h/2);
                vp.draw.stateless.rect.clearTempMod();

                vp.draw.stateless.text.withTempMod(tm);
                // draw the 'keylog' title
                vp.draw.stateless.text.pos_c(0, -h/2 - 6, "keylog");
                vp.draw.stateless.text.clearTempMod();

                // draw each character
                evenOff += step/2 * (toDisplay.size() - 1);
                for( Character c : toDisplay )
                {
                    if( (int)c != 65565 )
                    {
                        vp.draw.stateless.text.withMod()
                            .withOffset(xc, yc)
                            .withColour(255, 255, 255, 255)
                            .withSize(12)
                        ;

                        vp.draw.stateless.text.pos_c(xOff + evenOff, 0, "" + c);
                        xOff -= step;
                    }
                }

            }
        }
    };


    private boolean enabled = false;

    private Set<Character> toDisplay = new TreeSet<>();

    public Object run(ConsoleParser cp, Object[] args)
    {
        enabled = ! enabled;

        if( enabled )
            cp.console.engine.viewport.draw.stateless.perm.add(drawer);
        else
            cp.console.engine.viewport.draw.stateless.perm.remove(drawer);
        return null;
    }

    public ObjectParser getParser()
    {
        return PARSER;
    }

    public String describeCommand()
    {
        return "shows what keys are being pressed";
    }
}