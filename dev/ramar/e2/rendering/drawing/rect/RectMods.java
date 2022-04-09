package dev.ramar.e2.rendering.drawing.rect;


import dev.ramar.e2.rendering.drawing.Mod;
import dev.ramar.e2.rendering.drawing.mod_helpers.*;

public class RectMods implements Mod, ModHelperOwner
{
    public RectMods() {}

    /* Class-Fields
    --===-------------
    */


    public final OffsetHelper<RectMods> offset = new OffsetHelper<>(this);
    public final ColourHelper<RectMods> colour = new ColourHelper<>(this);
    public final FillHelper<RectMods>   fill = new FillHelper<>(this);


}