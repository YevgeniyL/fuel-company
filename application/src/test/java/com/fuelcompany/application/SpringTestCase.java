package com.fuelcompany.application;

import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.runner.RunWith;


//tests runner for maven and IDE
@RunWith(JUnitPlatform.class)
//scan test-classes scope for JUnit Jupiter runner
@SelectPackages("com.fuelcompany")
public class SpringTestCase {

}
