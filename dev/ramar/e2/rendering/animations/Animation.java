package dev.ramar.e2.rendering.animations;

public abstract class Animation
{
    public static enum AnimationState
    {
        IDLE, RUNNING, PAUSED
    };

    private AnimationState state;

    private Runnable finishedAction, startAction;

    public Animation()
    {
        state = AnimationState.IDLE;
    }


    public boolean isIdle()
    {   return state == AnimationState.IDLE;   }

    public boolean isRunning()
    {   return state == AnimationState.RUNNING;   }

    public boolean isPaused()
    {   return state == AnimationState.PAUSED;   }

    public AnimationState getState()
    {   return state;   }


    public Animation whenFinished(Runnable r)
    {
        finishedAction = r;
        return this;
    }


    public Animation whenStart(Runnable r)
    {
        startAction = r;
        return this;
    }



    public final void start()
    {
        if( state != AnimationState.IDLE )
            throw new IllegalStateException("Can only start when idle (currently " + state + ")" );
        onStart();
        state = AnimationState.RUNNING;
    }

    public final void stop()
    {
        if( state != AnimationState.RUNNING )
            throw new IllegalStateException("Can only stop when running (currently " + state + ")" );
        
        onFinish();

        state = AnimationState.IDLE;
        if( isRepeatable() )
            start();
    }

    public final void pause()
    {
        if( state != AnimationState.RUNNING )
            throw new IllegalStateException("Can only pause when running (currently " + state + ")" );
        onPause();
        state = AnimationState.PAUSED;
    }


    public final void unpause()
    {
        if( state != AnimationState.PAUSED )
            throw new IllegalStateException("Can only unpause when paused (currently " + state + ")");
        onContinue();
        state = AnimationState.RUNNING;
    }

    protected abstract boolean isRepeatable();

    protected void onStart() 
    {
        if( startAction != null )
            startAction.run();
    }

    protected void onPause() {}

    protected void onContinue() {}

    protected void onFinish() 
    {
        if( finishedAction != null )
            finishedAction.run();
    }

}