package runnerclass;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
    features = "featurefile\\LoginFeature.feature",
    glue = "stepdefinitions",
   // tags = "@smoke",
    dryRun = false,
    monochrome = true,
    plugin = {"pretty", "html:cucumberReporting/reportTestNG.html", "json:cucumberReporting/report.json"}
)
public class RunnerClassUsingTestNG extends  AbstractTestNGCucumberTests{

}
