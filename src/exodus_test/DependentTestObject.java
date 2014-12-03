package exodus_test;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import exodus_object.DependentGameObject;
import exodus_util.Transformable;
import exodus_util.Transformation;
import genesis_event.Actor;
import genesis_event.Drawable;
import genesis_event.HandlerRelay;
import genesis_util.StateOperator;
import genesis_util.Vector2D;

/**
 * These small test objects depend from the independent test object
 * 
 * @author Mikko Hilpinen
 * @since 3.12.2014
 */
public class DependentTestObject extends DependentGameObject<IndependentTestObject> implements
		Actor, Drawable, Transformable
{
	// ATTRIBUTES	-----------------------------
	
	private Transformation ownTransformation;
	
	
	// CONSTRUCTOR	------------------------------
	
	/**
	 * Creates a new testObject
	 * @param master The master the object depends from
	 * @param handlers The handlers that will handle the object
	 */
	public DependentTestObject(IndependentTestObject master,
			HandlerRelay handlers)
	{
		super(master, handlers);
		
		this.ownTransformation = new Transformation(new Vector2D(100, 0));
	}
	
	
	// IMPLEMENTED METHODS	---------------------

	@Override
	public Transformation getTransformation()
	{
		return getMaster().getTransformation().plus(this.ownTransformation);
	}

	@Override
	public void setTrasformation(Transformation t)
	{
		this.ownTransformation = t;
	}

	@Override
	public void drawSelf(Graphics2D g2d)
	{
		g2d.setColor(Color.PINK);
		AffineTransform lastTransform = g2d.getTransform();
		
		g2d.setTransform(getTransformation().toAffineTransform());
		g2d.drawRect(-5, -5, 10, 10);
		
		g2d.setTransform(lastTransform);
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
	public void setDepth(int depth)
	{
		// Not happening
	}

	@Override
	public void act(double arg0)
	{
		setTrasformation(this.ownTransformation.rotatedAroundAbsolutePoint(1, 
				Vector2D.zeroVector()));
	}
}
