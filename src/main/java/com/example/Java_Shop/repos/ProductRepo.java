package com.example.Java_Shop.repos;

import com.example.Java_Shop.domain.Product;
import com.sun.xml.bind.v2.model.core.ID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductRepo extends CrudRepository<Product, Integer> {
    List<Product> findByName(String name);
    Product findById(ID id);
}
