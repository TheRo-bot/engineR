package dev.ramar.e2.interfaces.events.listeners;

import dev.ramar.e2.structures.Timer;

/*
Interface: TimerListener
 - Interface for when a Timer finishes
*/
public interface TimerListener
{
	public void timerComplete(Timer t);
}