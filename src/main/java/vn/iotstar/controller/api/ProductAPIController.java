package vn.iotstar.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.iotstar.entity.Category;
import vn.iotstar.entity.Product;
import vn.iotstar.model.Response;
import vn.iotstar.service.ICategoryService;
import vn.iotstar.service.IProductService;
import vn.iotstar.service.IStorageService;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/product")
public class ProductAPIController {

    @Autowired
    private IProductService productService;

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private IStorageService storageService;

    // 1. Phân trang và Tìm kiếm
    @GetMapping
    public ResponseEntity<Response> getProducts(
            @RequestParam(name = "name", defaultValue = "") String name,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("productId").descending());
        Page<Product> productPage;

        if (name.trim().isEmpty()) {
            productPage = productService.findAll(pageable);
        } else {
            productPage = productService.findByProductNameContaining(name, pageable);
        }
        return ResponseEntity.ok(new Response(true, "Thành công", productPage));
    }

    // 2. Lấy chi tiết Product
    @GetMapping("/{id}")
    public ResponseEntity<Response> getProductById(@PathVariable("id") Long id) {
        Optional<Product> opt = productService.findById(id);
        if (opt.isPresent()) {
            return ResponseEntity.ok(new Response(true, "Thành công", opt.get()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Response(false, "Không tìm thấy Product", null));
        }
    }

    // 3. Thêm Product
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<Response> addProduct(
            @RequestParam("productName") String productName,
            @RequestParam("quantity") int quantity,
            @RequestParam("unitPrice") double unitPrice,
            @RequestParam(value = "discount", defaultValue = "0") double discount,
            @RequestParam("description") String description,
            @RequestParam("categoryId") Long categoryId,
            @RequestParam(value = "image", required = false) MultipartFile image) {

        Product product = new Product();
        product.setProductName(productName);
        product.setQuantity(quantity);
        product.setUnitPrice(unitPrice);
        product.setDiscount(discount);
        product.setDescription(description);
        product.setCreateDate(new Date());
        product.setStatus((short) 1);

        Optional<Category> categoryOpt = categoryService.findById(categoryId);
        categoryOpt.ifPresent(product::setCategory);

        if (image != null && !image.isEmpty()) {
            String filename = storageService.getSorageFilename(image, UUID.randomUUID().toString());
            storageService.store(image, filename);
            product.setImages(filename);
        }

        productService.save(product);
        return ResponseEntity.ok(new Response(true, "Thêm sản phẩm thành công", product));
    }

    // 4. Cập nhật Product
    @PutMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<Response> updateProduct(
            @RequestParam("productId") Long productId,
            @RequestParam("productName") String productName,
            @RequestParam("quantity") int quantity,
            @RequestParam("unitPrice") double unitPrice,
            @RequestParam(value = "discount", defaultValue = "0") double discount,
            @RequestParam("description") String description,
            @RequestParam("categoryId") Long categoryId,
            @RequestParam(value = "image", required = false) MultipartFile image) {

        Optional<Product> opt = productService.findById(productId);
        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Response(false, "Không tìm thấy sản phẩm", null));
        }

        Product product = opt.get();
        product.setProductName(productName);
        product.setQuantity(quantity);
        product.setUnitPrice(unitPrice);
        product.setDiscount(discount);
        product.setDescription(description);

        Optional<Category> categoryOpt = categoryService.findById(categoryId);
        categoryOpt.ifPresent(product::setCategory);

        if (image != null && !image.isEmpty()) {
            String filename = storageService.getSorageFilename(image, UUID.randomUUID().toString());
            storageService.store(image, filename);
            product.setImages(filename);
        }

        productService.save(product);
        return ResponseEntity.ok(new Response(true, "Cập nhật sản phẩm thành công", product));
    }

    // 5. Xóa Product
    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteProduct(@PathVariable("id") Long id) {
        Optional<Product> opt = productService.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Response(false, "Không tìm thấy sản phẩm", null));
        }
        productService.deleteById(id);
        return ResponseEntity.ok(new Response(true, "Xóa sản phẩm thành công", null));
    }
}