package api.runners;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import api.steps.BeforeActions;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;


@CucumberOptions(	
		features="test-output/common.feature",
		dryRun=false,
		monochrome=true,
		glue= {"com.ui.steps"}
)
public class TestExecution extends AbstractTestNGCucumberTests{
	
	public static String strResultConsolidatedFolder="";
		
		
		@BeforeClass
		public void setup() {
			System.out.println("Setting up Data & Configuration started.......");
			SimpleDateFormat sdf=new SimpleDateFormat("yyyymmdd_hhmmss");
			Date curDate=new Date();
			String strDate=sdf.format(curDate);
			String strResultFolder=System.getProperty("user.dir")+"/test-output/"+strDate;
			strResultConsolidatedFolder=strResultFolder;
			File fldrResult=new File(strResultConsolidatedFolder);
			fldrResult.mkdir();
			BeforeActions.strResultConsolidatedFolder=strResultConsolidatedFolder;
			System.out.println("Result Folder = " +strResultFolder);
		}
		
		@Test
		public void runTestNg() {
			System.out.println("Running TestNg................");
		}
		
		
		public void afterSteps() {
			
		}
	}

