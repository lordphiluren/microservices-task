package ru.sushchenko.supplier.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sushchenko.supplier.entity.Category;
import ru.sushchenko.supplier.repo.CategoryRepo;
import ru.sushchenko.supplier.util.exception.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepo categoryRepo;
    @Transactional
    public Category add(Category category) {
        return categoryRepo.save(category);
    }
    @Transactional
    public List<Category> getAll() {
        return categoryRepo.findAll();
    }
    @Transactional
    public Category getById(Long id) {
        return categoryRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Category with id: " + id + " doesn't exist"));
    }
    @Transactional
    public void deleteById(Long id) {
        categoryRepo.deleteById(id);
    }
    @Transactional
    public Category update(Category category) {
        return categoryRepo.save(category);
    }
}
