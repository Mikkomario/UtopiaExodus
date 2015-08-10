package exodus_world;

import java.io.FileNotFoundException;

import flow_recording.AbstractConstructor;
import flow_recording.Constructable;
import flow_recording.TextConstructorInstructor;
import genesis_event.Handled;
import genesis_util.HandlingStateOperatorRelay;
import genesis_util.Killable;
import genesis_util.StateOperator;

/**
 * AreaObjectCreators create new objects each time an area is activated. They also kill the 
 * objects when the area ends.
 * 
 * @author Mikko Hilpinen
 * @param <T> The type of object constructed by this objectCreator
 * @since 2.12.2014
 */
public class AreaObjectCreator<T extends Handled & Constructable<T>> implements AreaListener
{
	// ATTRIBUTES	----------------------------
	
	private TextConstructorInstructor instructor;
	private AbstractConstructor<T> constructor;
	private String fileName;
	private Area area;
	private HandlingStateOperatorRelay operators;
	
	
	// CONSTRUCTOR	---------------------------
	
	/**
	 * Creates a new AreaObjectCreator that uses the given file when constructing objects
	 * @param constructor The constructor that will construct the objects
	 * @param area the area the objects will be placed to
	 * @param fileName The name of the file that contains object information 
	 * ("data/" automatically included. %CHECK: is used for instructions)
	 */
	public AreaObjectCreator(AbstractConstructor<T> constructor, Area area, String fileName)
	{
		// Initializes attributes
		this.constructor = constructor;
		this.instructor = new TextConstructorInstructor(constructor);
		this.fileName = fileName;
		this.area = area;
		this.operators = new HandlingStateOperatorRelay(new StateOperator(true, false));
		
		// Adds the object to the handler(s)
		if (area != null)
			this.area.getListenerHandler().add(this);
	}
	
	/**
	 * Creates a new AreaObjectCreator. The creator uses the file indicated by the given area 
	 * to create its objects.
	 * @param constructor The constructor that will construct the objects
	 * @param area the area the objects will be placed to
	 */
	public AreaObjectCreator(AbstractConstructor<T> constructor, Area area)
	{
		// Initializes attributes
		this.constructor = constructor;
		this.instructor = new TextConstructorInstructor(constructor);
		this.fileName = area.getOjectConstructorFileName();
		this.area = area;
		this.operators = new HandlingStateOperatorRelay(new StateOperator(true, false));
		
		// Adds the object to the handler(s)
		if (area != null)
			this.area.getListenerHandler().add(this);
	}
	
	
	// IMPLEMENTED METHODS	-----------------------------

	@Override
	public StateOperator getIsDeadStateOperator()
	{
		// The creator's state is tied to the area's
		return this.area.getIsDeadStateOperator();
	}

	@Override
	public void onAreaStateChange(Area area, boolean newState)
	{
		// When area starts, creates new objects
		if (newState)
		{
			try
			{
				this.instructor.constructFromFile(this.fileName, "*");
			}
			catch (FileNotFoundException e)
			{
				throw new AbstractConstructor.ConstructorException("Couldn't find the file " + 
						this.fileName);
			}
		}
		// When area ends, kills them
		else
		{
			for (Killable construct : this.constructor.getConstructs().values())
			{
				construct.getIsDeadStateOperator().setState(true);
			}
			this.constructor.reset();
		}
	}

	@Override
	public HandlingStateOperatorRelay getHandlingOperators()
	{
		return this.operators;
	}
}
