package no.westerdals.frontend.frontend;

import no.westerdals.frontend.po.HomePageObject;
import no.westerdals.frontend.po.LoginPageObject;
import no.westerdals.frontend.po.NewUserPageObject;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeTrue;

public abstract class WebTestBase {
    private static WebDriver driver;

    @Before
    public void checkIfWildflyIsRunning(){
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

    private static WebDriver getFirefoxDriver(){
        setupDriverExecutable("geckodriver", "webdriver.gecko.driver");

        DesiredCapabilities desiredCapabilities = DesiredCapabilities.firefox();
        desiredCapabilities.setCapability("marionette", true);
        desiredCapabilities.setJavascriptEnabled(true);

        return  new FirefoxDriver(desiredCapabilities);
    }

    protected WebDriver getDriver(){
        return driver;
    }

    protected String getPageSource(){
        return driver.getPageSource();
    }

    protected void createAndLoginUser(String username, String password, HomePageObject home){
        LoginPageObject loginPageObject = home.toLoginPage();
        assertTrue(loginPageObject.isOnPage());

        NewUserPageObject newUserPageObject = loginPageObject.toNewUserPage();
        assertTrue(newUserPageObject.isOnPage());

        newUserPageObject.createNewUser(username, password, password);
        assertTrue(home.isOnPage());
        assertTrue(home.isLoggedIn(username));
    }

    protected void logout(HomePageObject home){
        home.logout();
        assertTrue(home.isOnPage());
        assertFalse(home.isLoggedIn());
    }

    protected void login(String username, String password, HomePageObject home){
        LoginPageObject loginPageObject = home.toLoginPage();
        assertTrue(loginPageObject.isOnPage());
        loginPageObject.login(username, password);
        assertTrue(home.isOnPage());
        assertTrue(home.isLoggedIn(username));
    }
}
