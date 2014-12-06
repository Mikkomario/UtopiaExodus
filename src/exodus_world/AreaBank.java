package exodus_world;

import java.util.ArrayList;
import java.util.List;

import exodus_util.ExodusResourceType;
import arc_bank.Bank;
import arc_bank.BankBank;
import arc_bank.BankBankInitializer;
import arc_bank.BankObjectConstructor;
import arc_bank.GamePhaseBank;
import arc_bank.MultiMediaHolder;
import arc_bank.ResourceInitializationException;
import arc_resource.GamePhase;

/**
 * AreaBank is a static accessor to the area resource.
 * 
 * @author Mikko Hilpinen
 * @since 2.12.2014
 */
public class AreaBank
{
	// CONSTRUCTOR	--------------------------
	
	private AreaBank()
	{
		// The constructor is hidden since the interface is static
	}
	
	
	// OTHER METHODS	----------------------
	
	/**
	 * Retrieves an areaBank from the currently active banks
	 * @param bankName The name of the areaBank
	 * @return An areaBank with the given name
	 */
	@SuppressWarnings("unchecked")
	public static Bank<Area> getAreaBank(String bankName)
	{
		return (Bank<Area>) MultiMediaHolder.getBank(ExodusResourceType.AREA, bankName);
	}
	
	/**
	 * Retrieves an area from an active area bank
	 * @param bankName The name of the areaBank that contains the area
	 * @param areaName The name of the area in the bank
	 * @return An area from the bank
	 */
	public static Area getArea(String bankName, String areaName)
	{
		return getAreaBank(bankName).get(areaName);
	}
	
	/**
	 * Activates an areaBank that has been initialized
	 * @param areaBankName The name of the area bank that will be initialized
	 */
	public static void activateAreaBank(String areaBankName)
	{
		MultiMediaHolder.activateBank(ExodusResourceType.AREA, areaBankName, false);
	}
	
	/**
	 * Deactivates an areaBank. The bank shouldn't be in use when this method is called.
	 * @param areaBankName The name of the bank that should be deactivated.
	 */
	public static void deactivateAreaBank(String areaBankName)
	{
		MultiMediaHolder.deactivateBank(ExodusResourceType.AREA, areaBankName);
	}
	
	/**
	 * Initializes the area resources. The GamePhases should be initialized before this method 
	 * is called (which means that areas can't be used in the GamePhases).
	 * @param fileName The name of the file that contains construction information 
	 * ("data/" automatically included). The file should be formatted as follows:<br>
	 * &bankName1<br>
	 * areaName1#phaseName1 (phaseBankName1#phaseName1 and 
	 * phaseBankName1#phaseName1#areaObjectFile are also possible)<br>
	 * areaName2#phaseName2<br>
	 * ...<br>
	 * @param handlerConstructor The object that will construct the required handlers
	 * @param objectConstructorProvider The object that will provide the created 
	 * AreaObjectCreators with suitable object constructors
	 * @see #activateAreaBank(String)
	 */
	public static void initializeAreaResources(String fileName, 
			AreaHandlerConstructor handlerConstructor, 
			AreaObjectConstructorProvider objectConstructorProvider)
	{
		MultiMediaHolder.initializeResourceDatabase(createAreaBankBank(fileName, 
				handlerConstructor, objectConstructorProvider));
	}
	
	/**
	 * Creates a new "AreaRelay relay" that handles different areaBanks.
	 * @param fileName The name of the file that contains construction information 
	 * ("data/" automatically included). The file should be formatted as follows:<br>
	 * &bankName1<br>
	 * areaName1#phaseName1 (phaseBankName1#phaseName1 and 
	 * phaseBankName1#phaseName1#areaObjectFile are also possible)<br>
	 * areaName2#phaseName2<br>
	 * ...<br>
	 * &bankName2<br>
	 * ...<br>
	 * @param handlerConstructor The object that will construct the required handlers
	 * @param objectConstructorProvider The object that will provide the created 
	 * AreaObjectCreators with suitable object constructors
	 * @return A bankBank for areas
	 */
	public static BankBank<Area> createAreaBankBank(String fileName, 
			AreaHandlerConstructor handlerConstructor, 
			AreaObjectConstructorProvider objectConstructorProvider)
	{
		return new BankBank<>(new BankBankInitializer<>(fileName, new AreaBankConstructor(), 
				new AreaConstructor(handlerConstructor, objectConstructorProvider)), 
				ExodusResourceType.AREA);
	}
	
	/**
	 * @return All the areas that are currently active / in use
	 */
	public static List<Area> getActiveAreas()
	{
		List<Area> activeAreas = new ArrayList<>();
		
		for (String areaBankName : MultiMediaHolder.getActiveBankNames(ExodusResourceType.AREA))
		{
			for (String areaName : getAreaBank(areaBankName).getContentNames())
			{
				Area area = getArea(areaBankName, areaName);
				if (area.getIsActiveStateOperator().getState())
					activeAreas.add(area);
			}
		}
		
		return activeAreas;
	}
	
	
	// SUBCLASSES	--------------------------
	
	private static class AreaConstructor implements BankObjectConstructor<Area>
	{
		// ATTRIBUTES	----------------------
		
		private AreaHandlerConstructor handlerConstructor;
		private AreaObjectConstructorProvider objectConstructorProvider;
		
		
		// CONSTRUCTOR	----------------------
		
		public AreaConstructor(AreaHandlerConstructor handlerConstructor, 
				AreaObjectConstructorProvider objectConstructorProvider)
		{
			this.handlerConstructor = handlerConstructor;
			this.objectConstructorProvider = objectConstructorProvider;
		}
		
		
		// IMPLEMENTED METHODS	---------------
		
		@Override
		public Area construct(String line, Bank<Area> bank)
		{
			// Line contains information:
			// objectName#phaseName
			// OR objectName#phaseBankName#phaseName
			// OR objectName#phaseBankName#phaseName#objectConstructorFileName
			String[] arguments = line.split("#");
			
			if (arguments.length < 2)
				throw new ResourceInitializationException("Can't construct an area from line: " 
						+ line + ". The line has too few arguments.");
			
			GamePhase phase = null;
			
			if (arguments.length >= 3)
				phase = GamePhaseBank.getGamePhase(arguments[1], arguments[2]);
			else
				phase = GamePhaseBank.getGamePhase(arguments[1]);
			
			Area newArea = new Area(arguments[0], phase, 
					this.handlerConstructor.constructRelay(arguments[1]));
			bank.put(arguments[0], newArea);
			
			// Creates an objectCreator if possible
			if (arguments.length >= 4)
				new AreaObjectCreator(
						this.objectConstructorProvider.getConstructor(newArea), 
						arguments[3], newArea);
			
			return newArea;
		}
	}
	
	private static class AreaBankConstructor implements BankObjectConstructor<Bank<Area>>
	{
		@Override
		public Bank<Area> construct(String line, Bank<Bank<Area>> bank)
		{
			Bank<Area> newBank = new Bank<>();
			bank.put(line, newBank);
			return newBank;
		}	
	}
}