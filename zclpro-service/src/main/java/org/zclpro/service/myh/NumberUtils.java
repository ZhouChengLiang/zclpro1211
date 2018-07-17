package org.zclpro.service.myh;

import java.util.Random;

public class NumberUtils {

  private static final String[] codes = {"0","1","2","3","4","5"};

  public static String getRandomCode(int len) {
    Random random = new Random();
    return codes[random.nextInt(len)];
  }

}
