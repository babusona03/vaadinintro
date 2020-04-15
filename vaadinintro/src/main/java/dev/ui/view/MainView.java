package dev.ui.view;
import javax.annotation.PostConstruct;
import javax.inject.Inject;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;

import dev.ui.entity.UserCredentials;
import dev.ui.service.UserCredentialService;



@Route("uivdn")
@PWA(name="simple CRUD operation",shortName = "CRUD")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")

public class MainView extends VerticalLayout{	
	
	@Inject	
	private UserCredentialService userCredentialService ;
	
	private static final long serialVersionUID = 1L;
//	private TextField textField;
//	private PasswordField passwordField;
//	private DatePicker datePicker;
//	private Button button;
	private Grid<UserCredentials> dataGrid = new Grid<>(UserCredentials.class,false);//the boolean value enables or disables automatic column addition according to the properties of the object
	
	private TextField textField =  new TextField("Search:");
	
	private Button createNew = new Button("Create new Credential");
	
	private CredentialForm credentialForm ;//= new CredentialForm(this);
	private HorizontalLayout mainContent ;//= new HorizontalLayout(dataGrid,credentialForm);
	private HorizontalLayout toolbar;
	
	/*
	 * The new Grid<>(Customer.class); constructor automatically adds the columns. 
	 * If you want to add columns manually, use the new Grid<>() constructor which doesn't add any columns.
	 * There are two differently behaving constructors in Grid.
	 * The first one takes a class parameter: new Grid<Customer>(Customer.class);. 
	 * This one will use Java reflection and scan the Customer.class class object to create columns for each of the available properties automatically.
	 *  You can still add and remove columns after this.
	 *  The second constructor takes no parameters: new Grid<Customer>();. 
	 *  Using this constructor gives you no columns by default, so you need to add them to see any data in the Grid. 
	 *  This is where the various flavors of grid.addColumn() come in.
	 *  You can avoid creating the auto-generated columns using the Grid(Class, boolean) constructor.


	 * */
	public MainView() {
//		textField = new TextField("Enter UserName:");
//		passwordField = new PasswordField("Enter Password:");
//		datePicker = new DatePicker("Enter Birthdate:");
//		button = new Button("Submit");
//		dataGrid = new Grid<>(UserCredentials.class);
		//dataGrid.setColumns("id","createdOn","updatedon","userName","password","birthdate");
//		dataGrid.removeColumn(null);
		
		//textField.setValue(" ");
		
		dataGrid.addColumn(UserCredentials::getId).setHeader("ID").setSortable(true);//user can sort the grid values based on this column
		dataGrid.addColumn(UserCredentials::getUserName).setHeader("USERNAME");
		dataGrid.addColumn(UserCredentials::getPassword).setHeader("PASSWORD");
		dataGrid.addColumn(UserCredentials::getBirthDate).setHeader("BIRTHDATE");
		dataGrid.addColumn(UserCredentials::getCreatedOn).setHeader("CREATED");
		dataGrid.addColumn(UserCredentials::getUpdatedOn).setHeader("UPDATED");
		
		
		
		textField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
		textField.setPlaceholder("Enter text to filter:");
		textField.setClearButtonVisible(true);
		textField.setValueChangeMode(ValueChangeMode.EAGER);
		textField.addValueChangeListener(e -> updatelist());
//		passwordField.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
//		button.addThemeVariants((ButtonVariant.LUMO_LARGE));
//		this.addClassName("centered-content");
//		this.mainContent.setSizeFull();
		this.dataGrid.setSizeFull();
		//this.dataGrid.setVerticalScrollingEnabled(true);
		
//		this.add(textField,mainContent);
//		this.setSizeFull();
//		this.credentialForm.setCredentials(null);
//		this.dataGrid.asSingleSelect().addValueChangeListener(event -> this.credentialForm.setCredentials(this.dataGrid.asSingleSelect().getValue()));
		
		//this.updatelist();
		
//		this.add(textField,passwordField,datePicker,button);
		
	}
	@PostConstruct
	public void triggerLoadData() {
		
		credentialForm = new CredentialForm(this,this.userCredentialService);
//		credentialForm = new CredentialForm(this);
		mainContent = new HorizontalLayout(dataGrid,credentialForm);
		createNew.addClickListener(event -> {
			dataGrid.asSingleSelect().clear();
			UserCredentials userCredentials = new UserCredentials();
			userCredentials.setId(0);
			//the erstwhile representation was credentialForm.setCredentials(new UserCredentials());			
			//this was creating null value for the id
			//the ID was going for the textFieldId; a TextField; 
			//TextField is not set to accept null unless explicitly set to do so
			//this was throwing illegalstateexception
			//this bypasses the null scenario with a dummy zero
			
			credentialForm.setCredentials(userCredentials);
		});
		toolbar= new HorizontalLayout(textField,createNew);
//		toolbar= new HorizontalLayout(textField);
		this.mainContent.setSizeFull();
		this.add(toolbar,mainContent);
		this.setSizeFull();
		this.credentialForm.setCredentials(null);//this effectively hides the credential form
		
		this.dataGrid.asSingleSelect().addValueChangeListener(event -> this.credentialForm.setCredentials(this.dataGrid.asSingleSelect().getValue()));
		this.updatelist();
	}
	public void updatelist() {
		dataGrid.setItems(userCredentialService.getAllCredentials(textField.getValue()));
	}
	

}

