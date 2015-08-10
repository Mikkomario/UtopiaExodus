package exodus_util;

import flow_recording.Constructable;
import genesis_event.Handled;

/**
 * These objects are constructed when an area starts / becomes active and are killed when 
 * the area ends.
 * 
 * @author Mikko Hilpinen
 * @since 2.12.2014
 */
public interface ConstructableHandled extends Handled, Constructable<ConstructableHandled>
{
	// This interface is a wrapper for combining the super interfaces.
}
