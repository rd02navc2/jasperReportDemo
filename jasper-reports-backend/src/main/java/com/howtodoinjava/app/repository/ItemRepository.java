package com.howtodoinjava.app.repository;

import com.howtodoinjava.app.model.Item;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource(path = "items")
public interface ItemRepository extends ListCrudRepository<Item, Long> {

}