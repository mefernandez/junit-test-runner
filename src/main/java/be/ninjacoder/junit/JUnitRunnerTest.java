package be.ninjacoder.junit;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;

import org.junit.Test;

public class JUnitRunnerTest {

	@Test
	public void outputsJSONResultsToStdOut() throws ClassNotFoundException {
		JUnitRunner runner = new JUnitRunner();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		runner.setOutpuStream(out);
		runner.runClass("com.unitdd.Test");
		String expected = "{ \"fail\": 1, \"pass\": 1, \"suites\": [ { \"name\": \"Test\", \"tests\": [ { \"name\": \"passingTest\", \"status\": \"pass\", \"info\": \"\"}, { \"name\": \"failingTest\", \"status\": \"fail\", \"info\": \"expected:<1> but was:<2>\"} ] } ] }";
		assertEquals(expected, out.toString());		
	}

}
