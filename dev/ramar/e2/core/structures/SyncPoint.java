package dev.ramar.e2.core.structures;

import dev.ramar.utils.lists.LinkedNodes.GrowableNode;
import dev.ramar.utils.lists.LinkedNodes.Node;


public abstract class SyncPoint
{

    private GrowableNode<SyncPoint> neighbours = new GrowableNode<>();


    public SyncPoint()
    {

    }

    /*
    Method: addNeighbour
     - Adds a two way link between this and <sp>
    */  
    public void addNeighbour(SyncPoint sp)
    {

        // int getNodeCount()
        // Node<E> getNode(int n)

        GrowableNode<SyncPoint> node = new GrowableNode<SyncPoint>(sp);

        neighbours.add(node);
        sp.neighbours.add(neighbours);
    }


    public void removeNeighbour(SyncPoint sp)
    {
        for( int ii = 0; ii < neighbours.getNodeCount(); ii++ )
        {
            Node<SyncPoint> n = neighbours.getNode(ii);
            if( sp.getID().equals(n.getValue().getID()) )
            {
                neighbours.remove(ii);
            }
        }
        neighbours.remove(sp);
        sp.neighbours.remove(neighbours);        
    }


    /*
    Method: addLink
     - Adds a one way link between this and <sp>
    */
    public void addLink(SyncPoint sp)
    {
        GrowableNode<SyncPoint> node = new GrowableNode<>(sp);

        neighbours.add(node);
    }

    
    public void removeLink(SyncPoint sp)
    {
        neighbours.remove(sp);
    }




    public abstract void setup();

    public abstract void load();

    public abstract void unloadTo(SyncPoint sp);

    public abstract String getID();

}