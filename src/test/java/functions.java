

import IttBasetest.BaseDriver;

import org.testng.Assert;
import org.testng.annotations.Test;

public class functions extends BaseDriver {

    @Test
    public void googleTest() throws InterruptedException {
        driver.get("https://google.com");
        // Assert.fail("Forced failure test");
        System.out.println("Title: " + driver.getTitle());
        Thread.sleep(5000);
    }
}
