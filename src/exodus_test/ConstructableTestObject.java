package exodus_test;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import flow_recording.Constructable;
import genesis_event.Drawable;
import genesis_event.GenesisHandlerType;
import genesis_event.HandlerRelay;
import genesis_util.DepthConstants;
import genesis_util.SimpleHandled;
import genesis_util.StateOperator;
import genesis_util.Transformable;
import genesis_util.Transformation;

/**
 * TestObject is made for testing exodus functionalities (transformation & construction)
 * 
 * @author Mikko Hilpinen
 * @since 3.12.2014
 */
public class ConstructableTestObject extends SimpleHandled implements Transformable,
		Drawable, Constructable<ConstructableTestObject>
{
	// ATTRIBUTES	----------------------------
	
	private Transformation transformation;
	private String id;
	
	
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
		getHandlingOperators().setShouldBeHandledOperator(GenesisHandlerType.DRAWABLEHANDLER, 
				new StateOperator(true, true));
		
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
