package be.ninjacoder.junit;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class JUnitRunner {

	public OutputStream outputStream;

	/**
	 * @param args
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws ClassNotFoundException {
		JUnitRunner runner = new JUnitRunner();
		runner.setOutpuStream(System.out);
		String testClassName = args[0];
		runner.runClass(testClassName);
	}

	public void setOutpuStream(OutputStream out) {
		this.outputStream = out;
		
	}

	public void runClass(String testClassName) throws ClassNotFoundException {
		Class testClass = Class.forName(testClassName);
		JUnitCore core = new JUnitCore();
		PassingTestsRunListener listener = new PassingTestsRunListener();
		core.addListener(listener);
		Result result = core.run(testClass);
		String resultsTemplate = "{ \"fail\": %d, \"pass\": %d, \"suites\": [ %s ] }";
		int fail = result.getFailureCount();
		int pass = result.getRunCount() - fail;
		String suitesTemplate = "{ \"name\": \"%s\", \"tests\": [ %s ] }";
		String testsTemplate = "{ \"name\": \"%s\", \"status\": \"%s\", \"info\": \"%s\"}";
		
		// First, print passing tests collected by the custom Listener		
		Iterator<String> itPassing =  listener.getPassingTestsNames();
		String testsJson = "";
		String sep = "";
		while (itPassing.hasNext()) {
			String testName = (String) itPassing.next();
			testsJson += sep + String.format(testsTemplate, testName, "pass", "");
			sep = ", ";
		}
		
		// Then, print failing tests. JUnit does not collect information for passing tests in Result object (too bad).
		List<Failure> failures = result.getFailures();
		Iterator<Failure> it = failures.iterator();
		while (it.hasNext()) {
			Failure failure = (Failure) it.next();
			testsJson += sep + String.format(testsTemplate, failure.getDescription().getMethodName(), "fail", failure.getMessage());
		}
		String suiteName = testClassName.substring(testClassName.lastIndexOf('.')+1);
		String suitesJson = String.format(suitesTemplate, suiteName, testsJson);
		String resultsJson = String.format(resultsTemplate, fail, pass, suitesJson);
		try {
			this.outputStream.write(resultsJson.getBytes());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}
	}


}
