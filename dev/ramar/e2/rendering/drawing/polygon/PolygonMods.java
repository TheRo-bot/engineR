package dev.ramar.e2.rendering.drawing.polygon;

import dev.ramar.e2.rendering.drawing.Mod;
import dev.ramar.e2.rendering.drawing.mod_helpers.ModHelperOwner;

import dev.ramar.e2.rendering.drawing.mod_helpers.ModHelperOwner;
import dev.ramar.e2.rendering.drawing.mod_helpers.OffsetHelper;
import dev.ramar.e2.rendering.drawing.mod_helpers.ColourHelper;
import dev.ramar.e2.rendering.drawing.mod_helpers.FillHelper;


/*
Mods: PolygonMods
 - Allows modification of the per-implementation default polygon gosh darnit!
*/
public class PolygonMods implements Mod, ModHelperOwner
{
    public PolygonMods() {}



    public final OffsetHelper<PolygonMods> offset = new OffsetHelper<>(this);
    public final ColourHelper<PolygonMods> colour = new ColourHelper<>(this);
    public final FillHelper<PolygonMods> fill = new FillHelper<>(this);


}