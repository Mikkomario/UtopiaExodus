package exodus_world;

import flow_recording.ObjectFormatException;
import flow_recording.ObjectParser;
import flow_structure.GraphRecording;

/**
 * AreaGraphRecordings are writable / constructable recordings of AreaGraphs
 * 
 * @author Mikko Hilpinen
 * @since 6.12.2014
 * @param <T> The type of data contained within the edges of the graph
 * @deprecated Graph recording is not supported on latest versions of Flow
 */
public class AreaGraphRecording<T> extends GraphRecording<Area, T>
{
	// CONSTRUCTOR	-------------------------
	
	/**
	 * Creates a new AreaGraphRecording
	 * 
	 * @param areaBankName The name of the bank that contains the areas used within the graph
	 * @param edgeDataParser The parser that can parse edge data
	 */
	public AreaGraphRecording(String areaBankName, ObjectParser<T> edgeDataParser)
	{
		super(new AreaNodeParser(areaBankName), edgeDataParser);
	}

	
	// SUBCLASSES	-------------------------
	
	private static class AreaNodeParser implements ObjectParser<Area>
	{
		// ATTRIBUTES	---------------------------
		
		private String areaBankName;
		
		
		// CONSTRUCTOR	---------------------------
		
		public AreaNodeParser(String areaBankName)
		{
			this.areaBankName = areaBankName;
		}
		
		// IMPLEMENTED METHODS	--------------------
		
		@Override
		public Area parseFromString(String s) throws ObjectFormatException
		{
			return AreaBank.getArea(this.areaBankName, s);
		}

		@Override
		public String parseToString(Area object)
		{
			return object.getName();
		}
	}
}
