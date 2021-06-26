package dev.ramar.e2.interfaces.events.producers;

import dev.ramar.e2.interfaces.events.listeners.ControlListener;

/*
Interface: Controller
 - An Event Producer for ControlListeners
*/
public interface Controller
{
	public void initialise();

	public void addListener(ControlListener cl);

	public void removeListener(ControlListener cl);

	// for ControlListeners to know what something is
	// without knowing the specific class
	public String classifyController();


}