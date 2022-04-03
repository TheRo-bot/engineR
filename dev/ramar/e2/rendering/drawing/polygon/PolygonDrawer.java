package dev.ramar.e2.rendering.drawing.stateless.polygon;

public class PolygonDrawer
{
    public PolygonDrawer() 
    {

    }

    public PolygonDrawer withViewPort(ViewPort vp)
    {
        this.viewPort = vp;
        return this;
    }


    public void drawPoints(double... points)
    {}


    public void drawOffsets(double x, double y, double... offsets)
    {
        int xs = new int[offsets.length / 2 + 1]
        int ys = new int[offsets.length / 2 + 1]

        for(int ii = 0; ii < points.length; ii += 2 )
        {
            xs[ii - 1] = offsets[ii    ];
            xs[ii    ] = offsets[ii + 1];
        }

        System.out.println(Arrays.toString(xs, ys));
    }
}