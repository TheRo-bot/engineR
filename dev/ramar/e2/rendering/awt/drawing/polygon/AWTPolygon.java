package dev.ramar.e2.rendering.awt.drawing.polygon;


import dev.ramar.e2.rendering.Drawable;
import dev.ramar.e2.rendering.ViewPort;
import dev.ramar.e2.rendering.drawing.polygon.PolygonMods;


import dev.ramar.e2.structures.Vec2;



import java.awt.Shape;
import java.awt.Rectangle;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.AffineTransform;

import java.awt.geom.PathIterator;
import java.awt.geom.FlatteningPathIterator;

import java.util.List;
import java.util.LinkedList;

import java.util.Iterator;
import java.util.Arrays;

public class AWTPolygon implements Shape, Drawable
{

    public AWTPolygon()
    {
        this.mods
            .colour.with(255, 0, 0, 255)
        ;
    }

    public PolygonMods mods = new PolygonMods();


    public boolean isEmpty()
    {
        return this.xs.length == 0;
    }

    public int size()
    {   
        return this.xs.length;
    }


    private double[] xs = new double[0],
                     ys = new double[0];


    public synchronized AWTPolygon addPoint(double x, double y)
    {   
        this.xs = Arrays.copyOf(this.xs, this.xs.length + 1);
        this.ys = Arrays.copyOf(this.ys, this.ys.length + 1);

        this.xs[this.xs.length - 1] = x;
        this.ys[this.ys.length - 1] = y;

        return this;
    }

    public synchronized AWTPolygon removePoint(int i)
    {
        if( !this.isEmpty() ) 
        {
            int size = this.size();
            if( 0 > i || i >= size )
                throw new IndexOutOfBoundsException(i + " for 0->" + (size - 1));

            synchronized(this)
            {
                double[] newX = Arrays.copyOf(this.xs, this.xs.length - 1);
                double[] newY = Arrays.copyOf(this.ys, this.ys.length - 1);
                for(int ii = i + 1; ii < this.size(); ii++ )
                {
                    newX[ii - 1] = this.xs[ii];
                    newY[ii - 1] = this.ys[ii];
                }

                this.xs = newX;
                this.ys = newY;
            }
        }

        return this;
    }


    public double getX(int i)
    {
        return this.xs[i];
    }

    public double getY(int i)
    {
        return this.ys[i];
    }


    public synchronized AWTPolygon withPoint(int i, double x, double y)
    {
        this.xs[i] = x;
        this.ys[i] = y;

        return this;
    }

    public synchronized AWTPolygon modPoint(int i, double x, double y)
    {
        this.xs[i] += x;
        this.ys[i] += y;

        return this;
    }


    public synchronized void drawAt(double x, double y, ViewPort vp)
    {
        AWTPolygonDrawer pon = (AWTPolygonDrawer)vp.draw.polygon;

        pon.withMod(this.mods);
        this.mods
            .offset.with(x, y)
        ;

        pon.points(0, 0, this);

        this.mods
            .offset.with(-x, -y)
        ;
    }




    /* Shape Implementation
    --===---------------------
    */

    private WindingRule windingRule = WindingRule.EVEN_ODD;

    public enum WindingRule
    {
        EVEN_ODD(PathIterator.WIND_EVEN_ODD), NON_ZERO(PathIterator.WIND_NON_ZERO);

        private int id;
        WindingRule(int id)
        {
            this.id = id;
        }

        public int intify() { return this.id; }
    }


    public boolean contains(double x, double y)
    {
        return false;
    }

    public boolean contains(double x, double y, double w, double h)
    {
        return false;
    }


    public boolean contains(Point2D p) 
    {  return contains(p.getX(), p.getY());  }


    public boolean contains(Rectangle2D r)
    {  return contains(r.getX(), r.getY(), r.getWidth(), r.getHeight());  }

    public Rectangle getBounds()
    {
        return getBounds2D().getBounds();
    }

    public Rectangle2D getBounds2D()
    {
        double x1 = 0.0,
               y1 = 0.0,
               x2 = 0.0,
               y2 = 0.0;

        y1 = y2 = this.getY(0);
        x1 = x2 = this.getX(0);

        for(int ii = 1; ii < this.size(); ii++ )
        {
            double x = this.getX(ii),
                   y = this.getY(ii);

            if (x < x1) x1 = x;
            if (x > x2) x2 = x;
            if (y < y1) y1 = y;
            if (y > y2) y2 = y;
        }

        return new Rectangle2D.Double(x1, y1, x2 - x1, y2 - y1);
    }

    public PathIterator getPathIterator(AffineTransform at)
    {
        return new AWTPolygonIterator(this, at);
    }

    public PathIterator getPathIterator(AffineTransform at, double flatness)
    {
        return new FlatteningPathIterator(this.getPathIterator(at), flatness);
    }

    public boolean intersects(double x, double y, double w, double h)
    {
        return false;
    }

    public boolean intersects(Rectangle2D r)
    {
        return false;
    }


    class AWTPolygonIterator implements PathIterator
    {
        AffineTransform at = null;
        AWTPolygon pg = null;
        int ii = 0;

        public AWTPolygonIterator(AWTPolygon pg, AffineTransform at)
        {
            this.pg = pg;
            this.at = at;
        }

        private double modX(double x)
        {
            double out = x;
            if( at != null )
                out += at.getTranslateX();

            return out;
        }

        private double modY(double y)
        {
            double out = y;
            if( at != null )
                out += at.getTranslateY();
            return out;
        }

        public int currentSegment(double[] coords)
        {
            if( coords != null ) {

                double xVal = 0.0, yVal = 0.0;
                if( this.ii == this.pg.xs.length )
                {
                    xVal = this.modX(this.pg.getX(0));
                    yVal = this.modY(this.pg.getY(0));
                }
                else
                {
                    xVal = this.modX(this.pg.getX(ii));
                    yVal = this.modY(this.pg.getY(ii));
                }

                if( coords.length > 0 )
                    coords[0] = xVal;

                if( coords.length > 1 ) 
                    coords[1] = yVal;
            }

            return ii == 0 ? PathIterator.SEG_MOVETO : PathIterator.SEG_LINETO;
        }

        public int currentSegment(float[] coords)
        {
            if( coords != null ) {

                double xVal = 0.0, yVal = 0.0;
                if( this.ii == this.pg.xs.length )
                {
                    xVal = this.pg.getX(0);
                    yVal = this.pg.getY(0);
                }
                else
                {
                    xVal = this.modX(this.pg.getX(ii));
                    yVal = this.modY(this.pg.getY(ii));
                }


                if( coords.length > 0 )
                    coords[0] = (float)xVal;

                if( coords.length > 1 ) 
                    coords[1] = (float)yVal;
            }

            return ii == 0 ? PathIterator.SEG_MOVETO : PathIterator.SEG_LINETO;
        }

        public int getWindingRule()
        {
            return this.pg.windingRule.intify();
        }

        public boolean isDone()
        {
            return this.ii > this.pg.xs.length;
        }

        public void next()
        {
            this.ii += 1;
        }   
    }

}