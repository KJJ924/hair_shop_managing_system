package hair_shop.demo;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

@AnalyzeClasses(packagesOf = App.class)
public class PackageDependencyTests {
    private static final String MEMBER = "..modules.member..";
    private static final String ORDER = "..modules.order..";

    @ArchTest
    ArchRule hairPackageRule = classes().that().resideInAPackage("hair_shop_managing_system..")
            .should().onlyBeAccessed().byClassesThat()
            .resideInAPackage("hair_shop_managing_system..");

    @ArchTest
    ArchRule MemberPackageRule = classes().that().resideInAPackage(MEMBER)
            .should().onlyBeAccessed().byClassesThat()
            .resideInAnyPackage(MEMBER,ORDER);

    @ArchTest
    ArchRule cycleCheck = slices().matching("hair_shop_managing_system.modules.(*)..")
            .should().beFreeOfCycles();
}
