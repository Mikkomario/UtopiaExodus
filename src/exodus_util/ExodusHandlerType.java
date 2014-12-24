package exodus_util;

import omega_util.SimpleGameObject;
import exodus_world.AreaListener;
import genesis_event.HandlerType;

/**
 * The different types of Handlers introduced in the Exodus module
 * 
 * @author Mikko Hilpinen
 * @since 2.12.2014
 */
public enum ExodusHandlerType implements HandlerType
{
	/**
	 * Areas contain GameObjects and usually connect different handlers as well
	 */
	AREA, 
	/**
	 * AreaListenerHandlers inform areaListeners
	 */
	AREALISTENERHANDLER;
	
	
	// IMPLEMENTED METHODS	-------------------------

	@Override
	public Class<?> getSupportedHandledClass()
	{
		switch (this)
		{
			case AREA: return SimpleGameObject.class;
			case AREALISTENERHANDLER: return AreaListener.class;
		}
		
		return null;
	}
}
