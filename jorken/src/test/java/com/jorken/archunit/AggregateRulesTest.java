package com.jorken.archunit;

import static com.jorken.archunit.rules.HaveExactlyOneAggregateRoot.HAVE_EXACTLY_ONE_AGGREGATE_ROOT;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

import com.jorken.JorkenApplication;
import com.jorken.annotations.AggregateRoot;
import com.jorken.annotations.View;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

@AnalyzeClasses(packages = "com.jorken")
public class AggregateRulesTest {
    @ArchTest
    static final ArchRule onlyAggregateRootShouldBePublic = classes()
            .that()
            .areNotAnnotatedWith(AggregateRoot.class)
            .and()
            .areNotAnnotatedWith(View.class)
            .and()
            .resideInAPackage("..domain..")
            .should()
            .notBePublic()
            .allowEmptyShould(true)
            .because("All implementations of an Aggregate should be package private");

    @ArchTest
    static final ArchRule eachAggregateHasExactlyOneAggregateRoot = slices()
            .matching("..domain.(*)..")
            .should(HAVE_EXACTLY_ONE_AGGREGATE_ROOT);

}
