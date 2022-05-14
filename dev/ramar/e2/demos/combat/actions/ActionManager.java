package dev.ramar.e2.demos.combat.actions;

import dev.ramar.utils.DiGraph;
import dev.ramar.utils.nodes.Node;
import dev.ramar.utils.HiddenList;

import java.util.List;
import java.util.ArrayList;

/*
Class: ActionManager
 - Uses a utils DiGraph to store all possible actions,
   where an x -> y connection means that x is blocked by y.
   x can only run if all connections aren't signalling to block 
*/
public class ActionManager
{
    public DiGraph<Action> actions = new DiGraph<>();

    public ActionManager()
    {

    }


    public void add(Action a)
    {
        actions.add(a);
    }

    public ActionManager withAdd(Action a)
    {
        add(a);
        return this;
    }


    public Action get(String name)
    {
        for( Action a : actions )
            if( a.getName().equals(name) )
                return a;

        return null;
    }


    // b will be blocked by a
    public void block(Action a, Action b)
    {
        if( !actions.contains(a) )
            actions.add(a);
        if( !actions.contains(b) )
            actions.add(b);

        actions.connect(b, a);
    }

    // b will be blocked by a
    public void block(String a, String b)
    {

        Action aa = get(a),
               ab = get(b);
        if( aa == null )
            throw new IllegalArgumentException("'" + a + "' is not an action");
        if( ab == null )
            throw new IllegalArgumentException("'" + a + "' is not an action");

        block(aa, ab);
    }

    public void unblock(Action a, Action b)
    {
        if( actions.contains(a) && actions.contains(b) )
        {
            Node<Action> from = actions.get(b),
                           to = actions.get(a);
            from.removeLink(to);

            // only fire if we're the last one to unblock
            if( ! this.isBlocked(b))
                b.onUnblock(this);
        }
    }
    
    public void unblock(String a, String b)
    {
        Action aa = get(a),
               ab = get(b);

        if( aa == null )
            throw new IllegalArgumentException("'" + a + "' is not an action");
        if( ab == null )
            throw new IllegalArgumentException("'" + a + "' is not an action");

        unblock(aa, ab);
    }


    public boolean isBlocked(String s)
    {
        boolean out = false;
        Action act = this.get(s);
        if( this.actions.contains(act) )
            out = this.isBlocked(act);

        return out;
    }

    public boolean isBlocked(Action act)
    {
        boolean out = false;

        // if the node of <act> has links, then it's being blocked by those links.
        Node<Action> node = this.actions.get(act);
        if( node != null )
            out = node.getLinks() != null && !node.getLinks().isEmpty();

        return out;
    }



    public boolean blockedRun(Action a, Object e)
    {
        boolean fired = false;


        if( a != null )
        {
            if( !this.isBlocked(a) )
            {
                a.act(this, a.convertObj(e));
                fired = true;
            }
            else
                a.blockedAct(this, a.convertObj(e));
        }

        return fired;
    }

    public void clear()
    {
        this.actions.clear();
    }

}