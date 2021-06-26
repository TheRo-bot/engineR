package dev.ramar.e2.interfaces.events.listeners;

import dev.ramar.e2.structures.Vec2;

import dev.ramar.e2.interfaces.events.producers.PositionMover;

public interface PositionMoveListener
{

	public void moved(PositionMover pm, double x, double y);

	public void moved(PositionMover pm, Vec2 pos);

}