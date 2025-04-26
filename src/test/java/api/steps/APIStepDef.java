package api.steps;

import com.actions.APIMainActions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;

public class APIStepDef {
	
	APIMainActions apiActions=new APIMainActions();
	
	
	@Given("^Endpoint Request URL with Function \"([^\"]*)\"$")
	public void endpointRequest_URL_With_WebMethod(String strEndPointURL) {
		
		strEndPointURL=BeforeActions.GetTestDataValue(strEndPointURL);
		apiActions.setEndPointURL(strEndPointURL);
		apiActions.CreateHeaders();
	}
	
	@Given("^Method Given as \"([^\"]*)\"$")
	public void methodGivenAs(String strMethod) {
		strMethod=BeforeActions.GetTestDataValue(strMethod);
		apiActions.SetMethod(strMethod);
	}
	
	@Given("^Select Body Content as \"([^\"]*)\"$")
	public void selectBodyContentAs(String strInputFile) {
		strInputFile=BeforeActions.GetTestDataValue(strInputFile);
		apiActions.SetInputBody(strInputFile, BeforeActions.GetAPIDataValues());
	}
	
	@When("^Submit the Request and get the Response")
	public void submit_the_request_and_get_the_result() {
		String strResponseFileName="";
		strResponseFileName=BeforeActions.GetTestDataValue("OUTPUTJSONFILE");
		apiActions.submitAndGetResponse(strResponseFileName);
	}

}
