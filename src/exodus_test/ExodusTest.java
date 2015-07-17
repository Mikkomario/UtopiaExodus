package exodus_test;

import java.awt.Color;
import java.awt.Graphics2D;

import omega_util.SimpleGameObject;
import exodus_world.AreaBank;
import exodus_world.AreaGraph;
import genesis_event.Drawable;
import genesis_event.EventSelector;
import genesis_event.HandlerRelay;
import genesis_event.KeyEvent;
import genesis_event.KeyEvent.KeyEventType;
import genesis_event.KeyListener;
import genesis_event.MouseEvent;
import genesis_event.MouseListener;
import genesis_util.LatchStateOperator;
import genesis_util.StateOperator;
import genesis_util.StateOperatorListener;
import genesis_util.Vector3D;
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
		GameWindow window = new GameWindow(new Vector3D(300, 300), "Exodus Test", true, 120, 
				20);
		GamePanel panel = window.getMainPanel().addGamePanel();
		
		// Initializes areas
		AreaBank.initializeAreaResources("testing/areas.txt", 
				new TestHandlerConstructor(window, panel), new TestObjectConstructorProvider());
		AreaBank.activateAreaBank("test");
		
		// Creates an area graph
		AreaGraph<Integer> areas = new AreaGraph<>("test", true);
		areas.connectAreas("area1", "area2", 1, true, false);
		
		// Moves to the first area
		areas.moveTo("area2");
		
		System.out.println("Edges leaving from area1: " + 
				areas.getCurrentArea().getLeavingEdgeAmount());
		System.out.println("Edges leaving from area2: " + 
				areas.findArea("area2").getLeavingEdgeAmount());
		System.out.println("Edge is both ways: " + 
				areas.getCurrentArea().getLeavingEdges().get(0).isBothWays());
		
		// Creates the input system
		new KeyCommander(window.getHandlerRelay(), areas);
		
		// Creates a test object as well
		new TestMouseObject(AreaBank.getArea("test", "area1").getHandlers());
	}
	
	
	// SUBCLASSES	------------------------
	
	private static class KeyCommander implements KeyListener
	{
		// ATTRIBUTES	----------------------
		
		private StateOperator isDeadStateOperator, isActiveStateOperator;
		private EventSelector<KeyEvent> selector;
		private AreaGraph<Integer> graph;
		
		
		// CONSTRUCTOR	----------------------
		
		public KeyCommander(HandlerRelay handlers, AreaGraph<Integer> graph)
		{
			this.isActiveStateOperator = new StateOperator(true, false);
			this.isDeadStateOperator = new LatchStateOperator(false);
			this.selector = KeyEvent.createEventTypeSelector(KeyEventType.PRESSED);
			this.graph = graph;
			
			handlers.addHandled(this);
		}
		
		
		// IMPLEMENTED METHODS	--------------
		
		@Override
		public StateOperator getIsDeadStateOperator()
		{
			return this.isDeadStateOperator;
		}

		@Override
		public EventSelector<KeyEvent> getKeyEventSelector()
		{
			return this.selector;
		}

		@Override
		public StateOperator getListensToKeyEventsOperator()
		{
			return this.isActiveStateOperator;
		}

		@Override
		public void onKeyEvent(KeyEvent event)
		{
			// On 1, activates area 1, on 2 deactivates
			// On 3, activates area 2, on 4 deactivates
			switch (event.getKey())
			{
				case '1':
					System.out.println("Starting area 1");
					AreaBank.getArea("test", "area1").start(false); 
					break;
				case '2': 
					System.out.println("Ending area 1");
					AreaBank.getArea("test", "area1").end(); 
					break;
				case '3': 
					System.out.println("Starting area 2");
					AreaBank.getArea("test", "area2").start(false); 
					break;
				case '4': 
					System.out.println("Ending area 2");
					AreaBank.getArea("test", "area2").end(); 
					break;
				case 'y':
					System.out.println("Switching area");
					System.out.println("success: " + this.graph.moveAlong(1));
					break;
			}
		}
	}
	
	private static class TestMouseObject extends SimpleGameObject implements MouseListener, 
			Drawable, StateOperatorListener
	{
		// ATTRIBUTES	---------------
		
		private EventSelector<MouseEvent> selector;
		private StateOperator isVisibleOperator;
		private Vector3D lastMousePosition;
		
		
		// CONSTRUCTOR	---------------
		
		public TestMouseObject(HandlerRelay handlers)
		{
			super(handlers);
			
			this.isVisibleOperator = new StateOperator(true, true);
			this.selector = MouseEvent.createMouseMoveSelector();
			this.lastMousePosition = Vector3D.zeroVector();
			
			getIsActiveStateOperator().getListenerHandler().add(this);
		}
		
		
		// IMPLEMENTED METHODS	--------

		@Override
		public StateOperator getListensToMouseEventsOperator()
		{
			return getIsActiveStateOperator();
		}

		@Override
		public EventSelector<MouseEvent> getMouseEventSelector()
		{
			return this.selector;
		}

		@Override
		public boolean isInAreaOfInterest(Vector3D position)
		{
			return false;
		}

		@Override
		public void onMouseEvent(MouseEvent event)
		{
			this.lastMousePosition = event.getPosition();
		}

		@Override
		public void drawSelf(Graphics2D g2d)
		{
			g2d.setColor(Color.BLUE);
			g2d.drawRect(this.lastMousePosition.getFirstInt() - 3, 
					this.lastMousePosition.getSecondInt() - 3, 7, 7);
		}

		@Override
		public int getDepth()
		{
			return 0;
		}

		@Override
		public StateOperator getIsVisibleStateOperator()
		{
			return this.isVisibleOperator;
		}

		@Override
		public void onStateChange(StateOperator source, boolean newState)
		{
			System.out.println("Mouse listening state: " + newState);
		}
	}
}
