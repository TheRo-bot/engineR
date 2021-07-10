
package dev.ramar.e2.rendering.control;

import dev.ramar.e2.structures.Vec2;

public abstract class MouseController
{   
    private final Vec2 pos = new Vec2(0, 0);




    public double getMouseX()
    {
        return pos.getX();
    }

    public double getMouseY()
    {
        return pos.getY();
    }

}