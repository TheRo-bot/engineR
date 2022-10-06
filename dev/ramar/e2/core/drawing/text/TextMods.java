package dev.ramar.e2.core.drawing.text;


import dev.ramar.e2.core.drawing.Mod;
import dev.ramar.e2.core.drawing.mod_helpers.*;

public class TextMods implements Mod, ModHelperOwner
{
    public TextMods() {}

    /* Class-Fields
    --===-------------
    */


    public final Vec2Helper<TextMods> offset = new Vec2Helper<>(this);

    public final IntHelper<TextMods> size = new IntHelper<>(this);

    public final StringHelper<TextMods> font = new StringHelper<>(this);

    public final ColourHelper<TextMods> colour = new ColourHelper<>(this);

    public final DoubleHelper<TextMods> scale = new DoubleHelper<>(this, 1.0);

}