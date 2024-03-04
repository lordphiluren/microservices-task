package ru.sushchenko.consumer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.sushchenko.consumer.config.SupplierServiceConfig;
import ru.sushchenko.consumer.dto.ReviewRequest;
import ru.sushchenko.consumer.dto.ReviewResponse;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final RestTemplate restTemplate;
    private final SupplierServiceConfig supplierService;

    public ReviewResponse add(ReviewRequest ReviewRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ReviewRequest> request = new HttpEntity<>(ReviewRequest, headers);

        return restTemplate.postForEntity(
                supplierService.getUrl() + "/reviews",
                request,
                ReviewResponse.class).getBody();
    }
    public void deleteById(Long id) {
        restTemplate.delete(supplierService.getUrl() + "/reviews/{id}", id);
    }
}
