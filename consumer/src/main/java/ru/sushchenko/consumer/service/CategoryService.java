package ru.sushchenko.consumer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.sushchenko.consumer.config.SupplierServiceConfig;
import ru.sushchenko.consumer.dto.CategoryRequest;
import ru.sushchenko.consumer.dto.CategoryResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final RestTemplate restTemplate;
    private final SupplierServiceConfig supplierService;
    public List<CategoryResponse> getAll() {
        ResponseEntity<CategoryResponse[]> responseEntity = restTemplate.getForEntity(
                supplierService.getUrl() + "/categories",
                CategoryResponse[].class);
        return List.of(responseEntity.getBody());
    }
    public CategoryResponse getById(Long id) {
        return restTemplate.getForObject(supplierService.getUrl() + "/categories/{id}",
                CategoryResponse.class,
                id);
    }
    public CategoryResponse add(CategoryRequest categoryRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CategoryRequest> request = new HttpEntity<>(categoryRequest, headers);

        return restTemplate.postForEntity(
                supplierService.getUrl() + "/categories",
                request,
                CategoryResponse.class).getBody();
    }
    public void deleteById(Long id) {
        restTemplate.delete(supplierService.getUrl() + "/categories/{id}", id);
    }
    public CategoryResponse update(Long id, CategoryRequest categoryRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CategoryRequest> request = new HttpEntity<>(categoryRequest, headers);

        ResponseEntity<CategoryResponse> responseEntity = restTemplate.exchange(
                supplierService.getUrl() + "/categories/{id}",
                HttpMethod.PUT,
                request,
                CategoryResponse.class,
                id);
        return responseEntity.getBody();
    }
}
