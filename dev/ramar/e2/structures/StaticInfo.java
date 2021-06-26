package dev.ramar.e2.structures;


import dev.ramar.e2.backend.World;

import dev.ramar.e2.interfaces.collision.HitManager;

import dev.ramar.e2.interfaces.rendering.RenderManager;
import dev.ramar.e2.interfaces.rendering.ViewPort;


import dev.ramar.e2.interfaces.updating.*;
import dev.ramar.e2.interfaces.events.producers.Controller;
import dev.ramar.e2.interfaces.events.producers.MouseController;
import dev.ramar.e2.interfaces.events.producers.TimerManager;

import dev.ramar.e2.interfaces.rendering.Sprite;

import java.util.*;
import java.io.*;



/*
Class: StaticInfo
 - A Large collection of static assumptions about the game state,
   all separated into separate classes, these inner classes describe themselves
*/
public class StaticInfo
{

	public static class Constants
	{
		public static final long DELTA_SECOND = 1000000000;
	}

	/*
	Inner Class: Functions
	 - Store useful functions that everything might want to use, but doesn't
	   necessarily belong at any one place
	*/
	public static class Functions
	{
		public static final Functions STATIC_REF = new Functions();

		public Functions()
		{

		}

		public static double roundDouble(double d, int places)
		{
			int place = 1;

			for( int ii = 0; ii < places; ii++ )
				place *= 10;

			double expD = Math.round(d * place);
			expD /= place;
			return expD;
		}

		public static String getClassName(Object o)
		{
			String str = o.getClass().toString();

			// str = str.replace(".", ".");


			if( str.contains(".") )
			{
				int lastPeriod = 0;
				for( int ii = 0; ii < str.length(); ii++ )
				{
					if( str.charAt(ii) == '.' )
						lastPeriod = ii;
				}

				str = str.substring(lastPeriod + 1);
			}


			return str;
		}


		public static String getAbsoluteFilePath(String relPath)
		{
			String path = System.getProperty("user.dir");

			relPath = relPath.replace("\\", File.separator);
			relPath = relPath.replace("/", File.separator);
			path += File.separator + relPath;

			return path;
		}

		public static File getFileFromName(String relPath)
		{
			return new File(getAbsoluteFilePath(relPath));
		}
	}



	public static class Sprites
	{

		private static Map<String, Sprite> loadedSprites = new HashMap<>();


		public static Sprite getSprite(String name)
		{
			return loadedSprites.get(name);
		}


		public static void putSprite(String name, Sprite s)
		{
			loadedSprites.put(name, s);
		}



	}





	/*
	Inner Class: UUID
	 - Creates and Stores a Set of Strings, which represent one specific entity in the World
	*/
	public static class UUID
	{
		private static Random random = new Random();
		private static int segments = 4;
		private static int segLength = 4;

		private static Set<String> uuids = new HashSet<>();

		private static final String UUID_CHARS = "abcdefghijklmnopqrstuvwxyz0123456789";

		private static String buffer = "";

		public static String createNewUUID()
		{
			buffer = "";
			do
			{
				makeID();
			}
			while(uuids.contains(buffer));

			String expString = new String(buffer);
			uuids.add(expString);
			return expString;
		}


		// creates a sequence of chars that is <segments> * <segLength> + (<segments> - 1) 
		// in length, representing a unique ID
		private static void makeID()
		{
			for( int ii = 0; ii < segments; ii++ )
			{
				makeSegment();
				// adds segLength chars from UUID_CHARS to buffer
				buffer += (ii == segments - 1 ? "" : "-");
			}

		}

		private static void makeSegment()
		{
			for( int ii = 0; ii < segLength; ii++ )
				buffer += UUID_CHARS.charAt(random.nextInt(UUID_CHARS.length()));
		}
	}



	/*
	Inner Class: Mouse
	 - Static Constants for mice positioning
	*/
	public static class Mouse
	{
		public static Vec2 windowOffset = new Vec2(0, 0);
		public static final Vec2 windowedOffset = new Vec2(5, 20);
		public static final Vec2 fullscreenOffset = new Vec2(0, 0);
	}


	/*
	Inner Class: Hit
	 - Static information pertaining to Names of Groups
	*/
	public static class Hit
	{
		public static class GroupNames
		{
			public static final String PLAYER = "plyr_grp";
			public static final String PLAYER_BULLETS = "plyr_blts";
			public static final String SHOOTABLE = "plyr_shootable";
			public static final String COLLIDABLE = "plyr_collidable";
		}
	}


	public static class Image
	{
		public static class Colours
		{
			public static final Colour TRANSPARENT = new Colour(20, 20, 20, 20);
		}
	}

	/*
	Inner Class: Control
	 - Static information related to standardisation of controls,
	 - stores "actions"; unique strings representing a functional element
	   of the game
	*/
	public static class Control
	{
		public static class ControllerTypes
		{
			public static final String KEYBOARD = "kb con";
			public static final String MOUSE = "ms con";

		}

		public static class Actions
		{
			public static final String MOVE_UP = "UP";
			public static final String MOVE_DOWN = "DOWN";
			public static final String MOVE_LEFT = "LEFT";
			public static final String MOVE_RIGHT = "RIGHT";
			public static final String MOUSE_MOVE = "MOUSE_MOVED";
			public static final String RELOAD = "RELOAD";

			public static final String LEFT_CLICK = "LEFT_CLICK";
			public static final String RIGHT_CLICK = "RIGHT_CLICK";
			public static final String MIDDLE_CLICK = "MIDDLE_CLICK";

		}

	}


	/*
	Inner Class: Objects
	 - A Static Class which stores objects that get initialised and kept somewhere known
	   throughout the entire game session. 
	*/
	public static class Objects
	{
		private static RenderManager renderManager;
		private static SUpdateManager slowUpdateManager;
		private static FUpdateManager fastUpdateManager;
		private static HitManager collisionManager;
		private static TimerManager timerManager;
		private static ViewPort viewPort;
		private static Controller mainController;
		private static MouseController mouseController;
		private static World world;
		private static Random random;


		public static boolean isInitialised()
		{
			// we're initialised if all the objects are set
			return renderManager != null &&
				slowUpdateManager != null &&
				fastUpdateManager != null &&
				collisionManager != null &&
				timerManager != null &&
				viewPort != null &&
				mainController != null &&
				mouseController != null &&
				world != null &&
				random != null;
		}

		public static void testInitialised()
		{
			if( isInitialised() )
				throw new IllegalStateException("Objects class is already initialised!");


		}


		public static void testNotInitialised()
		{
			if(! isInitialised() )
			{
				String exportString = "Objects class not initialised! [";

				exportString += (renderManager != null ? "" : "RenderManager, ");
				exportString += (slowUpdateManager != null ? "" : "RenderManager, ");
				exportString += (slowUpdateManager != null ? "" : "SlowUpdateManager, ");
				exportString += (fastUpdateManager != null ? "" : "FastUpdateManager, ");
				exportString += (collisionManager != null ? "" : "CollisionManager, ");
				exportString += (timerManager != null ? "" : "TimerManager, ");
				exportString += (viewPort != null ? "" : "ViewPort, ");
				exportString += (mainController != null ? "" : "MainController, ");
				exportString += (mouseController != null ? "" : "MouseController, ");
				exportString += (world != null ? "" : "World, ");
				exportString += (	random != null ? "" : "Random, ");
			
				if( exportString.substring(exportString.length() - 2, exportString.length()).equals(", ") )
					exportString = exportString.substring(0, exportString.length() - 2);

				exportString += "] not set";
				throw new IllegalStateException(exportString);
			}
		}




		public static void initialise(RenderManager renderManager, SUpdateManager slowUpdateManager, FUpdateManager fastUpdateManager,
		 HitManager collisionManager, TimerManager timerManager, ViewPort viewPort, Controller mainController, MouseController mouseController, World world )
		{
			if( isInitialised() )
				throw new IllegalStateException("All objects has already been set! cannot initialise!");
			Objects.renderManager = renderManager;
			Objects.slowUpdateManager = slowUpdateManager;
			Objects.fastUpdateManager = fastUpdateManager;
			Objects.collisionManager = collisionManager;
			Objects.timerManager = timerManager;
			Objects.viewPort = viewPort;
			Objects.mainController = mainController;
			Objects.mouseController = mouseController;
			Objects.world = world;

			Objects.random = new Random();

		}


		public static void initialise(RenderManager renderManager, SUpdateManager slowUpdateManager, FUpdateManager fastUpdateManager,
		 HitManager collisionManager, TimerManager timerManager, ViewPort viewPort, Controller mainController, MouseController mouseController, World world, Random random)
		{
			if( isInitialised() )
				throw new IllegalStateException("All objects has already been set! cannot initialise!");

			testInitialised();
			Objects.renderManager = renderManager;
			Objects.slowUpdateManager = slowUpdateManager;
			Objects.fastUpdateManager = fastUpdateManager;
			Objects.collisionManager = collisionManager;
			Objects.timerManager = timerManager;
			Objects.viewPort = viewPort;
			Objects.mainController = mainController;
			Objects.mouseController = mouseController;
			Objects.world = world;
			Objects.random = random;

		}

		/* Mutators
		--------------
			These only work if there is one thing unset
		*/


		public static void setRenderManager(RenderManager renderManager)
		{
			testInitialised();
			Objects.renderManager = renderManager;
		}


		public static void setSUpdateManager(SUpdateManager slowUpdateManager)
		{
			testInitialised();
			Objects.slowUpdateManager = slowUpdateManager;
		}


		public static void setFUpdateManager(FUpdateManager fastUpdateManager)
		{
			testInitialised();
			Objects.fastUpdateManager = fastUpdateManager;
		}


		public static void setHitManager(HitManager collisionManager)
		{
			testInitialised();
			Objects.collisionManager = collisionManager;
		}


		public static void setTimerManager(TimerManager timerManager)
		{
			testInitialised();
			Objects.timerManager = timerManager;
		}


		public static void setViewPort(ViewPort viewPort)
		{
			testInitialised();
			Objects.viewPort = viewPort;
		}


		public static void setMainController(Controller mainController)
		{
			testInitialised();
			Objects.mainController = mainController;
		}


		public static void setMouseController(MouseController mouseController)
		{
			testInitialised();
			Objects.mouseController = mouseController;
		}


		public static void setWorld(World world)
		{
			testInitialised();
			Objects.world = world;
		}


		public static void setRandom(Random random)
		{
			testInitialised();
			Objects.random = random;
		}

		/* Accessors
		-----------------
		*/


		public static RenderManager getRenderManager()
		{
			testNotInitialised();
			return Objects.renderManager;
		}


		public static SUpdateManager getSUpdateManager()
		{
			testNotInitialised();
			return Objects.slowUpdateManager;
		}


		public static FUpdateManager getFUpdateManager()
		{
			testNotInitialised();
			return Objects.fastUpdateManager;
		}


		public static HitManager getHitManager()
		{
			testNotInitialised();
			return Objects.collisionManager;

		}


		public static TimerManager getTimerManager()
		{
			testNotInitialised();
			return Objects.timerManager;
		}


		public static ViewPort getViewPort()
		{
			testNotInitialised();
			return Objects.viewPort;
		}


		public static Controller getMainController()
		{
			testNotInitialised();
			return Objects.mainController;

		}


		public static MouseController getMouseController()
		{
			testNotInitialised();
			return Objects.mouseController;
		}

		public static World getWorld()
		{
			testNotInitialised();
			return Objects.world; 
		}


		public static Random getRandom()
		{
			testNotInitialised();
			return Objects.random;
		}

	}




}