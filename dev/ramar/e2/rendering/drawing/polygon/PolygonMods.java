package dev.ramar.e2.rendering.drawing.polygon;

import dev.ramar.e2.rendering.drawing.JoinStyle;
import dev.ramar.e2.rendering.drawing.CapStyle;

import dev.ramar.e2.rendering.drawing.Mod;
import dev.ramar.e2.rendering.drawing.mod_helpers.ModHelperOwner;

import dev.ramar.e2.structures.Vec2;

import dev.ramar.e2.structures.Colour;



import dev.ramar.e2.rendering.drawing.mod_helpers.*;
// OffsetHelper, ColourHelper, FillHelper, ThicknessHelper, CapStyleHelper, JoinStyleHelper


public class PolygonMods implements Mod, ModHelperOwner
{

    public PolygonMods()
    {

    }

    public final OffsetHelper<PolygonMods> offset = new OffsetHelper<>(this);

    public final ColourHelper<PolygonMods> colour = new ColourHelper<>(this, 255, 255, 255, 255);

    public final FillHelper<PolygonMods> fill = new FillHelper<>(this);

    public final ThicknessHelper<PolygonMods> thickness = new ThicknessHelper<>(this);

    public final CapStyleHelper<PolygonMods> cap = new CapStyleHelper<>(this);

    public final JoinStyleHelper<PolygonMods> join = new JoinStyleHelper<>(this);

    public final MiterHelper<PolygonMods> miter = new MiterHelper<>(this);

}

