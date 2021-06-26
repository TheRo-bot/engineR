package dev.ramar.e2.backend.world_components;

import dev.ramar.e2.interfaces.rendering.Layerable;
import dev.ramar.e2.interfaces.rendering.Renderable;
import dev.ramar.e2.interfaces.rendering.ViewPort;

import dev.ramar.e2.interfaces.events.listeners.PositionMoveListener;
import dev.ramar.e2.interfaces.events.producers.PositionMover;

import dev.ramar.e2.structures.Vec2;
import dev.ramar.e2.structures.StaticInfo;
import dev.ramar.e2.structures.Colour;

import dev.ramar.e2.backend.world_components.Chunk;

import dev.ramar.e2.structures.items.weapons.guns.Bullet;

import java.util.*;


import java.util.concurrent.ArrayBlockingQueue;


public class ChunkLayer implements Renderable
{

	private Map<String, Layerable> layerables = java.util.Collections.synchronizedMap(new HashMap<String, Layerable>());

	private Chunk ownedChunk;

	private int layer;

	public ChunkLayer(Chunk c, int layer)
	{
		ownedChunk = c;
		this.layer = layer;
	}


	public String toString()
	{
		return "Layer " + layer + " at [" + ownedChunk.getX() + ", " + ownedChunk.getY() + "]";
	}


	public int getLayer()
	{
		return layer;
	}


	public void setLayer(int l)
	{
		layer = l;
	}


	private List<Colour> colours = new ArrayList<>();


	public void render(ViewPort vp)
	{
		Colour origC = vp.getColour();
		synchronized(layerables)
		{ 

			for( String s : layerables.keySet() )
			{
				Layerable l = layerables.get(s);
				if( l != null )
					l.render(vp);
			}
		}

		vp.setColour(origC);
	}


	public void drawSelf(Vec2 pos, ViewPort vp)
	{
		Colour origC = vp.getColour();

		synchronized(layerables)
		{ 
			for( String s : layerables.keySet() )
			{
				Layerable l = layerables.get(s);
				if( l != null )
					l.drawSelf(pos, vp);
			}
		}

		vp.setColour(origC);
	}


	public void addLayerable(Layerable l)
	{
		layerables.put(l.getUUID(), l);
	}


	public void removeLayerable(Layerable l)
	{
		layerables.remove(l.getUUID());
	}



}