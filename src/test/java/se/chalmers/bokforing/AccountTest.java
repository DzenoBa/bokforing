/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing;


import javax.persistence.EntityManager;
import static org.junit.Assert.assertNotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import se.chalmers.bokforing.config.TestApplicationConfig;
import org.junit.Test;
import se.chalmers.bokforing.util.InitializationUtil;




/**
 *
 * @author Isabelle
 */

 
@ContextConfiguration(classes = TestApplicationConfig.class)
public class AccountTest extends AbstractIntegrationTest {
    @Autowired
    EntityManager em;
    
    @Test
    public void testInsertDefaultAccounts(){
       // InitializationUtil.insertDefaultAccounts(em);
              //  assertNotNull();

    }
    
}
