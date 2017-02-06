package controller;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class LoginController extends GenericForwardComposer<Component>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Window winLogin;
	
	Textbox txtNIP;
	Textbox txtPassword;
	
	Button btnLogin;
	Button btnClear;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
	}
	
	
	public void onClick$btnLogin(){
		if (txtNIP.getValue().equalsIgnoreCase("admin") && txtPassword.getValue().equalsIgnoreCase("admin")) {
			Executions.getCurrent().sendRedirect("view/dashboard.zul");
		}else{
			alert("Mohon Maaf nip dan password anda salah");
		}
	}
	
	public void onClick$btnClear(){
		txtNIP.setValue("");
		txtPassword.setValue("");
		txtNIP.setFocus(true);
	}
	
	public void onOK$txtNIP(){
		txtPassword.focus();
	}
	
	public void onOK$txtPassword(){
		btnLogin.focus();
	}
	
}
