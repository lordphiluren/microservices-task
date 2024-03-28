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
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.containers.PostgreSQLContainer;
import ru.sushchenko.supplier.dto.CategoryRequest;
import ru.sushchenko.supplier.entity.Category;
import ru.sushchenko.supplier.repo.CategoryRepo;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

import java.util.HashSet;
import java.util.List;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class CategoryControllerTest {
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
    CategoryRepo categoryRepo;

    @BeforeEach
    void setUp() {
        categoryRepo.deleteAll();
        RestAssured.baseURI = "http://localhost:" + port + "/api/v1/categories";
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
        List<Category> categories = List.of(
                new Category(null, "sport", new HashSet<>()),
                new Category(null, "leisure", new HashSet<>())
        );
        categoryRepo.saveAll(categories);

        given()
                .contentType(ContentType.JSON)
                .when()
                .get()
                .then()
                .statusCode(200)
                .body(".", hasSize(2));
    }
    @Test
    void shouldDeleteCategoryById() {
        Category savedCategory1 = categoryRepo.save(new Category(null, "sport", new HashSet<>()));
        Category savedCategory2 = categoryRepo.save(new Category(null, "leisure", new HashSet<>()));

        given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/" + savedCategory1.getId())
                .then()
                .statusCode(200);

        List<Category> remainingCategories = categoryRepo.findAll();

        assertThat(remainingCategories).hasSize(1);
        assertThat(remainingCategories.get(0).getId()).isEqualTo(savedCategory2.getId());
    }
    @Test
    void shouldGetCategoryById() {
        Category category = new Category(null, "sport", new HashSet<>());
        Category savedCategory = categoryRepo.save(category);

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/" + savedCategory.getId())
                .then()
                .statusCode(200)
                .body("name", equalTo("sport"));
    }
    @Test
    void shouldThrowExceptionWhenCategoryDoesntExist() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/" + 123123)
                .then()
                .statusCode(404);
    }
    @Test
    void shouldAddCategory() {
        CategoryRequest categoryDto = new CategoryRequest("sport");

        given()
                .contentType(ContentType.JSON)
                .body(categoryDto)
                .when()
                .post()
                .then()
                .statusCode(200)
                .body("name", equalTo("sport"));

        List<Category> categories = categoryRepo.findAll();
        assertThat(categories).hasSize(1);
    }
    @Test
    void shouldUpdateCategoryById() {
        Category category = new Category(null, "sport", new HashSet<>());
        Category savedCategory = categoryRepo.save(category);

        CategoryRequest categoryDto = new CategoryRequest("updatedName");

        given()
                .contentType(ContentType.JSON)
                .body(categoryDto)
                .when()
                .put("/"+savedCategory.getId())
                .then()
                .statusCode(200)
                .body("name", equalTo("updatedName"));
    }
}
