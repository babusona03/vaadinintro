package dev.ui.util;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.spec.SecretKeySpec;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import javax.ws.rs.core.UriInfo;

import org.apache.shiro.codec.Hex;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha512Hash;
import org.apache.shiro.util.ByteSource;

//import dev.ui.service.UserCredentialService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RequestScoped
public class SecurityUtil {
	
	//service layer is injected here. required for database operations
//	@Inject 
//	private UserCredentialService userCredentialService;

	
	@Inject
	private Logger logger;
	
	//this method will accept the clearText password
	//it will generate the salt and the hashed password
	//salt and the hash value is then put to a map 
	//the map is returned
	public Map<String,String> hashPassword(String clearTextPassword) {
		ByteSource salt = this.getSalt();
		Map<String, String> credentialMap = new HashMap<>();
		credentialMap.put("hashedPassword", this.getHasedPassword(clearTextPassword, salt));
		credentialMap.put("salt",salt.toHex());
		credentialMap.put("ctp", clearTextPassword);
		return credentialMap;
	}
	
	
	//this method generates the salt
	private ByteSource getSalt() {
		SecureRandomNumberGenerator secureRandomNumberGenerator = new SecureRandomNumberGenerator();
		ByteSource salt = secureRandomNumberGenerator.nextBytes();
		return salt;
	}
	
	
	//this method generates the hash using the salt and the provided clearText password
	private String getHasedPassword(String clearTextPassword, ByteSource salt) {
		Sha512Hash hash = new Sha512Hash(clearTextPassword, salt, 2000000);
		return hash.toHex();		
	}
	
	//this method delegates authentication mechanism to service layer
//	public boolean authenticateUser(String userName, String password) {		
//		return userCredentialService.authenticateUser(userName, password);
//	}
	
	//this method verifies the provided password with the stored one and returns the truth value
	public boolean matchPassword(String storedHashedPassword, String saltText,String clearTextPassword) {
		ByteSource salt = ByteSource.Util.bytes(Hex.decode(saltText));
		String hashedPassword = this.getHasedPassword(clearTextPassword, salt);
		return storedHashedPassword.equals(hashedPassword);
	}
	
	//this method delegates the job of token creation to a private method
	public String getToken(String userName,UriInfo uriInfo) {
		//the uriInfo is received from the REST resource.
		return this.createBearerToken(userName,uriInfo);
	}
	
	//this method creates the bearer token
	private String createBearerToken(String userName,UriInfo uriInfo){
		Key key = this.generateKey(userName);
		System.out.println("details about key :"+key.getAlgorithm()+" "+key.toString());
		logger.log(Level.INFO,"Issuer info: {0}",uriInfo.getAbsolutePath().toString());
		String token = Jwts.builder().setSubject(userName).setIssuer(uriInfo.getAbsolutePath().toString()).setIssuedAt(new Date()).setExpiration(this.toDate(LocalDateTime.now().plusMinutes(15))).signWith(SignatureAlgorithm.HS512, key).setAudience(uriInfo.getBaseUri().toString()).compact();
//		String token = Jwts.builder().setSubject(email).   setIssuer(uriInfo.getAbsolutePath().toString()).setIssuedAt(new Date()).setExpiration(securityUtil.toDate(LocalDateTime.now().plusMinutes(15))).signWith(SignatureAlgorithm.HS512, key).setAudience(uriInfo.getBaseUri().toString())                .compact();
		logger.log(Level.INFO,"Generated token is {0}",token); 
		return token;
	}
	
	//this method generates the key to sign the token
	public Key generateKey(String keyString) {
		SecretKeySpec secretKeySpec =  new SecretKeySpec(keyString.getBytes(),0,keyString.getBytes().length, "DES");
		return secretKeySpec;
	}
	
	//converts a LocalDateTime to java.util.Date
	private Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}
