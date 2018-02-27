package com.zstar.SMMS.constant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil
{
  public static double compareDifference(String redu, String isRedu)
  {
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    double min = 0.0D;
    Date reduTime = null;
    Date isReduTime = null;
    try
    {
      reduTime = df.parse(redu);
      isReduTime = df.parse(isRedu);
    }
    catch (ParseException e)
    {
      e.printStackTrace();
    }
    long reduMesc = reduTime.getTime();
    long isReduMesc = isReduTime.getTime();
    
    double differen = reduMesc - isReduMesc;
    
    double total = differen / 1000.0D;
    
    min = total / 60.0D;
    
    return min;
  }
}
