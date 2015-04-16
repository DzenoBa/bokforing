/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing;

import com.lowagie.text.DocumentException;
import java.io.IOException;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import se.chalmers.bokforing.config.TestApplicationConfig;
import se.chalmers.bokforing.service.faktura.FakturaPresenter;

/**
 *
 * @author victor
 */
@ContextConfiguration(classes = TestApplicationConfig.class)
public class FakturaTest  extends AbstractIntegrationTest {
    
    @Test
    public void printPDF(){
        
        FakturaPresenter fp = new FakturaPresenter();
        try{
            fp.print();
        } catch (IOException | DocumentException e){
            assert(false);
        }
    }
}
