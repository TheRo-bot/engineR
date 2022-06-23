package dev.ramar.e2.rendering.drawing.mod_helpers;

import dev.ramar.e2.rendering.drawing.enums.CapStyle;

import dev.ramar.e2.rendering.drawing.enums.*;
/* CapStyle, FontStyle, JoinStyle */

import java.awt.Stroke;
import java.awt.BasicStroke;


/*
ModHelper: Stroke
 - Links back to this.owner (final!)
*/
public class StrokeHelper<E extends ModHelperOwner> implements ModHelperOwner
{
    protected final E owner;

    public StrokeHelper(E mho)
    {
        this.owner = mho;
    }

    public StrokeHelper(E mho, StrokeHelper sh)
    {
        this(mho);
        if( sh != null )
        {
            this.stroke = new BasicStroke(sh.stroke.getLineWidth(),
                                          sh.stroke.getEndCap(),
                                          sh.stroke.getLineJoin(),
                                          sh.stroke.getMiterLimit()
            );
        }
    }

    private BasicStroke stroke = new BasicStroke();

    public Stroke get()
    {  return this.stroke;  }


    public final FloatHelper<StrokeHelper> width = new FloatHelper<>(this)
    {
        public float get()
        {   return StrokeHelper.this.stroke.getLineWidth();   }

        public StrokeHelper with(float f)
        {
            BasicStroke s = StrokeHelper.this.stroke;
            StrokeHelper.this.stroke = new BasicStroke(f, 
                                                       s.getEndCap(), 
                                                       s.getLineJoin(),
                                                       s.getMiterLimit()
            );  
            return this.owner;
        }

    };



    public final JoinHelper<StrokeHelper> join = new JoinHelper<>(this)
    {
        @Override
        public JoinStyle get()
        {  return JoinStyle.fromInt(StrokeHelper.this.stroke.getLineJoin());  }

        @Override
        public StrokeHelper with(JoinStyle js)
        {  
            BasicStroke s = StrokeHelper.this.stroke;
            StrokeHelper.this.stroke = new BasicStroke(s.getLineWidth(), 
                                                       s.getEndCap(), 
                                                       js.intify(),
                                                       s.getMiterLimit()
            );  
            return this.owner;
        }
    };


    public final CapHelper<StrokeHelper> cap = new CapHelper<>(this)
    {
        public CapStyle get()
        {   return CapStyle.fromInt(StrokeHelper.this.stroke.getEndCap());   }


        public StrokeHelper with(CapStyle cs)
        {
            BasicStroke s = StrokeHelper.this.stroke;
            StrokeHelper.this.stroke = new BasicStroke(s.getLineWidth(), 
                                          cs.intify(),
                                          s.getLineJoin(),
                                          s.getMiterLimit()
            );
            return this.owner;
        }
    };


    public final FloatHelper<StrokeHelper> miter = new FloatHelper<>(this)
    {
        public float get()
        {   return StrokeHelper.this.stroke.getMiterLimit();   }

        public StrokeHelper with(float f)
        {
            BasicStroke s = StrokeHelper.this.stroke;
            StrokeHelper.this.stroke = new BasicStroke(s.getLineWidth(), 
                                          s.getEndCap(), 
                                          s.getLineJoin(),
                                          f
            );  
            return this.owner;
        }

    };


}