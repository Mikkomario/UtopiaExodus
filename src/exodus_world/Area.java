package exodus_world;

import java.util.List;

import exodus_util.ExodusHandlerType;
import arc_bank.GamePhaseBank;
import arc_resource.GamePhase;
import arc_resource.ResourceActivator;
import genesis_event.Handled;
import genesis_event.Handler;
import genesis_event.HandlerRelay;
import genesis_event.HandlerType;
import genesis_util.StateOperator;

/**
 * Areas contain objects and keep track of different handlers. They also each have a GamePhase 
 * that is activated when an area is active
 * @author Mikko Hilpinen
 * @since 2.12.2014
 */
// TODO: Implement state operator listener
public class Area extends Handler<Handled> // TODO: Area shouldn't extend handler (area's handler relay will handle that)
{
	// ATTRIBUTES	--------------------------------
	
	// TODO: Remove name (handled at bank level), remove connection to game phase 
	// (Create a separate extension to resource manager that handles areas), 
	// Object constructor file name needs to be 
	// the constructor's property
	private String name, gamePhaseBankName, gamePhaseName, objectConstructorFileName;
	private AreaListenerHandler listenerHandler;
	private HandlerRelay handlers;
	private StateOperator isActiveOperator; // Replace with enum state
	private boolean willDeactivateOthers, newState; // TODO: Remove these (?)
	// TODO: Add phase start default state. Add states in general
	
	
	// CONSTRUCTOR	--------------------------------
	
	/**
	 * Creates a new Area
	 * @param name The name of the area
	 * @param handlers The handlers that will be used in this area. The Area will be added 
	 * to the handlers as well.
	 * @param gamePhaseBankName The name of the GamePhaseBank that contains the gamePhase 
	 * that is used in this area (optional, default bank will be used if left null)
	 * @param gamePhaseName The name of the gamePhase used in this area
	 * @param objectConstructorFileName The name of the file that contains data used by an 
	 * objectCreator (optional, the fileName can be provided directly to the creator as well)
	 */
	public Area(String name, HandlerRelay handlers, String gamePhaseBankName, 
			String gamePhaseName, String objectConstructorFileName) // TODO: Remove unnecessary, add object constructor
	{
		super(false, handlers);
		
		// Initializes attributes
		this.name = name;
		this.gamePhaseBankName = gamePhaseBankName;
		this.gamePhaseName = gamePhaseName;
		this.objectConstructorFileName = objectConstructorFileName;
		this.handlers = handlers;
		this.isActiveOperator = new StateOperator(false, true);
		this.listenerHandler = new AreaListenerHandler(false);
		this.willDeactivateOthers = false;
		this.newState = getIsActiveStateOperator().getState();
		
		// Initializes handlers
		this.handlers.addHandler(this);
		this.handlers.addHandler(this.listenerHandler);
		
		getIsActiveStateOperator().getListenerHandler().add(this);
	}
	
	
	// IMPLEMENTED METHODS	--------------------------
	
	@Override
	public HandlerType getHandlerType() // TODO: Remove
	{
		return ExodusHandlerType.AREA;
	}
	
	@Override
	protected boolean handleObject(Handled h) // TODO: Manipulate the handlers instead of the handleds
	{
		// The objects can only be handled whilst the area is active
		h.getHandlingOperators().setAllStates(this.newState);
		return true;
	}
	
	@Override
	public void add(Handled h) // TODO: Remove
	{
		super.add(h);
		// Area handling state cannot be changed
		h.getHandlingOperators().setShouldBeHandledOperator(getHandlerType(), 
				new StateOperator(true, false));
		// Changes the objects state to match the state of the area as well
		handleObject(h);
	}
	
	// TODO: Make this function area listening (in an inner class)
	@Override
	public void onStateChange(StateOperator source, boolean newState)
	{
		super.onStateChange(source, newState);
		
		if (source == getIsActiveStateOperator())
		{
			this.newState = newState;
			
			// TODO: Only affect handlers. The object constructor should destroy 
			// non-persistent creations on end state
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
			
			this.listenerHandler.onAreaStateChange(this, newState);
			handleObjects(false);
		}
		// TODO: Remove, no death needed for areas
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
	public String getName() // TODO: Remove (?)
	{
		return this.name;
	}
	
	/**
	 * @return The gamePhase that will be activated when this area starts
	 */
	public GamePhase getPhase() // TODO: Remove
	{
		if (this.gamePhaseBankName == null)
			return GamePhaseBank.getGamePhase(this.gamePhaseName);
		else
			return GamePhaseBank.getGamePhase(this.gamePhaseBankName, this.gamePhaseName);
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
	 * @return The name of the GamePhaseBank this area uses
	 */
	protected String getGamePhaseBankName() // TODO: Remove
	{
		return this.gamePhaseBankName;
	}
	
	/**
	 * @return The name of the GamePhase this area uses
	 */
	protected String getGamePhaseName() // TODO: Remove
	{
		return this.gamePhaseName;
	}
	
	/**
	 * @return The file the objectCreators in this area should use by default
	 */
	protected String getOjectConstructorFileName() // TODO: Remove
	{
		return this.objectConstructorFileName;
	}

	
	// OTHER METHODS	-------------------------------
	
	/**
	 * @return The stateOperator that defines whether the area is active (started) or not
	 */
	public StateOperator getIsActiveStateOperator() // TODO: Remove. Replace with state getter + setters
	{
		return this.isActiveOperator;
	}
	
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
	
	private List<Area> getOtherActiveAreas() // TODO: Doesn't belong here
	{
		if (!getIsActiveStateOperator().getState())
			return AreaBank.getActiveAreas();
		
		List<Area> activeAreas = AreaBank.getActiveAreas();
		activeAreas.remove(this);
		return activeAreas;
	}
	
	private boolean otherAreasSharePhase() // TODO: Doesn't belong here
	{
		for (Area area : getOtherActiveAreas())
		{
			if (area.getPhase() == getPhase())
				return true;
		}
		
		return false;
	}
}
