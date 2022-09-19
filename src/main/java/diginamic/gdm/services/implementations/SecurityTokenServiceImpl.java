package diginamic.gdm.services.implementations;

import java.time.LocalDateTime;
import java.util.Optional;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import diginamic.gdm.dao.Collaborator;
import diginamic.gdm.dao.SecurityToken;
import diginamic.gdm.repository.CollaboratorRepository;
import diginamic.gdm.repository.SecurityTokeRepository;
import diginamic.gdm.services.SecurityTokenService;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SecurityTokenServiceImpl implements SecurityTokenService {

	private CollaboratorRepository collaboratorRepository;
	private SecurityTokeRepository securityRepository;

	
	public SecurityToken login(Collaborator user) throws Exception {
		SecurityToken token;
		// we get the token or create a new one
		token = this.securityRepository.findByCollaborator(user).orElseGet(() -> new SecurityToken());
			// we set its parameters 
			token.newGrant(user);
			// and save it
			securityRepository.save(token);						
		return token;
	}

	
	public SecurityToken getGranted(SecurityToken auth, Long validitySeconds) throws Exception {
		SecurityToken token = this.securityRepository.findByGrant(auth.getGrant())
				.orElseThrow(() -> new Exception(" le login n'as pas été validé "));
		if (token.getIssued().plusSeconds(validitySeconds).isAfter(LocalDateTime.now())) {
			throw new Exception("Le token est périmé");
		}
		token.granted();
		this.securityRepository.save(token);
		return token;
	}

	
	public SecurityToken Authenticate(SecurityToken auth, Long validitySeconds) throws Exception {
		SecurityToken token = this.securityRepository.findByAuthentification(auth.getAuthentification())
				.orElseThrow(() -> new Exception(" Le token n'existe pas"));
		// si le token est trop vieux
		if (token.getIssued().plusSeconds(validitySeconds).isAfter(LocalDateTime.now())) {
			//throw new Exception("Le token est périmé");
			System.out.println("Le token est périmé");
			return null;
		}
		return token;
	}

	public SecurityToken Refresh(SecurityToken auth, Long validitySeconds) throws Exception {
		SecurityToken token = this.securityRepository.findByRefresh(auth.getRefresh())
				.orElseThrow(() -> new Exception(" le token n'exite pas "));
		
		// we could decide that a refresh would have a limited 
		// validity but we didn't
	
		token.granted(); // here is your refreshed token
		this.securityRepository.save(token);
		return token;
	}	
	
	public boolean logout(SecurityToken auth) throws Exception {
		SecurityToken token = this.securityRepository.findByAuthentification(auth.getAuthentification())
				.orElseThrow(() -> new Exception(" Le token n'existe pas"));
		token.logOut();
		this.securityRepository.save(token);
		return true;
	}
}
