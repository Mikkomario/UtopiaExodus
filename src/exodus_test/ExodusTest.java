package exodus_test;

import exodus_world.AreaBank;
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
import genesis_video.MainPanel.ScreenSplit;
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
				20, ScreenSplit.HORIZONTAL, false);
		GamePanel panel = window.getMainPanel().addGamePanel();
		
		// Initializes areas
		AreaBank.initializeAreaResources("testing/areas.txt", 
				new TestHandlerConstructor(window, panel), new TestObjectConstructorProvider());
		AreaBank.activateAreaBank("test");
		
		// Creates the input system
		new KeyCommander(window.getHandlerRelay());
	}
	
	
	// SUBCLASSES	------------------------
	
	private static class KeyCommander implements AdvancedKeyListener
	{
		// ATTRIBUTES	----------------------
		
		private StateOperator isDeadStateOperator, isActiveStateOperator;
		private EventSelector<AdvancedKeyEvent> selector;
		
		
		// CONSTRUCTOR	----------------------
		
		public KeyCommander(HandlerRelay handlers)
		{
			this.isActiveStateOperator = new StateOperator(true, false);
			this.isDeadStateOperator = new LatchStateOperator(false);
			this.selector = AdvancedKeyEvent.createEventTypeSelector(KeyEventType.PRESSED);
			
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
			}
		}
	}
}
