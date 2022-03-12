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

    public static abstract class Action
    {
        public Action()
        {

        }   

        public HiddenList<Action> toBlock = new ActionList();

        private class ActionList extends HiddenList<Action>
        {
            private List<Action> getList()
            {
                return this.list;
            }
        }
        public boolean equals(Object in)
        {
            return in != null &&
                   in instanceof Action &&
                   ((Action)in).getName().equals(getName());
        }


        protected void blockAll(ActionManager am)
        {
            for( Action a : ((ActionList)this.toBlock).getList() )
                am.block(this, a);
        }

        protected void unblockAll(ActionManager am)
        {
            for( Action a : ((ActionList)this.toBlock).getList() )
                am.unblock(this, a);
        }

        public abstract String getName();


        public abstract boolean act(ActionManager am, Object... o);

        public void onUnblock() {}

        public boolean blockedAct(ActionManager am, Object... o)
        { return false; }

    }

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

    public void block(Action a, Action b)
    {
        if( !actions.contains(a) )
            actions.add(a);
        if( !actions.contains(b) )
            actions.add(b);

        actions.connect(b, a);
    }

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
            if( b != null && permitRun(b))
                b.onUnblock();
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
        if( this.actions.contains(s) )
            out = this.isBlocked(this.actions.get(s));

        return out;
    }

    public boolean isBlocked(Action act)
    {
        boolean out = false;

        // if the node of <act> has links, then it's being blocked by those links.
        Node<Actions> node = this.actions.get(act);
        if( node != null )
            out = n.getLinks() != null && !n.getLinks().isEmpty();

        return out;
    }


    public boolean blockedRun(Action a)
    {
        boolean fired = false;

        if( a != null )
        {
            if( !this.isBlocked(a) ) 
            {
                a.act(this);
                fired = true;
            }
            else
                a.blockedAct(this);
        }

        return fired;
    }

    public boolean blockedRun(Action a, Object... o)
    {
        boolean fired = false;
        if( a != null )
        {
            if( !this.isBlocked(a) )
            {
                a.act(this, o);
                fired = true;
            }
            else
                a.blockedAct(this, o);
        }

        return fired;
    }

    public void clean()
    {

    }
}