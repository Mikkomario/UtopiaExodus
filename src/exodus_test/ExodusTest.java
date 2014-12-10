package exodus_test;

import exodus_world.Area;
import exodus_world.AreaBank;
import exodus_world.AreaGraph;
import genesis_event.AdvancedKeyEvent;
import genesis_event.AdvancedKeyEvent.KeyEventType;
import genesis_event.AdvancedKeyListener;
import genesis_event.EventSelector;
import genesis_event.HandlerRelay;
import genesis_util.LatchStateOperator;
import genesis_util.StateOperator;
import genesis_util.Vector2D;
import genesis_video.GamePanel;
import genesis_video.GameWindow;
import arc_bank.GamePhaseBank;

/**
 * This class tests the new features intorduced in this module
 * 
 * @author Mikko Hilpinen
 * @since 3.12.2014
 */
public class ExodusTest
{
	// CONSTRUCTOR	--------------------------
	
	private ExodusTest()
	{
		// The constructor is hidden since interface is static
	}
	
	
	// MAIN METHOD	-------------------------
	
	/**
	 * Starts the test
	 * @param args not used
	 */
	public static void main(String[] args)
	{
		// Initializes resources
		GamePhaseBank.initializeGamePhaseResources("testing/gamePhases.txt", "test");
		
		// Opens the window
		GameWindow window = new GameWindow(new Vector2D(300, 300), "Exodus Test", true, 120, 
				20);
		GamePanel panel = window.getMainPanel().addGamePanel();
		
		// Initializes areas
		AreaBank.initializeAreaResources("testing/areas.txt", 
				new TestHandlerConstructor(window, panel), new TestObjectConstructorProvider());
		AreaBank.activateAreaBank("test");
		
		// Creates some test objects
		Area area1 = AreaBank.getArea("test", "area1");
		IndependentTestObject o = new IndependentTestObject(area1.getHandlers(), 
				new Vector2D(150, 150));
		DependentTestObject dependent = new DependentTestObject(o, area1.getHandlers());
		
		new TestTransformationInputObject(AreaBank.getArea("test", "area3").getHandlers(), 
				new Vector2D(150, 150));
		
		// Creates an area graph
		AreaGraph<Integer> areas = new AreaGraph<>("test", true);
		areas.connectAreas("area1", "area2", 1, true, false);
		
		// Moves to the first area
		areas.moveTo("area1");
		
		System.out.println("Edges leaving from area1: " + 
				areas.getCurrentArea().getLeavingEdgeAmount());
		System.out.println("Edges leaving from area2: " + 
				areas.findArea("area2").getLeavingEdgeAmount());
		System.out.println("Edge is both ways: " + 
				areas.getCurrentArea().getLeavingEdges().get(0).isBothWays());
		
		// Creates the input system
		new KeyCommander(window.getHandlerRelay(), areas, dependent);
	}
	
	
	// SUBCLASSES	------------------------
	
	private static class KeyCommander implements AdvancedKeyListener
	{
		// ATTRIBUTES	----------------------
		
		private StateOperator isDeadStateOperator, isActiveStateOperator;
		private EventSelector<AdvancedKeyEvent> selector;
		private AreaGraph<Integer> graph;
		private DependentTestObject dependent;
		
		
		// CONSTRUCTOR	----------------------
		
		public KeyCommander(HandlerRelay handlers, AreaGraph<Integer> graph, 
				DependentTestObject dependent)
		{
			this.isActiveStateOperator = new StateOperator(true, false);
			this.isDeadStateOperator = new LatchStateOperator(false);
			this.selector = AdvancedKeyEvent.createEventTypeSelector(KeyEventType.PRESSED);
			this.graph = graph;
			this.dependent = dependent;
			
			handlers.addHandled(this);
		}
		
		
		// IMPLEMENTED METHODS	--------------
		
		@Override
		public StateOperator getIsDeadStateOperator()
		{
			return this.isDeadStateOperator;
		}

		@Override
		public EventSelector<AdvancedKeyEvent> getKeyEventSelector()
		{
			return this.selector;
		}

		@Override
		public StateOperator getListensToKeyEventsOperator()
		{
			return this.isActiveStateOperator;
		}

		@Override
		public void onKeyEvent(AdvancedKeyEvent event)
		{
			// On 1, activates area 1, on 2 deactivates
			// On 3, activates area 2, on 4 deactivates
			switch (event.getKey())
			{
				case '1':
					System.out.println("Starting area 1");
					AreaBank.getArea("test", "area1").getIsActiveStateOperator().setState(true); 
					break;
				case '2': 
					System.out.println("Ending area 1");
					AreaBank.getArea("test", "area1").getIsActiveStateOperator().setState(false); 
					break;
				case '3': 
					System.out.println("Starting area 2");
					AreaBank.getArea("test", "area2").getIsActiveStateOperator().setState(true); 
					break;
				case '4': 
					System.out.println("Ending area 2");
					AreaBank.getArea("test", "area2").getIsActiveStateOperator().setState(false); 
					break;
				case 'y':
					System.out.println("Switching area");
					System.out.println("success: " + this.graph.moveAlong(1));
					break;
				case '5':
					System.out.println("Separates the dependent object");
					this.dependent.separate();
				case '6':
					System.out.println("Switching to mouse test area");
					this.graph.moveTo("area3");
					break;
			}
		}
	}
}
