package ozmud.commands;


import ozmud.world.Creature;


public abstract class AbstractCommand implements Command {


	/** Command name (must be overriden). */
	protected static final String NAME = "abstractCommand";
	
	
	public final String getName() {
		return NAME;
	}
	
	
	public abstract void execute(Creature sender, String argument);


}
