package dev.ramar.e2.rendering.animations;


public class RepeatableTaskAnimation extends Animation
{
    private long wait = 0;
    private int repeatTimes = -1;

    private Runnable task;
    private boolean stopAnim   = false,
                    pauseAnim  = false,
                    repeatable = false;

    private Thread runner;

    protected void constructorOverrideable() {}

    public RepeatableTaskAnimation()
    {
        super();
        constructorOverrideable();
    }

    public RepeatableTaskAnimation(long ms, int times)
    {
        this();
        wait = ms;
        repeatTimes = times;
    }

    public RepeatableTaskAnimation(Runnable t, long ms, int times)
    {
        this(ms, times);
        withTask(t);
    }

    public long getWaitTime()
    {   return wait;   }

    public int getRepeatCount()
    {   return repeatTimes;   }


    public RepeatableTaskAnimation withDelay(long d)
    {
        wait = d;
        return this;
    }

    public RepeatableTaskAnimation withRepeatCount(int c)
    {
        repeatTimes = c;
        return this;
    }

    public RepeatableTaskAnimation withExecTime(int time)
    {
        repeatTimes = (int)(time / wait);
        return this;
    }


    public RepeatableTaskAnimation withTask(Runnable t)
    {
        this.task = t;
        return this;
    }


    public RepeatableTaskAnimation withRepeatability(boolean b)
    {
        repeatable = b;
        return this;
    }


    private boolean continueRunning()
    {
        synchronized(this)
        {
            return ! stopAnim && task != null;
        }
    }


    @Override
    protected void onStart()
    {
        super.onStart();
        stopAnim = false;
        pauseAnim = false;
        Runnable wrapperTask = () ->
        {
            try
            {
                int runCount = 0;
                while(continueRunning() && runCount < repeatTimes)
                {
                    if( !pauseAnim )
                    {
                        task.run();
                        runCount++;
                    }
                    Thread.sleep(wait);
                }
            }
            catch(InterruptedException e) {}
            stop();
        };

        runner = new Thread(wrapperTask);
        runner.start();
    }


    @Override
    protected void onPause()
    {
        synchronized(this)
        {
            pauseAnim = true;
        }
    }

    @Override
    protected void onFinish()
    {
        super.onFinish();
        synchronized(this)
        {
            stopAnim = true;
        }
    }


    @Override
    protected void onContinue()
    {
        synchronized(this)
        {
            pauseAnim = false;
        }
    }

    protected boolean isRepeatable()
    {   return repeatable;   }

}