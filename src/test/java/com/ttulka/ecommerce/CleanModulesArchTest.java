package com.ttulka.ecommerce;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import com.ttulka.ecommerce.billing.PaymentCollected;
import com.ttulka.ecommerce.sales.OrderPlaced;
import com.ttulka.ecommerce.shipping.DeliveryDispatched;
import com.ttulka.ecommerce.warehouse.GoodsFetched;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static com.tngtech.archunit.core.domain.JavaClass.Predicates.NESTED_CLASSES;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideOutsideOfPackages;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.type;
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
    void sales_product_and_category_have_no_dependency_on_order() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages(
                "com.ttulka.ecommerce.sales.product",
                "com.ttulka.ecommerce.sales.category");
        ArchRule rule = classes()
                .should().onlyDependOnClassesThat().resideOutsideOfPackages(
                        "com.ttulka.ecommerce.sales.order..");
        rule.check(importedClasses);
    }

    @Test
    void warehouse_has_no_dependencies_on_others_except_events() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("com.ttulka.ecommerce.warehouse");
        ArchRule rule = classes()
                .should().onlyDependOnClassesThat(
                        resideOutsideOfPackages(
                                "com.ttulka.ecommerce.sales..",
                                "com.ttulka.ecommerce.shipping..",
                                "com.ttulka.ecommerce.billing.."
                        ).or(type(OrderPlaced.class).or(NESTED_CLASSES)
                        ).or(type(DeliveryDispatched.class).or(NESTED_CLASSES)));
        rule.check(importedClasses);
    }

    @Test
    void warehouse_only_listeners_have_dependencies_on_other_services_events() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("com.ttulka.ecommerce.warehouse");
        ArchRule rule = classes().that().resideOutsideOfPackage("com.ttulka.ecommerce.warehouse.listeners..")
                .should().onlyDependOnClassesThat().resideOutsideOfPackages(
                        "com.ttulka.ecommerce.sales..",
                        "com.ttulka.ecommerce.shipping..",
                        "com.ttulka.ecommerce.billing..");
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
        ArchRule rule = classes()
                .should().onlyDependOnClassesThat(
                        resideOutsideOfPackages(
                                "com.ttulka.ecommerce.sales..",
                                "com.ttulka.ecommerce.warehouse..",
                                "com.ttulka.ecommerce.billing.."
                        ).or(type(OrderPlaced.class).or(NESTED_CLASSES)
                        ).or(type(GoodsFetched.class).or(NESTED_CLASSES)
                        ).or(type(PaymentCollected.class).or(NESTED_CLASSES)));
        rule.check(importedClasses);
    }

    @Test
    void shipping_only_listeners_have_dependencies_on_other_services_events() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("com.ttulka.ecommerce.shipping");
        ArchRule rule = classes().that().resideOutsideOfPackage("com.ttulka.ecommerce.shipping.listeners..")
                .should().onlyDependOnClassesThat().resideOutsideOfPackages(
                        "com.ttulka.ecommerce.sales..",
                        "com.ttulka.ecommerce.warehouse..",
                        "com.ttulka.ecommerce.billing..");
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
        ArchRule rule = classes()
                .should().onlyDependOnClassesThat(
                        resideOutsideOfPackages(
                                "com.ttulka.ecommerce.sales..",
                                "com.ttulka.ecommerce.warehouse..",
                                "com.ttulka.ecommerce.shipping.."
                        ).or(type(OrderPlaced.class).or(NESTED_CLASSES)));
        rule.check(importedClasses);
    }

    @Test
    void billing_only_listeners_have_dependencies_on_other_services_events() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("com.ttulka.ecommerce.billing");
        ArchRule rule = classes().that().resideOutsideOfPackage("com.ttulka.ecommerce.billing.listeners..")
                .should().onlyDependOnClassesThat().resideOutsideOfPackages(
                        "com.ttulka.ecommerce.sales..",
                        "com.ttulka.ecommerce.warehouse..",
                        "com.ttulka.ecommerce.shipping..");
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
}
