package dev.ramar.e2.backend;

import dev.ramar.e2.structures.Vec2;
import dev.ramar.e2.structures.StaticInfo;

import dev.ramar.e2.backend.world_components.*;

import dev.ramar.e2.interfaces.rendering.ViewPort;
import dev.ramar.e2.interfaces.rendering.Renderable;

import java.util.*;


public class Camera implements Renderable
{
	private static final int CHUNK_RANGE_OFFSET = 3;

	private int screenWidth;
	private int screenHeight;

	// world position, should be center of the screen
	private Vec2 pos;


	private Vec2 lastChunkCheckPos;
	private ViewPort viewport;

	private World world;

	private Map<Vec2, Chunk> cachedChunks = new HashMap<Vec2, Chunk>();

	// renderables list which goes on an arbitrary layer
	private List<Renderable> renderables = new ArrayList<>(100);
	private int renderableLayer = Chunk.MAX_LAYERS -2;

	public Camera(Vec2 pos, int w, int h, ViewPort vp, World world)
	{
		screenWidth = w;
		screenHeight = h;
		this.pos = pos;
		viewport = vp;
		this.world = world;
	}


	public Camera(double x, double y, int w, int h, ViewPort vp, World world)
	{
		this.pos = new Vec2(x, y);
		screenWidth = w;
		screenHeight = h;
		viewport = vp;
		this.world = world;
	}


	public Vec2 getPos()
	{
		return pos;
	}


	public void setPos(Vec2 p)
	{
		pos.set(p);
	}


	/*
	Method: getChunksAroundView
	 - Ensures that the chunks in cachedChunks fill up the entire screen
	   (that is, all chunks are obtained from pos.x - screenWidth/2 -> pos.x + screenWidth/2 + 1)
	*/
	public void getChunksAroundView()
	{
		screenWidth = viewport.getScreenWidth();
		screenHeight = viewport.getScreenHeight();
		cachedChunks.clear();

		synchronized(cachedChunks)
		{
			for( int ii = ( (int)(pos.getX() - screenWidth/2) ) / Chunk.CHUNK_SIZE - CHUNK_RANGE_OFFSET; ii < ( (int)(pos.getX() + screenWidth/2) ) / Chunk.CHUNK_SIZE + CHUNK_RANGE_OFFSET; ii++)
			{
				for( int jj = ( (int)(pos.getY() - screenHeight/2) ) / Chunk.CHUNK_SIZE - CHUNK_RANGE_OFFSET; jj < ( (int)(pos.getY() + screenHeight/2) ) / Chunk.CHUNK_SIZE + CHUNK_RANGE_OFFSET; jj++)
				{
					Chunk c = world.getChunk_chunkCoord(ii, jj);

					if( c != null )
					// if( c == null )
					// {
					// 	c = new Chunk(ii, jj, world);
					// 	world.addChunk(c);
					// }
					cachedChunks.put(c.getPos(), c);

				}
			}
		}

	}


	private Thread thread;
	private boolean paused = false;
	private double distUntilUpdate = Chunk.CHUNK_SIZE / 16;

	private Vec2 lastCachedPos = new Vec2(-99999, 99999);

	private long timeSinceLastUpdate = 0;
	private int maxTimeToUpdate = 10000;

	public void render(ViewPort vp)
	{
		drawSelf(null, vp);
		// viewport.setWorldCenter(pos);

		// if( System.currentTimeMillis() - timeSinceLastUpdate > maxTimeToUpdate )
		// {
		// 	timeSinceLastUpdate = System.currentTimeMillis();
		// 	getChunksAroundView();
		// 	lastCachedPos.set(pos);
		// }
		// else
		// {
		// 	double moveX = pos.getX() - lastCachedPos.getX();

		// 	if( Math.abs(moveX) > distUntilUpdate )
		// 	{
		// 		getChunksAroundView();
		// 		lastCachedPos.set(pos);
		// 	}
		// 	else
		// 	{
		// 		double moveY = pos.getY() - lastCachedPos.getY();
		// 		if( Math.abs(moveY) > distUntilUpdate )
		// 		{
		// 			getChunksAroundView();
		// 			lastCachedPos.set(pos);
		// 		}
		// 	}
		// }

		// vp.setColour(0, 0, 0, 0);
		// vp.drawRect(-screenWidth/2, screenWidth/2, -screenHeight/2, screenHeight/2);

		// // System.out.println("camera render " + cachedChunks.size());

		// for( int ii = -1; ii < Chunk.MAX_LAYERS; ii++ )
		// {
		// 	for( Vec2 v : cachedChunks.keySet() )
		// 	{
		// 		Chunk thisChunk = cachedChunks.get(v);

		// 		// draw the background
		// 		if( ii == -1 )
		// 			thisChunk.drawSelf(viewport);
		// 		else
		// 		{
		// 			ChunkLayer thisLayer = thisChunk.getNLayer(ii);

		// 			if( thisLayer != null )
		// 				thisLayer.render(viewport);
		// 		}
		// 	}
		// }

	}


	private Vec2 memVec = new Vec2(0, 0);


	public void drawSelf(Vec2 p, ViewPort vp)
	{
		double xPos = pos.getX(),
			   yPos = pos.getY();
		if( p != null )
		{
			xPos += p.getX();
			yPos += p.getY();
		}

		viewport.setWorldCenter(xPos, yPos);

		if( System.currentTimeMillis() - timeSinceLastUpdate > maxTimeToUpdate )
		{
			timeSinceLastUpdate = System.currentTimeMillis();
			getChunksAroundView();
			lastCachedPos.set(xPos, yPos);
		}
		else
		{
			double moveX = xPos - lastCachedPos.getX();

			if( Math.abs(moveX) > distUntilUpdate )
			{
				getChunksAroundView();
				lastCachedPos.set(xPos, yPos);
			}
			else
			{
				double moveY = yPos - lastCachedPos.getY();
				if( Math.abs(moveY) > distUntilUpdate )
				{
					getChunksAroundView();
					lastCachedPos.set(xPos, yPos);
				}
			}
		}

		vp.setColour(0, 0, 0, 0);
		vp.drawRect(-screenWidth/2, screenWidth/2, -screenHeight/2, screenHeight/2);

		memVec.set(xPos, yPos);
		for( int ii = -1; ii < Chunk.MAX_LAYERS; ii++ )
		{
			for( Vec2 v : cachedChunks.keySet() )
			{
				Chunk thisChunk = cachedChunks.get(v);

				// draw the background
				if( ii == -1 )
					thisChunk.drawSelf(memVec, viewport);
				else
				{
					ChunkLayer thisLayer = thisChunk.getNLayer(ii);

					if( thisLayer != null )
						thisLayer.render(viewport);
				}
			}
		}
	}
}