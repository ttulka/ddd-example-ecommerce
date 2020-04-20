package com.ttulka.ecommerce;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import com.ttulka.ecommerce.common.events.DomainEvent;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static com.tngtech.archunit.core.domain.JavaClass.Predicates.NESTED_CLASSES;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.assignableTo;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAPackage;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAnyPackage;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideOutsideOfPackages;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

class CleanModulesArchTest {

    @Test
    void sales_service_has_no_dependency_on_other_services() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("com.ttulka.ecommerce.sales");
        ArchRule rule = classes()
                .should().onlyDependOnClassesThat().resideOutsideOfPackages(
                        "com.ttulka.ecommerce.warehouse..",
                        "com.ttulka.ecommerce.shipping..",
                        "com.ttulka.ecommerce.billing..");
        rule.check(importedClasses);
    }

    @Test
    void sales_domain_has_no_dependency_to_its_implementation() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("com.ttulka.ecommerce.sales");
        ArchRule rule = classes()
                .that().resideOutsideOfPackages(
                        "com.ttulka.ecommerce.sales.category.jdbc..",
                        "com.ttulka.ecommerce.sales.product.jdbc..",
                        "com.ttulka.ecommerce.sales.order.jdbc..")
                .should().onlyDependOnClassesThat().resideOutsideOfPackages(
                        "com.ttulka.ecommerce.sales.category.jdbc..",
                        "com.ttulka.ecommerce.sales.product.jdbc..",
                        "com.ttulka.ecommerce.sales.order.jdbc..");
        rule.check(importedClasses);
    }

    @Test
    void warehouse_has_no_dependencies_on_others_except_events() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("com.ttulka.ecommerce.warehouse");
        ArchRule rule = classes().that().haveSimpleNameNotEndingWith("Test")
                .should().onlyDependOnClassesThat(
                        resideOutsideOfPackages(
                                "com.ttulka.ecommerce.."
                        ).or(resideInAnyPackage("com.ttulka.ecommerce.warehouse..", "com.ttulka.ecommerce.common..")
                        ).or(assignableTo(DomainEvent.class).or(NESTED_CLASSES)));
        rule.check(importedClasses);
    }

    @Test
    void warehouse_domain_has_no_dependency_to_its_implementation() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("com.ttulka.ecommerce.warehouse");
        ArchRule rule = classes()
                .that().resideOutsideOfPackages(
                        "com.ttulka.ecommerce.warehouse.jdbc..")
                .should().onlyDependOnClassesThat().resideOutsideOfPackages(
                        "com.ttulka.ecommerce.warehouse.jdbc..");
        rule.check(importedClasses);
    }

    @Test
    void shipping_has_no_dependencies_on_others_except_events() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("com.ttulka.ecommerce.shipping");
        ArchRule rule = classes().that().haveSimpleNameNotEndingWith("Test")
                .should().onlyDependOnClassesThat(
                        resideOutsideOfPackages(
                                "com.ttulka.ecommerce.."
                        ).or(resideInAnyPackage("com.ttulka.ecommerce.shipping..", "com.ttulka.ecommerce.common..")
                        ).or(assignableTo(DomainEvent.class).or(NESTED_CLASSES)));
        rule.check(importedClasses);
    }

    @Test
    void shipping_only_listeners_have_dependencies_on_other_services_events() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("com.ttulka.ecommerce.shipping");
        ArchRule rule = classes().that()
                .resideOutsideOfPackage("com.ttulka.ecommerce.shipping.listeners..")
                .and().haveSimpleNameNotEndingWith("Test")
                .should().onlyDependOnClassesThat(
                        resideOutsideOfPackages(
                                "com.ttulka.ecommerce.."
                        ).or(resideInAnyPackage("com.ttulka.ecommerce.shipping..", "com.ttulka.ecommerce.common..")));
        rule.check(importedClasses);
    }

    @Test
    void shipping_domain_has_no_dependency_to_its_implementation() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("com.ttulka.ecommerce.shipping");
        ArchRule rule = classes()
                .that().resideOutsideOfPackages(
                        "com.ttulka.ecommerce.shipping.delivery.jdbc..")
                .should().onlyDependOnClassesThat().resideOutsideOfPackages(
                        "com.ttulka.ecommerce.shipping.delivery.jdbc..");
        rule.check(importedClasses);
    }

    @Test
    void billing_has_no_dependencies_on_others_except_events() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("com.ttulka.ecommerce.billing");
        ArchRule rule = classes().that().haveSimpleNameNotEndingWith("Test")
                .should().onlyDependOnClassesThat(
                        resideOutsideOfPackages(
                                "com.ttulka.ecommerce.."
                        ).or(resideInAnyPackage("com.ttulka.ecommerce.billing..", "com.ttulka.ecommerce.common..")
                        ).or(assignableTo(DomainEvent.class).or(NESTED_CLASSES)));
        rule.check(importedClasses);
    }

    @Test
    void billing_only_listeners_have_dependencies_on_other_services_events() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("com.ttulka.ecommerce.billing");
        ArchRule rule = classes().that()
                .resideOutsideOfPackage("com.ttulka.ecommerce.billing.listeners..")
                .and().haveSimpleNameNotEndingWith("Test")
                .should().onlyDependOnClassesThat(
                        resideOutsideOfPackages(
                                "com.ttulka.ecommerce.."
                        ).or(resideInAnyPackage("com.ttulka.ecommerce.billing..", "com.ttulka.ecommerce.common..")));
        rule.check(importedClasses);
    }

    @Test
    void billing_domain_has_no_dependency_to_its_implementation() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("com.ttulka.ecommerce.billing");
        ArchRule rule = classes()
                .that().resideOutsideOfPackages(
                        "com.ttulka.ecommerce.billing.payment.jdbc..")
                .should().onlyDependOnClassesThat().resideOutsideOfPackages(
                        "com.ttulka.ecommerce.billing.payment.jdbc..");
        rule.check(importedClasses);
    }

    @Test
    void no_service_has_dependencies_on_catalogue() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("com.ttulka.ecommerce");
        ArchRule rule = classes()
                .that().resideOutsideOfPackages(
                        "com.ttulka.ecommerce.catalogue..")
                .and().areNotAnnotatedWith(WebMvcTest.class)
                .should().onlyDependOnClassesThat().resideOutsideOfPackages(
                        "com.ttulka.ecommerce.catalogue..");
        rule.check(importedClasses);
    }

    @Test
    void catalogue_service_has_no_dependencies_on_billing() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("com.ttulka.ecommerce.catalogue");
        ArchRule rule = classes()
                .should().onlyDependOnClassesThat().resideOutsideOfPackages(
                        "com.ttulka.ecommerce.billing..");
        rule.check(importedClasses);
    }

    @Test
    void catalogue_web_uses_only_its_own_use_cases_and_no_direct_dependencies_on_other_services() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages(
                "com.ttulka.ecommerce.sales",
                "com.ttulka.ecommerce.warehouse",
                "com.ttulka.ecommerce.shipping.",
                "com.ttulka.ecommerce.billing");
        ArchRule rule = classes()
                .should().onlyHaveDependentClassesThat().resideOutsideOfPackage(
                        "com.ttulka.ecommerce.catalogue.web..");
        rule.check(importedClasses);
    }

    @Test
    void shopping_cart_has_no_dependency_on_others() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages(
                "com.ttulka.ecommerce.catalogue.cart");
        ArchRule rule = classes().should().onlyDependOnClassesThat(
                resideOutsideOfPackages(
                        "com.ttulka.ecommerce.."
                ).or(resideInAPackage("com.ttulka.ecommerce.catalogue.cart..")));
        rule.check(importedClasses);
    }
}
