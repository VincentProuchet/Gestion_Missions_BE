package diginamic.gdm.vars.errors.impl;

import diginamic.gdm.vars.errors.ErrorsMessage;

/**
 * Class for for mission errors description
 * I'm still searching for a way of internationalizing this
 *
 * @author Vincent
 *
 */
public class MissionErrors extends ErrorsMessage {

	public static String ARRIVAL = "Arrival ";
	public static String DEPARTURE = "Departure ";
	public static String NOT_MANAGER ="you're not manager ";

	public static class create {

	}

	public static class read {
		/** NOT_FOUND */
		public static String NOT_FOUND = "Mission not found.";
		public static String ACCESS_DENIED = "Can't access ";
	}

	public static class update {

		public static String CANT_UPDATE = "you can't update ";
		/** IS_VALIDATED */
		public static String IS_VALIDATED = "Mission is validated and can't be modified.";
		/** NOT_DONE */
		public static String NOT_DONE = "Mission not done ";
		/** IS_ENDED */
		public static String IS_ENDED = "Mission as ended ";
		public static String IS_REJECTED = "Mission was reject and can't be validated ";
		public static String IS_INIT = "Mission is in initialised state and can't be validated ";
		/** IS_WAITING_VALIDATION */
		public static String IS_WAITING_VALIDATION = "Mission is waiting validation ";

	}

	public static class delete {
		/** STATUSERROR */
		public static String STATUSERROR = "with status : ";
		/** CANT_BE_DELETED */
		public static String CANT_BE_DELETED="Mission can't be deleted ";
	}

	public static class invalid {
		public static String START_CANT_BE="Mission can't start ";
		public static String NULL_CITY="city can't be ";
		public static String NULL_COLLABORATOR="collaborator can't be ";
		public static String NULL_NATURE="nature can't be ";
		public static String END_CANT_BE="Mission can't end ";
		public static String AERIAL_TRANSPORT = "Mission with aerial transport can't start in less than ";
		public static String INACTIVE_NATURE = "Selected nature is not active.";
		public static String CANT_START_IN_NEXT_MISSION = "Mission can't start in the next mission ";
		public static String CANT_END_IN_NEXT_MISSION = "Mission can't end in the next mission ";
	}

	public static class status{
		public static String NOT_IN_YOUR_TEAM = "for someone not in your team ";
		public static String NOT_YOURS = "for someone else ";
		public static String CANT_VALIDATE ="You can't validate a mission ";
		public static String CANT_DELETE ="You can't delete a mission ";
		public static String CANT_REJECT ="You can't reject a mission ";
		public static String CANT_RESET ="You can't reset a mission ";


	}


}
