package dev.ramar.e2.rendering.drawing.line;


import dev.ramar.e2.rendering.drawing.Mod;
import dev.ramar.e2.rendering.drawing.mod_helpers.*;

public class LineMods implements Mod, ModHelperOwner
{
    /* Class-Fields
    --===-------------
    */

    public final OffsetHelper<LineMods>   offset = new OffsetHelper<>(this);
    public final ColourHelper<LineMods>   colour = new ColourHelper<>(this);
    public final ThicknessHelper<LineMods> width = new ThicknessHelper<>(this, 1);

}