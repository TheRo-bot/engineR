package dev.ramar.e2.rendering.drawing.polyline;

import dev.ramar.e2.rendering.drawing.Mod;
import dev.ramar.e2.rendering.drawing.mod_helpers.ModHelperOwner;

import dev.ramar.e2.rendering.drawing.mod_helpers.ModHelperOwner;
import dev.ramar.e2.rendering.drawing.mod_helpers.OffsetHelper;
import dev.ramar.e2.rendering.drawing.mod_helpers.ColourHelper;
import dev.ramar.e2.rendering.drawing.mod_helpers.FillHelper;


import dev.ramar.e2.rendering.drawing.mod_helpers.JoinHelper;
import dev.ramar.e2.rendering.drawing.mod_helpers.CapHelper;
import dev.ramar.e2.rendering.drawing.mod_helpers.FloatHelper;


public class PolylineMods implements Mod, ModHelperOwner
{

    public PolylineMods() {}


    public final OffsetHelper<PolylineMods> offset = new OffsetHelper<>(this);
    public final ColourHelper<PolylineMods> colour = new ColourHelper<>(this);


    public final JoinHelper<PolylineMods> join = new JoinHelper<>(this);
    public final CapHelper<PolylineMods> cap = new CapHelper<>(this);
    public final FloatHelper<PolylineMods> width = new FloatHelper<>(this);
    public final FloatHelper<PolylineMods> miter = new FloatHelper<>(this);

}