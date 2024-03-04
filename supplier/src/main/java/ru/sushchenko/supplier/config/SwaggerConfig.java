package ru.sushchenko.supplier.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Supplier service",
                description = "Supplier service API. First task Open Java School", version = "1.0.0",
                contact = @Contact(
                        name = "Sushchenko Artyom",
                        email = "artoymsushchenko@gmail.com",
                        url = "https://github.com/lordphiluren"
                )
        )
)
public class SwaggerConfig {
}
