package k.thees.shoppinglist.model;

import java.io.Serializable;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@SessionScoped
@Named
public class Login implements Serializable {

	private static final long serialVersionUID = 1950119103738161497L;

	private String loginName;
	private String password;
	private String message;

	public String validate() {

		if ("test".equalsIgnoreCase(loginName) && "test".equalsIgnoreCase(password)) {
			System.out.println("########### validate OK");
			return "shopping_list";
		} else {
			return "login";
		}
	}

}
