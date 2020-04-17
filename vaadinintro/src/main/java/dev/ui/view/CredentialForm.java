package dev.ui.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.PropertyId;
import com.vaadin.flow.data.converter.StringToIntegerConverter;

import dev.ui.entity.UserCredentials;
import dev.ui.service.UserCredentialService;

public class CredentialForm extends FormLayout{
	
	private UserCredentialService userCredentialService;
	
	private static final long serialVersionUID = 1L;
	@PropertyId(value = "id")
	private TextField textFieldId = new TextField("ID");
	
	@PropertyId(value = "userName")
	private EmailField textFieldUserName = new EmailField("UserName");
	
	@PropertyId(value = "password")
	private PasswordField passwordField = new PasswordField("Password");
	
	@PropertyId(value = "birthDate")
	private DatePicker birthDate = new DatePicker("BirthDate");
	
	private Button saveButton = new Button("Save");
	private Button deleteButton = new Button("Delete");
	private Button resetButton = new Button("Reset");
	
	private Binder<UserCredentials> dataBinder = new Binder<>(UserCredentials.class);
	private MainView mainView;
	
	public CredentialForm(MainView mainView,UserCredentialService userCredentialService) {

		this.mainView = mainView;
		this.userCredentialService=userCredentialService;
		textFieldUserName.setClearButtonVisible(true);
		textFieldUserName.setRequiredIndicatorVisible(true);
		//textFieldId.setValue(" ");
		HorizontalLayout buttons = new HorizontalLayout(saveButton, deleteButton, resetButton);
		VerticalLayout formComponents = new VerticalLayout();
		formComponents.add(textFieldId,textFieldUserName,passwordField,birthDate);
		saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		deleteButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
		resetButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
		
		//this is used to convert the integer Id data to String format 
		//null is used to render the Id value as non-editable ; there is no setter method 
		dataBinder.forField(this.textFieldId).withConverter(new StringToIntegerConverter(Integer.valueOf(0), "Integer Only")).bind(UserCredentials::getId, null);
//		dataBinder.forField(this.textFieldId).withNullRepresentation("");
		
		
		this.add(formComponents,buttons);
//		this.add(textFieldId,textFieldUserName,passwordField,birthDate,buttons);
		dataBinder.bindInstanceFields(this);
		saveButton.addClickListener(event -> saveCredential());
		deleteButton.addClickListener(event -> deleteCredential());
		resetButton.addClickListener(event -> resetCredential());
		
	}
	
	
	public void setCredentials(UserCredentials userCredentials) {
		dataBinder.setBean(userCredentials);
		
		if (userCredentials == null) {
			this.setVisible(false);
		}
		else {

			this.setVisible(true);
			this.textFieldUserName.focus();
		}
	}
	
	private void saveCredential() {
		UserCredentials userCredentials = dataBinder.getBean();
		if(userCredentials == null) {
			System.out.println("Null from form save.");
		}
		if(userCredentials.getId( )== null || userCredentials.getId()==0) {
			System.out.println("creating new");
			userCredentialService.createCredential(userCredentials);
		}
		else {
			System.out.println("new value:"+userCredentials.getId()+" "+userCredentials.getUserName()+" "+userCredentials.getPassword()+" "+userCredentials.getBirthDate());
			userCredentialService.updateCredential(userCredentials);
		}
		this.mainView.updatelist();
		this.setCredentials(null);
	}
	
	private void deleteCredential() {
		UserCredentials userCredentials = dataBinder.getBean();
		if(userCredentials == null) {
			System.out.println("Null from form delete.");
		}   
		else if(userCredentials.getId()== 0) {//delete tried in create new credential form
			System.out.println("Credential is not yet created.");
		}
		else {
			System.out.println(userCredentials.getId());
			System.out.println(userCredentialService.findCredentialById(userCredentials.getId()).getId());
			
			userCredentialService.deleteCredential(userCredentials);
		}
		this.mainView.updatelist();
		this.setCredentials(null);		
	}	
	private void resetCredential() {
		this.setCredentials(null);
	}	
}
