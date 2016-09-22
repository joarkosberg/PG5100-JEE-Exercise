package data;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class UserBeanTest {

    @Deployment
    public static JavaArchive createDeployment() {

        return ShrinkWrap.create(JavaArchive.class)
                .addClasses(A.class, Data.class)
                .addAsResource("META-INF/persistence.xml");
    }

    @Before
    public void init(){
    }

    @After
    public void tearDown(){

    }



}
