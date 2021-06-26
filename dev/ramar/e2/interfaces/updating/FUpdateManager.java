package dev.ramar.e2.interfaces.updating;

public interface FUpdateManager
{
	public void addFUpdater(FastUpdater fu);

	public void removeFUpdater(FastUpdater fu);

    public boolean hasFUpdater(FastUpdater fu);
    
	public void start();

	public void shutdown();

    public void waitForClose();

}