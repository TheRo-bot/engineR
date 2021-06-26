package dev.ramar.e2.structures.renderables;

import dev.ramar.e2.interfaces.rendering.Animatable;
import dev.ramar.e2.structures.Timer;


import dev.ramar.utils.Action;
import dev.ramar.utils.exceptions.IncorrectArgsException;


import java.util.*;

public abstract class Animation implements Animatable
{

    private boolean done = false,
                    looping = false,
                    paused = false,
                    finished = false;

    private Action finishActions = new Action()
    {
        @Override
        protected Void[] execute(Void... args) throws IncorrectArgsException
        {
            done = true;

            return null;
        }

        @Override
        public List<Void> getArgsList()
        {
            return null;
        }
     
    };


    public Animation()
    {

    }



    private void ensureFinished()
    {
        try
        {
            if(! finished )
            {
                finishActions.doAction((Void)null);
                finished = true;
            }
        }
        catch(IncorrectArgsException e)
        {
            finished = false;
        }
    }


    /* Animatable Implementation
    -------------------------------
    */


    public void start() throws IllegalStateException
    {

    }


    public void pause()
    {
        paused = true;
    }


    public void unpause()
    {
        paused = false;
    }   


    public boolean isDone()
    {
        ensureFinished();
        return done;
    }


    public void addFinishAction(Action act)
    {
        finishActions.addAction(act);
    }


    public void setLooping(boolean looping)
    {
        this.looping = looping;
    }


    public void timerComplete(String s)
    {
        event(s);
    }

    public void timerComplete(Timer t)
    {
        event(t.getDescription());
    }
}