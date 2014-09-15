package net.codingpark.cheesebrowser.junit;

import junit.framework.Assert;
import net.codingpark.cheesebrowser.Utils;

import org.junit.Test;

public class UtilsTest {

	@Test
	public void testGetSet() {
		Utils u = Utils.getInstance();
		Assert.assertNotNull(u);
	}

}
