package br.ufc.great.sysrepotd;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

import org.junit.Test;
import org.springframework.data.jpa.repository.JpaRepository;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;

import br.ufc.great.sysadmin.service.AbstractService;

public class CheckArchitecture {
	static JavaClasses importedClasses = new ClassFileImporter().importPackages("br.ufc.great.sysadmin");
	static JavaClasses importedClassesFromController = new ClassFileImporter().importPackages("br.ufc.great.sysadmin.controller");
	static JavaClasses importedClassesFromService = new ClassFileImporter().importPackages("br.ufc.great.sysadmin.service");
	static JavaClasses importedClassesFromRepository = new ClassFileImporter().importPackages("br.ufc.great.sysadmin.domain.repository");
	
	@Test
	public void showImportedClasses() {		
		String splitClasses[] = this.importedClasses.toString().split(",");
		
		System.out.println("Lista de classes importadas: " + splitClasses.length);
		
		for (String element : splitClasses) {
			System.out.println(element);
		}
	}
	
	@Test
	public void Controllers_haveSimpleNameEndingWithController() {
		ArchRule rule = classes()
				.that().haveSimpleNameEndingWith("Controller")
				.should().resideInAPackage("br.ufc.great.sysadmin.controller");
		
		rule.check(importedClassesFromController);
	}
	
	@Test
	public void Controllers_should_not_be_accessed_by_anyone() {
		 ArchRule rule = layeredArchitecture()
				    .layer("Controller").definedBy("br.ufc.great.sysadmin.controller..")
				    .whereLayer("Controller").mayNotBeAccessedByAnyLayer();
		 
		 rule.check(importedClasses);
	}
		
	@Test
	public void Services_haveSimplesNameEndingWithService() {
		ArchRule rule = classes()
				.that().haveSimpleNameEndingWith("Service")
				.should().resideInAnyPackage("br.ufc.great.sysadmin.service");
		
		rule.check(importedClassesFromService);
	}
	
	@Test
	public void Services_should_extend_from_AbstractService() {
		ArchRule rule = classes()				
				.that().areAssignableTo(AbstractService.class)
			    .should().onlyBeAccessed().byAnyPackage("..service..");
				
		rule.check(importedClassesFromService);
	}
	
	@Test
	public void Services_should_only_be_accessed_by_Controllers_or_Util_or_Security() {
	    ArchRule rule = classes()
	    		.that().resideInAPackage("..service..")
	    		.should().onlyBeAccessed().byAnyPackage("..controller..", "..service..", "..util..", "..security..");

	    rule.check(importedClasses);
	}

	@Test
	public void Repositories_haveSimpleNameEndingWithRepository() {
		ArchRule rule = classes()
				.that().haveSimpleNameEndingWith("Repository")
				.should().resideInAPackage("br.ufc.great.sysadmin.domain.repository");
		
		rule.check(importedClassesFromRepository);
	}

	@Test
	public void Repositories_should_extend_from_JpaRepository() {
		ArchRule rule = classes()				
				.that().areAssignableTo(JpaRepository.class)
			    .should().onlyBeAccessed().byAnyPackage("..repository..");
				
		rule.check(importedClassesFromRepository);
	}

	@Test
	public void Repositories_should_be_accessed_only_by_service() {
		 ArchRule rule = layeredArchitecture()
				 	.layer("Controller").definedBy("br.ufc.great.sysadmin.controller..")
				    .layer("Service").definedBy("br.ufc.great.sysadmin.service..")
				    .layer("Repository").definedBy("br.ufc.great.sysadmin.domain.repository..")
				    .whereLayer("Repository").mayOnlyBeAccessedByLayers("Service");
		 
		 rule.check(importedClasses);
	}

}