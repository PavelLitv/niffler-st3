package guru.qa.niffler.api;

import guru.qa.niffler.model.CategoryJson;
import io.qameta.allure.Step;

import java.io.IOException;

public class CategoryServiceClient extends RestService {

    public CategoryServiceClient() {
        super(config.nifflerSpendUrl());
    }

    private final CategoryService categoryService = retrofit.create(CategoryService.class);

    @Step("Create category")
    public CategoryJson addCategory(CategoryJson category) throws IOException {
        return categoryService
                .addCategory(category)
                .execute()
                .body();
    }
}
