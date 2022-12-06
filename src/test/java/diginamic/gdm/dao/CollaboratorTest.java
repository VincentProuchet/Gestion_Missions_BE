package diginamic.gdm.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import diginamic.gdm.Enums.Role;
import diginamic.gdm.dto.CollaboratorDTO;

/**
 * classe de test de DAO-DTO/Collaborator on teste aussi la conversion depuis et
 * vers le DTO
 *
 * @author Vincent
 *
 */
public class CollaboratorTest {

	@Test
	public void DAObaseValues() {
		// manager1 for the workers
		Collaborator user = new Collaborator();
		assertEquals(0, user.getId());
		assertNull(user.getFirstName());
		assertNull(user.getLastName());
		assertNull(user.getEmail());
		assertNull(user.getUsername());
		assertNull(user.getPassword());

		assertNull(user.getManager());
		assertEquals(new HashSet<Collaborator>(), user.getTeam());

		assertNull(user.getAuthorities());
		assertEquals(new HashSet<Mission>(), user.getMissions());
		// in a normal projet those should return false by default
		assertFalse(user.isActive());
		assertFalse(user.isAccountNonExpired());
		assertFalse(user.isAccountNonLocked());
		assertFalse(user.isCredentialsNonExpired());
		assertFalse(user.isEnabled());

	}

	@Test
	public void DAOsetValues() {

		// manager1 for the workers
		Collaborator user = new Collaborator();
		Collaborator user2 = new Collaborator();
		Collaborator user3 = new Collaborator();
		user.setFirstName("firstname");
		user.setLastName("lastname");
		user.setUsername("username");
		user.setEmail("user@mail.com");
		user.setPassword("1111");
		user.setActive(true);
		user.setManager(user);
		Set<Collaborator> team = user.getTeam(); // yep, you can just get the rerference and directly modify it
													// so much for the private
		team.add(user);
		team.add(user2);
		team.add(user3);
		// user.setTeam(team);
		List<Roles> authorities = new ArrayList<>();
		Roles collaborator = new Roles(Role.COLLABORATOR);
		authorities.add(collaborator);
		user.setAuthorities(authorities);

		assertEquals("firstname", user.getFirstName());
		assertEquals("lastname", user.getLastName());
		assertEquals("username", user.getUsername());
		assertEquals("user@mail.com", user.getEmail());
		assertEquals("1111", user.getPassword());

		assertEquals(user, user.getManager());
		assertEquals(3, user.getTeam().size());
		assertTrue(user.getTeam().contains(user));

		assertTrue(user.isActive());
		assertTrue(user.isAccountNonExpired());
		assertTrue(user.isAccountNonLocked());
		assertTrue(user.isCredentialsNonExpired());
		assertTrue(user.isEnabled());

		assertTrue(user.getAuthorities().contains(collaborator));
	}

	@Test
	public void convertionToDTO() {
		Collaborator user = new Collaborator();
		Collaborator user2 = new Collaborator();
		Collaborator user3 = new Collaborator();
		List<Roles> authorities = new ArrayList<>();
		Roles collaborator = new Roles(Role.COLLABORATOR);
		authorities.add(collaborator);

		user.setFirstName("firstname");
		user.setLastName("lastname");
		user.setUsername("username");
		user.setEmail("user@mail.com");
		user.setPassword("1111");
		user.setActive(true);
		user.setManager(user);
		user.setAuthorities(authorities);

		Set<Collaborator> team = user.getTeam();
		team.add(user);
		team.add(user2);
		team.add(user3);
		// we convert here
		CollaboratorDTO userDTO = new CollaboratorDTO(user);

		assertEquals("firstname", userDTO.getFirstName());
		assertEquals("lastname", userDTO.getLastName());
		assertEquals("username", userDTO.getUsername());
		assertEquals("user@mail.com", userDTO.getEmail());

		assertEquals(userDTO.getFirstName(), userDTO.getManager().getFirstName());
		assertNull(userDTO.getManager().getManager());
		assertTrue(userDTO.getRoles().contains(collaborator));

	}

	@Test
	public void convertionFromDTO() {
		CollaboratorDTO userDTO = new CollaboratorDTO();
		List<Roles> authorities = new ArrayList<>();
		Roles collaboratorRole = new Roles(Role.COLLABORATOR);
		authorities.add(collaboratorRole);

		userDTO.setFirstName("firstname");
		userDTO.setLastName("lastname");
		userDTO.setUsername("username");
		userDTO.setEmail("user@mail.com");
		userDTO.setManager(userDTO);
		userDTO.setRoles(authorities);
		// we convert here
		Collaborator user = new Collaborator(userDTO);

		assertEquals("firstname", user.getFirstName());
		assertEquals("lastname", user.getLastName());
		assertEquals("username", user.getUsername());
		assertEquals("user@mail.com", user.getEmail());

		assertEquals(user.getFirstName(), user.getManager().getFirstName());
		assertNull(user.getManager().getManager());
		assertTrue(user.getAuthorities().contains(collaboratorRole));
	}

	@Test
	public void testIsEmailValid() {
		// base // form
		assertTrue(Collaborator.isValidEmail("aze@aze.com"));
		assertTrue(Collaborator.isValidEmail("aze@az.cm"));
		assertFalse(Collaborator.isValidEmail("aze@az.c"));
		assertFalse(Collaborator.isValidEmail("aze@aze."));
		assertFalse(Collaborator.isValidEmail("azertyu.com"));
		assertFalse(Collaborator.isValidEmail("aze@azertycom"));
		assertFalse(Collaborator.isValidEmail("azertyuiopcom"));
		// Maj
		assertFalse(Collaborator.isValidEmail("aZe@gmail.com"));
		assertFalse(Collaborator.isValidEmail("aze@azeRty.com"));
		assertFalse(Collaborator.isValidEmail("azerty@uiop.cM"));
		// one dash
		assertTrue(Collaborator.isValidEmail("aze-rty@uiop.com"));
		assertTrue(Collaborator.isValidEmail("ez-f@gmail.com"));
		assertTrue(Collaborator.isValidEmail("a-ze@gmail.com"));
		assertFalse(Collaborator.isValidEmail("a-z@gmail.com"));
		assertFalse(Collaborator.isValidEmail("-z@gmail.com"));
		assertFalse(Collaborator.isValidEmail("a-@gmail.com"));
		// two dash
		assertTrue(Collaborator.isValidEmail("a-z-e@gmail.com"));
		assertFalse(Collaborator.isValidEmail("a--z@gmail.com"));
		assertFalse(Collaborator.isValidEmail("-z-e@gmail.com"));
		assertFalse(Collaborator.isValidEmail("a-z-@gmail.com"));
		// point
		assertTrue(Collaborator.isValidEmail("aze.rty@gmail.com"));
		assertTrue(Collaborator.isValidEmail("a.ze@gmail.com"));
		assertFalse(Collaborator.isValidEmail("a.z@gmail.com"));
		assertFalse(Collaborator.isValidEmail("a..ze@gmail.com"));
		assertFalse(Collaborator.isValidEmail("az..e@gmail.com"));
		assertFalse(Collaborator.isValidEmail("..zer@gmail.com"));
		assertFalse(Collaborator.isValidEmail("aze..@gmail.com"));
		// dash and point mixed
		assertTrue(Collaborator.isValidEmail("a.z-e@gmail.com"));
		assertTrue(Collaborator.isValidEmail("a-z.e@gmail.com"));

		assertFalse(Collaborator.isValidEmail(".a-ze@gmail.com"));
		assertFalse(Collaborator.isValidEmail(".az-e@gmail.com"));
		assertFalse(Collaborator.isValidEmail(".-aze@gmail.com"));
		assertFalse(Collaborator.isValidEmail("az.-e@gmail.com"));

		assertFalse(Collaborator.isValidEmail("-a.ze@gmail.com"));
		assertFalse(Collaborator.isValidEmail("-.aze@gmail.com"));
		assertFalse(Collaborator.isValidEmail("a-.ze@gmail.com"));

		assertFalse(Collaborator.isValidEmail("a.ze-@gmail.com"));
		assertFalse(Collaborator.isValidEmail("aze.-@gmail.com"));
		assertFalse(Collaborator.isValidEmail("a.-ze@gmail.com"));
		assertFalse(Collaborator.isValidEmail("aze-.@gmail.com"));
		assertFalse(Collaborator.isValidEmail("a-.ze@gmail.com"));
		//////////////////
		// numbers
		assertTrue(Collaborator.isValidEmail("45aze4rty5uiop4656@azerty.com"));
		assertTrue(Collaborator.isValidEmail("45aze-4rty5uiop4656@azerty.com"));
		assertTrue(Collaborator.isValidEmail("45aze-4rty5uiop-4656@azerty.com"));
		assertTrue(Collaborator.isValidEmail("45aze.4rty5uiop4656@azerty.com"));
		assertTrue(Collaborator.isValidEmail("45aze.4rty5uiop.4656@azerty.com"));
		assertTrue(Collaborator.isValidEmail("45aze.4rty5uiop.4656@az789erty.com"));

		assertTrue(Collaborator.isValidEmail("s45dfgsgd.ssv45sd@f-dsg.com"));
		assertTrue(Collaborator.isValidEmail("45dqs45fdqfk4656@qdsf-qdf.com"));
		assertFalse(Collaborator.isValidEmail("45dqs45fdqfk4656@qdsf..qdf.com"));

		assertFalse(Collaborator.isValidEmail("4s,46@qdsfq.com"));
		assertFalse(Collaborator.isValidEmail("45dqs?45fdqfk4656@qdsfqdf.com"));
		// Longest
		assertTrue(Collaborator.isValidEmail(
				"ooooooooodooooooooodooooooooodooooooooodooooooooodooooooooodooooooooodooooooooodoooooooood@qdsfqdf.com"));
		assertTrue(Collaborator.isValidEmail(
				"ooooooooodooooooooodoooooooood-ooooooooodooooooooodoooooooood-ooooooooodooooooooodoooooooood@qdsfqdf.com"));
		assertTrue(Collaborator.isValidEmail(
				"ooooooooodooooooooodoooooooood.ooooooooodooooooooodoooooooood.ooooooooodooooooooodoooooooood@qdsfqdf.com"));
		assertFalse(Collaborator.isValidEmail(
				"ooooooooodooooooooodooooooooodooooooooodooooooooodooooooooodooooooooodooooooooodoooooooooda@qdsfqdf.com"));
		assertFalse(Collaborator.isValidEmail(
				"ooooooooodooooooooodoooooooood.ooooooooodooooooooodoooooooood.ooooooooodooooooooodoooooooooda@qdsfqdf.com"));
		assertFalse(Collaborator.isValidEmail(
				"ooooooooodooooooooodoooooooood.ooooooooodooooooooodoooooooooda.ooooooooodooooooooodoooooooood@qdsfqdf.com"));
		assertFalse(Collaborator.isValidEmail(
				"ooooooooodooooooooodoooooooooda.ooooooooodooooooooodoooooooood.ooooooooodooooooooodoooooooood@qdsfqdf.com"));

		assertTrue(Collaborator.isValidEmail("aze@ooooooooodooooooooodooooooooodooooooooodooooooooodoooooooood.com"));
		assertFalse(Collaborator.isValidEmail("aze@ooooooooodooooooooodooooooooodooooooooodooooooooodoooooooooda.com"));
		assertFalse(
				Collaborator.isValidEmail("aze@ooooooooodooooooooodoooooooooda.ooooooooodooooooooodoooooooood.com"));
		assertFalse(
				Collaborator.isValidEmail("aze@ooooooooodooooooooodoooooooood.ooooooooodooooooooodoooooooooda.com"));

		assertTrue(Collaborator.isValidEmail("aze@rt.oooood"));
		assertFalse(Collaborator.isValidEmail("aze@rt.ooooood"));


		// punctuation
		String stringToTest;
		String charOfPunct = ",;:!*^$&\"'(_)=?/§%¨£°+~#{[|`\\^@]}<>";
		for (int i = 0; i < charOfPunct.length(); i++) {
			System.err.println("validMail before @ : "+ i);// corrected you're an idiot vincent
									// I know
			stringToTest = new StringBuilder("aze").append(charOfPunct.charAt(i)).append("rtu@yuiop.com").toString();
			assertFalse(Collaborator.isValidEmail(stringToTest));
		}
		// punctuation
				// yhea all that could be condensed in that
				// but when it fail it doesn't tells you wich one failled

		charOfPunct = ",;:!*^$&\"'(_)=?./§%¨£°+~#{[|`\\^@]}<>";
				for (int i = 0; i < charOfPunct.length(); i++) {
					System.err.println("validMail after @ : "+  i);// corrected you're an idiot vincent
											// I know
					stringToTest = new StringBuilder("aze@").append(charOfPunct.charAt(i)).append("rtuyuiop.com").toString();
					assertFalse(Collaborator.isValidEmail(stringToTest));
				}



	}

	@Test
	public void testIsLastNameValid() {
		// pretty long
		assertTrue(Collaborator.isValidLastName("ezdfqefqgmailcom"));
		// name with particule
		assertTrue(Collaborator.isValidLastName("PzdfqGefq-Gsmailcom"));
		// composed names
		assertTrue(Collaborator.isValidLastName("PzdfqGefq--Gsmailcom"));
		assertFalse(Collaborator.isValidLastName("PzdfqGefq---gsmailcom"));
		// space
		assertFalse(Collaborator.isValidLastName("PzdfqGefq gsmailcom"));
		// smallest names
		assertTrue(Collaborator.isValidLastName("aa"));
		assertTrue(Collaborator.isValidLastName("a-a"));
		assertTrue(Collaborator.isValidLastName("a--a"));
		assertFalse(Collaborator.isValidLastName("a"));
		assertFalse(Collaborator.isValidLastName("a-"));
		assertFalse(Collaborator.isValidLastName("-a"));
		assertFalse(Collaborator.isValidLastName("a--"));
		assertFalse(Collaborator.isValidLastName("--a"));
		// longest Names
		assertTrue(Collaborator.isValidLastName("aaaaaaaaaDaaaaaaaaaDaaaaaaaaaDaaaaaaaaaDaaaaaaaaaDaaaaaaaaaDqqqP"));
		assertFalse(Collaborator.isValidLastName("aaaaaaaaaDaaaaaaaaaDaaaaaaaaaDaaaaaaaaaDaaaaaaaaaDaaaaaaaaaDqqqPs"));
		assertTrue(Collaborator.isValidLastName("aaaaaaaaaDaaaaaaaaaDaaaaaaaaaDqq--aaaaaaaaaDaaaaaaaaaDaaaaaaaaaDqq"));
		assertFalse(
				Collaborator.isValidLastName("aaaaaaaaaDaaaaaaaaaDaaaaaaaaaDqqP--aaaaaaaaaDaaaaaaaaaDaaaaaaaaaDqq"));
		assertFalse(
				Collaborator.isValidLastName("aaaaaaaaaDaaaaaaaaaDaaaaaaaaaDqq--aaaaaaaaaDaaaaaaaaaDaaaaaaaaaDqqP"));
		// accented char
		assertTrue(Collaborator.isValidLastName("PzdfqéGefq-gsmaàilcom"));
		assertTrue(Collaborator.isValidLastName("aâäà-aâäà"));
		assertTrue(Collaborator.isValidLastName("aâäàeêëéè-aâäàeêëéè"));
		assertTrue(Collaborator.isValidLastName("aâäàeêëéèÿ-aâäàeêëéèÿ"));
		String mirrorValue = "aâäàãeêëéèyÿuûüùiîïçìoôöòõµñ";
		assertTrue(Collaborator.isValidLastName(mirrorValue + "--" + mirrorValue));
		// punctuation

		assertFalse(Collaborator.isValidLastName("PzdfqGefq.gsmailcom"));
		assertFalse(Collaborator.isValidLastName("PzdfqGefq,gsmailcom"));
		assertFalse(Collaborator.isValidLastName("PzdfqGefq;gsmailcom"));
		assertFalse(Collaborator.isValidLastName("PzdfqGefq?gsmailcom"));
		assertFalse(Collaborator.isValidLastName("PzdfqGefq/gsmailcom"));
		assertFalse(Collaborator.isValidLastName("PzdfqGefq:gsmailcom"));
		assertFalse(Collaborator.isValidLastName("PzdfqGefq!gsmailcom"));
		assertFalse(Collaborator.isValidLastName("PzdfqGefq§gsmailcom"));
		assertFalse(Collaborator.isValidLastName("PzdfqGefq%gsmailcom"));
		assertFalse(Collaborator.isValidLastName("PzdfqGefq*gsmailcom"));
		assertFalse(Collaborator.isValidLastName("PzdfqGefq^gsmailcom"));
		assertFalse(Collaborator.isValidLastName("PzdfqGefq¨gsmailcom"));
		assertFalse(Collaborator.isValidLastName("PzdfqGefq$gsmailcom"));
		assertFalse(Collaborator.isValidLastName("PzdfqGefq£gsmailcom"));
		assertFalse(Collaborator.isValidLastName("PzdfqGefq¤gsmailcom"));
		assertFalse(Collaborator.isValidLastName("PzdfqGefq@gsmailcom"));
		assertFalse(Collaborator.isValidLastName("PzdfqGefq~gsmailcom"));
		assertFalse(Collaborator.isValidLastName("PzdfqGefq#gsmailcom"));
		assertFalse(Collaborator.isValidLastName("PzdfqGefq\"gsmailcom"));
		assertFalse(Collaborator.isValidLastName("PzdfqGefq{gsmailcom"));
		assertFalse(Collaborator.isValidLastName("PzdfqGefq}gsmailcom"));
		assertFalse(Collaborator.isValidLastName("PzdfqGefq(gsmailcom"));
		assertFalse(Collaborator.isValidLastName("PzdfqGefq[gsmailcom"));
		assertFalse(Collaborator.isValidLastName("PzdfqGefq|gsmailcom"));
		assertFalse(Collaborator.isValidLastName("PzdfqGefq`gsmailcom"));
		assertFalse(Collaborator.isValidLastName("PzdfqGefq_gsmailcom"));
		assertFalse(Collaborator.isValidLastName("PzdfqGefq\\gsmailcom"));
		assertFalse(Collaborator.isValidLastName("PzdfqGefq)gsmailcom"));
		assertFalse(Collaborator.isValidLastName("PzdfqGefq]gsmailcom"));
		assertFalse(Collaborator.isValidLastName("PzdfqGefq°gsmailcom"));
		assertFalse(Collaborator.isValidLastName("PzdfqGefq=gsmailcom"));
		assertFalse(Collaborator.isValidLastName("PzdfqGefq=gsmailcom"));
		assertFalse(Collaborator.isValidLastName("PzdfqGefq}gsmailcom"));

		// yhea all that could be condensed in that
		// but when it fail it doesn't tells you wich one failled
		String stringToTest;
		String charToTest = ",;:!*^$&\"'(_)=?./§%¨£°+~#{[|`\\^@]}<>";
		for (int i = 0; i < charToTest.length(); i++) {
			System.err.println("valid last name " + i);// corrected you're an idiot vincent
									// I know
			stringToTest = new StringBuilder("Pzq").append(charToTest.charAt(i)).append("Gom").toString();
			assertFalse(Collaborator.isValidLastName(stringToTest));
		}

		// numbers //true for now
		assertTrue(Collaborator.isValidLastName("0123456789-123456789"));
		// email
		assertFalse(Collaborator.isValidLastName("ezdfqefq@gmail.com"));

		Collaborator user = new Collaborator();
		// We test the strip fonction of the setLastname
		user.setLastName("ezdfqefqgmailcom         ");
		assertTrue(Collaborator.isValidLastName(user.getLastName()));
		user.setLastName("        ezdfqefqgmailcom");
		assertTrue(Collaborator.isValidLastName(user.getLastName()));
		assertTrue(Collaborator.isValidLastName("PzdfqGefqgsmailcom"));
	}

	@Test
	public void testIsFirstNameValid() {
		// pretty long
		assertTrue(Collaborator.isValidFirstName("ezdfqefqgmailcom"));
		// name with particule
		assertTrue(Collaborator.isValidFirstName("PzdfqGefq-Gsmailcom"));
		// composed names
		assertFalse(Collaborator.isValidFirstName("PzdfqGefq--Gsmailcom"));
		assertFalse(Collaborator.isValidFirstName("PzdfqGefq---gsmailcom"));
		// space
		assertFalse(Collaborator.isValidFirstName("PzdfqGefq gsmailcom"));
		// smallest names
		assertTrue(Collaborator.isValidFirstName("aa"));
		assertTrue(Collaborator.isValidFirstName("a-a"));
		assertFalse(Collaborator.isValidFirstName("a--a"));
		assertFalse(Collaborator.isValidFirstName("a"));
		assertFalse(Collaborator.isValidFirstName("a-"));
		assertFalse(Collaborator.isValidFirstName("-a"));
		assertFalse(Collaborator.isValidFirstName("a--"));
		assertFalse(Collaborator.isValidFirstName("--a"));
		// longest Names
		assertTrue(Collaborator.isValidFirstName("aaaaaaaaaDaaaaaaaaaDaaaaaaaaaDaaaaaaaaaDaaaaaaaaaDaaaaaaaaaDqqqP"));
		assertFalse(Collaborator.isValidFirstName("aaaaaaaaaDaaaaaaaaaDaaaaaaaaaDaaaaaaaaaDaaaaaaaaaDaaaaaaaaaDqqqPs"));
		assertTrue(Collaborator.isValidFirstName("aaaaaaaaaDaaaaaaaaaDaaaaaaaaaDqq-aaaaaaaaaDaaaaaaaaaDaaaaaaaaaDqq"));
		assertFalse(
				Collaborator.isValidFirstName("aaaaaaaaaDaaaaaaaaaDaaaaaaaaaDqqP-aaaaaaaaaDaaaaaaaaaDaaaaaaaaaDqq"));
		assertFalse(
				Collaborator.isValidFirstName("aaaaaaaaaDaaaaaaaaaDaaaaaaaaaDqq-aaaaaaaaaDaaaaaaaaaDaaaaaaaaaDqqP"));
		// accented char
		assertTrue(Collaborator.isValidFirstName("PzdfqéGefq-gsmaàilcom"));
		assertTrue(Collaborator.isValidFirstName("aâäà-aâäà"));
		assertTrue(Collaborator.isValidFirstName("aâäàeêëéè-aâäàeêëéè"));
		assertTrue(Collaborator.isValidFirstName("aâäàeêëéèÿ-aâäàeêëéèÿ"));
		String mirrorValue = "aâäàãeêëéèyÿuûüùiîïçìoôöòõµñ";
		assertTrue(Collaborator.isValidFirstName(mirrorValue + "-" + mirrorValue));
		// punctuation
		String stringToTest;
		String charToTest = ",;:!*^$&\"'(_)=?./§%¨£°+~#{[|`\\^@]}<>";
		for (int i = 0; i < charToTest.length(); i++) {
			System.err.println(" valid firstName "+ i);// this console log is here to help you pinpoint witch test has failed
			stringToTest = new StringBuilder("Pzq").append(charToTest.charAt(i)).append("Gom").toString();
			assertFalse(Collaborator.isValidFirstName(stringToTest));
		}

		// numbers //true for now
		assertTrue(Collaborator.isValidFirstName("0123456789-123456789"));
		// email
		assertFalse(Collaborator.isValidFirstName("ezdfqefq@gmail.com"));

		Collaborator user = new Collaborator();
		// We test the strip fonction of the setLastname
		user.setFirstName("ezdfqefqgmailcom         ");
		assertTrue(Collaborator.isValidFirstName(user.getFirstName()));
		user.setFirstName("        ezdfqefqgmailcom");
		assertTrue(Collaborator.isValidFirstName(user.getFirstName()));
		assertTrue(Collaborator.isValidFirstName("PzdfqGefqgsmailcom"));
	}
	@Test
	public void testIsUsernameValid() {
		// pretty long
		assertTrue(Collaborator.isValidUserName("ezdfqefqgmailcom"));

		// composed names
		assertTrue(Collaborator.isValidUserName("PzdfqGefq-Gsmailcom"));
		assertTrue(Collaborator.isValidUserName("PzdfqGefq_Gsmailcom"));
		assertFalse(Collaborator.isValidUserName("PzdfqGefq--Gsmailcom"));
		assertFalse(Collaborator.isValidUserName("PzdfqGefq---gsmailcom"));
		assertFalse(Collaborator.isValidUserName("PzdfqGefq__Gsmailcom"));
		assertFalse(Collaborator.isValidUserName("PzdfqGefq-_gsmailcom"));
		assertFalse(Collaborator.isValidUserName("PzdfqGefq_-gsmailcom"));
		// space
		assertFalse(Collaborator.isValidUserName("PzdfqGefq gsmailcom"));
		// smallest names
		assertTrue(Collaborator.isValidUserName("aa"));
		assertTrue(Collaborator.isValidUserName("a-a"));
		assertFalse(Collaborator.isValidUserName("a--a"));
		assertFalse(Collaborator.isValidUserName("a"));
		assertFalse(Collaborator.isValidUserName("a-"));
		assertFalse(Collaborator.isValidUserName("-a"));
		assertFalse(Collaborator.isValidUserName("a--"));
		assertFalse(Collaborator.isValidUserName("--a"));
		// longest Names
		assertTrue(Collaborator.isValidUserName("aaaaaaaaaDaaaaaaaaaDaaaaaaaaaDaaaaaaaaaDaaaaaaaP"));
		assertTrue(Collaborator.isValidUserName("a-aaa-aaa-aaD-aaa-aaa-aaa-Daa-aaa-aaa-aDa-aaa-aaa-aaD-aaa-aaa-aP"));
		assertFalse(Collaborator.isValidUserName("aaaaaaDaaaaaaaaaDaaaaaaaaaDaaaaaaaaaDaaaaaaaaaDaP"));


		// accented char
		assertTrue(Collaborator.isValidUserName("PzdfqéGefq-gsmaàilcom"));
		assertTrue(Collaborator.isValidUserName("aâäàeêëéè-aâäàeêëéè"));
		assertTrue(Collaborator.isValidUserName("aâäàeêëéèÿ-aâäàeêëéèÿ"));
		String mirrorValue = "aâäàãeêëéèyÿ";
		assertTrue(Collaborator.isValidUserName(mirrorValue + "-" + mirrorValue));
		mirrorValue =  "uûüùiîïçìoôöòõµñ";
		assertTrue(Collaborator.isValidUserName(mirrorValue + "-" + mirrorValue));
		// punctuation
		String stringToTest;
		String charToTest = ",;:!*^$&\"'()=?./§%¨£°+~#{[|`\\^@]}<>";
		for (int i = 0; i < charToTest.length(); i++) {
			System.err.println(" valid username "+ i);// this console log is here to help you pinpoint witch test has failed
			stringToTest = new StringBuilder("Pzq").append(charToTest.charAt(i)).append("Gom").toString();
			assertFalse(Collaborator.isValidUserName(stringToTest));
		}

		// numbers //true for now
		assertTrue(Collaborator.isValidUserName("0123456789-123456789"));
		// email
		assertFalse(Collaborator.isValidUserName("ezdfqefq@gmail.com"));

		Collaborator user = new Collaborator();
		// We test the strip fonction of the setLastname
		user.setUsername("ezdfqefqgmailcom         ");
		assertTrue(Collaborator.isValidUserName(user.getUsername()));
		user.setUsername("        ezdfqefqgmailcom");
		assertTrue(Collaborator.isValidUserName(user.getUsername()));
		assertTrue(Collaborator.isValidUserName("PzdfqGefqgsmailcom"));
	}
}