package dev.ramar.e2.interfaces.events.listeners;


public interface EventListener extends dev.ramar.utils.Timer.TimerListener
{
    public void event(String eventName);
}