package utopia.exodus.world;

import exodus_world.AreaListenerHandler;
import utopia.inception.handling.HandlerRelay;

/**
 * Area is a collection of handlers tied to a certain setting. Areas have different states.
 * @author Mikko Hilpinen
 * @since 2.12.2014
 */
public class Area
{
	// ATTRIBUTES	--------------------------------
	
	// TODO: Remove name (handled at bank level), remove connection to game phase 
	// (Create a separate extension to resource manager that handles areas), 
	// Object constructor file name needs to be 
	// the constructor's property
	private String name;
	private AreaListenerHandler listenerHandler;
	private HandlerRelay handlers;
	private State state = State.INACTIVE;
	// TODO: Add phase start default state. Add states in general
	
	
	// CONSTRUCTOR	--------------------------------
	
	/**
	 * Creates a new Area
	 * @param name The name of the area
	 * @param handlers The handlers that will be used in this area
	 */
	// TODO: The area should create its handler relay by itself so that it "owns" it
	public Area(String name, HandlerRelay handlers) // TODO: add object constructor
	{
		// Initializes attributes
		this.name = name;
		this.handlers = handlers;
		this.listenerHandler = new AreaListenerHandler(false);
		
		// Sets the handling state to match that of the area
		this.handlers.setHandlingStates(false);
		// TODO: Add behavior configuration to enabling / disabling (Eg. Only drawing is 
		//disabled on pause or all but drawing is disabled on pause / etc.)
	}
	
	
	// GETTERS & SETTERS	---------------------------
	
	/**
	 * @return The name of the area
	 */
	public String getName()
	{
		return this.name;
	}
	
	/**
	 * @return The handlers used in this area
	 */
	public HandlerRelay getHandlers()
	{
		return this.handlers;
	}
	
	/**
	 * @return AreaListenerHandler that informs areaListeners about changes in this area
	 */
	public AreaListenerHandler getListenerHandler()
	{
		return this.listenerHandler;
	}
	
	/**
	 * @return The state the area is currently in
	 */
	public State getState()
	{
		return this.state;
	}
	
	/**
	 * Changes the area's state
	 * @param state The new state the area is in
	 */
	public void setState(State state)
	{
		this.state = state;
		getHandlers().setHandlingStates(getState() == State.ACTIVE);
		// TODO: Interact with the object creator as well
		// TODO: Inform listeners
	}
	
	
	// ENUMERATIONS	---------------------
	
	/**
	 * This enumeration represents an area's state
	 * @author Mikko Hilpinen
	 * @since 27.8.2016
	 */
	public static enum State
	{
		/**
		 * The area hasn't been started yet or it has been ended.
		 */
		INACTIVE,
		/**
		 * The area is paused. All objects are present but the handlers are not in use.
		 */
		PAUSED,
		/**
		 * The area is active. All objects are present and the handlers are working normally.
		 */
		ACTIVE;
	}
}
