package dev.ramar.e2.interfaces.events.listeners;

import dev.ramar.e2.interfaces.collision.Hittable;

/*
Interface: HitListener
 - Interface for anything that cares when a Hittable gets Hit
*/
public interface HitListener
{
	public void hitOn(Hittable hit, Hittable shot);

	public void noHits(Hittable hit);
}