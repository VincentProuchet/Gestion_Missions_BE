package diginamic.gdm.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import diginamic.gdm.Enums.Role;
import diginamic.gdm.dto.CollaboratorDTO;

/**
 * classe de test de DAO-DTO/Collaborator 
 * on teste aussi la conversion depuis et vers le DTO
 * 
 * @author Vincent
 *
 */
public class CollaboratorTest {

	
	@Test
	public void DAObaseValues() {
		   // manager1 for the workers
        Collaborator user = new Collaborator();
        assertEquals(0,user.getId());
        assertNull(user.getFirstName());
        assertNull(user.getLastName());
        assertNull(user.getEmail());
        assertNull(user.getUsername());
        assertNull(user.getPassword());

        assertNull(user.getManager());
        assertEquals(new HashSet<Collaborator>() ,user.getTeam());
        
                
        assertNull(user.getAuthorities());
        assertEquals(new HashSet<Mission>() ,user.getMissions());
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
        Set<Collaborator> team  = user.getTeam(); // yep, you can just get the rerference and directly modify it
        										// so much for the private
        team.add(user);
        team.add(user2);
        team.add(user3);
        //user.setTeam(team);
        List<Roles> authorities  = new ArrayList<Roles>();
        Roles collaborator =  new Roles(Role.COLLABORATOR);
        authorities.add(collaborator);
        user.setAuthorities(authorities);
        
        assertEquals("firstname",user.getFirstName() );
        assertEquals("lastname",user.getLastName() );
        assertEquals("username",user.getUsername() );
        assertEquals("user@mail.com",user.getEmail() );
        assertEquals("1111",user.getPassword() );

        assertEquals(user,user.getManager());
        assertEquals(3,user.getTeam().size());
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
        List<Roles> authorities  = new ArrayList<Roles>();
        Roles collaborator =  new Roles(Role.COLLABORATOR);
        authorities.add(collaborator);        
        
        user.setFirstName("firstname");
        user.setLastName("lastname");
        user.setUsername("username");
        user.setEmail("user@mail.com");
        user.setPassword("1111");
        user.setActive(true);
        user.setManager(user);
        user.setAuthorities(authorities);
        
        Set<Collaborator> team  = user.getTeam();
        team.add(user);
        team.add(user2);
        team.add(user3);
        // we convert here
        CollaboratorDTO userDTO = new CollaboratorDTO(user);
        
        assertEquals("firstname",userDTO.getFirstName() );
        assertEquals("lastname",userDTO.getLastName() );
        assertEquals("username",userDTO.getUsername() );
        assertEquals("user@mail.com",userDTO.getEmail() );

        assertEquals(userDTO.getFirstName(),userDTO.getManager().getFirstName());
        assertNull(userDTO.getManager().getManager());
        assertTrue(userDTO.getRoles().contains(collaborator));
        
	}
	
	@Test
	public void convertionFromDTO() {
		CollaboratorDTO userDTO = new CollaboratorDTO();
		List<Roles> authorities  = new ArrayList<Roles>();
	    Roles collaboratorRole =  new Roles(Role.COLLABORATOR);
	    authorities.add(collaboratorRole);
	    
		userDTO.setFirstName("firstname");
        userDTO.setLastName("lastname");
        userDTO.setUsername("username");
        userDTO.setEmail("user@mail.com");        
        userDTO.setManager(userDTO);
        userDTO.setRoles(authorities);
        // we convert here
        Collaborator user = new Collaborator(userDTO);
        
        assertEquals("firstname",user.getFirstName() );
        assertEquals("lastname",user.getLastName() );
        assertEquals("username",user.getUsername() );
        assertEquals("user@mail.com",user.getEmail() );

        assertEquals(user.getFirstName(),user.getManager().getFirstName());
        assertNull(user.getManager().getManager());
        assertTrue(user.getAuthorities().contains(collaboratorRole));
	}
	
}