package diginamic.gdm.services.implementations;

import java.time.LocalDateTime;

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

		SecurityToken token = this.securityRepository.findByCollaborator(user)
				.orElseThrow(() -> new Exception("user not found"));
		// ici soit on a quelque chose soit on à une exception
		// with OAuth2 respect we normally should only issue an grant
		token.setGrant(RandomStringUtils.random(6, false, true));
		// and generate the others in another function
		token.setIssued(LocalDateTime.now());
		return token;
	}

	public SecurityToken getGranted(SecurityToken auth, Long validitySeconds) throws Exception {
		SecurityToken token = this.securityRepository.findByGrant(auth.getGrant())
				.orElseThrow(() -> new Exception(" le login n'as pas été validé "));
		if (token.getIssued().plusSeconds(validitySeconds).isAfter(LocalDateTime.now())) {
			this.deleteToken(token);
			throw new Exception("Le token est périmé");
		}
		token.setGrant(null);
		// a brand new token
		token.setAuthentification(RandomStringUtils.random(6, false, true));
		token.setRefresh(RandomStringUtils.random(6, false, true));
		token.setIssued(LocalDateTime.now());
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
				.orElseThrow(() -> new Exception(" les token n'exite pas "));
		//// a refresh of the token
		token.setAuthentification(RandomStringUtils.random(6, false, true));
		token.setRefresh(RandomStringUtils.random(6, false, true));
		token.setIssued(LocalDateTime.now());

		return token;
	}	

	public void deleteToken(SecurityToken token) {
		this.securityRepository.delete(token);
	}
	
	public boolean logout(SecurityToken token) {
		this.deleteToken(token);
		return true;
	}
}
