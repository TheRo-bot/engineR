package dev.ramar.e2.interfaces.updating;

import dev.ramar.e2.backend.Moment;

public interface SlowUpdater
{
	public void update(Moment m);

	public boolean kill();

	public void onKill();

}