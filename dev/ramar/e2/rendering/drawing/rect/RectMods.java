package dev.ramar.e2.rendering.drawing.rect;

import dev.ramar.e2.rendering.drawing.Mod;
import dev.ramar.e2.rendering.drawing.mod_helpers.ModHelperOwner;
import dev.ramar.e2.rendering.drawing.mod_helpers.OffsetHelper;
import dev.ramar.e2.rendering.drawing.mod_helpers.ColourHelper;
import dev.ramar.e2.rendering.drawing.mod_helpers.FillHelper;


/*
Mod: RectMods
 - Modifications to the standard Rect! (implementation specific)
 - Could also use a default RectMods for cross-implementation standardisation
*/
public class RectMods implements Mod, ModHelperOwner
{
    public RectMods() {}

    // these classes are pretty hard to understand... try to follow along :sunglasses:

    public final OffsetHelper<RectMods> offset = new OffsetHelper<>(this);

    public final ColourHelper<RectMods> colour = new ColourHelper<>(this);
    
    public final FillHelper<RectMods> fill = new FillHelper<>(this);

}