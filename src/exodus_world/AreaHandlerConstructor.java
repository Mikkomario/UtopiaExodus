package exodus_world;

import genesis_event.HandlerRelay;

/**
 * HandlerSystemConstructor is able to create HandlerRelays
 * 
 * @author Mikko Hilpinen
 * @since 2.12.2014
 */
public interface AreaHandlerConstructor
{
	/**
	 * Creates a new HandlerRelay that will be used in an area
	 * @param areaName The name of the area the relay will be used in
	 * @return A HandlerRelay for that area
	 */
	public HandlerRelay constructRelay(String areaName);
}
