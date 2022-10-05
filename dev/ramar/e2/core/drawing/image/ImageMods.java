package dev.ramar.e2.core.drawing.image;

import dev.ramar.e2.core.drawing.Mod;
import dev.ramar.e2.core.drawing.mod_helpers.*;
/* ModHelperOwner, Vec2Helper, FloatHelper, ColourHelper, BooleanHelper */

public class ImageMods implements Mod, ModHelperOwner
{
    public ImageMods() {}

    /* Class-Fields
    --===-------------
    */


    public final Vec2Helper<ImageMods> offset = new Vec2Helper<>(this);
    public final DoubleHelper<ImageMods> scale = new DoubleHelper<>(this, 1);

}