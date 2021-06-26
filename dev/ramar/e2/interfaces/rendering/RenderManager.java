package dev.ramar.e2.interfaces.rendering;

import dev.ramar.e2.structures.WindowSettings;

/*
Interface: RenderManager
 - Describes the Relationship of a Renderable Manager
 - Any Renderer should implement RenderManager, so that
   specific classes can be
*/
public interface RenderManager
{
	public void addRenderable(Renderable r);

	public void removeRenderable(Renderable r);

	public void initialise(WindowSettings ws);

	public void start();

	public void pause();

	public void setSpeed(double fps);

    public void addCloseListener(RenderManagerListener rml);

    public void removeCloseListener(RenderManagerListener rml);

	public void shutdown();

    public void waitForClose();
}