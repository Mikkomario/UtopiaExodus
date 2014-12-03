package exodus_test;

import exodus_object.ConstructableGameObject;
import exodus_world.Area;
import exodus_world.AreaObjectConstructorProvider;
import flow_recording.AbstractConstructor;

/**
 * This class provides the area creation system with an object constructor
 * 
 * @author Mikko Hilpinen
 * @since 3.12.2014
 */
public class TestObjectConstructorProvider implements
		AreaObjectConstructorProvider
{
	// IMPLEMENTED METHODS	---------------------------
	
	@Override
	public AbstractConstructor<ConstructableGameObject> getConstructor(Area targetArea)
	{
		return new TestObjectConstructor(targetArea);
	}
	
	
	// SUBCLASSES	------------------------------
	
	private static class TestObjectConstructor extends AbstractConstructor<ConstructableGameObject>
	{
		// ATTRIBUTES	--------------------------
		
		private Area targetArea;
		
		
		// CONSTRUCTOR	--------------------------
		
		public TestObjectConstructor(Area targetArea)
		{
			this.targetArea = targetArea;
		}
		
		
		// IMPLEMENTED METHODS	------------------
		
		@Override
		protected ConstructableGameObject createConstructable(String instruction)
		{
			return new ConstructableTestObject(this.targetArea.getHandlers());
		}	
	}
}
