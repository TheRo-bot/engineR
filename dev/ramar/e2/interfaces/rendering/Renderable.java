package dev.ramar.e2.interfaces.rendering;

import dev.ramar.e2.structures.Vec2;
/*
Interface: Renderable
 - Interface for things that can be rendered to the ViewPort
 - render(ViewPort vp) allows the Renderable to render to the screen.
   these methods should generally:
   	- NOT create memory
   	- BE efficient
   	- BE (at least minorly) modifiable (i.e. colour changing, etc.)
*/
public interface Renderable
{

	public enum RENDER_TYPE {
		FULL, HITBOX, INVISIBLE
	}
	
	// public void doRenderType(RENDER_TYPE rt);

	// public void dontRenderType(RENDER_TYPE rt);

	public void render(ViewPort vp);

    public void drawSelf(Vec2 pos, ViewPort vp);
}