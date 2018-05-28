package com.zstar.SMMS.constant;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ReGroupUtil
{
  public static List<Map> reGroupList(List<Map> oldList, List<Map> otherList)
  {
    Iterator<Map> it;
    for (Iterator localIterator1 = oldList.iterator(); localIterator1.hasNext(); it.hasNext())
    {
      Map map = (Map)localIterator1.next();
      it = otherList.iterator();
      continue;
      Map m = (Map)it.next();
      if ((m.containsKey("IDC_ID")) && (m.containsKey("ROOM_IDX")))
      {
        if ((m.get("IDC_ID").equals(map.get("IDC_ID"))) && (m.get("ROOM_IDX").equals(map.get("ROOM_IDX"))))
        {
          if ((map.containsKey("SHZ")) && (m.containsKey("SHZ"))) {
            map.put("SHZ", Long.valueOf(((Long)m.get("SHZ")).longValue() + ((Long)map.get("SHZ")).longValue()));
          } else {
            for (Object obj : m.keySet()) {
              map.put(obj, m.get(obj));
            }
          }
          it.remove();
        }
      }
      else if (m.containsKey("IDC_ID"))
      {
        if (m.get("IDC_ID").equals(map.get("IDC_ID")))
        {
          for (Object obj : m.keySet()) {
            map.put(obj, m.get(obj));
          }
          it.remove();
        }
      }
      else if ((m.containsKey("IDC_NAME")) && 
        (m.get("IDC_NAME").equals(map.get("IDC_NAME"))))
      {
        for (Object obj : m.keySet()) {
          map.put(obj, m.get(obj));
        }
        it.remove();
      }
    }
    if ((otherList.size() > 0) && (otherList != null) && (!otherList.isEmpty())) {
      oldList.addAll(otherList);
    }
    return oldList;
  }
  
  public static List addMap(List<Map> oldList)
  {
    for (Map map : oldList) {
      if (map.containsKey("THREAT_TYPE1"))
      {
        String type = (String)map.get("THREAT_TYPE1");
        if ("01".equals(type)) {
          map.put("INVADE", map.get("COUNT_THREAT_TYPE1"));
        } else if ("02".equals(type)) {
          map.put("HACKER", map.get("COUNT_THREAT_TYPE1"));
        } else if ("03".equals(type)) {
          map.put("BLACK_PROFIT", map.get("COUNT_THREAT_TYPE1"));
        } else if ("04".equals(type)) {
          map.put("INSIDER_ATTACK", map.get("COUNT_THREAT_TYPE1"));
        }
      }
      else if (map.containsKey("EVENT_TYPE1"))
      {
        String type = (String)map.get("EVENT_TYPE1");
        if ("vpno".equals(type)) {
          map.put("OVER_WALL", map.get("COUNT_EVENT_TYPE1"));
        }
        if ("proxy".equals(type)) {
          map.put("PROXY", map.get("COUNT_EVENT_TYPE1"));
        }
        if ("vpn".equals(type)) {
          map.put("VPN", map.get("COUNT_EVENT_TYPE1"));
        }
      }
      else if (map.containsKey("EVENT_NAME"))
      {
        String type = (String)map.get("EVENT_NAME");
        if ("SMB扫描".equals(type)) {
          map.put("SMBSM", map.get("EVENT_COUNT"));
        } else if ("漏洞扫描".equals(type)) {
          map.put("LDSM", map.get("EVENT_COUNT"));
        } else if ("webshell上传攻击".equals(type)) {
          map.put("webshellSCGJ", map.get("EVENT_COUNT"));
        } else if ("通用型C&C通信".equals(type)) {
          map.put("TYXTX", map.get("EVENT_COUNT"));
        } else if ("违规访问，蜜罐".equals(type)) {
          map.put("WGFWMG", map.get("EVENT_COUNT"));
        } else if ("恶意软件，Sality家族".equals(type)) {
          map.put("WYRJSalityJZ", map.get("EVENT_COUNT"));
        } else if ("恶意软件，Gbot家族".equals(type)) {
          map.put("WYRJGbotJZ", map.get("EVENT_COUNT"));
        } else if ("恶意软件，Sdbot家族".equals(type)) {
          map.put("EYRJSdbotJZ", map.get("EVENT_COUNT"));
        } else if ("恶意软件，Zeus家族".equals(type)) {
          map.put("EYRJZeusZJ", map.get("EVENT_COUNT"));
        } else if ("JCE僵尸网络".equals(type)) {
          map.put("JCEJSWL", map.get("EVENT_COUNT"));
        } else if ("IRC僵尸网络".equals(type)) {
          map.put("IRCJSWL", map.get("EVENT_COUNT"));
        } else if ("勒索软件，WannaCry".equals(type)) {
          map.put("LSRJWannaCry", map.get("EVENT_COUNT"));
        } else if ("飞客蠕虫".equals(type)) {
          map.put("FKRC", map.get("EVENT_COUNT"));
        } else if ("Ramnit家族".equals(type)) {
          map.put("RamnitJZ", map.get("EVENT_COUNT"));
        } else if ("LPK家族".equals(type)) {
          map.put("LPKJZ", map.get("EVENT_COUNT"));
        } else if ("DDOS家族".equals(type)) {
          map.put("DDOSJZ", map.get("EVENT_COUNT"));
        } else if ("XCodeGhost".equals(type)) {
          map.put("XCodeGhost", map.get("EVENT_COUNT"));
        } else if ("暗云木马III".equals(type)) {
          map.put("AYMMIII", map.get("EVENT_COUNT"));
        } else if ("DGA随机域名，LSTM算法，机器学习".equals(type)) {
          map.put("DGASJYMLSTMSFJQXX", map.get("EVENT_COUNT"));
        } else if ("勒索软件，Petya".equals(type)) {
          map.put("LSRJPetya", map.get("EVENT_COUNT"));
        } else if ("Sobig蠕虫".equals(type)) {
          map.put("SobigRC", map.get("EVENT_COUNT"));
        } else if ("Gamarue蠕虫".equals(type)) {
          map.put("GamarueRC", map.get("EVENT_COUNT"));
        } else if ("Nitol僵尸网络".equals(type)) {
          map.put("NitolJSWL", map.get("EVENT_COUNT"));
        } else if ("Virut僵尸网络".equals(type)) {
          map.put("VirutJSWL", map.get("EVENT_COUNT"));
        } else if ("魔鼬".equals(type)) {
          map.put("MY", map.get("EVENT_COUNT"));
        } else if ("webshell后门".equals(type)) {
          map.put("webshellHM", map.get("EVENT_COUNT"));
        } else if ("Xshell后门".equals(type)) {
          map.put("XshellHM", map.get("EVENT_COUNT"));
        } else if ("异常UA".equals(type)) {
          map.put("YCUA", map.get("EVENT_COUNT"));
        } else if ("风险访问".equals(type)) {
          map.put("FXFW", map.get("EVENT_COUNT"));
        } else if ("黑链".equals(type)) {
          map.put("HL", map.get("EVENT_COUNT"));
        } else if ("虚拟货币挖矿".equals(type)) {
          map.put("XNHBWK", map.get("EVENT_COUNT"));
        } else if ("DDOS攻击".equals(type)) {
          map.put("DDOSGJ", map.get("EVENT_COUNT"));
        } else if ("暴力破解".equals(type)) {
          map.put("BLPJ", map.get("EVENT_COUNT"));
        } else if ("webshell扫描".equals(type)) {
          map.put("webshellSM", map.get("EVENT_COUNT"));
        } else if ("web漏洞利用".equals(type)) {
          map.put("webLDLY", map.get("EVENT_COUNT"));
        } else if ("system漏洞利用".equals(type)) {
          map.put("systemLDLY", map.get("EVENT_COUNT"));
        } else if ("webshell上传攻击".equals(type)) {
          map.put("webshellSCGJ", map.get("EVENT_COUNT"));
        } else if ("SQL注入".equals(type)) {
          map.put("SQLZR", map.get("EVENT_COUNT"));
        } else if ("SMB协议扫描".equals(type)) {
          map.put("SMBXYSM", map.get("EVENT_COUNT"));
        } else if ("web整站系统漏洞攻击".equals(type)) {
          map.put("webZZXTLDGJ", map.get("EVENT_COUNT"));
        } else if ("XSS攻击".equals(type)) {
          map.put("XSSGJ", map.get("EVENT_COUNT"));
        } else if ("目录遍历攻击".equals(type)) {
          map.put("MLBLGJ", map.get("EVENT_COUNT"));
        } else if ("网站扫描".equals(type)) {
          map.put("WZSM", map.get("EVENT_COUNT"));
        } else if ("漏洞扫描".equals(type)) {
          map.put("LDSM", map.get("EVENT_COUNT"));
        } else if ("恶意链接".equals(type)) {
          map.put("EYLJ", map.get("EVENT_COUNT"));
        } else if ("暴力破解".equals(type)) {
          map.put("BLPJ", map.get("EVENT_COUNT"));
        } else if ("勒索病毒".equals(type)) {
          map.put("LSBD", map.get("EVENT_COUNT"));
        } else if ("端口扫描".equals(type)) {
          map.put("DKSM", map.get("EVENT_COUNT"));
        } else if ("IP扫描".equals(type)) {
          map.put("IPSM", map.get("EVENT_COUNT"));
        } else if ("SQL注入".equals(type)) {
          map.put("SQLZR", map.get("EVENT_COUNT"));
        } else if ("web漏洞利用".equals(type)) {
          map.put("webLDLY", map.get("EVENT_COUNT"));
        } else if ("system漏洞利用".equals(type)) {
          map.put("systemLDLY", map.get("EVENT_COUNT"));
        } else if ("webshell上传".equals(type)) {
          map.put("webshellSC", map.get("EVENT_COUNT"));
        } else if ("SMB协议扫描".equals(type)) {
          map.put("SMBXYSM", map.get("EVENT_COUNT"));
        } else if ("SMB协议攻击".equals(type)) {
          map.put("SMBXYGJ", map.get("EVENT_COUNT"));
        }
      }
    }
    return oldList;
  }
  
  public static void main(String[] args)
  {
    List<Map> list1 = new ArrayList();
    Map map1 = new HashMap();
    map1.put("IDC_ID", "0756001");
    map1.put("ROOM_IDX", "001");
    
    map1.put("THREAT_TYPE1", "01");
    map1.put("COUNT_THREAT_TYPE1", "3");
    map1.put("d", "4");
    list1.add(map1);
    Map map2 = new HashMap();
    map2.put("IDC_ID", "0756002");
    map2.put("ROOM_IDX", "002");
    map2.put("THREAT_TYPE1", "vpn");
    map2.put("COUNT_THREAT_TYPE1", "4");
    map2.put("b", "22");
    map2.put("c", "33");
    map2.put("d", "44");
    list1.add(map2);
    
    List<Map> list2 = new ArrayList();
    list2.addAll(list1);
    
    Map map23 = new HashMap();
    map23.put("IDC_ID", "IDC_IDtest");
    map23.put("e", "teste");
    map23.put("f", "testf");
    map23.put("g", "testg");
    list2.add(map23);
    String type;
    for (Map map : list1) {
      if (map.containsKey("THREAT_TYPE1"))
      {
        type = (String)map.get("THREAT_TYPE1");
        if ("01".equals(type)) {
          map.put("INVADE", map.get("COUNT_THREAT_TYPE1"));
        }
        if ("02".equals(type)) {
          map.put("HACKER", map.get("COUNT_THREAT_TYPE1"));
        }
        if ("03".equals(type)) {
          map.put("BLACK_PROFIT", map.get("COUNT_THREAT_TYPE1"));
        }
        if ("04".equals(type)) {
          map.put("INSIDER_ATTACK", map.get("COUNT_THREAT_TYPE1"));
        }
      }
    }
    List<Map> list = reGroupList(list1, list2);
    for (Map map : list)
    {
      for (Object obj : map.keySet()) {
        System.out.println("key:" + obj.toString() + " value:" + map.get(obj).toString());
      }
      System.out.println("--------------------------------");
    }
  }
}
