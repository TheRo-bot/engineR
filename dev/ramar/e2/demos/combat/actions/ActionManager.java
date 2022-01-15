package dev.ramar.e2.demos.combat.actions;

import dev.ramar.utils.DiGraph;
import dev.ramar.utils.nodes.Node;

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
        public boolean equals(Object in)
        {
            return in != null &&
                   in instanceof Action &&
                   ((Action)in).getName().equals(getName());
        }

        public abstract String getName();

        public abstract boolean act(Object[] o);

        public void onUnblock() {}

        public boolean blockedAct(Object[] o)
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



    public boolean permitRun(Action a)
    {
        Node<Action> n = actions.get(a);

        return n.getLinks().isEmpty();
    }

        // bind(kc, (KeyCombo press) ->
        // {
        //     actionManager.blockedRun(myAction);
        // });

    public boolean blockedRun(Action a, Object[] o)
    {
        boolean fired = false;

        if( permitRun(a) )
            a.act(o);
        else
            a.blockedAct(o);

        return fired;
    }

    public void clean()
    {

    }
}