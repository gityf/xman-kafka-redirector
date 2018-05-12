package com.xman;

import com.xman.service.http.jetty.LaunchJettyInEmbeddedMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Administrator on 2015/9/22.
 */
@Configuration
@ComponentScan(basePackages = "com.xman")
public class StartServer {

    @Autowired
    ApplicationContext applicationContext;

    @Bean(initMethod = "startup", destroyMethod = "shutdown")
    public LaunchJettyInEmbeddedMode getLaunchJetty() {
        LaunchJettyInEmbeddedMode launchJettyInEmbeddedMode = new LaunchJettyInEmbeddedMode();
        launchJettyInEmbeddedMode.setContext(applicationContext);
        launchJettyInEmbeddedMode.setPort(8081);
        launchJettyInEmbeddedMode.setScanPackages(new String[]{"com.yiche"});
        return launchJettyInEmbeddedMode;
    }

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(StartServer.class);
    }
}
