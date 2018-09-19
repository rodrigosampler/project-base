package br.com.rodrigosampler.model;

import org.json.*;

public class UsuarioFacebook {

	private Long id; 
	private String firstName; 
	private Integer timezone; 
	private String email; 
	private Boolean verified;	
	private String middleName;	
	private String gender;	
	private String lastName;
	private String link;
	private String locale;	
	private String name;	
	private String updatedTime;
	
	public UsuarioFacebook(JSONObject jsonUsuario){
		
		try {
			id = jsonUsuario.getLong("id");
			firstName = jsonUsuario.getString("first_name");
			timezone = jsonUsuario.getInt("timezone");
			email = jsonUsuario.getString("email");
			verified = jsonUsuario.getBoolean("verified");
			middleName = jsonUsuario.getString("middle_name");
			gender = jsonUsuario.getString("gender");
			lastName = jsonUsuario.getString("last_name");
			link = jsonUsuario.getString("link");
			locale = jsonUsuario.getString("locale");
			name = jsonUsuario.getString("name");
			updatedTime = jsonUsuario.getString("updated_time");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "UsuarioFacebook [id=" + id + ", firstName=" + firstName
				+ ", timezone=" + timezone + ", email=" + email + ", verified="
				+ verified + ", middleName=" + middleName + ", gender="
				+ gender + ", lastName=" + lastName + ", link=" + link
				+ ", locale=" + locale + ", name=" + name + ", updatedTime="
				+ updatedTime + "]";
	}
	
}