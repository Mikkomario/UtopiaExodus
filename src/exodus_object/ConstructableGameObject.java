package exodus_object;

import flow_recording.Constructable;

/**
 * These objects are constructed when an area starts / becomes active and are killed when 
 * the area ends.
 * 
 * @author Mikko Hilpinen
 * @since 2.12.2014
 */
public interface ConstructableGameObject extends GameObject, Constructable<ConstructableGameObject>
{
	// This interface is a wrapper for combining the super interfaces.
}
