package dev.ramar.e2.awt.objects;

import dev.ramar.e2.core.objects.RObject;

import dev.ramar.e2.core.drawing.text.TextMods;
import dev.ramar.e2.core.drawing.image.ImageMods;

import dev.ramar.e2.core.rendering.Viewport;
import dev.ramar.e2.awt.rendering.AWTViewport;

import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.RenderingHints;

import java.awt.image.BufferedImage;

public class BufferedText extends RObject
{
	private final BufferedImage FONT_GETTER = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);

	private FontMetrics getMetricsFor(Font f)
	{
		Graphics2D g2d = (Graphics2D)FONT_GETTER.getGraphics();
		if( g2d != null )
		{
			g2d.setFont(f);
			return g2d.getFontMetrics();
		}

		return null;
	}

	public BufferedText()
	{

	}

	public BufferedText(String text)
	{
		this.setText(text);
	}

	private String text = null;

	public String getText()
	{  return this.text;  }

	public void setText(String text)
	{
		boolean changed = text != null && !text.equals(this.text);

		this.text = text;

		if( changed )
			this.onTextChange();
	}




	public final TextMods textMods = new TextMods();

	public final ImageMods imageMods = new ImageMods();

	private double getHeightFor(String text, FontMetrics fm)
	{
        float h = fm.getHeight();

        return h;
	}

	public void updateBuffer()
	{
		this.onTextChange();
	}

	protected void onTextChange()
	{
		if( this.text == null )
			return;
		
		int w = 0,
		    h = 0;

		int size = this.textMods.size.get();
		Font thisFont = new Font(this.textMods.font.get(), Font.PLAIN, size);

		FontMetrics metrics = getMetricsFor(thisFont);
		w = metrics.stringWidth(this.text);
		h = (int)(this.getHeightFor(this.text, metrics) * 1.5);

		BufferedImage newImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = newImage.createGraphics();

        RenderingHints rh = new RenderingHints(
            RenderingHints.KEY_TEXT_ANTIALIASING,
            RenderingHints.VALUE_TEXT_ANTIALIAS_GASP
        );
        g2d.setRenderingHints(rh);

		g2d.setFont(thisFont);

		g2d.drawString(this.text, 0.0f, (float)(h * 0.5));

		this.image = newImage;
	}

	public BufferedImage image = null;


	public void drawAt(double x, double y, Viewport vp)
	{
		if( image != null && vp instanceof AWTViewport )
		{
			AWTViewport avp = (AWTViewport)vp;
			avp.draw.image.withMod(this.imageMods);
			avp.draw.image.at(x, y, this.image);
			avp.draw.image.clearMod();
		}
	}
}