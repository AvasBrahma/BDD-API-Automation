package api.runners;

import org.testng.TestNG;

import com.utils.ConfigDetails;
import com.utils.ScenariosSelector;


public class TestRunner {

	
public static void main(String[] args) {
		
		System.out.println("Execution Started..............");
		String strConfigFile="";
		String strSingleTagName="";
		ConfigDetails.setConfigPath(strConfigFile);
		
		
		ScenariosSelector scenariosSelector=new ScenariosSelector();
		scenariosSelector.creatorSelector(strSingleTagName);
		
		TestNG testSuite=new TestNG();
		testSuite.setTestClasses(new Class[] {});
		testSuite.setUseDefaultListeners(false);
		testSuite.run();
	

	}
}
