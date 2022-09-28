package diginamic.gdm.errors;

/**
 * Class for for mission errors description these may be internationalized in
 * the future and it can be done by just swapping class but all replacing class
 * Must extends this one just in case you forget on element the default elements
 * will take its place
 * 
 * @author Vincent
 *
 */
public class MissionServiceErrors {
	
	public static String END = "end ";
	public static String START = "start ";
	public static String NOW = "today date";
	public static String ON = " on ";
	public static String CITY =" city ";
	public static String CANT_BE =" can't be ";
	public static String NULL = "null.";
	public static String FROM_TODAY = " days from today.";
	
	public static class create {

	}

	public static class read {
		public static final String NOTFOUND = " mission not found ";
	}

	public static class update {
		public static final String INCONSISTENT_ID = " Inconsistent IDs ";
		public static final String IS_VALIDATED = " mission is validated and can't be modified.";
		public static final String IS_ENDED = " mission as ended.";
		public static final String IS_WAITING_VALIDATION = " mission is waiting validation.";
		public static final String CANT_BE_MODIFIED = " can't be modified.";
	}

	public static class delete {
		public static final String STATUSERROR = " Mission as the status : ";
		public static final String CANT_BE_DELETED=" and can't be deleted.";
	}

	public static class invalid {
		public static final String START_CANT_BE=" mission can't start ";
		public static final String CITY_CANT_BE=" city can't be ";
		public static final String END_CANT_BE=" mission can't end ";
		public static final String BEFORE = "before the ";
		public static final String AERIAL_TRANSPORT = " Mission with aerial transport can't start in less than ";
		public static final String INACTIVE_NATURE = "nature is not active.";
		public static final String CANT_START_IN_NEXT_MISSION = "Mission start in the next mission ";
		public static final String CANT_END_IN_NEXT_MISSION = "Mission end in the next mission ";
	}
	

}
