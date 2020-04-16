package com.example.Java_Shop.service;

import com.example.Java_Shop.domain.OrderedItem;
import com.example.Java_Shop.domain.Product;
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

    private Map<Integer, Long> orderedItems = new HashMap<>();

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

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void byuItems() throws Exception {

        for (var item : orderedItems.entrySet()) {
            Product product = productRepo.findById(item.getKey()).get();
            if(product.getAmount() < item.getValue())
                throw new RuntimeException("Amount is not enough to buy");
            product.setAmount(product.getAmount() - item.getValue());
            productRepo.save(product);
        }
        orderedItems.clear();
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public boolean orderItem(Integer id, long amount){
        Optional<Product> itemDb = productRepo.findById(id);
        if(itemDb.isPresent() && itemDb.get().getAmount() >= amount + orderedItems.getOrDefault(id, 0L)){
            orderedItems.put(id, amount + orderedItems.getOrDefault(id, 0L));
            return true;
        }
        return false;
    }

    public Map<Integer, Long> getOrderedItems() {
        return orderedItems;
    }

    public void clearBasket(){
        orderedItems.clear();
    }
}
