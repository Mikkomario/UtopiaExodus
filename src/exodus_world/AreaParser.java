package exodus_world;

import flow_recording.ObjectFormatException;
import flow_recording.ObjectParser;
import genesis_event.HandlerRelay;

/**
 * AreaParser is able to parse areas from strings and vice versa
 * 
 * @author Mikko Hilpinen
 * @since 6.12.2014
 */
public class AreaParser implements ObjectParser<Area>
{
	// ATTRIBUTES	--------------------------
	
	private AreaHandlerConstructor handlerConstructor;
	private AreaObjectConstructorProvider<?> objectConstructorProvider;
	
	
	// CONSTRUCTOR	--------------------------
	
	/**
	 * Creates a new AreaParser that is able to read areas as well as write them.
	 * @param handlerConstructor The handlerConstructor that will provide the handler's 
	 * used in the area (optional, only used when creating areas)
	 * @param constructorProvider The object constructor provider used by the 
	 * areaObjectCreators (optional, used only when creating areas with objectCreators)
	 */
	public AreaParser(AreaHandlerConstructor handlerConstructor, 
			AreaObjectConstructorProvider<?> constructorProvider)
	{
		this.handlerConstructor = handlerConstructor;
		this.objectConstructorProvider = constructorProvider;
	}
	
	/**
	 * Creates a new parser that can only write areas
	 */
	public AreaParser()
	{
		// Attributes left as null
	}
	
	
	// IMPLEMENTED METHODS	------------------

	@Override
	public Area parseFromString(String s) throws ObjectFormatException
	{
		// Line contains information:
		// objectName#phaseName
		// OR objectName#phaseBankName#phaseName
		// OR objectName#phaseBankName#phaseName#objectConstructorFileName
		String[] arguments = s.split("#");
		
		if (arguments.length < 2)
			throw new ObjectFormatException("Can't construct an area from string: " 
					+ s + ". The string has too few arguments.");
		
		String phaseName = null;
		String phaseBankName = null;
		String objectConstructionFileName = null;
		
		if (arguments.length >= 3)
		{
			phaseBankName = arguments[1];
			phaseName = arguments[2];
		}
		else
			phaseName = arguments[1];
		
		if (arguments.length >= 4)
			objectConstructionFileName = arguments[3];
		
		HandlerRelay handlers = null;
		if (this.handlerConstructor != null)
			handlers = this.handlerConstructor.constructRelay(arguments[0]);
		else
			handlers = new HandlerRelay();
		
		Area newArea = new Area(arguments[0], handlers, phaseBankName, phaseName, 
				objectConstructionFileName);
		
		// Creates an objectCreator if possible
		if (objectConstructionFileName != null && this.objectConstructorProvider != null)
			new AreaObjectCreator<>(this.objectConstructorProvider.getConstructor(newArea), 
					newArea);

		return newArea;
	}

	@Override
	public String parseToString(Area area)
	{
		// Line contains information:
		// objectName#phaseName
		// OR objectName#phaseBankName#phaseName
		// OR objectName#phaseBankName#phaseName#objectConstructorFileName
		String s = area.getName();
		
		if (area.getGamePhaseBankName() != null)
			s += "#" + area.getGamePhaseBankName();
		
		s += "#" + area.getGamePhaseName();
		
		if (area.getOjectConstructorFileName() != null)
			s += "#" + area.getOjectConstructorFileName();
		
		return s;
	}
}
