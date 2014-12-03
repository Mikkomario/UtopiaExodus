package exodus_util;

/**
 * Transformable objects have a transformation and can be transformed.
 * 
 * @author Mikko Hilpinen
 * @since 3.12.2014
 */
public interface Transformable
{
	/**
	 * @return The object's current transformation
	 */
	public Transformation getTransformation();
	
	/**
	 * Changes the object's current transformation
	 * @param t The new transformation the object will receive
	 */
	public void setTrasformation(Transformation t);
}
