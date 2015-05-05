/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 *
 * @author Jakob
 */
@Configuration
@Import({TestDatabaseConfiguration.class})
@ComponentScan(basePackages = {
    "se.chalmers.bokforing.controller",
    "se.chalmers.bokforing.persistence",
    "se.chalmers.bokforing.session",
    "se.chalmers.bokforing.service"
})
public class TestApplicationConfig {

}
