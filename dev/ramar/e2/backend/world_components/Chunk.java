package dev.ramar.e2.backend.world_components;

import dev.ramar.e2.interfaces.rendering.Layerable;
import dev.ramar.e2.interfaces.rendering.Renderable;
import dev.ramar.e2.interfaces.rendering.ViewPort;

import dev.ramar.e2.interfaces.events.listeners.PositionMoveListener;
import dev.ramar.e2.interfaces.events.producers.PositionMover;


import dev.ramar.e2.backend.World;

import dev.ramar.e2.structures.Vec2;
import dev.ramar.e2.structures.Colour;
import dev.ramar.e2.structures.StaticInfo;


import java.util.*;

import dev.ramar.e2.structures.items.weapons.guns.Bullet;

public class Chunk implements Renderable, PositionMoveListener
{
	private World worldRef;

	public static final int CHUNK_SIZE = 250;

	public static final int MAX_LAYERS = 10;

	private Vec2 pos;

	private List<ChunkLayer> layers = new ArrayList<>(MAX_LAYERS);

	private Map<String, Layerable> layerablesInsideChunk = java.util.Collections.synchronizedMap(new HashMap<>());


	private void initialise()
	{
		for( int ii = 0; ii < MAX_LAYERS; ii++ )
		{
			layers.add(null);
		}
	}


	public Chunk(int x, int y, World worldRef)
	{
		pos = new Vec2(x, y);
		this.worldRef = worldRef;

		initialise();
	}


	public String toString()
	{
		return "Chunk at " + pos;
	}


	public ChunkLayer getNLayer(int n)
	{
		if( 0 > n && n <= MAX_LAYERS )
			throw new IndexOutOfBoundsException(n + " out of bounds for range 0-" + MAX_LAYERS);

		ChunkLayer expLayer = null;
		try
		{
			expLayer = layers.get(n);
		}
		catch(IndexOutOfBoundsException e) {}

		if( expLayer == null )
		{
			expLayer = new ChunkLayer(this, n);
			putLayer(expLayer, n);
		}

		return expLayer;
	}


	public Vec2 getPos()
	{
		return pos;
	}


	public int getX()
	{
		return (int)pos.getX();
	}

	public int getY()
	{
		return (int)pos.getY();
	}


	public double getWorldX()
	{
		return pos.getX() * CHUNK_SIZE + CHUNK_SIZE/2;
	}


	public double getWorldY()
	{
		return pos.getY() * CHUNK_SIZE + CHUNK_SIZE/2;
	}


	public void addLayer(ChunkLayer l)
	{
		layers.add(l);
	}


	public void putLayer(ChunkLayer l, int place)
	{
		ChunkLayer thisLayer = layers.set(place, l);

		if( thisLayer != null )
		{
			thisLayer.setLayer(place);
			int index = place;
			while( thisLayer != null )
			{
				try
				{
					index++;
					thisLayer = layers.set(index, thisLayer);	
				}
				catch(IndexOutOfBoundsException e) {}
			}
		}
	}


	public void addLayerable(Layerable l, int layer)
	{
		ChunkLayer cl = layers.get(layer);

		if( cl == null )
		{
			cl = new ChunkLayer(this, layer);
			putLayer(cl, layer);
		}


		// System.out.println("[Chunk] adding " + l + " to " + this + ", layer 2 (" + layer + ")");
		cl.addLayerable(l);
		l.setLayer(cl);
		l.setChunk(this);

		layerablesInsideChunk.put(l.getUUID(), l);
	}


	public void removeLayerable(Layerable l)
	{
		for( int ii = 0; ii < layers.size(); ii++ )
		{
			ChunkLayer cl = layers.get(ii);
			if( cl != null )
				cl.removeLayerable(l);
		}
		l.clearChunk();
		l.clearLayer();
		layerablesInsideChunk.remove(l.getUUID());
	}


	public boolean hasLayerable(Layerable l)
	{
		return layerablesInsideChunk.get(l.getUUID()) != null;
	}


	/* Renderable implementation
	-------------------------------
	*/

	private Colour mainColour = new Colour(30, 30, 30, 255);


	public void setMainColour(int r, int g, int b, int a)
	{
		mainColour.set(r, g, b, a);
	}



	public void render(ViewPort vp)
	{
		// this is a very weird way of iterating through the chunk layers, 
		// yes, but it's so CMOD Exceptions don't happen here, rendering is
		// "whatever state this chunk is in, render that", if a layer is being
		// modified, we shouldn't care, essentially

		Colour colour = vp.getColour();

		drawSelf(null, vp);
		int chunkSize = layers.size();
		for( int ii = 0; ii < chunkSize; ii++ )
		{
			ChunkLayer cl = layers.get(ii);
			cl.render(vp);
		}

		vp.setColour(colour);



	}

	private List<Colour> colours = new ArrayList<>();

	public void drawSelf(Vec2 p, ViewPort vp)
	{
		double worldX = getWorldX(),
			   worldY = getWorldY();

		
		Colour c = vp.getColour();
		// draw a rect for our chunk
		vp.setColour(mainColour);
		vp.drawRect(getWorldX() - CHUNK_SIZE / 2, getWorldY() - CHUNK_SIZE / 2, CHUNK_SIZE, CHUNK_SIZE);

		int offset = 20;
		vp.setColour(mainColour.getRed() + offset, mainColour.getBlue() + offset, mainColour.getGreen() + offset, mainColour.getAlpha());
		vp.outlineRect(getWorldX() - CHUNK_SIZE / 2, getWorldY() - CHUNK_SIZE / 2, CHUNK_SIZE, CHUNK_SIZE);

		// optionally render everything thats inside of this chunk


		// synchronized(layerablesInsideChunk)
		// {
		// 	if( !layerablesInsideChunk.isEmpty() )
		// 	{
		// 		// ensure we have enough colours
		// 		Random rd = StaticInfo.Objects.getRandom();
		// 		for( int ii = colours.size(); ii < layerablesInsideChunk.size(); ii++ )
		// 			colours.add(new Colour(rd.nextInt(255), rd.nextInt(255), rd.nextInt(255), 255));

		// 		int index = 0;
		// 		int thisOffset = 15;
		// 		vp.setColour(255, 255, 255, 255);
		// 		vp.drawText("(" + getX() + ", " + getY() + ")", getWorldX(), getWorldY() - 20);
		// 		vp.drawText("Layerables:", getWorldX(), getWorldY());

		// 		for( String s : layerablesInsideChunk.keySet())
		// 		{
		// 			Layerable l = layerablesInsideChunk.get(s);

		// 			vp.setColour(colours.get(index));

		// 			vp.drawLine(l.getPos().getX(), l.getPos().getY(), getWorldX(), getWorldY() + thisOffset);
		// 			vp.drawText(s, getWorldX(), getWorldY() + thisOffset);

		// 			thisOffset += 15;
		// 			index++;
		// 		}
		// 	}
		// }

		vp.setColour(c);
	}



	private void updateToChunk(PositionMover pm, int cX, int cY)
	{
		assert worldRef != null: " Chunk.worldRef not set";
		assert pm instanceof Layerable : " PositionMover not a Layerable";


		Layerable l = (Layerable)pm;

		Chunk newChunk = worldRef.getChunk_chunkCoord(cX, cY);

		if( newChunk == null )
		{
			newChunk = new Chunk(cX, cY, worldRef);
			worldRef.addChunk(newChunk);
		}

		int layer = l.getLayer().getLayer();
		removeLayerable(l);
		newChunk.addLayerable(l, layer);
		
	}


	public void moved(PositionMover pm, double x, double y)
	{
		int xChunk = (int)Math.floor(x / CHUNK_SIZE),
			yChunk = (int)Math.floor(y / CHUNK_SIZE);

		if( 
			xChunk != (int)pos.getX() || 
			yChunk != (int)pos.getY() )
		{
			updateToChunk(pm, xChunk, yChunk);
		}
	}

	public void moved(PositionMover pm, Vec2 pos)
	{
		moved(pm, pos.getX(), pos.getY());
	}


	public Map<String, Layerable> getLayerables()
	{
		return layerablesInsideChunk;
	}
}