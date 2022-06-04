package dev.ramar.e2.rendering.drawing.mod_helpers;

import dev.ramar.e2.structures.Colour;


/*
ModHelper: Colour
 - Stores a Colour, allows modification of that colour easily!
 - Links back to this.owner (final!)
*/
public class ColourHelper<E extends ModHelperOwner>
{
    private final E owner;

    public ColourHelper(E mho)
    {
        this.owner = mho;
    }

    private Colour colour = new Colour(255, 255, 255, 255);

    public Colour get()
    {   return this.colour;   }

    public int getR()
    {   return this.colour.getRed();   }

    public int getG()
    {   return this.colour.getGreen();   }

    public int getB()
    {   return this.colour.getBlue();   }

    public int getA()
    {   return this.colour.getAlpha();   }


    public E withR(int val)
    {
        val = Math.max(0, Math.min(255, val));
        this.colour.setRed(val);
        return this.owner;
    }

    public E withR(double val)
    {   return this.withR((int)(val * 255));   }


    public E withG(int val)
    {   
        val = Math.max(0, Math.min(255, val));
        this.colour.setGreen(val);
        return this.owner;
    }

    public E withG(double val)
    {   return this.withG((int)(val * 255));   }


    public E withB(int val)
    {   
        val = Math.max(0, Math.min(255, val));

        this.colour.setBlue(val);
        return this.owner;  }

    public E withB(double val)
    {   return this.withB((int)(val * 255));   }


    public E withA(int val)
    {   
        val = Math.max(0, Math.min(255, val));
        this.colour.setAlpha(val);

        return this.owner;  
    }

    public E withA(double val)
    {   return this.withA((int)(val * 255));   }



    public E with(Colour c)
    {
        this.colour = c;
        return this.owner;
    }

    public E with(int r, int g, int b, int a)
    {
        this.colour.set(r, g, b, a);
        return this.owner;
    }

    public E with(double r, double g, double b, double a)
    {
        r = Math.max(0, Math.min(1.0, r)) * 255;
        g = Math.max(0, Math.min(1.0, g)) * 255;
        b = Math.max(0, Math.min(1.0, b)) * 255;
        a = Math.max(0, Math.min(1.0, a)) * 255;

        return this.with((int)r, (int)g, (int)b, (int)a);
    }
}