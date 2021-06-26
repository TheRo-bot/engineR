package dev.ramar.e2.interfaces.rendering;

import dev.ramar.e2.backend.world_components.*;

import dev.ramar.e2.interfaces.events.producers.PositionMover;
import dev.ramar.e2.interfaces.rendering.ViewPort;

import dev.ramar.e2.structures.Vec2;

public interface Layerable extends PositionMover, Renderable
{

	public Vec2 getPos();

	public String getUUID();

	public void setChunk(Chunk c);

	public Chunk getChunk();

	public void clearChunk();

	public void setLayer(ChunkLayer cl);

	public ChunkLayer getLayer();

	public void clearLayer();

}