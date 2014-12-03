package exodus_test;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import exodus_object.ConstructableGameObject;
import exodus_object.SimpleGameObject;
import exodus_util.Transformable;
import exodus_util.Transformation;
import genesis_event.Drawable;
import genesis_event.HandlerRelay;
import genesis_util.DepthConstants;
import genesis_util.StateOperator;
import genesis_util.Vector2D;

/**
 * TestObject is made for testing exodus functionalities (transformation & construction)
 * 
 * @author Mikko Hilpinen
 * @since 3.12.2014
 */
public class ConstructableTestObject extends SimpleGameObject implements Transformable,
		Drawable, ConstructableGameObject
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
		
		switch (attributeName)
		{
			case "position": setTrasformation(getTransformation().withPosition(
					Vector2D.parseFromString(attributeValue))); break;
			case "scaling": setTrasformation(getTransformation().withScaling(
					Vector2D.parseFromString(attributeValue))); break;
			case "shear": setTrasformation(getTransformation().withShear(
					Vector2D.parseFromString(attributeValue))); break;
			case "angle": setTrasformation(getTransformation().withAngle(
					Double.parseDouble(attributeValue))); break;
		}
	}

	@Override
	public void setID(String id)
	{
		this.id = id;
	}

	@Override
	public void setLink(String linkName, ConstructableGameObject target)
	{
		// Doesn't use links
	}

	@Override
	public void drawSelf(Graphics2D g2d)
	{
		
		AffineTransform lastTransform = g2d.getTransform();
		
		g2d.setTransform(getTransformation().toAffineTransform());
		g2d.drawOval(-5, -5, 10, 10);
		
		g2d.setTransform(lastTransform);
		
		/*
		g2d.drawOval(getTransformation().getPosition().getFirstInt(), 
				getTransformation().getPosition().getSecondInt(), 10, 10);
				*/
		//g2d.drawRect(10, 10, 100, 100);
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
	public void setDepth(int arg0)
	{
		// Not gonna change
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
