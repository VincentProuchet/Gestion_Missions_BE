package diginamic.gdm.controllers;

public interface GuiControl {

	/**
	 * action de fermeture du programme
	 */
	public void closeProgram();
	/**
	 *  action de connection d'un utilisateur
	 */
	public void loginUser(String userName, String password);
	/**
	 * action de déconnexion
	 */
	public void logOutUser();


}
