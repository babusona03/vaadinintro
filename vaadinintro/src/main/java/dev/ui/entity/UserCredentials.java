package dev.ui.entity;

import dev.ui.entity.AbstractUser;
import dev.ui.util.AbstractUserListener;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;



@Entity
//@EntityListeners()
@EntityListeners({AbstractUserListener.class})

@Table(name="CREDENTIAL")
// here goes a named query for retrieving a user based on the email provided.
@NamedQuery(name=UserCredentials.FIND_USER_BY_UNAME,query = "select e from UserCredentials e where e.userName like concat('%',:username,'%') order by e.userName")//('%',:address ,'%')
public class UserCredentials extends  AbstractUser implements Serializable{
	public static final String FIND_USER_BY_UNAME = "User.findByUname";
	@Transient
	private static final long serialVersionUID = 1L;
	// validation logic goes here
//	@FormParam("username")
	@Pattern(regexp="^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "Email must be in the form : user@domain.com")
	@NotEmpty(message = "Username must be provided.")
	@Email(message="Username must be a valid email id.")
	@Column(name="USERNAME")
	private String userName;
	
	@NotEmpty(message = "Password must be set.")
	@Size(min = 8)
	@Pattern(regexp = "^(?=.*[0-9]+.*)(?=.*[a-zA-Z]+.*)[0-9a-zA-Z]{6,}$", message= "password must contain at least 1 letter, 1  digit and at least 6 characters long. ")
//	@FormParam("password")//JPA does not care about this annotation
	@Column(name="PASSWORD")
	private String password;
	
	@Column(name = "SALT")
	private String salt;
	
	@Column(name = "BIRTHDATE")
	@NotNull(message= "Date of birth must be set.")
	private LocalDate birthDate;
	
	@Column(name="CTP")
	private String ctp;//the clear text password
//	@FormParam("role")
//	@Column(name="role")	
//	private String role;
//	// SysAdmin , Admin , Operator
//	
	
	public UserCredentials() {	
//		this.setUserName("");
//		this.setPassword("");
//		this.setBirthDate(LocalDate.now());
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}
	public String getCtp() {
		return ctp;
	}
	public void setCtp(String ctp) {
		this.ctp = ctp;
	}
	public  LocalDate getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}		
}

