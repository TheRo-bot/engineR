package dev.ramar.e2.interfaces.updating;

public interface FastUpdater
{
	public void update(double delta);

	public boolean kill();

	public void onKill();

}