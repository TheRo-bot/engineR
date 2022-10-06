package dev.ramar.e2.core.drawing.rect;


import dev.ramar.e2.core.drawing.Mod;
import dev.ramar.e2.core.drawing.mod_helpers.*;

public class RectMods implements Mod, ModHelperOwner
{
    public RectMods() {}

    /* Class-Fields
    --===-------------
    */


    public final Vec2Helper<RectMods> offset = new Vec2Helper<>(this);
    public final ColourHelper<RectMods> colour = new ColourHelper<>(this);
    public final BooleanHelper<RectMods>   fill = new BooleanHelper<>(this);
    public final DoubleHelper<RectMods> rotation = new DoubleHelper<>(this);


    public void set(RectMods mods)
    {
        this.offset.set(mods.offset);
        this.colour.set(mods.colour);
        this.fill.set(mods.fill);
    }
}