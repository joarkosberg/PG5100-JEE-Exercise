package no.joarkosberg.exam.frontend;

import no.joarkosberg.exam.frontend.po.HomePageObject;
import no.joarkosberg.exam.frontend.po.LoginPageObject;
import no.joarkosberg.exam.frontend.po.NewUserPageObject;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeTrue;

public abstract class WebTestBase {
    private static WebDriver driver;
    private static final AtomicLong counter = new AtomicLong(System.currentTimeMillis());

    @Before
    public void checkIfWildflyIsRunning() {
        assumeTrue("Wildfly is not up and running", JBossUtil.isJBossUpAndRunning());
    }

    @BeforeClass
    public static void init() throws InterruptedException {
        driver = getChromeDriver();

        //Wait for wildfly to be ready
        for (int i = 0; i < 30; i++) {
            boolean ready = JBossUtil.isJBossUpAndRunning();
            if (!ready) {
                Thread.sleep(1000);
                continue;
            } else {
                break;
            }
        }
    }

    @AfterClass
    public static void tearDown() {
        driver.close();
    }

    private static boolean tryToSetGeckoIfExists(String property, Path path) {
        if (Files.exists(path)) {
            System.setProperty(property, path.toAbsolutePath().toString());
            return true;
        }
        return false;
    }

    protected static String getUniqueId() {
        return "foo" + counter.incrementAndGet();
    }

    protected static String getUniqueText() {
        return "text" + counter.incrementAndGet();
    }

    private static void setupDriverExecutable(String executableName, String property) {
        String homeDir = System.getProperty("user.home");

        //first try Linux/Mac executable
        if (!tryToSetGeckoIfExists(property, Paths.get(homeDir, executableName))) {
            //then check if on Windows
            if (!tryToSetGeckoIfExists(property, Paths.get(homeDir, executableName + ".exe"))) {
                fail("Cannot locate the " + executableName + " in your home directory " + homeDir);
            }
        }
    }

    private static WebDriver getChromeDriver() {
        setupDriverExecutable("chromedriver", "webdriver.chrome.driver");
        return new ChromeDriver();
    }

    protected WebDriver getDriver() {
        return driver;
    }

    protected void createAndLoginNewUser(String username, String password, HomePageObject home){
        LoginPageObject loginPageObject = home.toLoginPage();
        NewUserPageObject newUserPageObject = loginPageObject.toNewUserPage();
        assertTrue(newUserPageObject.isOnPage());

        newUserPageObject.createNewUser(username, password, password);
        assertTrue(home.isOnPage());
        assertTrue(home.isLoggedIn(username));
    }

    protected void loginUser(String username, String password, HomePageObject home){
        LoginPageObject loginPageObject = home.toLoginPage();
        assertTrue(loginPageObject.isOnPage());

        loginPageObject.login(username, password);
        assertTrue(home.isOnPage());
        assertTrue(home.isLoggedIn(username));
    }
}
