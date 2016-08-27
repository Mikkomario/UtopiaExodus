package exodus_world;

import java.util.ArrayList;
import java.util.List;

import flow_structure.Graph;
import flow_structure.GraphEdge;
import flow_structure.GraphNode;

/**
 * The areaGraphs are graphs that can work as maps. One can traverse in the map and activate 
 * new areas that way.
 * 
 * @author Mikko Hilpinen
 * @since 6.12.2014
 * @param <T> The type of data contained within the edges of this graph
 */
public class AreaGraph<T> extends Graph<Area, T>
{
	// TODO: Integrate with the new area interface when it has been implemented
	
	// ATTRIBUTES	------------------------
	
	private String areaBankName;
	private GraphNode<Area, T> currentNode;
	
	
	// CONSTRUCTOR	-------------------------
	
	/**
	 * Creates a new AreaGraph
	 * @param areaBankName The areaBank that contains the areas used in this graph
	 * @param insertAllAreas Should all the areas from the bank be added to the graph right away
	 */
	public AreaGraph(String areaBankName, boolean insertAllAreas)
	{
		// Initializes attributes
		this.areaBankName = areaBankName;
		
		if (insertAllAreas)
		{
			for (String areaName : AreaBank.getAreaBank(areaBankName).getContentNames())
			{
				addArea(areaName);
			}
		}
	}
	
	
	// GETTERS & SETTERS	-----------------
	
	/**
	 * @return The currently occupied / active area in the graph
	 */
	public GraphNode<Area, T> getCurrentArea()
	{
		return this.currentNode;
	}
	
	/**
	 * @return The name of the AreaBank that contains the areas used within this graph
	 */
	public String getUsedBankName()
	{
		return this.areaBankName;
	}
	
	
	// OTHER METHODS	---------------------
	
	/**
	 * Adds a new area to the graph
	 * @param areaName The name of the area
	 * @return A node that represents the area
	 */
	public GraphNode<Area, T> addArea(String areaName)
	{
		return addNode(AreaBank.getArea(this.areaBankName, areaName));
	}
	
	/**
	 * Creates a connecting edge between two areas
	 * @param startAreaName The name of the area the edge leaves from
	 * @param endAreaName The name of the area the edge goes to
	 * @param edgeData The data contained within the edge
	 * @param bothWays If the edge should be two way instead of one way
	 * @param overWrite Should the previous edge data be overwritten if it exists (true) or 
	 * should the new edge be parallel to possible previous ones (false)
	 */
	public void connectAreas(String startAreaName, String endAreaName, T edgeData, 
			boolean bothWays, boolean overWrite)
	{
		connectNodes(findArea(startAreaName).getID(), findArea(endAreaName).getID(), edgeData, 
				bothWays, overWrite);
	}
	
	/**
	 * Finds the area with the given name from the graph
	 * @param areaName The name of the area to be searched from the graph
	 * @return a node representing the area with the given name
	 */
	public GraphNode<Area, T> findArea(String areaName)
	{
		return findNodeWithData(AreaBank.getArea(this.areaBankName, areaName));
	}
	
	/**
	 * Moves to a different area in the graph, if possible. Ends the previously occupied area.
	 * @param areaName The name of the new area that will be activated.
	 * @return Was the move successful
	 */
	public boolean moveTo(String areaName)
	{
		return moveTo(findArea(areaName));
	}
	
	/**
	 * @return A list of "routes" from the current area to other connected areas
	 */
	public ArrayList<T> getRoutesFromCurrentArea()
	{
		ArrayList<T> routes = new ArrayList<>();
		
		if (getCurrentArea() == null)
			return routes;
		
		for (GraphEdge<Area, T> edge : getCurrentArea().getLeavingEdges())
		{
			routes.add(edge.getData());
		}
		
		return routes;
	}
	
	/**
	 * Moves from the current node along the given "route" / edge.
	 * @param route The route that connects this area to some other area
	 * @return Was the move successful
	 */
	public boolean moveAlong(T route)
	{
		if (getCurrentArea() == null)
			return false;
		
		// Finds the edge that works as the route to the new area
		List<GraphEdge<Area, T>> edges = getCurrentArea().getLeavingEdgesWithData(route);
		
		if (edges.isEmpty())
			return false;
		
		// Moves to the new area
		GraphNode<Area, T> destination = edges.get(0).getEndNode();
		if (destination == getCurrentArea())
			destination = edges.get(0).getStartNode();
		
		return moveTo(destination);
	}
	
	private boolean moveTo(GraphNode<Area, T> newNode)
	{
		if (newNode == null)
			return false;
		if (newNode == getCurrentArea())
			return true;
		
		Area previousArea = null;
		if (this.currentNode != null)
			previousArea = this.currentNode.getData();
		
		this.currentNode = newNode;
		this.currentNode.getData().start(false);
		
		if (previousArea != null)
			previousArea.end();
		
		return true;
	}
}
