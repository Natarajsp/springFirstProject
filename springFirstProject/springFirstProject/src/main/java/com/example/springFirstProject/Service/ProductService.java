package com.example.springFirstProject.Service;

import com.example.springFirstProject.Beans.Product;
import com.example.springFirstProject.DTO.productdto.ProductRequest;
import com.example.springFirstProject.DTO.productdto.ProductResponse;
import com.example.springFirstProject.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private final ProductRepository productRepository;

    public ProductService (ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ResponseEntity<String> createProduct(ProductRequest productRequest) {
        Product product = new Product();
        updateProductFromRequest(product, productRequest);
        Product addedProductToDB = productRepository.save(product);
        if (productRepository.existsById(addedProductToDB.getProductId())) {
            return new ResponseEntity<>("Product Added Successfully.", HttpStatusCode.valueOf(201));
        } else {
            return new ResponseEntity<>("Something Went Wrong while Adding New Product In to the Data Base!!!", HttpStatusCode.valueOf(403));
        }
    }

    public ResponseEntity<?> getAllProducts() {
        List<ProductResponse> productList =
                 productRepository.findByActiveTrue()
                        .stream()
                .map(this::mapToProductResponse).toList();
        if (productList.isEmpty()) {
            return ResponseEntity.status(HttpStatusCode.valueOf(404)).body("Products List Out Of Stock!!! Please try again later...");
        } else {
            return new ResponseEntity<>(productList, HttpStatusCode.valueOf(200));
        }
    }

    public ResponseEntity<?> updateProduct(Long productId, ProductRequest productRequest) {
        Optional<ProductResponse> productResponse = productRepository.findById(productId)
                .map(existingProduct -> {
                    updateProductFromRequest(existingProduct, productRequest);
                    Product updatedProduct = productRepository.save(existingProduct);
                    return mapToProductResponse(updatedProduct);
                });
        return productResponse.isEmpty() ? new ResponseEntity<>("Product is Not Updated!!! and its Product ID: " + productId, HttpStatusCode.valueOf(404))
                : new ResponseEntity<>(productResponse.get(), HttpStatusCode.valueOf(200));
    }

    public ResponseEntity<?> getProductById(Long productId) {
        Optional<ProductResponse> productResponse = productRepository.findByproductIdAndActiveTrue(productId)
                .map(this::mapToProductResponse);
        return productResponse.isEmpty() ? new ResponseEntity<>("Product Not Found!!! for the Product ID: " + productId, HttpStatusCode.valueOf(404))
                : new ResponseEntity<>(productResponse.get(), HttpStatusCode.valueOf(200));
    }

    public ResponseEntity<?> searchProduct(String searchKey) {
        List<ProductResponse> foundProducts = productRepository.findProductBySearchKey(searchKey)
                .stream()
                .map(this::mapToProductResponse)
                .toList();
        if (foundProducts.isEmpty()) {
            return new ResponseEntity<>("Product Not Found with the SearchKey : " + searchKey, HttpStatusCode.valueOf(404));
        } else {
            return new ResponseEntity<>(foundProducts, HttpStatusCode.valueOf(200));
        }
    }

    public ResponseEntity<String> deleteProduct(Long productId) {
        Optional<Product> existingProduct = productRepository.findByproductIdAndActiveTrue(productId)
                .map(product -> {
                    product.setActive(false);
                    return productRepository.save(product);
                });
        if (existingProduct.isEmpty()) {
            return ResponseEntity.status(HttpStatusCode.valueOf(404)).body("Product Not Deleted, because It's Not Found!!!");
        } else {
            return ResponseEntity.status(HttpStatusCode.valueOf(200)).body("Product with Product ID : " + productId + " is Deleted Successfully.");
        }
    }

    private void updateProductFromRequest(Product product, ProductRequest productRequest) {
        product.setProductName(productRequest.getProductName());
        product.setProductDescription(productRequest.getProductDescription());
        product.setProductPrice(productRequest.getProductPrice());
        product.setProductStockQuantity(productRequest.getProductStockQuantity());
        product.setProductCategory(productRequest.getProductCategory());
        product.setProductURL(productRequest.getProductURL());
        product.setActive(productRequest.getActive());
    }

    private ProductResponse mapToProductResponse (Product product) {
        ProductResponse productResponse = new ProductResponse ();
        productResponse.setProductName(product.getProductName());
        productResponse.setProductDescription(product.getProductDescription());
        productResponse.setProductPrice(product.getProductPrice());
        productResponse.setProductStockQuantity(product.getProductStockQuantity());
        productResponse.setProductCategory(product.getProductCategory());
        productResponse.setProductURL(product.getProductURL());
        productResponse.setActive(product.getActive());
        return productResponse;
    }
}
