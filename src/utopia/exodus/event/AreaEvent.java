package utopia.exodus.event;

import java.util.ArrayList;
import java.util.List;

import utopia.exodus.world.Area;
import utopia.inception.event.Event;

/**
 * These events are created when an area's state changes
 * @author Mikko Hilpinen
 * @since 31.8.2016
 */
public class AreaEvent implements Event
{
	// ATTRIBUTES	----------------
	
	private Area area;
	private AreaStateChange stateChange;
	
	
	// CONSTRUCTOR	----------------
	
	/**
	 * Creates a new event
	 * @param source The area whose state changed
	 * @param previousState The previous state of the area
	 * @param newState The new state of the area
	 */
	public AreaEvent(Area source, Area.State previousState, Area.State newState)
	{
		this.area = source;
		this.stateChange = new AreaStateChange(previousState, newState);
	}
	
	/**
	 * Creates a new event
	 * @param source The area whose state changed
	 * @param stateChange The state change that happened
	 */
	public AreaEvent(Area source, AreaStateChange stateChange)
	{
		this.area = source;
		this.stateChange = stateChange;
	}
	
	
	// IMPLEMENTED METHODS	--------

	@Override
	public List<Feature> getFeatures()
	{
		List<Feature> features = new ArrayList<>();
		features.add(this.stateChange);
		return features;
	}
	
	
	// ACCESSORS	----------------
	
	/**
	 * @return The area whose state changed
	 */
	public Area getSource()
	{
		return this.area;
	}
	
	/**
	 * @return The change that occurred in the area's state
	 */
	public AreaStateChange getStateChange()
	{
		return this.stateChange;
	}
	
	
	// NESTED CLASSES	-------------
	
	/**
	 * AreaStateChange is a struct containing a change in an area's state
	 * @author Mikko Hilpinen
	 * @since 31.8.2016
	 */
	public static class AreaStateChange implements Feature
	{
		// ATTRIBUTES	-------------

		private Area.State previousState, newState;
		
		
		// CONSTRUCTOR	-------------
		
		/**
		 * Creates a new state change instance
		 * @param previousState The state that was previously in place
		 * @param newState The state that is currently in place
		 */
		public AreaStateChange(Area.State previousState, Area.State newState)
		{
			this.previousState = previousState;
			this.newState = newState;
		}
		
		
		// IMPLEMENTED METHODS	-----
		
		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((this.newState == null) ? 0 : this.newState.hashCode());
			result = prime * result + ((this.previousState == null) ? 0
					: this.previousState.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj)
		{
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (!(obj instanceof AreaStateChange))
				return false;
			AreaStateChange other = (AreaStateChange) obj;
			if (this.newState != other.newState)
				return false;
			if (this.previousState != other.previousState)
				return false;
			return true;
		}
		
		
		// ACCESSORS	--------------
		
		/**
		 * @return The state that was previously in place
		 */
		public Area.State getPreviousState()
		{
			return this.previousState;
		}
		
		/**
		 * @return The new state of the area
		 */
		public Area.State getNewState()
		{
			return this.newState;
		}
	}
}
