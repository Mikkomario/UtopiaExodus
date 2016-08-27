package exodus_world;

import genesis_event.Handled;

/**
 * AreaListeners react to changes in area's states.<br>
 * 
 * @author Mikko Hilpinen.
 * @since 11.7.2013.
 * @see Area
 */
public interface AreaListener extends Handled
{
	// TODO: Refine by adding real events and selectors
	/**
	 * This method will be called when an area's state changes.
	 * @param area The area which received a new state.
	 * @param newState The area's new state of activity
	 */
	public void onAreaStateChange(Area area, boolean newState);
}
