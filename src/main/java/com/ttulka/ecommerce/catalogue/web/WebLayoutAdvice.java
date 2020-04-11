package com.ttulka.ecommerce.catalogue.web;

import com.ttulka.ecommerce.catalogue.Catalogue;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Web Layout Advice for Catalogue.
 * <p>
 * Adds a list of Category items into the layout model.
 */
@ControllerAdvice(basePackageClasses = WebLayoutAdvice.class)
@RequiredArgsConstructor
class WebLayoutAdvice {

    private final @NonNull Catalogue catalogue;

    @ModelAttribute
    public void decorateWithCategories(Model model) {
        model.addAttribute("categories", catalogue.categories());
    }
}
