package com.example.javaShop.repos;

import com.example.javaShop.domain.Product;
import com.sun.xml.bind.v2.model.core.ID;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductRepo extends CrudRepository<Product, Integer> {
    List<Product> findByName(String name);
    Product findById(ID id);
}
