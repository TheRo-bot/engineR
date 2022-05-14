package dev.ramar.e2.demos.combat.actions.movement;

import dev.ramar.e2.demos.combat.actions.ActionArgs;

public class MovementArgs extends ActionArgs
{
    public MovementArgs() {}
    public MovementArgs(String name, boolean proc)
    {
        this.name = name;
        this.proc = proc;
    }

    public String  name = null;
    public boolean proc = false;

    public MovementArgs withName(String n) { this.name = n; return this; }
    public MovementArgs withProc(boolean p) { this.proc = p; return this; }
}
