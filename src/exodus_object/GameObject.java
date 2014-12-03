package exodus_object;

import genesis_event.Handled;
import genesis_util.StateOperator;

/**
 * GameObjects are the main model elements in a game. GameObjects often reside in an area. 
 * They may be visible or invisible, independent or dependent and so on.
 * 
 * @author Mikko Hilpinen
 * @since 2.12.2014
 */
public interface GameObject extends Handled
{
	/**
	 * @return The stateOperator that handles the universal activity of an object. The activity 
	 * should affect the other states of the object (visibility, mobility, ect.) as well.
	 */
	public StateOperator getIsActiveStateOperator();
}
