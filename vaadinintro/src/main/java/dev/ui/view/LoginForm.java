package dev.ui.view;



import javax.inject.Inject;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.PropertyId;
import com.vaadin.flow.router.Route;
import dev.ui.entity.UserCredentials;
import dev.ui.service.UserCredentialService;

@Route("login")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")

public class LoginForm extends FormLayout{

	private static final long serialVersionUID = 1L;
	
	
	@PropertyId(value="userName")
	private EmailField userName;
	
	@PropertyId(value="password")
	private PasswordField password;
	
	private Button loginButton;
	private Button resetButton;	
	private HorizontalLayout buttonSet;
	
	private VerticalLayout formComponents;
	private Binder<UserCredentials> credentialBinder;
	
	private UserCredentials userCredentials;
	
	//the UserCredentialService is injected by the container, constructor injection is used 
	//field injection is not appropriate, container might inject the resource after object construction, 
	//so using the injected resource from container might lead to NPE
	// construction injection is the approach here, if injection is necessary
	
	@Inject
	public LoginForm(UserCredentialService userCredentialService) {
		
		
		userName = new EmailField("UserName");		
		password = new PasswordField("Password");
		loginButton = new Button("log In");
		resetButton = new Button("Reset");
		buttonSet= new HorizontalLayout(loginButton,resetButton);
		credentialBinder = new Binder<>(UserCredentials.class);//binder created in usercredentials class
		formComponents = new VerticalLayout();
		userCredentials = new UserCredentials();//creating the bean instance to receive the credential data from the form
		
		userName.setClearButtonVisible(true);
		userName.setRequiredIndicatorVisible(true);
		
		formComponents.add(userName, password );
		this.add(formComponents, buttonSet);
		
		credentialBinder.bindInstanceFields(this);//the annotated fields from the bean class get bound to the Binder named credentialBinder
		
		credentialBinder.setBean(userCredentials);
		//When you use the setBean method, the business object instance updates whenever the user changes the value of a bound field. 
		//without the setbean, credentialBinder.getBean() was returning null
		
		loginButton.addClickListener(event -> attemptLogin(userCredentialService));
		this.setSizeFull();
	}
	private void attemptLogin(UserCredentialService userCredentialService) {
		//UserCredentials userCredentials = new UserCredentials();
		userCredentials= credentialBinder.getBean();
		System.out.println("username= "+userCredentials.getUserName());
		System.out.println("ctp = "+userCredentials.getPassword());
		System.out.println("random user = "+userCredentialService.findCredentialById(128));
		if(userCredentialService.authenticateUser(userCredentials.getUserName(), userCredentials.getPassword()) == true) {
			System.out.println("User Authenticated.");
		}
		else {
			System.out.println("403");
		}
	}

}
