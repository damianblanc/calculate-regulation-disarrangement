package com.bymatech.calculateregulationdisarrangement.config;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppCorsConfiguration {

//    @Bean
//    public WebMvcConfigurer corsConfigurer() {
//      return new WebMvcConfigurer() {
//        @Override
//        public void addCorsMappings(CorsRegistry registry) {
//          registry.addMapping("/**")
//              .allowedOrigins("*")
//              .allowedMethods("GET", "POST", "PUT", "DELETE")
//              .allowedHeaders("*");
//        }
//      };
//    }

  @Bean
  public FilterRegistrationBean corsFilter() {
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
           CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
//        config.setAllowedOrigins(List.of("*"));
        config.setAllowedOrigins(List.of("https://dark-doodles-happen.loca.lt", "https://spicy-moons-know.loca.lt", "https://f7e9-2800-810-5dc-53-8460-f386-11e-6107.ngrok-free.app", "http://192.168.0.253:3000", "http://localhost:3000", "http://localhost:3001", "http://localhost:3002", "http://localhost:8098"));
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
        bean.setOrder(0);
        return bean;
}

//    @Bean
//    CorsConfigurationSource corsConfigurationSource() {
//
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(List.of("https://e386-2800-810-5dc-53-2838-ec-4b6a-70fb.ngrok-free.app", "http://localhost:3000"));
//        configuration.setAllowedMethods(List.of("GET", "POST", "PUT"));
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }
//
//    private boolean isTrustedOrigin(String origin) {
//        List<String> trustedOrigins = List.of(
//            "https://e386-2800-810-5dc-53-2838-ec-4b6a-70fb.ngrok-free.app",
//            "http://localhost:3000",
//            "http://localhost:3001",
//            "http://localhost:3002",
//            "http://localhost:8098"
//        );
//        return trustedOrigins.contains(origin);
//    }


//    @Bean
//    public FilterRegistrationBean corsFilter() {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowCredentials(true);
//        config.setAllowedHeaders(List.of("*"));
//        config.setAllowedMethods(List.of("*"));
//
//        // Dynamically set allowedOrigins based on the Origin header of the request
//        config.setAllowedOrigins((origin) -> {
//            HttpServletRequest httpRequest = (HttpServletRequest) request.getAttribute("HttpServletRequest");
//            String requestOrigin = httpRequest.getHeader("Origin");
//            return requestOrigin != null && requestOrigin.equals(origin);
//        });
//
//        source.registerCorsConfiguration("/**", config);
//        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
//        bean.setOrder(0);
//        return bean;
//
////        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
////        CorsConfiguration config = new CorsConfiguration();
////        config.setAllowCredentials(true);
////        config.setAllowedOrigins(List.of("*")); // Allow all origins
////        config.addAllowedHeader("*");
////        config.addAllowedMethod("*");
////        source.registerCorsConfiguration("/**", config);
////        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
////        bean.setOrder(0);
////        return bean;
//
////        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
////        CorsConfiguration config = new CorsConfiguration();
////        config.setAllowCredentials(true);
////        config.setAllowedOrigins(List.of("https://e386-2800-810-5dc-53-2838-ec-4b6a-70fb.ngrok-free.app, http://localhost:3000", "http://localhost:3001", "http://localhost:3002", "http://localhost:8098"));
////        config.addAllowedHeader("*");
////        config.addAllowedMethod("*");
////        source.registerCorsConfiguration("/**", config);
////        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
////        bean.setOrder(0);
////        return bean;
//    }
//
//    private List<String> trustedOrigins = List.of(
//        "https://e386-2800-810-5dc-53-2838-ec-4b6a-70fb.ngrok-free.app",
//        "http://localhost:3000",
//        "http://localhost:3001",
//        "http://localhost:3002",
//        "http://localhost:8098"
//    );
//
//    // Check if the origin is trusted
//    private boolean isTrustedOrigin(String origin) {
//        return trustedOrigins.contains(origin);
//    }
}
