package dev.ramar.e2.console;


import dev.ramar.e2.rendering.ViewPort;
import dev.ramar.e2.rendering.Drawable;

import dev.ramar.e2.rendering.drawing.rect.Rect;
import dev.ramar.e2.rendering.drawing.enums.*;
/* CapStyle, FontStyle, JoinStyle */

import java.util.List;
import java.util.ArrayList;

// captures input and sends characters to ConsoleInput
// listens to ConsoleOutput's lines, and displays them on the screen

public class Console
{

    public final ConsoleInput in = new ConsoleInput();
    public final ConsoleOutput out = new ConsoleOutput();

    public final ConsoleParser parser = new ConsoleParser(this);

    public Console()
    {
        this.out.setLineCount(30);
    }



    public void attach(ViewPort vp)
    {
        this.input.attach(vp);
    }

    public void release(ViewPort vp)
    {
        this.input.release(vp);
    }


    private InputDrawable   input = new InputDrawable();
    private OutputDrawable output = new OutputDrawable();


    private class InputDrawable
    {
        public InputDrawable()
        {

        }
        private Rect rect = null;
        private void setup(ViewPort vp)
        {
            this.rect = new Rect();

            double w = vp.window.width();
            double h = vp.window.height();

            this.rect.len.set(w * 0.8,
                              h * 0.15);

            this.rect.pos.set(w * -0.5 + w * 0.1, 0);

            this.rect.getMod()
                .colour.with(255, 255, 255, 255)
                .fill.with()
                .stroke
                    .width.with(10)
                    .join.with(JoinStyle.Round)
            ;
        }

        public void attach(ViewPort vp)
        {
            if( this.rect == null )
                this.setup(vp);
            vp.layers.top.add(this.rect);
        }

        public void release(ViewPort vp)
        {
            vp.layers.top.remove(this.rect);
        }

    }

    private class OutputDrawable
    {
        public OutputDrawable()
        {

        }

        private void setup(ViewPort vp)
        {
        }

        public void attach(ViewPort vp)
        {
        }

        public void release(ViewPort vp)
        {
        }

    }
}