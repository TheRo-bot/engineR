package dev.ramar.e2.rendering.drawing.rect;

import dev.ramar.e2.rendering.drawing.Mod;
import dev.ramar.e2.rendering.drawing.mod_helpers.ModHelperOwner;
import dev.ramar.e2.rendering.drawing.mod_helpers.OffsetHelper;
import dev.ramar.e2.rendering.drawing.mod_helpers.ColourHelper;
import dev.ramar.e2.rendering.drawing.mod_helpers.FillHelper;
import dev.ramar.e2.rendering.drawing.mod_helpers.StrokeHelper;
import dev.ramar.e2.rendering.drawing.mod_helpers.BooleanHelper;


/*
Mod: RectMods
 - Modifications to the standard Rect! (implementation specific)
 - Could also use a default RectMods for cross-implementation standardisation
*/
public class RectMods implements Mod, ModHelperOwner
{
    public RectMods() 
    {
        offset = new OffsetHelper<>(this);
        colour = new ColourHelper<>(this);
        fill   = new FillHelper<>(this);
        stroke = new StrokeHelper<>(this);
        visible = new BooleanHelper<>(this, true);
    }


    public RectMods(RectMods rm)
    {
        this.offset = new OffsetHelper<>(this, rm.offset);
        this.colour = new ColourHelper<>(this, rm.colour);
        this.fill   = new FillHelper<>(this, rm.fill);
        this.stroke = new StrokeHelper<>(this, rm.stroke);
        this.visible = new BooleanHelper<>(this, rm.visible);
    }

    // these classes are pretty hard to understand... try to follow along :sunglasses:

    public final OffsetHelper<RectMods> offset;

    public final ColourHelper<RectMods> colour;
    
    public final FillHelper<RectMods> fill;

    public final StrokeHelper<RectMods> stroke;

    public final BooleanHelper<RectMods> visible;
}