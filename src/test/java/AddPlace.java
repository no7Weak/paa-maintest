

import IttBasetest.BaseDriver;
import businesslogic.helper.codeHelper.CodeHelperFactory;
import io.restassured.RestAssured;
import parser.TestDataFilePath;
import static io.restassured.RestAssured.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import static org.hamcrest.Matchers.*;

public class AddPlace extends BaseDriver {
    private static final Logger LOG = LoggerFactory.getLogger(AddPlace.class);
    private ObjectNode tempPayload;

    @Test
    @TestDataFilePath(dataFilePath = "testdata/restAssuTestData.json")
    public void addPlaceTest() throws Exception {

        //given() all input details
        //when() Submit the api. Resource an http method detail
        //then() Valiate the response

        String addPlacePayload = dataModelHelper.getDataModelHelperRestAssuTestData().getAddPlacePayload().toPrettyString();

        RestAssured.baseURI = "https://rahulshettyacademy.com";
        String addPlaceResponse = given().log().all()
        .queryParam("key", "qaclick123")
        .header("content-type", "application/json")
        .body(addPlacePayload)
        .when().post("maps/api/place/add/json")
        .then().log().all().assertThat().statusCode(200).body("scope", equalTo("APP"))
        .header("Server", "Apache/2.4.52 (Ubuntu)").extract().response().asString();

        LOG.info("addPlaceResponse is :"+addPlaceResponse);
        String placeIdValue = codeHelperFactory.getJsonValue(addPlaceResponse, "place_id");
        LOG.info("placeIdValue is :"+placeIdValue);
        Assert.assertNotNull(placeIdValue, "Place Id is Null.");

//Upate Place
        String upatePlacePayload = dataModelHelper.getDataModelHelperRestAssuTestData().getUpatePlacePayload().toPrettyString();
        LOG.info("upatePlacePayload before editing :"+upatePlacePayload);
        tempPayload = CodeHelperFactory.readJsonTree(upatePlacePayload);
        CodeHelperFactory.setJsonValue(tempPayload, "place_id", placeIdValue);
        String newAddress = CodeHelperFactory.getText(tempPayload, "address");

        upatePlacePayload = CodeHelperFactory.jsontoString(tempPayload);
        LOG.info("upatePlacePayload after editing :"+upatePlacePayload);

        String upatePlaceResponse = given().log().all()
        .queryParam("key", "qaclick123")
        .header("content-type", "application/json")
        .body(upatePlacePayload)
        .when().put("maps/api/place/update/json")
        .then().assertThat().statusCode(200)
        .body("msg", equalTo("Address successfully updated"))
        .extract().response().asString();

        LOG.info("upatePlaceResponse is :"+upatePlaceResponse);
        String successMsg = codeHelperFactory.getJsonValue(upatePlaceResponse, "msg");
        LOG.info("successMsg :"+successMsg);
        Assert.assertEquals(successMsg, "Address successfully updated", "Address is Diff.");


//Get Place

        String getPlaceResponse = given().log().all()
        .queryParams("key", "qaclick123", "place_id", placeIdValue)
        .header("content-type", "application/json")
        .when().get("maps/api/place/get/json")
        .then().assertThat().statusCode(200)
        .body("name", equalTo("Sova House"))
        .extract().response().asString();
        LOG.info("getPlaceResponse is :"+getPlaceResponse);

        tempPayload = CodeHelperFactory.readJsonTree(getPlaceResponse);
        String getPlaceResponseAddress = CodeHelperFactory.getText(tempPayload, "address");
        LOG.info("getPlaceResponseAddress is :"+getPlaceResponseAddress);

        String name = codeHelperFactory.getJsonValue(getPlaceResponse, "name");
        LOG.info("name :"+name);
        Assert.assertEquals(name, "Sova House", "Name is Diff.");
        Assert.assertEquals(getPlaceResponseAddress, newAddress, "Address is Diff.");

//Delete Place

        String deletePlacePayload = dataModelHelper.getDataModelHelperRestAssuTestData().getDeletePlacePayload().toPrettyString();

        tempPayload = CodeHelperFactory.readJsonTree(deletePlacePayload);
        CodeHelperFactory.setJsonValue(tempPayload, "place_id", placeIdValue);
        deletePlacePayload = CodeHelperFactory.jsontoString(tempPayload);
        LOG.info("deletePlacePayload after editing :"+deletePlacePayload);

        String deletePlaceResponse = given().log().all()
        .queryParams("key", "qaclick123")
        .header("content-type", "application/json")
        .body(deletePlacePayload)
        .when().get("maps/api/place/delete/json")
        .then().assertThat().statusCode(200)
        .body("status", equalTo("OK"))
        .extract().response().asString();
        LOG.info("deletePlaceResponse is :"+deletePlaceResponse);
    }
}