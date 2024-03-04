package ru.sushchenko.consumer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.sushchenko.consumer.config.SupplierServiceConfig;
import ru.sushchenko.consumer.dto.ProductRequest;
import ru.sushchenko.consumer.dto.ProductResponse;

import java.math.BigDecimal;
import java.util.List;
@Service
@RequiredArgsConstructor
public class ProductService {
    private final RestTemplate restTemplate;
    private final SupplierServiceConfig supplierService;
    public List<ProductResponse> getAll(Long categoryId, String search, BigDecimal minPrice,
                                        BigDecimal maxPrice, Integer offset, Integer limit) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(supplierService.getUrl() + "/products")
                .queryParam("category", categoryId)
                .queryParam("search", search)
                .queryParam("min_price", minPrice)
                .queryParam("max_price", maxPrice)
                .queryParam("offset", offset)
                .queryParam("limit", limit);

        ResponseEntity<ProductResponse[]> responseEntity = restTemplate.getForEntity(
                builder.toUriString(),
                ProductResponse[].class);
        return List.of(responseEntity.getBody());
    }
    public ProductResponse getById(Long id) {
        return restTemplate.getForObject(supplierService.getUrl() + "/products/{id}",
                ProductResponse.class,
                id);
    }
    public ProductResponse add(ProductRequest ProductRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ProductRequest> request = new HttpEntity<>(ProductRequest, headers);

        return restTemplate.postForEntity(
                supplierService.getUrl() + "/products",
                request,
                ProductResponse.class).getBody();
    }
    public void deleteById(Long id) {
        restTemplate.delete(supplierService.getUrl() + "/products/{id}", id);
    }
    public ProductResponse update(Long id, ProductRequest ProductRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ProductRequest> request = new HttpEntity<>(ProductRequest, headers);

        ResponseEntity<ProductResponse> responseEntity = restTemplate.exchange(
                supplierService.getUrl() + "/products/{id}",
                HttpMethod.PUT,
                request,
                ProductResponse.class,
                id);
        return responseEntity.getBody();
    }
}
