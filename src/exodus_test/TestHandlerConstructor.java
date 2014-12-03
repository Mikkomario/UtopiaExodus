package exodus_test;

import exodus_world.AreaHandlerConstructor;
import genesis_event.ActorHandler;
import genesis_event.DrawableHandler;
import genesis_event.HandlerRelay;
import genesis_event.KeyListenerHandler;
import genesis_event.MouseListenerHandler;
import genesis_util.DepthConstants;
import genesis_video.GamePanel;
import genesis_video.GameWindow;

/**
 * This handlerConstructor is used for the tests in this package. Only the basic handlers 
 * (introduced in Genesis) are provided.
 * 
 * @author Mikko Hilpinen
 * @since 3.12.2014
 */
public class TestHandlerConstructor implements AreaHandlerConstructor
{
	// ATTRIBUTES	------------------------------
	
	private GameWindow window;
	private GamePanel panel;
	
	
	// CONSTRUCTOR	------------------------------
	
	/**
	 * Creates a new constructor
	 * @param window The window that provides superHandlers
	 * @param panel The panel that provides a drawableHandler
	 */
	public TestHandlerConstructor(GameWindow window, GamePanel panel)
	{
		// Initializes attributes
		this.window = window;
		this.panel = panel;
	}
	
	
	// IMPLEMENTED METHODS	----------------------

	@Override
	public HandlerRelay constructRelay(String areaName)
	{
		HandlerRelay relay = new HandlerRelay();
		
		relay.addHandler(new DrawableHandler(false, false, DepthConstants.NORMAL, 1, 
				this.panel.getDrawer()));
		relay.addHandler(new ActorHandler(false, this.window.getHandlerRelay()));
		relay.addHandler(new MouseListenerHandler(false, this.window.getHandlerRelay()));
		relay.addHandler(new KeyListenerHandler(false, this.window.getHandlerRelay()));
		
		return relay;
	}
}
