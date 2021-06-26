package dev.ramar.e2.structures;


public class Matrix
{

    private int n, m;

    private double[][] mat;
    private boolean settedUp = false;

    private void setup()
    {
        mat = new double[m][n];
        settedUp = true;
    }


    public Matrix(int n, int m)
    {
        this.n = n;
        this.m = m;

        setup();
    }


    public Matrix(int n, int m, boolean makeMat)
    {
        this.n = n;
        this.m = m;

        if( makeMat )
            setup();
    }


    /* Helper Methods
    ---------------------
    */

    private static boolean inBounds(int index, int max)
    {
        return index >= 0 && index < max;
    }


    private void throwOnBoundsCheck(int x, int y)
    {
        if(! inBounds(x, n) )
            throw new IndexOutOfBoundsException("X Index " + x + " Out of bounds for range 0 - " + n);

        if(! inBounds(y, m) )
            throw new IndexOutOfBoundsException("Y Index " + y + " Out of bounds for range 0 - " + m);
    }


    public void testSetup()
    {
        if( mat != null )
        {
            boolean allArraysGood = true;
            for( double[] d : mat )
            {
                allArraysGood = allArraysGood && d != null;
                if(! allArraysGood )
                    break;
            }

            settedUp = allArraysGood;
        }
    }


    public boolean isSetup()
    {
        return settedUp;
    }


    public void output()
    {
        String exportString = "";
        for( int ii = 0; ii < m; ii++ )
        {
            exportString += "[ ";
            for( int jj = 0; jj < n; jj++ )
                exportString += mat[ii][jj] + ", ";

            exportString = exportString.substring(0, exportString.length() - 2) + " ]\n";
        }
        System.out.print(exportString);
    }

    /* Accessors
    ---------------
    */

    public int getN()
    {
        return n;
    }


    public int getM()
    {
        return m;
    }


    public double getEntry(int x, int y)
    {
        throwOnBoundsCheck(x, y);
        return mat[y][x];
    }


    public double[] sequence()
    {
        return sequenceFrom(0);
    }


    public double[] sequence(double[] in)
    {
        return sequenceFrom(0, in);
    }


    public double[] sequenceFrom(int start)
    {
        return sequenceFrom(start, new double[m * n]);    
    }


    public double[] sequenceFrom(int start, double[] in)
    {

        int index = start;

        int tooFar = Math.min(in.length, m * n);

        for( int ii = index; ii < tooFar; ii++ )
        {
            int jj = (int)(ii / n),
                modII = ii % n;

            in[ii] = mat[jj][modII];
            index++;
        }

        return in;
    }


    public double[] getArray(int array)
    {
        if( array < 0 || array > n )
            throw new IndexOutOfBoundsException("Index " + array + " Out of bounds for range 0 - " + n);
        return mat[array];
    }


    public double[][] getCoreArray()
    {
        return mat;
    }


    /* Mutators
    --------------
    */

    public void set(double... ds)
    {
        set(0, ds);
    }

    public void set(int start, double... ds)
    {
        if( ds.length > start + (n * m) )
            throw new IndexOutOfBoundsException("size " + ds.length + " sequence will not fit in " + (n * m) + " size matrix");


        int ii = start;
        for( double d : ds )
        {
            int jj = (int)(ii / n);

            int modII = ii % n;
            mat[jj][modII] = d;

            ii++;
        }
    }


    public void setEntry(int x, int y, double val)
    {
        throwOnBoundsCheck(x, y);

        mat[y][x] = val;
    }


    public void setCoreArray(double[][] arr)
    {
        mat = arr;
    }

    public void setArray(int array, double[] arr)
    {
        if( array < 0 || array > n)
            throw new IndexOutOfBoundsException("Index " + array + " Out of bounds for range 0 - " + n);

        mat[array] = arr;
    }


    /* Math Methods
    ------------------
    */

    private double multiplyArray(double[] arr)
    {
        double exp = 0;
        for( double d : arr )
            exp *= d;

        return exp;
    }


    // public Matrix multiply(Matrix in)
    // {
    //     if( n != in.m )
    //         throw new IllegalArgumentException("Cannot multiply a Matrix of height " + in.m + " with a matrix of length " + n);

    //     Matrix output = new Matrix(in.n, m);

    //     for( int hh = 0; hh < n; hh++ )
    //     {
    //         for( int ii = 0; ii < m; ii++ )
    //         {
    //             double ourCalc = multiplyArray(mat[ii]);

    //             double inCalc = 0.0;
    //             for( int jj = 0; jj < in.m; jj++ )
    //             {
    //                 inCalc *= in.getEntry(ii, jj);
    //             }

    //             output.setEntry(hh, ii, ourCalc + inCalc);
    //         }

    //     }

    //     return output;
    // }


    public Matrix multiply(Matrix in)
    {

        if( n != in.m )
            throw new IllegalArgumentException("Cannot multiply a Matrix of height " + in.m + " with a matrix of length " + n);

        // in.m == n
        Matrix output = new Matrix(n, m);


        // for every row

        // for every in column

            // value = 0

            // for index = 0 -> our n

                // value += our mat[index][our row index] * in mat[in column index][index]


            // entry [my row index][my in column index] = value

        for( int ii = 0; ii < m; ii++ )
        {
            for( int jj = 0; jj < n; jj++ )
            {
                double value = 0;

                for( int kk = 0; kk < n; kk++ )
                    value += (getEntry(kk, ii)) * (in.getEntry(jj, kk));

                // System.out.println("setting value " + ii + " " + jj + " to " + value);
                output.setEntry(jj, ii, value);
            }
        }


        return output;
    }
}