/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.springstuff;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 *
 * @author Jakob
 */
@Configuration
@Import(WebConfig.class)
public class ApplicationConfig {
    
    /**
     * Nothing here at the moment, used for configuring spring application.
     */
    //@Bean
    public static void getConfig() {
        ;
    }
}
