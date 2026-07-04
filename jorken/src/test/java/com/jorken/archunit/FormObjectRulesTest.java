package com.jorken.archunit;

import com.jorken.annotations.FormModel;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

@AnalyzeClasses(packages = "com.jorken")
public class FormObjectRulesTest {
    @ArchTest
    static final ArchRule formObjectsInWebAdapterAreAnnotatedWithFormModel = classes()
            .that()
            .resideInAPackage("..form..")
            .should()
            .beAnnotatedWith(FormModel.class)
            .because("Classes in web adapter should be annotated with FormModel");
}
