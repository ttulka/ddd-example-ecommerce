package com.ttulka.ecommerce;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;

import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

class CleanCodeArchTest {

    @Test
    void no_classes_ending_with_impl() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("com.ttulka.ecommerce");
        ArchRule rule = classes()
                .should().haveSimpleNameNotEndingWith("Impl");

        rule.check(importedClasses);
    }

    @Test
    void no_classes_ending_with_service() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("com.ttulka.ecommerce");
        ArchRule rule = classes()
                .should().haveSimpleNameNotEndingWith("Service");

        rule.check(importedClasses);
    }

    @Test
    void no_classes_ending_with_dto() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("com.ttulka.ecommerce");
        ArchRule rule = classes()
                .should().haveSimpleNameNotEndingWith("DTO")
                .andShould().haveSimpleNameNotEndingWith("Dto");

        rule.check(importedClasses);
    }

    @Test
    void no_classes_ending_with_repository() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("com.ttulka.ecommerce");
        ArchRule rule = classes()
                .should().haveSimpleNameNotEndingWith("Repository")
                .andShould().haveSimpleNameNotEndingWith("Repo");

        rule.check(importedClasses);
    }

    @Test
    void no_classes_ending_with_entity() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("com.ttulka.ecommerce");
        ArchRule rule = classes()
                .should().haveSimpleNameNotEndingWith("Entity");

        rule.check(importedClasses);
    }

    @Test
    void no_classes_ending_with_aggregate() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("com.ttulka.ecommerce");
        ArchRule rule = classes()
                .should().haveSimpleNameNotEndingWith("Aggregate");

        rule.check(importedClasses);
    }
}
