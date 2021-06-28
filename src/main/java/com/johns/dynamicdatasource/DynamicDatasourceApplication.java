package com.johns.dynamicdatasource;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.spring.web.SpringfoxWebMvcConfiguration;
import java.net.InetAddress;


/**
 * 动态数据源的应用程序
 *
 * @author johns-li
 * @date 2021/06/23
 */
@ConditionalOnClass(SpringfoxWebMvcConfiguration.class)
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@MapperScan("com.johns.dynamicdatasource.mapper")
@Slf4j
public class DynamicDatasourceApplication implements WebMvcConfigurer {

    @SneakyThrows
    public static void main(String[] args) {
        ConfigurableApplicationContext application = SpringApplication.run(DynamicDatasourceApplication.class, args);
        Environment env = application.getEnvironment();
        log.info("\n----------------------------------------------------------\n\t" +
                        "应用 '{}' 运行成功! 访问连接:\n\t" +
                        "Swagger文档: \t\thttp://{}:{}/doc.html\n\t" +
                        "数据库监控: \t\thttp://{}:{}/druid\n" +
                        "----------------------------------------------------------",
                env.getProperty("spring.application.name"),
                InetAddress.getLocalHost().getHostAddress(),
                env.getProperty("server.port", "9990"),
                InetAddress.getLocalHost().getHostAddress(),
                env.getProperty("server.port", "9990"));
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
