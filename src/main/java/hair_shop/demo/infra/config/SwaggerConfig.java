package hair_shop.demo.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author dkansk924@naver.com
 * @since 2021/06/16
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    private final Tag memberController = new Tag("memberController", "회원 API");
    private final Tag designerController = new Tag("designerController", "디자이너 API");
    private final Tag orderController = new Tag("orderController", "예약 API");
    private final Tag memberShipController = new Tag("memberShipController", "회원권 API");
    private final Tag menuController = new Tag("menuController", "메뉴 API");

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
            .apiInfo(apiInfo())
            .select()
            .apis(RequestHandlerSelectors.any())
            .paths(PathSelectors.any())
            .build()
            .tags(memberController, designerController, orderController, menuController,
                memberShipController);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            .title("Hair-Shop Swagger Documentation")
            .description("Hair-Shop Documentation")
            .version("1.0")
            .license("License Version 1.0")
            .licenseUrl("https://github.com/KJJ924/hair_shop_managing_system")
            .build();
    }
}

