package ozmud.world;


public abstract class Util {
	

	/** Noun ("you", "Frodo", Ross"). */
	public static final String NOUN = "${you}";
	
	/** Pronoun ("I", "he", "she", "it"). */
	public static final String PRONOUN = "${he}";
	
	/** Possessive adjective ("your", "his", "her", "its"). */
	public static final String POSSESSIVE = "${his}";
	

//	/**
//	 * Formats a message according to the specified perspective from the
//	 * specified creature. Replaces the following sequences:
//	 * <ul>
//	 * <li>"${you}" = "you/Jimmy/Ross"</li>
//	 * <li>"${he}" = "you/he/she/it"</li>
//	 * <li>"${him}" = "you/him/her/it"</li>
//	 * <li>"${your}" = "your/Jimmy's/Ross'"</li>
//	 * <li>"${s}" = "enter/enters"</li>
//	 * </ul>
//	 * 
//	 * @param input
//	 *            the message to be formatted
//	 * @param perspective
//	 *            the perspective to format the text for
//	 * @return the formatted message
//	 */
//	public String format(
//			String input, Perspective perspective, Creature creature) {
//		String output = input;
//		boolean isDone = false;
//		int p;
//		String replaceWith = null;
//
//		do {
//			isDone = true;
//
//			p = output.indexOf(NOUN);
//			if (p != -1) {
//				switch (perspective) {
//				case SELF: {
//					replaceWith = "you";
//					break;
//				}
//				case TARGET: {
//					replaceWith = "you";
//					break;
//				}
//				case OTHERS: {
//					replaceWith = creature.getName();
//					break;
//				}
//				}
//				output = output.substring(0, p) + replaceWith
//						+ output.substring(p + 6);
//				isDone = false;
//			}
//
//			p = output.indexOf("{/he}");
//			if (p != -1) {
//				switch (perspective) {
//				case Creature.PERSPECTIVE_YOU: {
//					replaceWith = "you";
//					break;
//				}
//				case Creature.PERSPECTIVE_VIEWER: {
//					replaceWith = "he";
//					break;
//				}
//				}
//				// System.out.println("Replacing '{/he}' with '" + replaceWith +
//				// "'.");
//				output = output.substring(0, p) + replaceWith
//						+ output.substring(p + 5);
//				isDone = false;
//			}
//
//			p = output.indexOf("{/him}");
//			if (p != -1) {
//				switch (perspective) {
//				case Creature.PERSPECTIVE_YOU: {
//					replaceWith = "you";
//					break;
//				}
//				case Creature.PERSPECTIVE_VIEWER: {
//					String name = creature.getShortName();
//					replaceWith = "him";
//					break;
//				}
//				}
//				// System.out.println("Replacing '{/him}' with '" + replaceWith
//				// + "'.");
//				output = output.substring(0, p) + replaceWith
//						+ output.substring(p + 6);
//				isDone = false;
//			}
//
//			p = output.indexOf("{/your}");
//			if (p != -1) {
//				switch (perspective) {
//				case Creature.PERSPECTIVE_YOU: {
//					replaceWith = "your";
//					break;
//				}
//				case Creature.PERSPECTIVE_VIEWER: {
//					String name = creature.getShortName();
//					if (name.endsWith("s")) {
//						replaceWith = name + "'";
//					} else {
//						replaceWith = name + "'s";
//					}
//					break;
//				}
//				}
//				// System.out.println("Replacing '{/your}' with '" + replaceWith
//				// + "'.");
//				output = output.substring(0, p) + replaceWith
//						+ output.substring(p + 7);
//				isDone = false;
//			}
//
//			p = output.indexOf("{/s}");
//			if (p != -1) {
//				switch (perspective) {
//				case Creature.PERSPECTIVE_YOU: {
//					replaceWith = "";
//					break;
//				}
//				case Creature.PERSPECTIVE_VIEWER: {
//					String name = creature.getShortName();
//					replaceWith = "s";
//					break;
//				}
//				}
//				// System.out.println("Replacing '{/s}' with '" + replaceWith +
//				// "'.");
//				output = output.substring(0, p) + replaceWith
//						+ output.substring(p + 4);
//				isDone = false;
//			}
//		} while (!isDone);
//
//		return output;
//	}


}
