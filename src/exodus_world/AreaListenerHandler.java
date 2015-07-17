package exodus_world;

import exodus_util.ExodusHandlerType;
import genesis_event.Handler;
import genesis_event.HandlerRelay;
import genesis_event.HandlerType;

/**
 * AreaListenerHandler informs areaListeners about area state changes
 *
 * @author Mikko Hilpinen.
 * @since 11.7.2013.
 */
public class AreaListenerHandler extends Handler<AreaListener> implements AreaListener
{
	// ATTRIBUTES	------------------------------------------------------
	
	private Area lastArea;
	private boolean newState;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new AreaListenerHandler with the given information
	 *
	 * @param autodeath Will the handler die when it runs out of handleds
	 * @param superHandler The areaListenerHandler that informs this 
	 * AreaListenerHandler about area events (optional)
	 */
	public AreaListenerHandler(boolean autodeath, AreaListenerHandler superHandler)
	{
		super(autodeath);
		
		if (superHandler != null)
			superHandler.add(this);
	}
	
	/**
	 * Creates a new handler
	 * @param autoDeath Will the handler die once it runs out of handleds
	 * @param handlers The handlers that will handle this handler
	 */
	public AreaListenerHandler(boolean autoDeath, HandlerRelay handlers)
	{
		super(autoDeath, handlers);
	}
	
	/**
	 * Creates a new areaListenerHandler
	 * @param autoDeath Will the handler die once it runs out of handleds
	 */
	public AreaListenerHandler(boolean autoDeath)
	{
		super(autoDeath);
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------
	
	@Override
	public void onAreaStateChange(Area area, boolean newState)
	{
		this.lastArea = area;
		this.newState = newState;
		handleObjects();
		this.lastArea = null;
	}

	@Override
	public HandlerType getHandlerType()
	{
		return ExodusHandlerType.AREALISTENERHANDLER;
	}

	@Override
	protected boolean handleObject(AreaListener h)
	{
		h.onAreaStateChange(this.lastArea, this.newState);
		return true;
	}
}
