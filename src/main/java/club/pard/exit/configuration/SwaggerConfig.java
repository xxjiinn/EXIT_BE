package club.pard.exit.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(info = @Info(title = "EXIT API 명세서", description = "EXIT 앱에서 클라이언트가 서버와 통신할 때 사용할 수 있는 request와 그로부터 받는 response에 대한 내용", version = "v1"))
@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI()
    {
        return new OpenAPI().components(new Components());
    }
}

