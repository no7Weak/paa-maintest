import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import IttBasetest.BaseDriver;
import businesslogic.helper.pojo.response.LoginResponse;
import config.ConfigManager;
import io.restassured.RestAssured;
import parser.TestDataFilePath;
import static io.restassured.RestAssured.*;

public class GetToken extends BaseDriver {
    private static final Logger LOG = LoggerFactory.getLogger(GetToken.class);
    private String token;

    @Test
    @TestDataFilePath(dataFilePath = "testdata/tpim.json")
    public void loginToGetToken() throws Exception {
        String username = dataModelHelper.getDataModelHelperLoginCreds().getUsername();
        String password = dataModelHelper.getDataModelHelperLoginCreds().getPassword();
        String proxyName = dataModelHelper.getDataModelHelperLoginCreds().getProxyName();
        String casLogin = dataModelHelper.getDataModelHelperLoginCreds().getCasLogin();

        pojoModelHelper.getLoginPayload().setUsername(username);
        pojoModelHelper.getLoginPayload().setPassword(password);
        pojoModelHelper.getLoginPayload().setProxyName(proxyName);
        pojoModelHelper.getLoginPayload().setCasLogin(casLogin);

        RestAssured.baseURI = ConfigManager.getBaseUrl();
        LoginResponse lr = given().log().all()
        .header("content-type", "application/json")
        .header("x-authorization", "Bearer null")
        .header("x-requested-with", "XMLHttpRequest")
        .body(pojoModelHelper.getLoginPayload())
        .when().post(ConfigManager.getEndpoint("login.endpoint"))
        .then().log().all().extract().response().as(LoginResponse.class);

        token = lr.getToken();

        LOG.info("Token extracted is :"+token);
    }
}
