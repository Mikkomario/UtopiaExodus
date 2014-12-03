package exodus_object;

import genesis_event.HandlerRelay;
import genesis_util.LatchStateOperator;
import genesis_util.StateOperator;

/**
 * GameObject represents any game entity. All of the gameobjects can be created, 
 * handled and killed. Pretty much any visible or invisible object in a game 
 * that can become an 'object' of an action should inherit this class.
 *
 * @author Mikko Hilpinen.
 * @since 11.7.2013.
 */
public class SimpleGameObject implements GameObject
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private StateOperator isDeadOperator, isActiveOperator;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new gameobject that is alive until it is killed
	 * @param handlers The handlers that handle the object
	 */
	public SimpleGameObject(HandlerRelay handlers)
	{
		// Initializes attributes
		this.isDeadOperator = new LatchStateOperator(false);
		this.isActiveOperator = new StateOperator(true, true);
		
		// Adds the object to the handler(s)
		if (handlers != null)
			handlers.addHandled(this);
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------
	
	@Override
	public StateOperator getIsDeadStateOperator()
	{
		return this.isDeadOperator;
	}
	
	@Override
	public StateOperator getIsActiveStateOperator()
	{
		return this.isActiveOperator;
	}
}
