package club.pard.exit.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    protected SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {
        /*
            라인별 해석:
            1. cors정책 (현재는 Application에서 작업을 해뒀으므로 기본 설정 사용)
            2. csrf 대책 (현재는 CSRF에 대한 대책을 비활성화)
            3. basic 인증 (현재는 bearer token 인증방법을 사용하기 때문에 비활성화)
            4. 세션 기반 인증 (현재는 Session 기반 인증을 사용하지 않기 때문에 상태를 없앰)
            5. "/" "/api/auth" 모듈에 대해서는 모두 허용 (인증을 하지 않고 사용 가능하게 함)
            6. 나머지 REquest에 대해서는 모두 인증된 사용자만 사용가능하게함
         */
        httpSecurity
                .cors(Customizer.withDefaults())
                .csrf((csrf) -> csrf.disable())
                .httpBasic((httpBasic) -> httpBasic.disable())
                // .sessionManagement((sessionManagement) ->sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests.requestMatchers("/api/**", "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**")
                .permitAll().anyRequest().authenticated());

        return httpSecurity.build();
    }

}