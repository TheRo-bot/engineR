package dev.ramar.e2.interfaces.rendering;

import dev.ramar.e2.structures.*;

import dev.ramar.e2.interfaces.events.producers.Controller;

import java.util.List;
import java.util.Map;

import java.io.File;

import java.awt.Font;
/*
Interface: ViewPorts
 - The Interface for a Window which the User sees the screen from
*/
public interface ViewPort
{
	public void initialise(WindowSettings settings);

	public Sprite createSprite(String fileName);

	public Sprite createSprite(File file);

	public AnimatedSprite createAnimatedSprite(String fileName, double fps);

	public AnimatedSprite createAnimatedSprite(File file, double fps);

	// public Vec2 getResolution();

	// public Vec2 setResolution();

	public int getScreenWidth();

	public int getScreenHeight();

	public void setColour(Colour c);

	public void setColour(int r, int g, int b, int a);

	public Colour getColour();

	public void setFont(String s);

	public void setFont(Font f);

	public Font getFont();

	public void setWorldCenter(Vec2 v);

	public void setWorldCenter(double x, double y);

	public Vec2 getWorldCenter();

	public void setWorldScale(double x, double y);

	public Vec2 getWorldScale();

	public void setPixel(int x, int y, int r, int g, int b, int a); 

	public void drawRect(double px, double py, int w, int h);

	public void drawRect(double px1, double py1, double px2, double py2);

	public void drawRect(Vec2 p1, int w, int h);

	public void drawRect(Vec2 p1, Vec2 p2);

	public void outlineRect(double px, double py, int w, int h);

	public void outlineRect(double px1, double py1, double px2, double py2);

	public void outlineRect(Vec2 p1, int w, int h);

	public void outlineRect(Vec2 p1, Vec2 p2);

	public void drawSpline(List<Vec2> positions);

	public void drawSpline(List<Vec2> positions, Vec2 thisOff);

	public void drawSpline(List<Vec2> positions, double offX, double offY);

	public void drawSpline(int[] xs, int[] ys, int count);

	public void drawColouredSpline(Map<Integer, Colour> colours, List<Vec2> positions);

	public void drawPoly(List<Vec2> positions);

	public void drawPoly(int[] xs, int[] ys, int count);

	public void drawCircle(double x, double y, double r);

	public void drawCircle(Vec2 pos, double r);

	public void outlineCircle(double x, double y, double r);

	public void outlineCircle(Vec2 pos, double r);

	public void drawOval(double x, double y, double r1, double r2);

	public void drawOval(Vec2 pos, double r1, double r2);

	public void outlineOval(double x, double y, double r1, double r2);

	public void outlineOval(Vec2 pos, double r1, double r2);

	public void drawLine(Vec2 start, Vec2 end);

	public void drawLine(double startX, double startY, double endX, double endY);

	public void drawLine(Vec2 start, double endX, double endY);

	public void drawLine(double startX, double startY, Vec2 end);

	public void drawAbsText(String text, double x, double y);

	public void drawText(String text, double cX, double cY);

	public void drawText(String text, Vec2 cPos);

	public void drawSprite(Sprite s, double cX, double cY);

	public void drawSprite(Sprite s, Vec2 cPos);


	// Overlayer specific methods

	public void overlayRect(double x, double y, int w, int h);

	public void overlayRect(double x, double y, double x2, double y2);

	public void overlayFilledRect(double x, double y, int w, int h);

	public void overlayFilledRect(double x, double y, double x2, double y2);


	public void overlaySpline(List<Vec2> positions);

	public void overlaySpline(List<Vec2> positions, Vec2 off);

	public void overlaySpline(int[] xs, int[] ys, int count);

	public void overlayPoly(List<Vec2> positions);

	public void overlayPoly(int[] xs, int[] ys, int count);


	public void overlayCircle(double x, double y, double r);

	public void overlayCircle(Vec2 v, double r);


	public void overlayOval(double x, double y, double r1, double r2);

	public void overlayOval(Vec2 v, double r1, double r2);


	public void overlayLine(double x1, double y1, double x2, double y2);

	public void overlayLine(Vec2 s, double ex, double ey);

	public void overlayLine(Vec2 s, Vec2 e);


	public void overlayText(String text, double cX, double cY);

	public void overlayText(String text, Vec2 v);

	public void overlayAbsText(String text, double x, double y);


	public void overlaySprite(Sprite s, double x, double y);

	public void overlaySprite(Sprite s, Vec2 v);

}