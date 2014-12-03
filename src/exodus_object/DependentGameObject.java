package exodus_object;

import genesis_event.HandlerRelay;
import genesis_util.StateOperator;

/**
 * Dependent GameObjects depend from other gameObjects' states.
 * 
 * @author Mikko Hilpinen
 * @param <T> The type of object this gameObject depends from
 * @since 2.12.2014
 */
public class DependentGameObject<T extends GameObject> implements GameObject
{
	// ATTRIBUTES	---------------------------------
	
	private T master;
	
	
	// CONSTRUCTOR	---------------------------------
	
	/**
	 * Creates a new GameObject. The object will be dependent from the given master object.
	 * 
	 * @param master The object this object depends from.
	 * @param handlers The handlers that will handle this object (optional)
	 */
	public DependentGameObject(T master, HandlerRelay handlers)
	{
		// Initializes attributes
		this.master = master;
		
		// Adds the object to the handler(s)
		if (handlers != null)
			handlers.addHandled(this);
	}
	
	
	// IMPLEMENTED METHODS	----------------------------

	@Override
	public StateOperator getIsDeadStateOperator()
	{
		return this.master.getIsDeadStateOperator();
	}

	@Override
	public StateOperator getIsActiveStateOperator()
	{
		return this.master.getIsActiveStateOperator();
	}
	
	
	// GETTERS & SETTERS	----------------------------
	
	/**
	 * @return The object this object depends from
	 */
	protected T getMaster()
	{
		return this.master;
	}
}
