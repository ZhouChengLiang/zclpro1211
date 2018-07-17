package org.zclpro.service.myh;

import java.util.Random;

public class RandomUtil {

  /**
   * 休眠几秒
   *
   * @param time 单位秒
   */
  public static void sleepTime(int time) {
    if (time <= 0) {
      return;
    }

    Random random = new Random();
    long sleep_time = (long) (time * 1000 + random.nextDouble() * 1000);

    try {
      Thread.sleep(sleep_time);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    for (int i = 0; i < 100; i++) {
      Random random = new Random();
      long sleep_time = (long) (3200 + random.nextDouble() * 1000);
      System.out.println(sleep_time);
    }
  }

}
