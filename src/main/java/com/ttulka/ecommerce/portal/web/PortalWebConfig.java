package com.ttulka.ecommerce.portal.web;

import java.util.Map;

import com.ttulka.ecommerce.sales.catalog.FindCategories;

import org.springframework.context.annotation.Configuration;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Configuration for Portal Web.
 */
@Configuration
class PortalWebConfig {

    /**
     * Web Layout Advice for Portal.
     * <p>
     * Adds a list of Category items into the layout model.
     */
    @ControllerAdvice(basePackageClasses = PortalWebConfig.class)
    @RequiredArgsConstructor
    class WebLayoutAdvice {

        private final @NonNull FindCategories findCategories;

        @ModelAttribute
        public void decorateWithCategories(Model model) {
            model.addAttribute("categories", findCategories.all().stream()
                    .map(category -> Map.of(
                            "uri", category.uri().value(),
                            "title", category.title().value()))
                    .toArray());
        }
    }
}
