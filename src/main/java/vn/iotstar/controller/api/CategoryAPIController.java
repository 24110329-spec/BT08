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
import vn.iotstar.model.Response;
import vn.iotstar.service.ICategoryService;
import vn.iotstar.service.IStorageService;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/category")
public class CategoryAPIController {

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private IStorageService storageService;

    // 1. Lấy tất cả danh mục (không phân trang)
    @GetMapping("/all")
    public ResponseEntity<Response> getAllCategories() {
        return ResponseEntity.ok(new Response(true, "Thành công", categoryService.findAll()));
    }

    // 2. Tìm kiếm + Phân trang
    @GetMapping
    public ResponseEntity<Response> getCategoriesWithPagination(
            @RequestParam(name = "name", defaultValue = "") String name,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("categoryId").descending());
        Page<Category> resultPage;

        if (name.trim().isEmpty()) {
            resultPage = categoryService.findAll(pageable);
        } else {
            resultPage = categoryService.findByCategoryNameContaining(name, pageable);
        }
        return ResponseEntity.ok(new Response(true, "Lấy danh sách thành công", resultPage));
    }

    // 3. Lấy chi tiết theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Response> getCategoryById(@PathVariable("id") Long id) {
        Optional<Category> opt = categoryService.findById(id);
        if (opt.isPresent()) {
            return ResponseEntity.ok(new Response(true, "Thành công", opt.get()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Response(false, "Không tìm thấy Category", null));
        }
    }

    // 4. Thêm Category mới (Upload icon)
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<Response> addCategory(
            @RequestParam("categoryName") String categoryName,
            @RequestParam(value = "icon", required = false) MultipartFile icon) {

        Category category = new Category();
        category.setCategoryName(categoryName);

        if (icon != null && !icon.isEmpty()) {
            String filename = storageService.getSorageFilename(icon, UUID.randomUUID().toString());
            storageService.store(icon, filename);
            category.setIcon(filename);
        }
        categoryService.save(category);
        return ResponseEntity.ok(new Response(true, "Thêm Category thành công", category));
    }

    // 5. Cập nhật Category
    @PutMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<Response> updateCategory(
            @RequestParam("categoryId") Long categoryId,
            @RequestParam("categoryName") String categoryName,
            @RequestParam(value = "icon", required = false) MultipartFile icon) {

        Optional<Category> opt = categoryService.findById(categoryId);
        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Response(false, "Không tìm thấy Category cần sửa", null));
        }

        Category category = opt.get();
        category.setCategoryName(categoryName);

        if (icon != null && !icon.isEmpty()) {
            String filename = storageService.getSorageFilename(icon, UUID.randomUUID().toString());
            storageService.store(icon, filename);
            category.setIcon(filename);
        }
        categoryService.save(category);
        return ResponseEntity.ok(new Response(true, "Cập nhật thành công", category));
    }

    // 6. Xóa Category
    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteCategory(@PathVariable("id") Long id) {
        Optional<Category> opt = categoryService.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Response(false, "Không tìm thấy Category", null));
        }
        categoryService.deleteById(id);
        return ResponseEntity.ok(new Response(true, "Xóa Category thành công", null));
    }
}