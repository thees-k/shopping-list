package k.thees.shoppinglist.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import k.thees.shoppinglist.model.User;



public interface UserRepositoryInterface extends JpaRepository<User, Integer>, QueryByExampleExecutor<User> {
	//
}
