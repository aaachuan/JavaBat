package soundsystem;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;

public class MyTest {
	@Rule
	public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

	@Test
	public void writesTextToSystemOut() {
		// System.out.print("hello world");
		// systemOutRule.clearLog();
		System.out.print("foo");
		assertEquals("foo", systemOutRule.getLog());
	}
}
