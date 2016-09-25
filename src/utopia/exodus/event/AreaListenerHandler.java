package utopia.exodus.event;

import utopia.inception.event.EventSelector;
import utopia.inception.event.StrictEventSelector;
import utopia.inception.handling.Handler;
import utopia.inception.handling.HandlerType;

/**
 * This handler informs numerous listeners about area events
 * @author Mikko Hilpinen
 * @since 25.9.2016
 */
public class AreaListenerHandler extends Handler<AreaListener> implements AreaListener
{
	// ATTRIBUTES	-------------------
	
	private AreaEvent lastEvent = null;
	// This handler doesn't filter any events
	private final EventSelector selector = StrictEventSelector.createAllAcceptingSelector();
	
	
	// IMPLEMENTED METHODS	-----------

	@Override
	public void onAreaEvent(AreaEvent event)
	{
		// Informs the listeners of the event
		this.lastEvent = event;
		handleObjects(true);
		this.lastEvent = null;
	}

	@Override
	public EventSelector getAreaEventSelector()
	{
		return this.selector;
	}

	@Override
	public HandlerType getHandlerType()
	{
		return ExodusHandlerType.AREA_LISTENER_HANDLER;
	}

	@Override
	protected boolean handleObject(AreaListener h)
	{
		// Informs the listener about the event (if it is willing to accept it)
		if (h.getAreaEventSelector().selects(this.lastEvent))
			h.onAreaEvent(this.lastEvent);
		
		return true;
	}
}
