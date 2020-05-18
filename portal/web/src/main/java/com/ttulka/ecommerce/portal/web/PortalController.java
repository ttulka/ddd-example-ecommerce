package com.ttulka.ecommerce.portal.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

/**
 * Web controller for Portal.
 */
@Controller
@RequestMapping("/")
@RequiredArgsConstructor
class PortalController {

    @GetMapping
    public String index() {
        return "index";
    }
}
