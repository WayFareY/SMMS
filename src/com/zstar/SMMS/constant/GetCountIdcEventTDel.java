package com.zstar.SMMS.constant;

import com.opensymphony.xwork2.ActionContext;
import com.strutsframe.db.DBSqlSession;
import com.zstar.fmp.core.base.FMPContex;
import com.zstar.fmp.core.base.delegate.BaseDelegate;
import com.zstar.fmp.log.FMPLog;
import java.util.List;
import java.util.Map;

public class GetCountIdcEventTDel
  extends BaseDelegate
{
  public GetCountIdcEventTDel(ActionContext contex)
  {
    super(contex);
  }
  
  public List<Map> getCountWebCaseRidNull()
  {
    List<Map> list = this.sqlSession.selectList("SmmsEventMain.getCountWebCaseRidNull");
    return list;
  }
  
  public List<Map> getCountThreatType4Sk()
  {
    List<Map> list = this.sqlSession.selectList("SmmsEventMain.getCountThreatType4Sk");
    return list;
  }
  
  public List<Map> getCountThreatType4Sz()
  {
    List<Map> list = this.sqlSession.selectList("SmmsEventMain.getCountThreatType4Sz");
    return list;
  }
  
  public List<Map> getCountThreatType4Sw()
  {
    List<Map> list = this.sqlSession.selectList("SmmsEventMain.getCountThreatType4Sw");
    return list;
  }
  
  public List<Map> getCountThreatType4Sh()
  {
    List<Map> list = this.sqlSession.selectList("SmmsEventMain.getCountThreatType4Sh");
    return list;
  }
  
  public List<Map> getCountThreatType4Sd()
  {
    List<Map> list = this.sqlSession.selectList("SmmsEventMain.getCountThreatType4Sd");
    return list;
  }
  
  public List<Map> getCountThreatType4Sdu()
  {
    List<Map> list = this.sqlSession.selectList("SmmsEventMain.getCountThreatType4Sdu");
    return list;
  }
  
  public List<Map> getCountEventType1Vpno()
  {
    List<Map> list = this.sqlSession.selectList("SmmsEventMain.getCountEventType1Vpno");
    return list;
  }
  
  public List<Map> getCountZSRQ()
  {
    List<Map> list = this.sqlSession.selectList("SmmsEventMain.getCountZSRQ");
    return list;
  }
  
  public List<Map> getCountHCML()
  {
    List<Map> list = this.sqlSession.selectList("SmmsEventMain.getCountHCML");
    return list;
  }
  
  public List<Map> getCountHKKZ()
  {
    List<Map> list = this.sqlSession.selectList("SmmsEventMain.getCountHKKZ");
    return list;
  }
  
  public List<Map> getCountNBGJ()
  {
    List<Map> list = this.sqlSession.selectList("SmmsEventMain.getCountNBGJ");
    return list;
  }
  
  public List<Map> getCountWebCaseRidNullCLZ()
  {
    List<Map> list = this.sqlSession.selectList("SmmsEventMain.getCountWebCaseRidNullCLZ");
    return list;
  }
  
  public List<Map> getCountThreatType4SkCLZ()
  {
    List<Map> list = this.sqlSession.selectList("SmmsEventMain.getCountThreatType4SkCLZ");
    return list;
  }
  
  public List<Map> getCountThreatType4SzCLZ()
  {
    List<Map> list = this.sqlSession.selectList("SmmsEventMain.getCountThreatType4SzCLZ");
    return list;
  }
  
  public List<Map> getCountThreatType4SwCLZ()
  {
    List<Map> list = this.sqlSession.selectList("SmmsEventMain.getCountThreatType4SwCLZ");
    return list;
  }
  
  public List<Map> getCountThreatType4ShCLZ()
  {
    List<Map> list = this.sqlSession.selectList("SmmsEventMain.getCountThreatType4ShCLZ");
    return list;
  }
  
  public List<Map> getCountThreatType4SdCLZ()
  {
    List<Map> list = this.sqlSession.selectList("SmmsEventMain.getCountThreatType4SdCLZ");
    return list;
  }
  
  public List<Map> getCountThreatType4SduCLZ()
  {
    List<Map> list = this.sqlSession.selectList("SmmsEventMain.getCountThreatType4SduCLZ");
    return list;
  }
  
  public List<Map> getCountEventType1VpnoCLZ()
  {
    List<Map> list = this.sqlSession.selectList("SmmsEventMain.getCountEventType1VpnoCLZ");
    return list;
  }
  
  public List<Map> getCountZSRQCLZ()
  {
    List<Map> list = this.sqlSession.selectList("SmmsEventMain.getCountZSRQCLZ");
    return list;
  }
  
  public List<Map> getCountHCMLCLZ()
  {
    List<Map> list = this.sqlSession.selectList("SmmsEventMain.getCountHCMLCLZ");
    return list;
  }
  
  public List<Map> getCountHKKZCLZ()
  {
    List<Map> list = this.sqlSession.selectList("SmmsEventMain.getCountHKKZCLZ");
    return list;
  }
  
  public List<Map> getCountNBGJCLZ()
  {
    List<Map> list = this.sqlSession.selectList("SmmsEventMain.getCountNBGJCLZ");
    return list;
  }
  
  public List<Map> getCountWebCaseRidNullYCL()
  {
    List<Map> list = this.sqlSession.selectList("SmmsEventMain.getCountWebCaseRidNullYCL");
    return list;
  }
  
  public List<Map> getCountThreatType4SkYCL()
  {
    List<Map> list = this.sqlSession.selectList("SmmsEventMain.getCountThreatType4SkYCL");
    return list;
  }
  
  public List<Map> getCountThreatType4SzYCL()
  {
    List<Map> list = this.sqlSession.selectList("SmmsEventMain.getCountThreatType4SzYCL");
    return list;
  }
  
  public List<Map> getCountThreatType4SwYCL()
  {
    List<Map> list = this.sqlSession.selectList("SmmsEventMain.getCountThreatType4SwYCL");
    return list;
  }
  
  public List<Map> getCountThreatType4ShYCL()
  {
    List<Map> list = this.sqlSession.selectList("SmmsEventMain.getCountThreatType4ShYCL");
    return list;
  }
  
  public List<Map> getCountThreatType4SdYCL()
  {
    List<Map> list = this.sqlSession.selectList("SmmsEventMain.getCountThreatType4SdYCL");
    return list;
  }
  
  public List<Map> getCountThreatType4SduYCL()
  {
    List<Map> list = this.sqlSession.selectList("SmmsEventMain.getCountThreatType4SduYCL");
    return list;
  }
  
  public List<Map> getCountEventType1VpnoYCL()
  {
    List<Map> list = this.sqlSession.selectList("SmmsEventMain.getCountEventType1VpnoYCL");
    return list;
  }
  
  public List<Map> getCountZSRQYCL()
  {
    List<Map> list = this.sqlSession.selectList("SmmsEventMain.getCountZSRQYCL");
    return list;
  }
  
  public List<Map> getCountHCMLYCL()
  {
    List<Map> list = this.sqlSession.selectList("SmmsEventMain.getCountHCMLYCL");
    return list;
  }
  
  public List<Map> getCountHKKZYCL()
  {
    List<Map> list = this.sqlSession.selectList("SmmsEventMain.getCountHKKZYCL");
    return list;
  }
  
  public List<Map> getCountNBGJYCL()
  {
    List<Map> list = this.sqlSession.selectList("SmmsEventMain.getCountNBGJYCL");
    return list;
  }
  
  public void insertSmmsIdcEvent()
  {
    List<Map> oldList = getCountWebCaseRidNull();
    
    List<Map> getCountThreatType4SkList = getCountThreatType4Sk();
    oldList = ReGroupUtil.reGroupList(oldList, getCountThreatType4SkList);
    
    List<Map> getCountThreatType4SzList = getCountThreatType4Sz();
    oldList = ReGroupUtil.reGroupList(oldList, getCountThreatType4SzList);
    
    List<Map> getCountThreatType4SwList = getCountThreatType4Sw();
    oldList = ReGroupUtil.reGroupList(oldList, getCountThreatType4SwList);
    
    List<Map> getCountThreatType4ShList = getCountThreatType4Sh();
    oldList = ReGroupUtil.reGroupList(oldList, getCountThreatType4ShList);
    
    List<Map> getCountThreatType4SdList = getCountThreatType4Sd();
    oldList = ReGroupUtil.reGroupList(oldList, getCountThreatType4SdList);
    
    List<Map> getCountThreatType4SduList = getCountThreatType4Sdu();
    oldList = ReGroupUtil.reGroupList(oldList, getCountThreatType4SduList);
    
    List<Map> getCountEventType1VpnoList = getCountEventType1Vpno();
    oldList = ReGroupUtil.reGroupList(oldList, getCountEventType1VpnoList);
    
    List<Map> getCountZSRQList = getCountZSRQ();
    oldList = ReGroupUtil.reGroupList(oldList, getCountZSRQList);
    
    List<Map> getCountHCMLList = getCountHCML();
    oldList = ReGroupUtil.reGroupList(oldList, getCountHCMLList);
    
    List<Map> getCountHKKZList = getCountHKKZ();
    oldList = ReGroupUtil.reGroupList(oldList, getCountHKKZList);
    
    List<Map> getCountNBGJList = getCountNBGJ();
    oldList = ReGroupUtil.reGroupList(oldList, getCountNBGJList);
    
    List<Map> getCountWebCaseRidNullCLZList = getCountWebCaseRidNullCLZ();
    oldList = ReGroupUtil.reGroupList(oldList, getCountWebCaseRidNullCLZList);
    
    List<Map> getCountThreatType4SkCLZList = getCountThreatType4SkCLZ();
    oldList = ReGroupUtil.reGroupList(oldList, getCountThreatType4SkCLZList);
    
    List<Map> getCountThreatType4SzCLZList = getCountThreatType4SzCLZ();
    oldList = ReGroupUtil.reGroupList(oldList, getCountThreatType4SzCLZList);
    
    List<Map> getCountThreatType4SwCLZList = getCountThreatType4SwCLZ();
    oldList = ReGroupUtil.reGroupList(oldList, getCountThreatType4SwCLZList);
    
    List<Map> getCountThreatType4ShCLZList = getCountThreatType4ShCLZ();
    oldList = ReGroupUtil.reGroupList(oldList, getCountThreatType4ShCLZList);
    
    List<Map> getCountThreatType4SdCLZList = getCountThreatType4SdCLZ();
    oldList = ReGroupUtil.reGroupList(oldList, getCountThreatType4SdCLZList);
    
    List<Map> getCountThreatType4SduCLZList = getCountThreatType4SduCLZ();
    oldList = ReGroupUtil.reGroupList(oldList, getCountThreatType4SduCLZList);
    
    List<Map> getCountEventType1VpnoCLZList = getCountEventType1VpnoCLZ();
    oldList = ReGroupUtil.reGroupList(oldList, getCountEventType1VpnoCLZList);
    
    List<Map> getCountZSRQCLZList = getCountZSRQCLZ();
    oldList = ReGroupUtil.reGroupList(oldList, getCountZSRQCLZList);
    
    List<Map> getCountHCMLCLZLiist = getCountHCMLCLZ();
    oldList = ReGroupUtil.reGroupList(oldList, getCountHCMLCLZLiist);
    
    List<Map> getCountHKKZCLZList = getCountHKKZCLZ();
    oldList = ReGroupUtil.reGroupList(oldList, getCountHKKZCLZList);
    
    List<Map> getCountNBGJCLZList = getCountNBGJCLZ();
    oldList = ReGroupUtil.reGroupList(oldList, getCountNBGJCLZList);
    
    List<Map> getCountWebCaseRidNullYCLList = getCountWebCaseRidNullYCL();
    oldList = ReGroupUtil.reGroupList(oldList, getCountWebCaseRidNullYCLList);
    
    List<Map> getCountThreatType4SkYCLList = getCountThreatType4SkYCL();
    oldList = ReGroupUtil.reGroupList(oldList, getCountThreatType4SkYCLList);
    
    List<Map> getCountThreatType4SzYCLList = getCountThreatType4SzYCL();
    oldList = ReGroupUtil.reGroupList(oldList, getCountThreatType4SzYCLList);
    
    List<Map> getCountThreatType4SwYCLList = getCountThreatType4SwYCL();
    oldList = ReGroupUtil.reGroupList(oldList, getCountThreatType4SwYCLList);
    
    List<Map> getCountThreatType4ShYCLList = getCountThreatType4ShYCL();
    oldList = ReGroupUtil.reGroupList(oldList, getCountThreatType4ShYCLList);
    
    List<Map> getCountThreatType4SdYCLList = getCountThreatType4SdYCL();
    oldList = ReGroupUtil.reGroupList(oldList, getCountThreatType4SdYCLList);
    
    List<Map> getCountThreatType4SduYCLList = getCountThreatType4SduYCL();
    oldList = ReGroupUtil.reGroupList(oldList, getCountThreatType4SduYCLList);
    
    List<Map> getCountEventType1VpnoYCLList = getCountEventType1VpnoYCL();
    oldList = ReGroupUtil.reGroupList(oldList, getCountEventType1VpnoYCLList);
    
    List<Map> getCountZSRQYCLList = getCountZSRQYCL();
    oldList = ReGroupUtil.reGroupList(oldList, getCountZSRQYCLList);
    
    List<Map> getCountHCMLYCLList = getCountHCMLYCL();
    oldList = ReGroupUtil.reGroupList(oldList, getCountHCMLYCLList);
    
    List<Map> getCountHKKZYCLList = getCountHKKZYCL();
    oldList = ReGroupUtil.reGroupList(oldList, getCountHKKZYCLList);
    
    List<Map> getCountNBGJYCLList = getCountNBGJYCL();
    oldList = ReGroupUtil.reGroupList(oldList, getCountNBGJYCLList);
    if ((!oldList.isEmpty()) && (oldList.size() > 0))
    {
      int deleteResult = this.sqlSession.delete("SmmsIdcEvent.delete");
      
      int sum = 0;
      for (Map eventStatMap : oldList)
      {
        eventStatMap.put("RID", FMPContex.getNewUUID());
        
        eventStatMap.put("CREATTIME", FMPContex.getCurrentTime());
        
        eventStatMap.put("RECORDSTATE", "0");
        
        int j = this.sqlSession.insert("SmmsIdcEvent.insertSave", eventStatMap);
        sum += j;
      }
      FMPLog.debug("插入idc汇总表共" + sum + "条数据");
    }
  }
}
