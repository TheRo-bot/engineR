package dev.ramar.e2.core.drawing.line;


import dev.ramar.e2.core.drawing.Mod;
import dev.ramar.e2.core.drawing.mod_helpers.*;

public class LineMods implements Mod, ModHelperOwner
{
    /* Class-Fields
    --===-------------
    */

    public final Vec2Helper<LineMods>   offset = new Vec2Helper<>(this);
    public final ColourHelper<LineMods>   colour = new ColourHelper<>(this);
    public final DoubleHelper<LineMods> width = new DoubleHelper<>(this, 1);

}