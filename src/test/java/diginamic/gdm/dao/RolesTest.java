package diginamic.gdm.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import diginamic.gdm.Enums.Role;

/**
 * test de la classe roles
 * dût à certains choix indépendant de Ma volonté
 * les Roles doivent avoir une description/label/nom
 * commençant par ROLE_
 * @author Vincent
 *
 */
public class RolesTest {

	/**
	 * dût à certains choix indépendant de Ma volonté
	 * en Spring Security les grantedAuthority
	 * doivent avoir une description/label/nom
	 * commençant par ROLE_
	 * notre architecture à créer une Enum Role qui implémente l'interface GrantedAuthority
	 * Pourquoi l'énum ?
	 * parce qu'e l'on peut appeler ses instances de n'importe ou dans le code
	 * sans avoir à passer par plusieurs lignes de code
	 */
	@Test
	public void TestRoleEnumAuthorities() {
		for (Role role : Role.values()) {
			assertTrue(role.getAuthority().startsWith("ROLE_"));
		}
	}
	@Test
	public void TestRoleIDs() {
		// admin
		assertEquals(1, Role.ADMIN.getId());
		// manager
		assertEquals(2000, Role.MANAGER.getId());
		// collaborator
		assertEquals(3000, Role.COLLABORATOR.getId());
		// user
		assertEquals(4000, Role.USER.getId());
		// anon
		assertEquals(5000, Role.ANON.getId());

	}
	@Test
	public void TestRoleToRolesConversion() {
		Roles roles = null;
		for (Role role : Role.values()) {
			roles = new Roles(role);
			assertEquals(roles.getId(), role.getId());
			assertEquals(roles.getLabel(), role.getLABEL());
			assertEquals(roles.getAuthority(), role.getAuthority());
		}
	}


}
