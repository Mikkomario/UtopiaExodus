package exodus_world;

import java.io.FileNotFoundException;

import exodus_object.ConstructableGameObject;
import flow_recording.AbstractConstructor;
import flow_recording.TextConstructorInstructor;
import genesis_util.StateOperator;

/**
 * AreaObjectCreators create new objects each time an area is activated. They also kill the 
 * objects when the area ends.
 * 
 * @author Mikko Hilpinen
 * @since 2.12.2014
 */
public class AreaObjectCreator implements AreaListener
{
	// ATTRIBUTES	----------------------------
	
	private TextConstructorInstructor instructor;
	private AbstractConstructor<? extends ConstructableGameObject> constructor;
	private String fileName;
	private Area area;
	
	
	// CONSTRUCTOR	---------------------------
	
	/**
	 * Creates a new AreaObjectCreator
	 * @param constructor The constructor that will construct the objects
	 * @param fileName The name of the file that contains object information 
	 * ("data/" automatically included. %CHECK: is used for instructions)
	 * @param area the area the objects will be placed to
	 */
	public AreaObjectCreator(AbstractConstructor<? extends ConstructableGameObject> constructor, 
			String fileName, Area area)
	{
		// Initializes attributes
		this.constructor = constructor;
		this.instructor = new TextConstructorInstructor(constructor);
		this.fileName = fileName;
		this.area = area;
		
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
	public void onAreaStateChange(Area area)
	{
		// When area starts, creates new objects
		if (area.getIsActiveStateOperator().getState())
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
			for (ConstructableGameObject construct : this.constructor.getConstructs().values())
			{
				construct.getIsDeadStateOperator().setState(true);
			}
			this.constructor.reset();
		}
	}
}
