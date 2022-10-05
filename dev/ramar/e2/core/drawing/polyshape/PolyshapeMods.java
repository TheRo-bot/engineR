package dev.ramar.e2.core.drawing.polyshape;

import dev.ramar.e2.core.drawing.Mod;
import dev.ramar.e2.core.drawing.mod_helpers.*;
/* ModHelperOwner, Vec2Helper, FloatHelper, ColourHelper, BooleanHelper */

public class PolyshapeMods implements Mod, ModHelperOwner
{
    public PolyshapeMods() {}

    /* Class-Fields
    --===-------------
    */


    public final Vec2Helper<PolyshapeMods>    offset = new Vec2Helper<>(this);
    public final ColourHelper<PolyshapeMods>  colour = new ColourHelper<>(this);
    public final FloatHelper<PolyshapeMods>    width = new FloatHelper<>(this, 1);
    public final BooleanHelper<PolyshapeMods>   fill = new BooleanHelper<>(this, false);
    public final BooleanHelper<PolyshapeMods>  loops = new BooleanHelper<>(this, true);

    

}