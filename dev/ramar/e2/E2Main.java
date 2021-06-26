package dev.ramar.e2;
// - e2
//   - backend
import dev.ramar.e2.backend.*;
import dev.ramar.e2.backend.world_components.*;
import dev.ramar.e2.backend.renderers.awt.*;


//   - interfaces
import dev.ramar.e2.interfaces.collision.*;
import dev.ramar.e2.interfaces.events.listeners.*;
import dev.ramar.e2.interfaces.events.producers.*;
import dev.ramar.e2.interfaces.rendering.*;
import dev.ramar.e2.interfaces.updating.*;

//   - structures
import dev.ramar.e2.structures.*;
import dev.ramar.e2.structures.hitboxes.*;
import dev.ramar.e2.structures.renderables.*;
import dev.ramar.e2.structures.renderables.entities.*;
import dev.ramar.e2.structures.items.weapons.*;
import dev.ramar.e2.structures.items.weapons.guns.*;

import dev.ramar.e2.structures.renderables.hud.*;
import dev.ramar.e2.structures.renderables.hud.elements.*;


//   - backend
import dev.ramar.e2.backend.renderers.*; 
import dev.ramar.e2.backend.updaters.*;


// - utils
import dev.ramar.utils.Timer;
import dev.ramar.utils.Action;
import dev.ramar.utils.exceptions.*;

// java
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import java.util.Map;
import java.util.HashMap;

import java.util.Arrays;

import java.io.*;

import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;
/*
TEMP IMPORTS
*/

import javax.imageio.ImageReader;
import javax.imageio.ImageIO;
import javax.imageio.stream.*;
import javax.imageio.ImageReadParam;


import java.awt.image.Raster;
import java.awt.image.DataBuffer;
import java.awt.image.BufferedImage;

public class E2Main
{

	public static void setup()
	{
		Main.Entrypoints.addEntrypoint((String[] args) ->
        {
        	System.out.println("start!");
        	new E2Main().waitForClose();
        	System.out.println("E2 close!");
        });
	}


	private Vec2 screenDim = new Vec2(1280, 720);
	// private Vec2 screenDim = new Vec2(1000, 750).multiply(1.125);

	private double ups = 60.0;

	private SUpdateManager sManager;
	private FUpdateManager fManager;
	private RenderManager rManager;
	private AWTRenderer awtRenderer;
	private HitManager hManager;
	private ViewPort viewPort;
	private World world;
	private Random rd;


	public E2Main()
	{
		sManager = new SlowUpdateManager(ups);
		fManager = new FastUpdateManager();
		awtRenderer = new AWTRenderer();
		rManager = awtRenderer;
		hManager = new CollisionManager();
		world = new World();
		viewPort = awtRenderer;

		StaticInfo.Objects.initialise(rManager, sManager, fManager, hManager, new TimerManager()
		{
			public void startTimer(dev.ramar.e2.structures.Timer t)
			{
				throw new IllegalAccessError("UHHHH WTF");
			}

		}, awtRenderer, awtRenderer, awtRenderer, world);


		rd = StaticInfo.Objects.getRandom();
		init();
		start();
		postitialise();
		imageTest();
		animationTest();
		hudTest();

		rManager.addRenderable(HUD.HUDManager.STATIC_REF);

	}

	public void waitForClose()
	{
		Main.outputLN("waiting!!");
		sManager.waitForClose();
		Main.outputLN("sManager close!!");
		fManager.waitForClose();
		Main.outputLN("fManager close!!");
		rManager.waitForClose();
		Main.outputLN("rManager close!!");
		awtRenderer.waitForClose();
		Main.outputLN("awtRenderer close!!");

		
	}


	private Sprite getSprite(String relPath)
	{
		return viewPort.createSprite(StaticInfo.Functions.getFileFromName(relPath));
	}


	private void loadSprites()
	{
		StaticInfo.Sprites.putSprite("GRASS", getSprite("resources/textures/world/grass.png"));
	}


	public void init()
	{
		loadSprites();
		WindowSettings ws = new WindowSettings((int)screenDim.getX(), (int)screenDim.getY(), (int)screenDim.getX(), (int)screenDim.getY(), false, "AIRSQUAD");


		StaticInfo.Objects.getViewPort().initialise(ws);
		((Controller)rManager).initialise();

		Group g = new Group(StaticInfo.Hit.GroupNames.PLAYER);
		g.addGroupToList(StaticInfo.Hit.GroupNames.SHOOTABLE);
		g.addGroupToList(StaticInfo.Hit.GroupNames.COLLIDABLE);

		Group bullets = new Group(StaticInfo.Hit.GroupNames.PLAYER_BULLETS);
		bullets.addGroupToList(StaticInfo.Hit.GroupNames.SHOOTABLE);
		bullets.addGroupToList(StaticInfo.Hit.GroupNames.COLLIDABLE);

		
		hManager.registerGroup(g);
		hManager.registerGroup(bullets);

		Sprite grassSprite = StaticInfo.Sprites.getSprite("GRASS");

		HitBox chunkBox = new HitBox(new HitRect(0, 0, 250, 250), 0, 0);
		chunkBox.addToHitBox(new HitRect(0, 0, 250, 250));

		int squareSize = 50;
		for( int ii = -squareSize/2; ii < squareSize/2; ii++ )
		{
			for( int jj = -squareSize/2; jj < squareSize/2; jj++)
			{
				Chunk theChunk = world.getChunk_chunkCoord(ii, jj);
				if( theChunk == null )
				{
					theChunk = new Chunk(ii, jj, world);
					world.addChunk(theChunk);
				}
				
				final Chunk c = theChunk;
				Tier1_SlowEntity se = new Tier1_SlowEntity(chunkBox, c.getWorldX(), c.getWorldY(), 0, 0)
				{
					@Override
					public void update(Moment m)
					{}


					@Override
					public void render(ViewPort vp)
					{
						Colour origC = vp.getColour();
						Colour toUse = isColliding() ? mainColour : secondaryColour;

						int size = 10;
						// vp.drawSprite(grassSprite, pos);
						vp.drawRect(c.getWorldX() - size/2, c.getWorldY() - size/2, size, size);

						vp.setColour(origC);
					}


					@Override
					public void onCollision(Hittable h)
					{
						super.onCollision(h);
					}
				};

				world.addLayerable(se, 1);
				se.startUpdating();

			}
		}


	}

	public void start()
	{
		sManager.start();
		fManager.start();
		rManager.start();
	}


	private int temp = 0;
	private Object mutex = new Object();
	public Tier2_FastEntity player;

	private double xScale = 1.0,
				   yScale = 1.0;

	public void postitialise()
	{

		Camera thisCamera = new Camera(0, 0, (int)(screenDim.getX()), (int)(screenDim.getY()), (AWTRenderer)rManager, world);
		rManager.addRenderable(thisCamera);

		FastEntity mouseCursor = new FastEntity(0, 0, 0, 0)
		{
			@Override
			public void update(double delta)
			{
				setPos(((AWTRenderer)rManager).getMousePos());
			}

			@Override
			public void render(ViewPort vp)
			{
				drawSelf(null, vp);
			}

			@Override
			public void drawSelf(Vec2 v, ViewPort vp)
			{
				super.drawSelf(v, vp);
				vp.drawText(pos.getX() + ", " + pos.getY(), 500, 30);
			}
		};

		mouseCursor.startUpdating();
		world.addLayerable(mouseCursor, 4);

		HitBox hb2 = new HitBox(new HitRect(0, 0, 500, 500), screenDim.newDivide(2));
		HitBox hb4 = new HitBox(new HitRect(0, 0, 30, 30), 0, 0);
		HitBox hb5 = new HitBox(new HitRect(0, 0, 10, 10), screenDim.newDivide(2));

		hb2.addToHitBox(new HitRect(0, 0, 400, 400));
		hb4.addToHitBox(new HitRect(0, 0, 15, 15));

		hb5.addToHitBox(new HitCircle(0, 0, 5));

		// should be approx. units per second. a unit is one pixel right now
		// so it's quite the fucking distance
		final Vec2 playerVel = new Vec2(500);

		player = new Tier2_FastEntity(hb4.clone(), 30, 60, 0, 0)
		{
			private final Vec2 VEL = playerVel;

			@Override
			public void initialise()
			{
				super.initialise();
				if( this.drag == null )
					this.drag = new Vec2(0);

				// drag isn't really drag, it more so is an acceleration modifier
				// anything < 1.0 makes moving slower
				// anything > 1.0 makes moving faster and snappier
				this.drag.set(20);
			}


			@Override
			public void render(ViewPort vp)
			{
				drawSelf(null, vp);
			}


			@Override
			public void drawSelf(Vec2 v, ViewPort vp)
			{
				super.drawSelf(v, vp);
				Colour origC = vp.getColour();

				// vp.setColour(255, 255, 255, 255);
				// vp.drawText(getUUID(), pos.getX(), pos.getY() + 35);
				vp.drawText(pos.toString(), pos.getX(), pos.getY() + 55);
				vp.drawLine(pos, pos.newAdd(vel));
				// vp.drawText(vel.toString(), pos.getX(), pos.getY() + 65);
				// vp.setColour(0, 0, 0, 255);
				// vp.drawText(items.get(selectedItem).name, pos.getX(), pos.getY() + 45);

				vp.setColour(origC);

			}


			@Override
			public void update(double delta)
			{
				super.update(delta);
				thisCamera.setPos(getPos());
			}


			@Override
			protected void moveUP(double delta)
			{
				setYVel(Math.max(vel.getY() + -VEL.getY() * delta, vel.getY() + -maxSpeed.getY() * delta));
			}


			@Override
			protected void moveDOWN(double delta)
			{
				setYVel(Math.min(vel.getY() + VEL.getY() * delta, vel.getY() + maxSpeed.getY() * delta));
			}


			@Override
			protected void moveRIGHT(double delta)
			{
				setXVel(Math.min(vel.getX() + VEL.getX() * delta, vel.getX() + maxSpeed.getX() * delta));
			}


			@Override
			protected void moveLEFT(double delta)
			{	
				setXVel(Math.max(vel.getX() + -VEL.getX() * delta, vel.getX() + -maxSpeed.getX() * delta));
			}
		};


		Tier2_FastEntity xTestEntity = new Tier2_FastEntity(hb4.clone(), 30, 30, 0, 0)
		{
			protected void initialise()
			{
				if( this.drag == null )
					drag = new Vec2(0);
 
				drag.set(20);
			}

			@Override
			protected void moveRIGHT(double delta)
			{
				setXVel(Math.min(vel.getX() + playerVel.getX() * delta, vel.getX() + maxSpeed.getX() * delta));
			}


			@Override
			protected void moveLEFT(double delta)
			{	
				setXVel(Math.max(vel.getX() + -playerVel.getX() * delta, vel.getX() + -maxSpeed.getX() * delta));
			}
		};

		xTestEntity.startUpdating();
		xTestEntity.doKeyListening();
		world.addLayerable(xTestEntity, 3);


		Bullet bullet = new HitScanBullet(2, StaticInfo.Hit.GroupNames.PLAYER_BULLETS, hb5.clone(), 0, 0, 0, 0)
		// bullet 
		{
			@Override
			public void initialise()
			{
				super.initialise();
				maxDistance = 500;
				// timeToLive = 5000;
				// drag.set(1.0);
			}
		};

		bullet.registerToGroup(StaticInfo.Hit.GroupNames.PLAYER);

		player.addItem(new Gun("Game her gun", bullet)
		{
			@Override
			protected void initialise()
			{
				maxAmmo = 500;
				clip = maxAmmo;
				force = 5000.0;
				reloadTime = 250;
				timeBetweenShots = 250;
				spread = 0.00;
			}
		});

		player.registerToGroup(StaticInfo.Hit.GroupNames.PLAYER);

		player.holdItem(0);

		player.startUpdating();
		player.doKeyListening();
		world.addLayerable(player, 2);


		FastEntity test2 = new FastEntity(0, 0, 0, 0)
		{
			private Map<String, Boolean> pressed = java.util.Collections.synchronizedMap(new HashMap<>());

			@Override
			protected void onAction(String s)
			{
				super.onAction(s);
				String action = s.toLowerCase();
				Boolean isPressed = pressed.get(action);

				if( isPressed == null || ! isPressed )
				{
					pressed.put(action, true);

					switch(action.toLowerCase())
					{
						case "p":
							FastUpdateManager fum = (FastUpdateManager)StaticInfo.Objects.getFUpdateManager();
							System.out.println("SUCCESSFUL P");
							break;

						case "o":
							placeRandomShootables(hb4, StaticInfo.Hit.GroupNames.SHOOTABLE, 50);
							break;

					}
				}
			}


			private double deltaToTime = 0.0;

			private long checkTime = System.currentTimeMillis() + 10;

			@Override
			public void update(double d)
			{
				long nowTime = System.currentTimeMillis();
				if( nowTime > checkTime )
				{
					checkTime = nowTime + 10; 
					synchronized(actions)
					{
						for( String s : actions )
							onAction(s);
					}

					if(! actions.contains("P") )
						pressed.put("P", false);

					if(! actions.contains("O") )
						pressed.put("O", false);
				}
			}

		};

		test2.startUpdating();
		test2.doKeyListening();

		placeRandomShootables(hb4, StaticInfo.Hit.GroupNames.SHOOTABLE, 50);

		// int m_ = 5, n = 2;
		// Matrix m = new Matrix(n, m_);

		// int count = 0;
		// for( int mm = 0; mm < m_; mm++ )
		// {
		// 	for( int nn = 0; nn < n; nn++ )
		// 	{
		// 		m.setEntry(nn, mm, count);
		// 		count++;
		// 	}
		// }


		// System.out.println(m.isSetup() ? "setup!" : "not setup!");

		// // m.set(1.0, 2.0, 3.0,
		// // 	  4.0, 5.0, 6.0,
		// // 	  7.0, 8.0, 9.0);

		// m.output();

		// for( int ii = 0; ii < m_; ii++ )
		// {
		// 	for( int jj = 0; jj < n; jj++ )
		// 	{
		// 		System.out.println("mat[" + jj + "][" + ii + "] = " + m.getEntry(jj, ii));
		// 	}
		// }


		Matrix m = new Matrix(2, 3);

		m.set(1.0, 1.0,
			  3.0, 3.0,
			  5.0, 5.0);

		System.out.println("sequence = " + Arrays.toString(m.sequence()));
		// for( int ii = 0; ii < m_; ii++ )
		// 	System.out.println("array " + ii + ": " + Arrays.toString(m.getArray(ii)));

		Matrix rotate180 = new Matrix(2, 2);
		rotate180.set(  0.976296007, 0.216439614,
					   -0.216439614, 0.976296007);

		Matrix multiplied = m.multiply(rotate180);

		multiplied.output();
	}


	/*
	 - Places random shootable entities in a square radius [squareDistFromSpawn]
	*/
	public void placeRandomShootables(HitBox hb, String groupName, int amount)
	{
		int squareDistFromSpawn = 1000;
		for( int ii = 0; ii < amount; ii++ )
		{
			Tier2_FastEntity t2 = new Tier2_FastEntity(hb.clone(), rd.nextInt(squareDistFromSpawn) - squareDistFromSpawn / 2, rd.nextInt(squareDistFromSpawn) - squareDistFromSpawn / 2, 0, 0)
			{
				@Override
				public void onCollision(Hittable h)
				{
					super.onCollision(h);
					onKill();
				}
			};

			t2.startUpdating();
			world.addLayerable(t2, 3);
			t2.registerToGroup(groupName);

		}
	} 

	private BufferedImage createCharImage(char c, Font f)
	{
		Graphics2D randGraphics = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB).createGraphics();
		randGraphics.setFont(f);
		FontMetrics fm = randGraphics.getFontMetrics();

		int w = fm.stringWidth("" + c),
			h = fm.getHeight();

		BufferedImage exp = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = exp.createGraphics();
		g2d.setFont(f);
		g2d.drawString("" + c, (int)exp.getWidth() / 2- w/2, (int)exp.getHeight() / 2 + h/4);

		return exp;
	}


	private void textTest()
	{
		Font f = new Font("Dialog", Font.PLAIN, 100);

		final List<Sprite> sprites = new ArrayList<>();

		String str = "ssiss";

		for( char c : str.toCharArray() )
		{
			sprites.add(new AWTSprite(createCharImage(c, f)));
		}

		world.addLayerable(new FastEntity(0, 0, 0, 0)
		{
			// private Sprite sprite = new AWTSprite(bi);
			public void render(ViewPort vp)
			{
				drawSelf(null, vp);
			}

			public void drawSelf(Vec2 v, ViewPort vp)
			{
				vp.setColour(0, 255, 255, 255);

			}
		}, 4);
	}


	public void imageTest()
	{
		textTest();
		try
		{
			boolean doTest = false;

			if( doTest )
			{

				final BufferedImage i = ImageIO.read(new File("resources/textures/world/trans_owoarrow.png")); 

				int dist = 2000;
				for( int ii = 0; ii < 20; ii++ )
				{
					AWTSprite s = new AWTSprite(i);

					FastEntity fe = new FastEntity(rd.nextInt(dist) - dist / 2, rd.nextInt(dist) - dist / 2, 0, 0)
					{


						private double time = 0.025;
						private double startAng = (double)(rd.nextInt(360)) + rd.nextDouble();
						private double moveSpeed = rd.nextDouble() * (rd.nextBoolean() ? 1 : -1);

						private double scaleSpeed = 1;

						@Override
						public void render(ViewPort vp)
						{
							s.drawSelf(pos, vp);
						}

						private boolean firstUpdate = true;
						@Override
						public void update(double delta)
						{
							if( firstUpdate )
							{
								s.rotate(startAng);
								firstUpdate = false;
							}
							time -= delta;

							if( time < 0.0 )
							{

								s.rotate(moveSpeed);
								time = 0.025;
								scaleSpeed += rd.nextDouble() % 0.01 - 0.005;
								s.scale(scaleSpeed, scaleSpeed);

							}


						}	
					};
					fe.startUpdating();
					world.addLayerable(fe, 3);

				}
			}

/*			Animatable animationTest = new Animatable()
			{
				private boolean done = false,
								loop = false,
								paused = false;

				private List<Action> finishActions = new ArrayList<>();

				private long pauseStart = 0;
				private List<Action> unpauseActions = new ArrayList<>();

				private double scale_1_30 = 0, scale_2_40 = 0,
							   trans_1_40 = 0;
				private double scaleX = 10, scaleY = 10;
				private double posX = 0, posY = 0;

				private List<dev.ramar.e2.structures.Timer> timers = new ArrayList<>();

			    public void event(String eventName)
			    {
			    	if( paused )
			    	{
			    		unpauseActions.add(new Action()
		    			{
	    				    protected Void[] execute(Void... args) throws dev.ramar.utils.exceptions.IncorrectArgsException
	    				    {
	    				    	dev.ramar.e2.structures.Timer t = new dev.ramar.e2.structures.Timer((Animatable)this, 100, eventName);
	    				    	t.start();
	    				    	timers.add(t);
	    				    	return null;
	    				    }

							public List<?> getArgsList()
							{
								return null;
							}

		    			});
			    	}
			    	else
			    	{
				    	switch(eventName)
				    	{
				    		case "timer_scale_1_30":
				    			scale_1_30++;
				    			if( scale_1_30 < 30 )
				    			{
		    				    	dev.ramar.e2.structures.Timer t = new dev.ramar.e2.structures.Timer(this, 100, "scale_1_30");
		    				    	t.start();
		    				    	timers.add(t);
				    				scaleX += 5;
				    				scaleY += 5;
				    			}
				    			else
				    			{
		    				    	dev.ramar.e2.structures.Timer t = new dev.ramar.e2.structures.Timer(this, 100, "trans_1_40");
		    				    	t.start();
		    				    	timers.add(t);
		    				    	t = new dev.ramar.e2.structures.Timer(this, 100, "scale_2_40");
		    				    	t.start();
		    				    	timers.add(t);

				    			}
				    			break;

				    		case "timer_trans_1_40":

				    			trans_1_40++;
				    			if( trans_1_40 < 120 )
				    			{
				    				posX += 0.1 * trans_1_40;
				    				posY += 0.1 * trans_1_40;
				    				Timer.wait(10, "trans_1_40", this);
				    			}
				    			break;

				    		case "timer_scale_2_40":
			    				scale_2_40++;
				    			if( scale_2_40 < 120 )
				    			{
				    				scaleX -= 0.1 * scale_2_40;
				    				scaleY -= 0.1 * scale_2_40;
				    				Timer.wait(10, "scale_2_40", this);
				    			}
				    			break;

				    	}			    		
			    	}
			    }

				public void timerComplete(dev.ramar.e2.structures.Timer t)
				{
					event("timer_" + t.getDescription());
				}


				public void timerComplete(String name)
				{
					event("timer_" + name);
				}


			    public void start() throws IllegalStateException
			    {
			    	dev.ramar.e2.structures.Timer t = new dev.ramar.e2.structures.Timer(this, 100, "scale_1_30");
			    	t.start();
			    	timers.add(t);
			    }


			    public void pause()
			    {
			    	// lol idk
			    	pauseStart = System.currentTimeMillis();
			    	paused = true;
			    }


			    public void unpause()
			    {
			    	// lol idk
			    	paused = false;
			    	for( Action a : unpauseActions )
			    	{
			    		try
				    	{
				    		a.doAction();
				    	}
				    	catch(dev.ramar.utils.exceptions.IncorrectArgsException e) {}
			    	}
			    	pauseStart = -1;
			    }

			    public boolean isDone()
			    {
			    	return done;
			    }

			    public void addFinishAction(Action act)
			    {
			    	finishActions.add(act);
			    }

			    public void setLooping(boolean looping)
			    {
			    	loop = looping;
			    }

		    	public void render(ViewPort vp)
		    	{
		    		drawSelf(new Vec2(30, 30), vp);
		    	}

    			public void drawSelf(Vec2 pos, ViewPort vp)
    			{
    				Colour c = vp.getColour();
    				vp.setColour(255, 255, 255, 255);
    				double x = posX, y = posY;

    				if( pos != null )
    				{
    					x += pos.getX();
    					y += pos.getY();
    				}

    				vp.drawRect(x - scaleX/2, y - scaleY /2, (int)scaleX / 2, (int)scaleY / 2);
    				vp.setColour(c);
    			}
			};


			rManager.addRenderable(animationTest);
			new Thread(new Runnable()
			{
				public void run()
				{
					try
					{
						Thread.sleep(3000);
					}
					catch(InterruptedException e) {}

					System.out.println("ANIMATION START");
					animationTest.start();
				}
			}).start();*/
			// final AWTSprite s = new AWTSprite("resources/textures/world//single_health_bar.svg");

			// FastEntity fe = new FastEntity(0, 0, 0, 0)
			// {
			// 	private double rAmount = 0.0;
			// 	private AWTSprite sp = s;


			// 	@Override
			// 	public void render(ViewPort vp)
			// 	{
			// 		drawSelf(null, vp);
			// 	}

			// 	@Override
			// 	public void drawSelf(Vec2 v, ViewPort vp)
			// 	{
			// 		double x = getXPos(),
			// 			   y = getYPos();

			// 		if( v != null )
			// 		{
			// 			x += v.getX();
			// 			y += v.getY();
			// 		}

			// 		sp.drawSelf(pos, vp);
			// 		vp.setColour(0, 0, 0, 255);
			// 	}


			// 	private double deltaAdd = 0.0;
			// 	private double deltaSec = 0.0;
			// 	private boolean firstUpdate = true;


			// 	private int rotations = 0;
			// 	@Override
			// 	public void update(double delta)
			// 	{
			// 		if( firstUpdate )
			// 		{
			// 			firstUpdate = false;
			// 		}

			// 		// deltaAdd += delta;
			// 		// deltaSec += delta;

			// 		// if( deltaSec > 1.0)
			// 		// {
			// 		// 	sp.rotate(45);
			// 		// 	rotations++;

			// 		// 	if( deltaSec > 3.0 )
			// 		// 	{
			// 		// 		deltaSec = 0.0;
			// 		// 	}
			// 		// }

			// 		sp.scale(xScale, yScale);

			// 		setPos(player.getPos());

			// 		Vec2 thisPos = StaticInfo.Objects.getMouseController().getMousePos();

			// 		// if( Math.abs(thisPos.getX() - lastPos.getX() ) > 10.0 ||
			// 		// 	Math.abs(thisPos.getY() - lastPos.getY() ) > 10.0 )
			// 		// {
			// 			double distX = player.getXPos() - thisPos.getX();
			// 			double distY = player.getYPos() - thisPos.getY();


			// 			if( distX != 0 || distY != 0 )
			// 			{
			// 	           double rads = Math.acos(distX / Math.sqrt(distX * distX + distY * distY));

			// 	           double angle = Math.toDegrees(rads);

			// 	            if( player.getYPos() < thisPos.getY() )
			// 					angle *= -1;

			// 				angle -= 90;

			// 				sp.rotateTo(angle);
			// 				rotations++;
			// 			}
			// 			lastPos = thisPos;
			// 		// }



			// 	}

			// 	private Vec2 lastPos = new Vec2(0);

			// };

			// fe.startUpdating();
			// rManager.addRenderable(fe);

		}
		catch(IOException e) {}


	}


	public void animationTest()
	{

		try
		{
			final BufferedImage i = ImageIO.read(new File("resources/textures/world/trans_owoarrow.png")); 

			final Sprite s = new AWTSprite(i);
			Animation ani1 = new Animation()
			{
				private Sprite this_Sprite = new AWTSprite(i);
				private Map<String, dev.ramar.e2.structures.Timer> timers = new HashMap<>();
				private Map<String, Integer> counters = new HashMap<>();
				private double x = 0, y = 0;


				private dev.ramar.e2.structures.Timer makeTimer(String name, long ttc)
				{
					dev.ramar.e2.structures.Timer timer = new dev.ramar.e2.structures.Timer(ttc, name, this);
					timers.put(name, timer);
					return timer;
				}


				@Override
				public void start()
				{
					// makeTimer("rotate_1", 20).start();
					makeTimer("move_2", 20).start();
				}	

				private double scaleAmount = 1.1;
				private double amount = 1.0;
				@Override
				public void event(String e)
				{
					if(! counters.containsKey(e) )
						counters.put(e, -1);

					counters.put(e, counters.get(e) + 1);

					switch(e)
					{
						case "move_2":
							if( counters.get(e) < 100 )
							{
								amount += 0.0001;
								x += amount;
								timers.get(e).restart();
							}
							else
							{
								makeTimer("scale_1", 40).start();
								makeTimer("rotate_1", 10).start();
							}
							break;

						case "scale_1":
							if( counters.get(e) < 10 )
							{
								this_Sprite.scale(scaleAmount, scaleAmount);
								timers.get(e).restart();
							}
							break;

						case "rotate_1":
							if( counters.get(e) < 360 )
							{
								this_Sprite.rotate(1);
								timers.get(e).restart();
							}
							break;
					}
				}

				public void render(ViewPort vp)
				{
					this_Sprite.drawSelf(new Vec2(x, y), vp);
				}

				public void drawSelf(Vec2 v, ViewPort vp)
				{
					render(vp);
				}

			};


		}
		catch(IOException e) {}

	}



	public void hudTest()
	{
    // public Text(double x, double y, String text, String fontName, int fontSize)
    	// Font vcr = null;
    	// try
    	// {
    	// 	vcr = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream("resources/fonts/VCR_OSD_MONO.ttf"));
    	// 	vcr = vcr.deriveFont(150.0f);

    	// }
    	// catch(IOException | FontFormatException e) 
    	// {
    	// 	System.out.println("failed! " + e.getMessage());
    	// }
		Text text = new Text(500, 500, "HEY YOU", null, 150, new Colour(255, 255, 255, 255));
		new Thread(new Runnable()
		{
			public void run()
			{
				try
				{
					Thread.sleep(2000);
					text.setText("YOU'RE A..");
					Thread.sleep(1500);

					text.setText("B");
					int times = rd.nextInt(20);
					for( int ii = 0; ii < 5 + times; ii++ )
					{
						text.setText(text.getText() + "I");
						Thread.sleep(100);
					}

					text.setText(text.getText() + "T");
					Thread.sleep(100);
					text.setText(text.getText() + "C");
					Thread.sleep(100);
					text.setText(text.getText() + "H");
				}
				catch(InterruptedException e) {}
			}
		}).start();
		rManager.addRenderable(text);

		HUD testHud = new HUD()
		{
			@Override
		    protected void init()
		    {
		    	Font toUse = null;
		    	try
		    	{
		    		toUse = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream("resources/fonts/Oswald-Medium.ttf"));
		    		toUse = toUse.deriveFont(30.0f);

		    	}
		    	catch(IOException | FontFormatException e) 
		    	{
		    		System.out.println("failed! " + e.getMessage());
		    	}

		    	super.init();
		    	TextElement te = addText(10, screenDim.getY() * 0.91, "NO PAIN", new Colour(0, 0, 0, 255), ((AWTRenderer)rManager).getFont() );
		    	RectangleElement r = addRect(10, screenDim.getY() * 0.90, 200, 20, new Colour(43, 43, 43, 255)).
		    						 hasFill().showBorder(new Colour(0, 0, 0, 255), 2);
		    	te.setAlignment(HUDElement.HorizontalAlign.RIGHT, null);
		    	r.setAlignment(HUDElement.HorizontalAlign.RIGHT, null);

		    	// DialogueElement dialogue = addDialogue(50, 50, 400, 100, "bruh moment montage!", new Colour(255, 255, 255, 255), toUse.deriveFont(5.0f));
		    }

		    @Override
		    public String getAreaID()
		    {
		    	return "TEST_1";
		    }
		};

		HUD.HUDManager.addHUD(testHud);


		// new Thread(new Runnable()
		// {
		// 	public void run()
		// 	{
		// 		try
		// 		{
		// 			while(true)
		// 			{
		// 				Thread.sleep(30);
		// 				awtRenderer.scaleBy(1.01, 1.01);
		// 			}
		// 		}
		// 		catch(InterruptedException e) {}
		// 	}

		// }).start();
	}


}