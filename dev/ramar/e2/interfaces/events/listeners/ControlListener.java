package dev.ramar.e2.interfaces.events.listeners;

import dev.ramar.e2.interfaces.events.producers.Controller;


/*
Interface: ControlListener
 - A Listener interface for callbacks, describes what controller
   sent what action, if the listener needs to care
*/
public interface ControlListener
{
	public void controlCallback(Controller c, String action);
}