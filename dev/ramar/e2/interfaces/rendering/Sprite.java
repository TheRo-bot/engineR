package dev.ramar.e2.interfaces.rendering;

import java.io.File;

public interface Sprite extends Renderable
{
	public void load(String fileName);

	public void load(File f);

	public int getWidth();

	public int getHeight();

    public void rotate(double deg);

    public void rotateTo(double deg);

    public void translate(double x, double y);

    public void scale(double xFactor, double yFactor);

    public Sprite copyArea(int x, int y, int w, int h);

}


