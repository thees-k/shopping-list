package k.thees.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import k.thees.model.Item;


public interface ItemRepositoryInterface extends JpaRepository<Item, Integer> {
	//
}
