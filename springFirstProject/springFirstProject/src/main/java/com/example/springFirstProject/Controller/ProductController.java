package com.example.springFirstProject.Controller;

import com.example.springFirstProject.DTO.productdto.ProductRequest;
import com.example.springFirstProject.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private final ProductService productService;

    public ProductController (ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<String> createProduct (@RequestBody ProductRequest productRequest) {
        return productService.createProduct(productRequest);
    }

    @GetMapping
    public ResponseEntity<?> getAllProducts () {
        return productService.getAllProducts();
    }

    @GetMapping("/{productId}")
    public ResponseEntity<?> getProductById (@PathVariable Long productId) {
        return productService.getProductById(productId);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<?> updateProduct(@PathVariable Long productId, @RequestBody ProductRequest productRequest) {
        return productService.updateProduct(productId, productRequest);
    }

    @GetMapping("/searchKey")
    public ResponseEntity<?> searchProduct(@RequestParam String searchKey) {
        return productService.searchProduct(searchKey);
    }

    @DeleteMapping("/deleteById/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId) {
        return productService.deleteProduct(productId);
    }
}
