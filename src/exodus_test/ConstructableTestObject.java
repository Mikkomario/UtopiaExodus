package exodus_test;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import omega_util.SimpleGameObject;
import omega_util.Transformable;
import omega_util.Transformation;
import flow_recording.Constructable;
import genesis_event.Drawable;
import genesis_event.HandlerRelay;
import genesis_util.DepthConstants;
import genesis_util.StateOperator;

/**
 * TestObject is made for testing exodus functionalities (transformation & construction)
 * 
 * @author Mikko Hilpinen
 * @since 3.12.2014
 */
public class ConstructableTestObject extends SimpleGameObject implements Transformable,
		Drawable, Constructable<ConstructableTestObject>
{
	// ATTRIBUTES	----------------------------
	
	private Transformation transformation;
	private String id;
	private StateOperator isVisibleOperator;
	
	
	// CONSTRUCTOR	----------------------------
	
	/**
	 * Creates a new TestObject
	 * 
	 * @param handlers The handlers that will handle the object
	 */
	public ConstructableTestObject(HandlerRelay handlers)
	{
		super(handlers);
		
		this.transformation = Transformation.identityTransformation();
		this.isVisibleOperator = new StateOperator(true, true);
		
		System.out.println("New testObject created");
	}
	
	
	// IMPLEMENTED METHODS	---------------------

	@Override
	public String getID()
	{
		return this.id;
	}

	@Override
	public void setAttribute(String attributeName, String attributeValue)
	{
		System.out.println(getID() + "updating " + attributeName + " to " + attributeValue);
		
		setTrasformation(getTransformation().withAttribute(attributeName, attributeValue));
	}

	@Override
	public void setID(String id)
	{
		this.id = id;
	}

	@Override
	public void setLink(String linkName, ConstructableTestObject target)
	{
		// Doesn't use links
	}

	@Override
	public void drawSelf(Graphics2D g2d)
	{
		g2d.setColor(Color.RED);
		
		AffineTransform lastTransform = g2d.getTransform();
		
		getTransformation().transform(g2d);
		g2d.drawOval(-5, -5, 10, 10);
		
		g2d.setTransform(lastTransform);
	}

	@Override
	public int getDepth()
	{
		return DepthConstants.NORMAL;
	}

	@Override
	public StateOperator getIsVisibleStateOperator()
	{
		return this.isVisibleOperator;
	}

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
}
