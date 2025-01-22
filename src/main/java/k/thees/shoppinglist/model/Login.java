package k.thees.shoppinglist.model;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import k.thees.shoppinglist.repositories.UserRepositoryInterface;
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

	@Autowired
	private UserRepositoryInterface userRepository;

	private String loginName;
	private String password;
	private String message;

	public String validate() {

		Example<User> example = Example.of(User.builder().loginName(loginName).build());

		return userRepository.findOne(example)
				.filter(user -> user.getPassword().equals(password))
				.map(it -> "shopping_list")
				.orElse("login");
	}
}
