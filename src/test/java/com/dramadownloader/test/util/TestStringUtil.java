package com.dramadownloader.test.util;

import com.dramadownloader.common.util.StringUtil;
import org.junit.Assert;
import org.junit.Test;

public class TestStringUtil {
  @Test
  public void testUrlize() {
    String[] inputs = {
        "Hello World",
        "Hello World (Movie)",
        "Hello World! with Symbols?"
    };

    String[] expecteds = {
        "hello-world",
        "hello-world-movie",
        "hello-world-with-symbols"
    };

    for(int i = 0; i < inputs.length; i++) {
      String output = StringUtil.urlize(inputs[i]);
      System.out.println(output);
      Assert.assertEquals(output, expecteds[i]);
    }
  }
}
