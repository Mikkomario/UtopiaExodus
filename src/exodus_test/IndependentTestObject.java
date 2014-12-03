package exodus_test;

import java.awt.Color;
import java.awt.Graphics2D;

import exodus_object.SimpleGameObject;
import exodus_util.Transformable;
import exodus_util.Transformation;
import genesis_event.Drawable;
import genesis_event.HandlerRelay;
import genesis_util.StateOperator;
import genesis_util.Vector2D;

/**
 * This object is not created in an areaObjectCreator and is not dependent from other objects.
 * 
 * @author Mikko Hilpinen
 * @since 3.12.2014
 */
public class IndependentTestObject extends SimpleGameObject implements
		Drawable, Transformable
{
	// ATTRIBUTES	------------------------
	
	private Transformation transformation;
	
	
	// CONSTRUCTOR	------------------------
	
	/**
	 * Creates a new object.
	 * @param handlers The handlers that will handle the object
	 * @param position The object's new position
	 */
	public IndependentTestObject(HandlerRelay handlers, Vector2D position)
	{
		super(handlers);
		
		this.transformation = Transformation.transitionTransformation(position);
	}
	
	
	// IMPLEMENTED METHODS	------------------

	@Override
	public Transformation getTransformation()
	{
		return this.transformation;
	}

	@Override
	public void setTrasformation(Transformation t)
	{
		this.transformation = t;
	}

	@Override
	public void drawSelf(Graphics2D g2d)
	{	
		g2d.setColor(Color.BLACK);
		g2d.drawOval(getTransformation().getPosition().getFirstInt() - 10, 
				getTransformation().getPosition().getSecondInt() - 10, 20, 20);
	}

	@Override
	public int getDepth()
	{
		return 0;
	}

	@Override
	public StateOperator getIsVisibleStateOperator()
	{
		return getIsActiveStateOperator();
	}

	@Override
	public void setDepth(int arg0)
	{
		// Not possible
	}
}
