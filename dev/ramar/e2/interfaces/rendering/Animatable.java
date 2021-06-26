package dev.ramar.e2.interfaces.rendering;

// - dev.ramar

//   - e2
import dev.ramar.e2.interfaces.events.listeners.EventListener;
import dev.ramar.e2.interfaces.events.listeners.TimerListener;
import dev.ramar.e2.interfaces.rendering.Renderable;

//   - utils
import dev.ramar.utils.Action;


public interface Animatable extends EventListener, Renderable, TimerListener
{
    public void start() throws IllegalStateException;

    public void pause();

    public void unpause();

    public boolean isDone();

    public void addFinishAction(Action act);

    public void setLooping(boolean looping);

}