package com.example.Java_Shop.controller;

import com.example.Java_Shop.domain.Product;
import com.example.Java_Shop.repos.ProductRepo;
import com.example.Java_Shop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.spel.ast.OpGE;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class ProductController {
    @Autowired
    private ProductService productService;


    @GetMapping("/")
    public String main(Model model){
        Iterable<Product> products = productService.listItems();
        model.addAttribute("products", products);
        return "main";
    }

    @PostMapping("add")
    public String addProduct(@RequestParam(required = false, defaultValue = "-1") Integer id, @RequestParam String name,
                             @RequestParam Integer amount, Model model){
        Product product = new Product(name, amount);
        product.setId(id);
        productService.addItems(product);
        model.addAttribute("products", productService.listItems());
        return "main";
    }

    @PostMapping("filter")
    public String filterByName(@RequestParam String name, Model model){
        Iterable<Product> products;
        if(name != null && !name.isEmpty()){
            products = productService.findByName(name);
        }
        else{
            products = productService.listItems();
        }
        model.addAttribute("products", products);
        return "main";
    }

    @PostMapping("order")
    public String orderProduct(@RequestParam Integer id, @RequestParam long amount, Model model){
        if(productService.orderItem(id, amount)){
            model.addAttribute("message", "Product was added to your basket");
        }
        else{
            model.addAttribute("message", "Error, please try again later");
        }
        model.addAttribute("products", productService.listItems());
        return "main";
    }

    @GetMapping("/basket")
    public String basket(Model model){
        model.addAttribute("products", productService.getOrderedItems().entrySet());
        return "basket";
    }

    @PostMapping("/basket")
    public String buyProducts(Model model){
        try {
            productService.byuItems();
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("products", productService.getOrderedItems().entrySet());
        return "basket";
    }

    @PostMapping("/basket/clear")
    public String clearBasket(Model model){
        productService.clearBasket();
        model.addAttribute("products", productService.getOrderedItems().entrySet());
        return "basket";
    }
}