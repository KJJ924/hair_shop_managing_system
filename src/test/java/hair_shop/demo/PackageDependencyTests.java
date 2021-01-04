package hair_shop.demo;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

@AnalyzeClasses(packagesOf = App.class)
public class PackageDependencyTests {
    private static final String MEMBER = "..modules.member..";
    private static final String DESIGNER = "..modules.designer..";
    private static final String ORDER = "..modules.order..";
    private static final String MENU = "..modules.menu..";



    @ArchTest
    ArchRule hairPackageRule = classes().that().resideInAPackage("hair_shop_managing_system..")
            .should().onlyBeAccessed().byClassesThat()
            .resideInAPackage("hair_shop_managing_system..");

    @ArchTest
    ArchRule MemberPackageRule = classes().that().resideInAPackage(MEMBER)
            .should().onlyBeAccessed().byClassesThat()
            .resideInAnyPackage(MEMBER,ORDER);

    @ArchTest
    ArchRule orderPackageRule = classes().that().resideInAPackage(ORDER)
            .should().accessClassesThat().resideInAnyPackage(DESIGNER,MENU,ORDER);

    @ArchTest
    ArchRule cycleCheck = slices().matching("hair_shop_managing_system.modules.(*)..")
            .should().beFreeOfCycles();
}
