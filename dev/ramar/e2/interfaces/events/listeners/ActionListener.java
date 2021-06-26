package dev.ramar.e2.interfaces.events.listeners;

import dev.ramar.e2.interfaces.events.producers.Controller;

/*
Interface: ActionListener
 - A Listener interface for "Actions"
 - Actions are defined as an input from the user, which
   is either happening, or not happening. This refers to
   actions like key-presses, left/right/middle mouse clicking,
   controller button pressing(?), etc. 
*/
public interface ActionListener extends ControlListener
{
	public void actionStart(Controller c, String action);

	public void actionEnd(Controller c, String action);
}