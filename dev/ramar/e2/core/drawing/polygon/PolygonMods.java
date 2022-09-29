package dev.ramar.e2.core.drawing.polygon;

import dev.ramar.e2.core.drawing.Mod;
import dev.ramar.e2.core.drawing.mod_helpers.*;
/* ModHelperOwner, Vec2Helper, ColourHelper, BooleanHelper */

public class PolygonMods implements Mod, ModHelperOwner
{
    public PolygonMods() {}

    /* Class-Fields
    --===-------------
    */


    public final Vec2Helper<PolygonMods> offset = new Vec2Helper<>(this);
    public final ColourHelper<PolygonMods> colour = new ColourHelper<>(this);
    public final BooleanHelper<PolygonMods>   fill = new BooleanHelper<>(this);


}