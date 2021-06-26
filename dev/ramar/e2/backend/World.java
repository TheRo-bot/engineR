package dev.ramar.e2.backend;

import dev.ramar.e2.backend.world_components.*;

import dev.ramar.e2.interfaces.rendering.Layerable;

import dev.ramar.e2.structures.Vec2;

import java.util.*;


public class World
{
	public Map<Vec2, Chunk> worldChunks = java.util.Collections.synchronizedMap(new HashMap<Vec2, Chunk>());


	public World()
	{
	}


	private Vec2 memVec = new Vec2(0, 0);


	public Chunk getChunk_chunkCoord(int x, int y)
	{
		memVec.set(x, y);
		Chunk c = worldChunks.get(memVec);

		// if( c == null )
		// {
		// 	c = new Chunk(x, y, this);
		// 	addChunk(c);
		// }
		
		return c;
	}

	public Chunk getChunk_realCoord(double x, double y)
	{
		memVec.set((int)(x / Chunk.CHUNK_SIZE), (int)(y / Chunk.CHUNK_SIZE));
		return worldChunks.get(memVec);
	}


	public void addChunk(Chunk c)
	{
		worldChunks.put(c.getPos(), c);
	}


	public void addLayerable(Layerable l, int layer)
	{
		Vec2 p = l.getPos();

		memVec.set((int)(p.getX() / Chunk.CHUNK_SIZE), (int)(p.getY() / Chunk.CHUNK_SIZE) );

		Chunk c = worldChunks.get(memVec);

		if( c == null )
		{
			c = new Chunk((int)memVec.getX(), (int)memVec.getY(), this);
			addChunk(c);
		}

		c.addLayerable(l, layer);
	}

}