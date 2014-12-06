package exodus_world;

import java.util.List;

import exodus_object.GameObject;
import exodus_util.ExodusHandlerType;
import arc_resource.GamePhase;
import arc_resource.ResourceActivator;
import genesis_event.Handler;
import genesis_event.HandlerRelay;
import genesis_event.HandlerType;
import genesis_util.StateOperator;

/**
 * Areas contain objects and keep track of different handlers. They also each have a GamePhase 
 * that is activated when an area is active.
 * 
 * @author Mikko Hilpinen
 * @since 2.12.2014
 */
public class Area extends Handler<GameObject> implements GameObject
{
	// ATTRIBUTES	--------------------------------
	
	private String name;
	private AreaListenerHandler listenerHandler;
	private HandlerRelay handlers;
	private GamePhase phase;
	private StateOperator isActiveOperator;
	private boolean willDeactivateOthers;
	
	
	// CONSTRUCTOR	--------------------------------
	
	/**
	 * Creates a new Area
	 * @param name The name of the area
	 * @param phase The gamePhase that will be activated once the area starts
	 * @param handlers The handlers that will be used in this area
	 */
	public Area(String name, GamePhase phase, HandlerRelay handlers)
	{
		super(false, handlers);
		
		// Initializes attributes
		this.phase = phase;
		this.handlers = handlers;
		this.isActiveOperator = new StateOperator(false, true);
		this.listenerHandler = new AreaListenerHandler(false);
		this.willDeactivateOthers = false;
		
		// Initializes handlers
		this.handlers.addHandler(this);
		this.handlers.addHandler(this.listenerHandler);
		
		getIsActiveStateOperator().getListenerHandler().add(this);
	}
	
	
	// IMPLEMENTED METHODS	--------------------------

	@Override
	public StateOperator getIsActiveStateOperator()
	{
		return this.isActiveOperator;
	}
	
	@Override
	public HandlerType getHandlerType()
	{
		return ExodusHandlerType.AREA;
	}

	@Override
	protected boolean handleObject(GameObject h)
	{
		// Initializes / uninitializes the object
		h.getIsActiveStateOperator().setState(getIsActiveStateOperator().getState());
		return true;
	}
	
	@Override
	public void add(GameObject g)
	{
		super.add(g);
		
		// Changes the objects state to match the state of the area as well
		handleObject(g);
	}
	
	@Override
	public void onStateChange(StateOperator source, boolean newState)
	{
		super.onStateChange(source, newState);
		
		if (source == getIsActiveStateOperator())
		{
			// Activates or deactivates the gameObjects inside the area
			if (newState)
			{
				// May deactivate other areas
				if (this.willDeactivateOthers)
				{
					for (Area area : getOtherActiveAreas())
					{
						area.end();
					}
				}
				
				ResourceActivator.startPhase(getPhase(), this.willDeactivateOthers);
				this.willDeactivateOthers = false;
			}
			// May deactivate resources if the gamePhase is not used by any area
			else if (!otherAreasSharePhase())
				ResourceActivator.endPhase(getPhase());
			
			this.listenerHandler.onAreaStateChange(this);
			handleObjects();
		}
		// Kills the listenerHandler on death
		else if (source == getIsDeadStateOperator() && newState)
		{
			this.listenerHandler.removeAllHandleds();
			this.listenerHandler.getIsDeadStateOperator().setState(true);
		}
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
	 * @return The gamePhase that will be activated when this area starts
	 */
	public GamePhase getPhase()
	{
		return this.phase;
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

	
	// OTHER METHODS	-------------------------------
	
	/**
	 * Starts the area. The same (without closing other areas) can be achieved just by 
	 * activating the area.
	 * @param endPreviousAreas Will the previous areas be ended as this area starts (also 
	 * handles the GamePhases and resource deactivation)
	 */
	public void start(boolean endPreviousAreas)
	{
		this.willDeactivateOthers = endPreviousAreas;
		getIsActiveStateOperator().setState(true);
	}
	
	/**
	 * Ends the area, ending the gamePhase if possible. The same can be achieved by 
	 * simply deactivating the area.
	 */
	public void end()
	{
		getIsActiveStateOperator().setState(false);
	}
	
	private List<Area> getOtherActiveAreas()
	{
		if (!getIsActiveStateOperator().getState())
			return AreaBank.getActiveAreas();
		
		List<Area> activeAreas = AreaBank.getActiveAreas();
		activeAreas.remove(this);
		return activeAreas;
	}
	
	private boolean otherAreasSharePhase()
	{
		for (Area area : getOtherActiveAreas())
		{
			if (area.getPhase() == getPhase())
				return true;
		}
		
		return false;
	}
}
