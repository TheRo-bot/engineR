package dev.ramar.e2.interfaces.rendering;

import dev.ramar.e2.backend.Moment;


public interface AnimatedSprite
{
	public int getFrameCount();

	public Sprite getNFrame(int n);

	public double getFPS();

	public Sprite getFrame(long startTime, long passedTime);

	public Sprite getFrame(int startSec, int startUP, Moment time);
}