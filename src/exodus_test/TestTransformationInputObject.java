package exodus_test;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import exodus_util.Transformation;
import genesis_event.AdvancedKeyEvent;
import genesis_event.AdvancedKeyListener;
import genesis_event.AdvancedMouseEvent;
import genesis_event.AdvancedMouseListener;
import genesis_event.EventSelector;
import genesis_event.HandlerRelay;
import genesis_util.StateOperator;
import genesis_util.Vector2D;

/**
 * This class tests some methods in the transformation and visually presents them
 * 
 * @author Mikko Hilpinen
 * @since 9.12.2014
 */
public class TestTransformationInputObject extends IndependentTestObject
		implements AdvancedMouseListener, AdvancedKeyListener
{
	// ATTRIBUTES	--------------------------
	
	private Vector2D lastRelativeMousePosition;
	private EventSelector<AdvancedMouseEvent> mouseSelector;
	private EventSelector<AdvancedKeyEvent> keySelector;
	
	
	// CONSTRUCTOR	--------------------------
	
	/**
	 * Creates a new object
	 * @param handlers The handlers that will handle the object
	 * @param position The oject's new position
	 */
	public TestTransformationInputObject(HandlerRelay handlers,
			Vector2D position)
	{
		super(handlers, position);
		
		// initializes attributes
		this.lastRelativeMousePosition = Vector2D.zeroVector();
		this.mouseSelector = AdvancedMouseEvent.createMouseMoveSelector();
		this.keySelector = AdvancedKeyEvent.createEventTypeSelector(
				AdvancedKeyEvent.KeyEventType.DOWN);
	}
	
	
	// IMPLEMENTED METHODS	-------------------

	@Override
	public void drawSelf(Graphics2D g2d)
	{
		AffineTransform lastTransform = g2d.getTransform();
		g2d.setColor(Color.BLACK);
		getTransformation().transform(g2d);
		g2d.drawRect(-20, -20, 40, 40);
		g2d.drawOval(this.lastRelativeMousePosition.getFirstInt() - 5, 
				this.lastRelativeMousePosition.getSecondInt() - 5, 10, 10);
		g2d.setTransform(lastTransform);
	}
	
	@Override
	public EventSelector<AdvancedKeyEvent> getKeyEventSelector()
	{
		return this.keySelector;
	}

	@Override
	public StateOperator getListensToKeyEventsOperator()
	{
		return getIsActiveStateOperator();
	}

	@Override
	public void onKeyEvent(AdvancedKeyEvent event)
	{
		switch (event.getKey())
		{
			// On W, scales up
			case 'w':
				setTrasformation(getTransformation().plus(
						Transformation.scalingTransformation(1.05 * event.getDuration())));
				break;
			// On S, scales down
			case 's':
				setTrasformation(getTransformation().minus(
						Transformation.scalingTransformation(1.05 * event.getDuration())));
				break;
			// On A rotates +
			case 'a':
				setTrasformation(getTransformation().plus(
						Transformation.rotationTransformation(2 * event.getDuration())));
				break;
			// On D rotates -
			case 'd':
				setTrasformation(getTransformation().minus(
						Transformation.rotationTransformation(2 * event.getDuration())));
				break;
		}
	}

	@Override
	public StateOperator getListensToMouseEventsOperator()
	{
		return getIsActiveStateOperator();
	}

	@Override
	public EventSelector<AdvancedMouseEvent> getMouseEventSelector()
	{
		return this.mouseSelector;
	}

	@Override
	public boolean isInAreaOfInterest(Vector2D position)
	{
		return false;
	}

	@Override
	public void onMouseEvent(AdvancedMouseEvent event)
	{
		// Updates the mouse position (transformed)
		this.lastRelativeMousePosition = getTransformation().inverseTransform(
				event.getPosition());
	}
}
