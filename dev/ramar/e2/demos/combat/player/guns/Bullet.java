package dev.ramar.e2.demos.combat.player.guns;

import dev.ramar.e2.EngineR2;

import dev.ramar.e2.rendering.Drawable;
import dev.ramar.e2.rendering.ViewPort;

import dev.ramar.e2.demos.combat.DeltaUpdater;
import dev.ramar.e2.demos.combat.DeltaUpdater.Updatable;

import dev.ramar.e2.demos.combat.RegisteredER2Instances;

import dev.ramar.e2.structures.Vec2;

public class Bullet implements Drawable, Updatable
{

	public final Vec2 pos = new Vec2();
	public final Vec2 vel = new Vec2();

	public Bullet(double x, double y, double xv, double yv)
	{
		this.pos.set(x, y);
		this.vel.set(xv, yv);
	}


	private double timeToLive = -999.0;
	public Bullet withTimeToLive(double ttl)
	{
		this.timeToLive = ttl;
		return this;
	}

	public boolean update(double delta)
	{
		this.timeToLive -= delta;


		double deltaMoveX = this.vel.getX() * delta,
			   deltaMoveY = this.vel.getY() * delta;

		this.pos.add (deltaMoveX, deltaMoveY);
		this.vel.take(deltaMoveX, deltaMoveY);

		boolean cease =  (timeToLive < 0 && timeToLive > -999) ||
			             (Math.abs(this.vel.getX()) < 0.5 && Math.abs(this.vel.getY()) < 0.5); 

        if( cease )
            for( EngineR2 er : RegisteredER2Instances.getInstance().instances )
                er.viewport.draw.stateless.perm.queueRemove(this);

        return cease;
	}


	public void drawAt(double x, double y, ViewPort vp)
	{
		vp.draw.stateless.rect.withMod()
			.withColour(255, 255, 255, 255)
			.withFill()
			.withOffset(x, y)
			.withOffset(this.pos.getX(), this.pos.getY())
		;

        double size = 3;
		vp.draw.stateless.rect.poslen(-size, -size, size * size, size * 2);
	}
}