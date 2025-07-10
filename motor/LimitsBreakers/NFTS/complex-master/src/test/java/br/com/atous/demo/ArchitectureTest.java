package br.com.atous.demo;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

@AnalyzeClasses(packages = "br.com.atous.demo", importOptions = ImportOption.DoNotIncludeTests.class)
class ArchitectureTest {

    @ArchTest
    static final ArchRule layer_dependencies_are_respected = layeredArchitecture()
           .consideringAllDependencies()
           .layer("Entrypoints").definedBy("br.com.atous.demo.entrypoints..")
           .layer("Application").definedBy("br.com.atous.demo.application..")
           .layer("Domain").definedBy("br.com.atous.demo.domain..")
           .layer("Infrastructure").definedBy("br.com.atous.demo.infrastructure..")

           .whereLayer("Entrypoints").mayNotBeAccessedByAnyLayer()
           .whereLayer("Application").mayOnlyBeAccessedByLayers("Entrypoints", "Infrastructure")
           .whereLayer("Infrastructure").mayOnlyBeAccessedByLayers("Entrypoints")
           .whereLayer("Domain").mayOnlyBeAccessedByLayers("Application", "Infrastructure", "Entrypoints");
}