package urjc.isi.pruebasSparkJava;

import static org.junit.Assert.*;
import org.junit.*;
import java.util.*;

public class MainTest {

        private String s;

        @Before      // Set up - Called before every test method.
        public void setUp()
        {
                ;
        }

        @After      // Tear down - Called after every test method.
        public void tearDown()
        {
                ;
        }

        @Test //(expected = NullPointerException.class)
        public void testExample() {
                //assertTrue("It has failed", );
                System.out.println("It has failed");
        }

}
