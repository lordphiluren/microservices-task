package ru.sushchenko.supplier.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.sushchenko.supplier.dto.ProductRequest;
import ru.sushchenko.supplier.dto.ProductResponse;
import ru.sushchenko.supplier.entity.Category;
import ru.sushchenko.supplier.entity.Product;
import ru.sushchenko.supplier.repo.CategoryRepo;
import ru.sushchenko.supplier.repo.ProductRepo;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class ProductControllerTest {
    @LocalServerPort
    private Integer port;
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }
    @Autowired
    ProductRepo productRepo;
    @Autowired
    CategoryRepo categoryRepo;

    @BeforeEach
    void setUp() {
        productRepo.deleteAll();
        RestAssured.baseURI = "http://localhost:" + port + "/api/v1/products";
    }

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }
    @Test
    void shouldGetAllCategories() {
        Category category = new Category(1L, "sport", new HashSet<>());
        List<Product> products = List.of(
            new Product(null, "product1", "description1", BigDecimal.valueOf(100), category, new HashSet<>()),
            new Product(null, "product2", "description2", BigDecimal.valueOf(200), category, new HashSet<>())
        );
        productRepo.saveAll(products);

        given()
                .contentType(ContentType.JSON)
                .when()
                .get()
                .then()
                .statusCode(200)
                .body(".", hasSize(2));
    }
    @Test
    void shouldGetById() {
        Category category = new Category(1L, "sport", new HashSet<>());
        Product savedProduct1 = productRepo.save(
                new Product(null, "product1", "description1",
                        BigDecimal.valueOf(100), category, new HashSet<>())
        );
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/" + savedProduct1.getId())
                .then()
                .statusCode(200)
                .body("name", equalTo("product1"));
    }
    @Test
    void shouldDeleteById() {
        Category category = new Category(1L, "sport", new HashSet<>());
        Product savedProduct1 = productRepo.save(
                new Product(null, "product1", "description1",
                        BigDecimal.valueOf(100), category, new HashSet<>())
        );
        Product savedProduct2 = productRepo.save(
                new Product(null, "product2", "description2",
                        BigDecimal.valueOf(100), category, new HashSet<>())
        );

        given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/" + savedProduct1.getId())
                .then()
                .statusCode(200);

        List<Product> remainingProducts = productRepo.findAll();
        assertThat(remainingProducts).hasSize(1);
        assertThat(remainingProducts.get(0).getId()).isEqualTo(savedProduct2.getId());
    }
    @Test
    void shouldUpdateById() {
        Category category = new Category(1L, "sport", new HashSet<>());
        Product savedProduct1 = productRepo.save(
                new Product(null, "product1", "description1",
                        BigDecimal.valueOf(100), category, new HashSet<>())
        );
        ProductRequest productDto = new ProductRequest();
        productDto.setName("updatedName");
        productDto.setCategoryId(category.getId());

        given()
                .contentType(ContentType.JSON)
                .when()
                .body(productDto)
                .put("/" + savedProduct1.getId())
                .then()
                .statusCode(200)
                .body("name", equalTo("updatedName"));
    }
    @Test
    void shouldAddProduct() {
        Category category = categoryRepo.save(new Category(1L, "sport", new HashSet<>()));
        ProductRequest productDto = new ProductRequest("product", "description",
                BigDecimal.valueOf(100), category.getId());

        given()
                .contentType(ContentType.JSON)
                .when()
                .body(productDto)
                .post()
                .then()
                .statusCode(200)
                .body("name", equalTo("product"));

        List<Product> products = productRepo.findAll();
        assertThat(products).hasSize(1);
    }
    @Test
    void shouldThrowExceptionIfProductDoesntExist() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/" + 123123)
                .then()
                .statusCode(404);
    }
    @Test
    void shouldThrowExceptionIfCategoryIdIsNotValid() {
        ProductRequest productDto = new ProductRequest("product", "description",
                BigDecimal.valueOf(100), 123123L);

        given()
                .contentType(ContentType.JSON)
                .when()
                .body(productDto)
                .post()
                .then()
                .statusCode(404);
    }
}
