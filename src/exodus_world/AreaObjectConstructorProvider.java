package exodus_world;

import exodus_object.ConstructableGameObject;
import flow_recording.AbstractConstructor;

/**
 * This class provides an interface between the engine and the user. The provider should 
 * provide access to the constructor class / classes the user wants to use in area object 
 * creation.
 * 
 * @author Mikko Hilpinen
 * @since 3.12.2014
 */
public interface AreaObjectConstructorProvider
{
	/**
	 * This method should provide a suitable object constructor for the given area.
	 * @param targetArea the area the objects will be placed to
	 * @return An objectConstructor that can be used for constructing the objects in the area.
	 */
	public AbstractConstructor<ConstructableGameObject> getConstructor(Area targetArea);
}
