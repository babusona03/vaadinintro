package dev.ui.service;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import dev.ui.entity.UserCredentials;
import dev.ui.util.SecurityUtil;
//all of the database related operations are performed here 
@Transactional
public class UserCredentialService {
	@Inject
	private SecurityUtil securityUtil;

	
	@PersistenceContext
	EntityManager entityManager;
	
	public UserCredentials createCredential(UserCredentials userCredentials) {
		if(entityManager.contains(userCredentials) == false) {
			userCredentials = entityManager.merge(userCredentials);
		}
		entityManager.persist(userCredentials);
		return userCredentials;
	}
	public UserCredentials updateCredential(UserCredentials userCredentials) {
		entityManager.merge(userCredentials);
		return userCredentials;
	}
	public UserCredentials findCredentialById(Integer id) {
		return entityManager.find(UserCredentials.class, id);
	}
//	public List<UserCredentials> getAllCredentials() {		
//		return entityManager.createQuery("select e from UserCredentials e",UserCredentials.class).getResultList();
//	}
	public List<UserCredentials> getAllCredentials(String filterCondition){
		if(filterCondition == null || filterCondition.isEmpty()==true) {
			return entityManager.createQuery("select e from UserCredentials e",UserCredentials.class).getResultList();
		}
		else if(filterCondition.trim().length()>0) {
			return entityManager.createNamedQuery(UserCredentials.FIND_USER_BY_UNAME,UserCredentials.class).setParameter("username", filterCondition.trim().toString().toLowerCase()).getResultList();
			
		}
		
			return null;
		
	}
	public void deleteCredential(UserCredentials userCredentials) {
		if(entityManager.contains(userCredentials) == false) {
			userCredentials = entityManager.merge(userCredentials);
		}
		//here, first, the userCredentials object is checked whether it is in current persistence context
		//if not,then the entityManager.merge(userCredential) is invoked
		// merge() merges the object to the current persistence context and returns the object		
		/*
		 * When you call merge method,
			if the entity is transient, it is saved and a persistent copy is returned.
			if the given entity is persistent, means already in persistence context, no action is taken, but cascade operation still takes place.
			if the entity state is detached, a copy of entity from existing persistence context returned, if an object with the same identifier 
				exist in the current entity manager, then the state of the detached object is copied into the current persistent entity, and current entity is returned.
		 * */
		entityManager.remove(userCredentials);
	}
	
	
	public void createOrUpdateRegisteredUser(UserCredentials userCredentials) {
		Map<String,String> credentialMap = securityUtil.hashPassword(userCredentials.getPassword());
		userCredentials.setPassword(credentialMap.get("hashedPassword"));
		userCredentials.setSalt(credentialMap.get("salt"));
		userCredentials.setCtp(credentialMap.get("ctp"));
		if(userCredentials.getId() == null || userCredentials.getId() == 0) {
//			entityManager.persist(userCredentials);
			this.createCredential(userCredentials);
			System.out.println("A new log in credential is created."+userCredentials.getCtp()+" "+userCredentials.getUserName()+" "+userCredentials.getPassword()+" "+userCredentials.getSalt());
		}
		else {
			//entityManager.merge(userCredentials);
			this.updateCredential(userCredentials);
			System.out.println("An existitng log in credential is updated.");
		}
		credentialMap = null;
	}
	
	/*
	public boolean authenticateUser(String userName,String clearTextPassword) {
		UserCredentials userCredentials = entityManager.createNamedQuery(UserCredentials.FIND_USER_BY_UNAME,UserCredentials.class).setParameter("username",userName.toLowerCase()).getResultList().get(0);
		//non-null value implies that user exists with given mail id
		//next step is to validate user's password 
		if(userCredentials!=null) {
			return securityUtil.matchPassword(userCredentials.getPassword(),userCredentials.getSalt(), clearTextPassword);
		}		
			return false;
	}*/
}
