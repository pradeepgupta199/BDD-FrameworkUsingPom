package runnerclass;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "featurefile\\LoginFeature.feature",
    glue = "stepdefinitions",
  //  tags = "@smoke",
    dryRun = false,
    monochrome = true,
    		  plugin = {"pretty", "html:cucumberReporting/reportJUnit.html", "json:cucumberReporting/report.json"}
)

public class RunnerClassUsingJUnit {

}
