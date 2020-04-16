package com.example.Java_Shop.service;

import com.example.Java_Shop.domain.Product;
import com.example.Java_Shop.domain.User;
import com.example.Java_Shop.repos.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ProductService {
    @Autowired
    private ProductRepo productRepo;

    public Iterable<Product> findByName(String name){
        return productRepo.findByName(name);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Iterable<Product> listItems(){
        return productRepo.findAll();
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void addItems(Product product){
        Optional<Product> itemDb = productRepo.findById(product.getId());
        itemDb.ifPresent(value -> product.setAmount(product.getAmount() + value.getAmount()));
        if(product.getAmount() >= 0) {
            productRepo.save(product);
        }
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void byuItems(User user) {

        for (var item : user.basket.entrySet()) {
            Product product = productRepo.findById(item.getKey()).get();
            if(product.getAmount() < item.getValue())
                throw new RuntimeException("Amount is not enough to buy");
            product.setAmount(product.getAmount() - item.getValue());
            productRepo.save(product);
        }
        user.basket.clear();
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public boolean orderItem(Integer id, long amount, User user){
        Optional<Product> itemDb = productRepo.findById(id);
        if(itemDb.isPresent() && itemDb.get().getAmount() >= amount + user.basket.getOrDefault(id, 0L)){
            user.basket.put(id, amount + user.basket.getOrDefault(id, 0L));
            return true;
        }
        return false;
    }






}
