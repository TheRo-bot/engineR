package dev.ramar.e2.console;

import java.io.OutputStream;
import java.io.PrintStream;

import java.util.Arrays;

import java.util.List;
import java.util.LinkedList;


/*
Class: ConsoleOutput
 - The generic PrintStream for the EngineR2 console.
 - Most this file realistically is the inner ConsoleOutputStream,
   but the PrintStream exists so you can actually use it like a PrintStream 
*/
public class ConsoleOutput extends PrintStream
{

    public ConsoleOutput()
    {
        super(new ConsoleOutputStream());
    }

    public String toString()
    {
        return this.out.toString();
    }

    public void setLineCount(int count)
    {
        ((ConsoleOutputStream)this.out).setLineCount(count);
    }

    public void setLineLength(int length)
    {
        ((ConsoleOutputStream)this.out).setLineLength(length);   
    }

    private static class ConsoleOutputStream extends OutputStream
    {
        private String[] lines = new String[0];
        private String buffer = "";
        private int length;

        public ConsoleOutputStream()
        {

        }

        public String toString()
        {
            String out = "";
            for( int ii = this.lines.length - 1; ii >= 0; ii-- )
                out += this.lines[ii] + '\n';

            out += buffer;

            return out;
        }

        public synchronized void setLineLength(int length)
        {
            List<String> newLines = new LinkedList<>();
            this.length = length;

            for( int ii = this.lines.length - 1; ii >= 0; ii-- )
            {
                String line = this.lines[ii];
                while(line != null && line.length() > 0)
                {
                    String section = line.substring(0, Math.min(length, line.length()));
                    newLines.add(0, section);

                    if( line.length() > length )
                        line = line.substring(length, line.length());
                    else
                        line = null;
                }
            }

            String[] out = newLines.toArray(new String[0]);
            this.lines = out;
        }

        public synchronized void setLineCount(int lineCount)
        {
            this.lines = Arrays.copyOf(this.lines, lineCount);
            for( int ii = 0; ii < this.lines.length; ii++ )
                if( this.lines[ii] == null )
                    this.lines[ii] = "";
        }

        /* OutputStream Implementation
        --===----------------------------
        */

        public synchronized void clear()
        {
            for( int ii = 0; ii < this.lines.length; ii++ )
                lines[ii] = "";
        }

        public synchronized void flush()
        {
            String[] newLines = new String[this.lines.length + 1];
            newLines[0] = this.buffer;
            for( int ii = 1; ii < newLines.length; ii++ )
                newLines[ii] = this.lines[ii - 1];

            this.lines = newLines;
            buffer = "";
        }

        public synchronized void write(int b)
        {
            char c = (char)b;
            if( c == '\n' )
                this.flush();
            else
                buffer += c;

            if( buffer.length() > this.length ) 
                this.flush();
        }
    }


}