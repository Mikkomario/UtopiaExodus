package utopia.exodus.event;

import utopia.inception.event.EventSelector;
import utopia.inception.handling.Handled;

/**
 * AreaListeners are informed when an area's state changes
 * @author Mikko Hilpinen
 * @since 3.9.2016
 */
public interface AreaListener extends Handled
{
	/**
	 * This method will be called when an area effect occurs. Only events selected by the 
	 * listeners event selector are provided
	 * @param event The event that occurred
	 * @see #onAreaEvent(AreaEvent)
	 */
	public void onAreaEvent(AreaEvent event);
	
	/**
	 * @return The event selector that decides which area events will be relayed to this listener
	 */
	public EventSelector getAreaEventSelector();
}
