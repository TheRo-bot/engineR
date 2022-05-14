package dev.ramar.e2.demos.combat;


import dev.ramar.e2.structures.Vec2;

import dev.ramar.utils.HiddenList;
import dev.ramar.utils.PairedValues;

public class VecList extends HiddenList<PairedValues<Vec2, VecModifier>> 
{
    public Vec2 create()
    {
        return this.create(null);
    }

    public Vec2 create(VecModifier vm)
    {
        Vec2 move = new Vec2();

        this.add(new PairedValues<>(move, vm));
        return move;
    }

    public PairedValues<Vec2, VecModifier> find(VecModifier vm)
    {
        for( PairedValues<Vec2, VecModifier> pv : this.list )
            if( pv.getV().equals(vm) )
                return pv;

        return null;
    }

    public PairedValues<Vec2, VecModifier> find(Vec2 v2)
    {
        for( PairedValues<Vec2, VecModifier> pv : this.list )
            if( pv.getK().equals(v2) )
                return pv;

        return null;
    }

    public void remove(VecModifier vm)
    {
        this.remove(this.find(vm));
    }

    public void remove(Vec2 v2)
    {
        this.remove(this.find(v2));
    }
}