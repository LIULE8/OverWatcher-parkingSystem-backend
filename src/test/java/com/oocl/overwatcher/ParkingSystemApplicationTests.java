package com.oocl.overwatcher;

import com.oocl.overwatcher.entities.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class ParkingSystemApplicationTests {
  private static final String COMMON_DB_DATE_STRING_FORMAT = "yyyyMMddHHmmss.SSS";

  @Test
  public void contextLoads() {


  }


  public static void main(String[] args) {
    new Thread(() -> {
      Date now = DateFormatter.now();
      SimpleDateFormat smf = new SimpleDateFormat(COMMON_DB_DATE_STRING_FORMAT);
      String format = smf.format(now);
      System.out.println(Thread.currentThread().getName() + "->" + format);
    }).start();

    new Thread(() -> {
      Date now = DateFormatter.now();
      SimpleDateFormat smf = new SimpleDateFormat(COMMON_DB_DATE_STRING_FORMAT);
      String format = smf.format(now);
      System.out.println(Thread.currentThread().getName() + "->" + format);
    }).start();

    new Thread(() -> {
      Date now = DateFormatter.now();
      SimpleDateFormat smf = new SimpleDateFormat(COMMON_DB_DATE_STRING_FORMAT);
      String format = smf.format(now);
      System.out.println(Thread.currentThread().getName() + "->" + format);
    }).start();

    new Thread(() -> {
      Date now = DateFormatter.now();
      SimpleDateFormat smf = new SimpleDateFormat(COMMON_DB_DATE_STRING_FORMAT);
      String format = smf.format(now);
      System.out.println(Thread.currentThread().getName() + "->" + format);
    }).start();

  }

}

class DateFormatter {

  private static final String COMMON_DB_DATE_STRING_FORMAT = "yyyyMMddHHmmss.SSS";

  public static Date now() {
    Date now = new Date();
    SimpleDateFormat smf = new SimpleDateFormat(COMMON_DB_DATE_STRING_FORMAT);
    String format = smf.format(now);
    System.out.println(format);
    return now;
  }
}


