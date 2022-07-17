package dev.ramar.e2.structures;


public class WindowInitialiser
{
	public Vec2 screenRes = new Vec2(1920, 1080);
    public Vec2 screenSize = new Vec2(1920, 1080);
    public double ppmm = 96;

    public String title = "EngineR2";
    public FullscreenState fs = FullscreenState.WINDOWED;

    public WindowInitialiser() {}

    public WindowInitialiser withRes(double w, double h)
    {
        this.screenRes.set(w, h);
        return this;
    }

    public WindowInitialiser withSize(int w, int h)
    {
        screenSize.set(w, h);
        return this;
    }

    public WindowInitialiser withTitle(String s)
    {
        title = s;
        return this;
    }

    public WindowInitialiser withFullscreenState(FullscreenState fs)
    {
        this.fs = fs;
        return this;
    }


    public void build(ViewPort vp)
    {
        vp.init(screenW, screenH, title, fs);
    }
}