package utopia.exodus.event;

import utopia.inception.handling.HandlerType;

/**
 * These are the new handler types introduced in the Exodus project
 * @author Mikko Hilpinen
 * @since 25.9.2016
 */
public enum ExodusHandlerType implements HandlerType
{
	/**
	 * This handler type informs numerous area event listeners about area events
	 */
	AREA_LISTENER_HANDLER;

	@Override
	public Class<?> getSupportedHandledClass()
	{
		return AreaListener.class;
	}
}
