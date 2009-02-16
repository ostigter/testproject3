package ozmud.world;


/**
 * A viewer's perspective of an action.
 * 
 * @author Oscar Stigter
 */
public enum Perspective {
	

	/** Sender's perspective (first-person, e.g. "Frodo", "you"). */
	SELF,
	
	/** Target's perspective (second-person, e.g. "Frodo", "he", "she", "it"). */
	TARGET,
	
	/** Bystanders (third-person, e.g. "Frodo"). */
	OTHERS,


}
