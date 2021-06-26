package dev.ramar.e2.interfaces.updating;


public interface SUpdateManager
{
	public boolean addSUpdater(SlowUpdater su);

	public boolean removeSUpdater(SlowUpdater su);

    public boolean hasSUpdater(SlowUpdater su);

	public void start();

	public void shutdown();
    
    public void waitForClose();

}