// package src.structures.renderables.entities;

// import src.interfaces.ItemHolder;
// import src.interfaces.events.listeners.GunListener;

// import src.structures.StaticInfo;
// import src.structures.items.Item;
// import src.structures.items.weapons.Gun;
// import src.structures.items.weapons.guns.Bullet;
// import src.interfaces.events.producers.MouseController;

// import src.structures.Vec2;

// import src.backend.Moment;

// import java.util.*;

// public class InventoryEntity extends ControllableEntity implements ItemHolder, GunListener
// {

// 	private static MouseController mouseController;

// 	public static void setMouseController(MouseController mc)
// 	{
// 		mouseController = mc;
// 	}


// 	protected List<Item> items = new ArrayList<>();
// 	protected int heldItem = 0;

// 	public InventoryEntity(double x, double y, double xv, double yv)
// 	{
// 		super(x, y, xv, yv);
// 	}

// 	public InventoryEntity(Vec2 pos, Vec2 vel)
// 	{
// 		super(pos.getX(), pos.getY(), vel.getX(), vel.getY());
// 	}

// 	public InventoryEntity(Vec2 pos, double xv, double yv)
// 	{
// 		super(pos.getX(), pos.getY(), xv, yv);
// 	}

// 	public void addItem(Item i)
// 	{
// 		items.add(i);
// 	}

// 	public void selectItem(int index)
// 	{
// 		heldItem = index;
// 	}

// 	public Item getHeldItem()
// 	{
// 		try
// 		{
// 			return items.get(heldItem);
// 		}
// 		catch(IndexOutOfBoundsException e)
// 		{
// 			return null;
// 		}
// 	}

// 	@Override
// 	protected void leftClick()
// 	{
// 		Item i = getHeldItem();
// 		if( i != null )
// 			i.use(0, this);
// 	}


// 	@Override
// 	protected void rightClick()
// 	{
// 		Item i = getHeldItem();
// 		if( i != null )
// 			i.use(1, this);
// 	}


// 	protected void reload()
// 	{
// 		Item i = getHeldItem();
// 		if( i != null && i instanceof Gun )
// 		{

// 			((Gun)i).reload();		
// 		}
// 	}



// 	@Override
// 	protected void onAction(String action)
// 	{
// 		super.onAction(action);
// 		switch(action)
// 		{
// 			case StaticInfo.Control.Actions.RELOAD:
// 				reload();
// 				break;
// 		}

// 	}


// 	/* ItemHolder implementation
// 	------------------------------
// 	*/

// 	public void itemUseCallback(Item i)
// 	{}


// 	/* GunListener implementation
// 	---------------------------------
// 	*/

// 	protected void shootTo(Bullet b, Vec2 p1, Vec2 p2)
// 	{
// 		double distX = p2.getX() - p1.getX();
// 		double distY = p2.getY() - p1.getY();

// 		Gun g = ((Gun)items.get(heldItem));

// 		Random rd = StaticInfo.getRandom();

// 		b.getPos().set(p1);


// 		if( distX != 0 || distY != 0 )
// 		{
//            double angle = Math.acos(distX / Math.sqrt(distX * distX + distY * distY));

//            if( p2.getY() < p1.getY() )
// 				angle *= -1;

//            angle += g.getSpread() * (rd.nextDouble() * (rd.nextBoolean() ? 1 : -1));

//            double velX = g.getForce() * Math.cos(angle);
//            double velY = g.getForce() * Math.sin(angle);

//            b.getVel().set(velX, velY);

// 		}
// 	}

// 	public void fireShot(Bullet b)
// 	{
// 		StaticInfo.getRenderer().addRenderable(b);
// 		b.startUpdating();
// 		StaticInfo.getSUpdateManager().addSUpdater(b);
// 		b.registerToGroup(StaticInfo.Hit.GroupNames.PLAYER_BULLETS);

// 		shootTo(b, pos, mouseController.getMousePos());
// 	}

// }