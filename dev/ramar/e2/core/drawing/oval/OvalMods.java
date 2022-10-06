package dev.ramar.e2.core.drawing.oval;

import dev.ramar.e2.core.drawing.Mod;
import dev.ramar.e2.core.drawing.mod_helpers.*;

public class OvalMods implements Mod, ModHelperOwner
{
    public OvalMods() {}

    /* Class-Fields
    --===-------------
    */


    public final Vec2Helper<OvalMods> offset = new Vec2Helper<>(this);
    public final ColourHelper<OvalMods> colour = new ColourHelper<>(this);
    public final BooleanHelper<OvalMods>   fill = new BooleanHelper<>(this);
    public final DoubleHelper<OvalMods> rotation = new DoubleHelper<>(this);

    public void set(OvalMods mods)
    {
        this.offset.set(mods.offset);
        this.colour.set(mods.colour);
        this.fill.set(mods.fill);
        // this.rotation.set(mods.rotation);
    }
}