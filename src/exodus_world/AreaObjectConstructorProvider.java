package exodus_world;

import exodus_object.GameObject;
import flow_recording.AbstractConstructor;
import flow_recording.Constructable;

/**
 * This class provides an interface between the engine and the user. The provider should 
 * provide access to the constructor class / classes the user wants to use in area object 
 * creation.
 * 
 * @author Mikko Hilpinen
 * @param <T> The type of object constructed by the constructors provided by this object
 * @since 3.12.2014
 */
public interface AreaObjectConstructorProvider<T extends GameObject & Constructable<T>>
{
	/**
	 * This method should provide a suitable object constructor for the given area.
	 * @param targetArea the area the objects will be placed to
	 * @return An objectConstructor that can be used for constructing the objects in the area.
	 */
	public AbstractConstructor<T> getConstructor(Area targetArea);
}
