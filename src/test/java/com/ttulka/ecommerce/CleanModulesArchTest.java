package com.ttulka.ecommerce;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import com.ttulka.ecommerce.common.events.DomainEvent;

import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.core.domain.JavaClass.Predicates.NESTED_CLASSES;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.assignableTo;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAPackage;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideOutsideOfPackages;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

class CleanModulesArchTest {

    @Test
    void sales_catalog_service_has_no_dependency_on_others() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages(
                "com.ttulka.ecommerce.sales.catalog");
        ArchRule rule = classes().should().onlyDependOnClassesThat(
                resideOutsideOfPackages(
                        "com.ttulka.ecommerce.."
                ).or(resideInAPackage("com.ttulka.ecommerce.sales.catalog.."
                ).or(resideInAPackage("com.ttulka.ecommerce.common.."))));
        rule.check(importedClasses);
    }

    @Test
    void sales_catalog_domain_has_no_dependency_to_its_implementation() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages(
                "com.ttulka.ecommerce.sales.catalog");
        ArchRule rule = classes()
                .that().resideOutsideOfPackages(
                        "com.ttulka.ecommerce.sales.catalog.jdbc..")
                .should().onlyDependOnClassesThat().resideOutsideOfPackages(
                        "com.ttulka.ecommerce.sales.catalog.jdbc..");
        rule.check(importedClasses);
    }

    @Test
    void sales_order_service_has_no_dependencies_on_others_except_events() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages(
                "com.ttulka.ecommerce.sales.order");
        ArchRule rule = classes().should().onlyDependOnClassesThat(
                resideOutsideOfPackages(
                        "com.ttulka.ecommerce.."
                ).or(resideInAPackage("com.ttulka.ecommerce.sales.order.."
                ).or(resideInAPackage("com.ttulka.ecommerce.common..")
                ).or(assignableTo(DomainEvent.class).or(NESTED_CLASSES))));
        rule.check(importedClasses);
    }

    @Test
    void sales_order_domain_has_no_dependency_to_its_implementation() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages(
                "com.ttulka.ecommerce.sales.order");
        ArchRule rule = classes()
                .that().resideOutsideOfPackages(
                        "com.ttulka.ecommerce.sales.order.jdbc..")
                .should().onlyDependOnClassesThat().resideOutsideOfPackages(
                        "com.ttulka.ecommerce.sales.order.jdbc..");
        rule.check(importedClasses);
    }

    @Test
    void sales_cart_service_has_no_dependency_on_others() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages(
                "com.ttulka.ecommerce.sales.cart");
        ArchRule rule = classes().should().onlyDependOnClassesThat(
                resideOutsideOfPackages(
                        "com.ttulka.ecommerce.."
                ).or(resideInAPackage("com.ttulka.ecommerce.sales.cart.."
                ).or(resideInAPackage("com.ttulka.ecommerce.common.."))));
        rule.check(importedClasses);
    }

    @Test
    void billing_payment_service_has_no_dependencies_on_others_except_events() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages(
                "com.ttulka.ecommerce.billing.payment");
        ArchRule rule = classes().should().onlyDependOnClassesThat(
                resideOutsideOfPackages(
                        "com.ttulka.ecommerce.."
                ).or(resideInAPackage("com.ttulka.ecommerce.billing.payment.."
                ).or(resideInAPackage("com.ttulka.ecommerce.common..")
                ).or(assignableTo(DomainEvent.class).or(NESTED_CLASSES))));
        rule.check(importedClasses);
    }

    @Test
    void billing_payment_domain_has_no_dependency_to_its_implementation() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages(
                "com.ttulka.ecommerce.billing.payment");
        ArchRule rule = classes()
                .that().resideOutsideOfPackages(
                        "com.ttulka.ecommerce.billing.payment.jdbc..")
                .should().onlyDependOnClassesThat().resideOutsideOfPackages(
                        "com.ttulka.ecommerce.billing.payment.jdbc..");
        rule.check(importedClasses);
    }

    @Test
    void shipping_delivery_service_has_no_dependencies_on_others_except_events() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages(
                "com.ttulka.ecommerce.shipping.delivery");
        ArchRule rule = classes().should().onlyDependOnClassesThat(
                resideOutsideOfPackages(
                        "com.ttulka.ecommerce.."
                ).or(resideInAPackage("com.ttulka.ecommerce.shipping.delivery.."
                ).or(resideInAPackage("com.ttulka.ecommerce.common..")
                ).or(assignableTo(DomainEvent.class).or(NESTED_CLASSES))));
        rule.check(importedClasses);
    }

    @Test
    void shipping_delivery_domain_has_no_dependency_to_its_implementation() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages(
                "com.ttulka.ecommerce.shipping.delivery");
        ArchRule rule = classes()
                .that().resideOutsideOfPackages(
                        "com.ttulka.ecommerce.shipping.delivery.jdbc..")
                .should().onlyDependOnClassesThat().resideOutsideOfPackages(
                        "com.ttulka.ecommerce.shipping.delivery.jdbc..");
        rule.check(importedClasses);
    }

    @Test
    void warehouse_service_has_no_dependencies_on_others_except_events() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages(
                "com.ttulka.ecommerce.warehouse");
        ArchRule rule = classes().should().onlyDependOnClassesThat(
                resideOutsideOfPackages(
                        "com.ttulka.ecommerce.."
                ).or(resideInAPackage("com.ttulka.ecommerce.warehouse.."
                ).or(resideInAPackage("com.ttulka.ecommerce.common..")
                ).or(assignableTo(DomainEvent.class).or(NESTED_CLASSES))));
        rule.check(importedClasses);
    }

    @Test
    void warehouse_domain_has_no_dependency_to_its_implementation() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages(
                "com.ttulka.ecommerce.warehouse");
        ArchRule rule = classes()
                .that().resideOutsideOfPackages(
                        "com.ttulka.ecommerce.warehouse.jdbc..")
                .should().onlyDependOnClassesThat().resideOutsideOfPackages(
                        "com.ttulka.ecommerce.warehouse.jdbc..");
        rule.check(importedClasses);
    }

    @Test
    void catalog_service_has_no_dependencies_on_billing() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("com.ttulka.ecommerce.portal");
        ArchRule rule = classes()
                .should().onlyDependOnClassesThat().resideOutsideOfPackages(
                        "com.ttulka.ecommerce.billing..");
        rule.check(importedClasses);
    }

    @Test
    void portal_web_uses_only_its_own_use_cases_and_no_direct_dependencies_on_other_services() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages(
                "com.ttulka.ecommerce.sales",
                "com.ttulka.ecommerce.warehouse",
                "com.ttulka.ecommerce.shipping",
                "com.ttulka.ecommerce.billing");
        ArchRule rule = classes()
                .should().onlyHaveDependentClassesThat().resideOutsideOfPackage(
                        "com.ttulka.ecommerce.portal.web..");
        rule.check(importedClasses);
    }
}
