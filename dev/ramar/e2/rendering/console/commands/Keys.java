package dev.ramar.e2.rendering.console.commands;

import dev.ramar.e2.EngineR2;
import dev.ramar.e2.rendering.ViewPort;

import dev.ramar.e2.rendering.console.*;
import dev.ramar.e2.rendering.console.parsers.*;

import dev.ramar.e2.rendering.Drawable;

import dev.ramar.e2.rendering.control.Stealer;
import dev.ramar.e2.rendering.control.Stealable;

import dev.ramar.e2.rendering.drawing.text.TextMods;
import dev.ramar.e2.rendering.drawing.rect.RectMods;

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
                .font.withSize(12)
                .colour.with(255, 255, 255, 255)
                .offset.with(xc, yc)
            ;
            synchronized(Keys.this)
            {

                // draw the main rect
                RectMods rm = new RectMods()
                    .offset.with(xc, yc)
                    .colour.with(100, 100, 100, 150)
                    .fill.with()
                ;
                double rectWidth = Math.max(tm.getWidthOfText("keylog"), step * toDisplay.size());

                vp.draw.layered.rect.withMod(rm);
                vp.draw.layered.rect.poslen(-rectWidth / 2 - 2, 
                                              -h/2 - 12,
                                              rectWidth + 4,
                                              h + 12);
                rm.colour.with(125, 125, 125, 150);

                vp.draw.layered.rect.poslen(-(step * toDisplay.size()) / 2, -h/4, step * toDisplay.size(), h/2);
                vp.draw.layered.rect.clearMod();

                vp.draw.layered.text.withMod(tm);
                // draw the 'keylog' title
                vp.draw.layered.text.at(0, -h/2 - 6, "keylog");
                vp.draw.layered.text.clearMod();

                // draw each character
                evenOff += step/2 * (toDisplay.size() - 1);
                for( Character c : toDisplay )
                {
                    if( (int)c != 65565 )
                    {
                        vp.draw.layered.text.withMod()
                            .offset.with(xc, yc)
                            .colour.with(255, 255, 255, 255)
                            .font.withSize(12)
                        ;

                        vp.draw.layered.text.at(xOff + evenOff, 0, "" + c);
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
            cp.console.engine.viewport.draw.layered.layers.mid.add(drawer);
        else
            cp.console.engine.viewport.draw.layered.layers.mid.remove(drawer);
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