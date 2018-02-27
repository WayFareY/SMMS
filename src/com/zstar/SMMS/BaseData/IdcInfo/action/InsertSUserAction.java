package com.zstar.SMMS.BaseData.IdcInfo.action;

import com.strutsframe.db.DBSqlSession;
import com.zstar.fmp.core.base.FMPContex;
import com.zstar.fmp.core.frame.action.FrameAction;
import com.zstar.fmp.core.frame.delegate.Md5Del;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

public class InsertSUserAction
  extends FrameAction
{
  public String bizExecute()
    throws Exception
  {
    String rid = (String)getWebData("RID");
    
    Map map = new HashMap();
    map.put("RID", rid);
    Map deptUser = new HashMap();
    Map roleUser = new HashMap();
    Map mapIdName = (Map)this.sqlSession.selectOne("IdcInfo.selectIdcId", map);
    String idcId = (String)mapIdName.get("IDC_ID");
    String idc_userid = FMPContex.getSystemProperty("IDC_USERID");
    
    roleUser.put("RID", FMPContex.getNewUUID());
    roleUser.put("ROLEID", FMPContex.getSystemProperty("IDC_ROLEID"));
    roleUser.put("USERID", idcId);
    roleUser.put("CREATORID", getWebData("CURR_USERID"));
    roleUser.put("CREATTIME", FMPContex.getCurrentTime());
    
    deptUser.put("RID", FMPContex.getNewUUID());
    deptUser.put("ORGID", getWebData("CURR_ORGID"));
    deptUser.put("DEPTID", FMPContex.getSystemProperty("IDC_DEPTID"));
    deptUser.put("USERID", idcId);
    deptUser.put("CREATORID", getWebData("CURR_USERID"));
    deptUser.put("CREATTIME", FMPContex.getCurrentTime());
    
    String IdcShortName = (String)mapIdName.get("IDC_SHORT_NAME");
    System.out.println("999999999" + IdcShortName);
    Map ridMap = new HashMap();
    String mima = Md5Del.MD5Create(idcId + "*" + "888888");
    
    ridMap.put("RID", FMPContex.getNewUUID());
    
    ridMap.put("USERID", idcId);
    ridMap.put("LOGINCODE", idcId);
    
    ridMap.put("LOGINPASSWORD", mima);
    
    ridMap.put("USERNAME", IdcShortName + "用户");
    ridMap.put("NICKNAME", IdcShortName + "用户");
    ridMap.put("ORGID", getWebData("CURR_ORGID"));
    
    ridMap.put("DEPTID", "000069DEPTID");
    ridMap.put("DEPTNAME", getWebData("CURR_DEPTNAME"));
    
    ridMap.put("ROLEID", getWebData("CURR_ROLEID"));
    ridMap.put("ROLENAME", getWebData("CURR_ROLENAME"));
    ridMap.put("MAINORGID ", getWebData("CRD_MAINORGID "));
    
    ridMap.put("CREATORID", getWebData("CURR_USERID"));
    ridMap.put("CREATTIME", FMPContex.getCurrentTime());
    ridMap.put("MODIFIEDTIME", FMPContex.getCurrentTime());
    ridMap.put("MODIFIERID", getWebData("CURR_USERID"));
    ridMap.put("LIABMAN", getWebData("CURR_USERID"));
    ridMap.put("RECORDSTATE", "0");
    ridMap.put("STATE", "2");
    
    Long num = (Long)this.sqlSession.selectOne("SUser.selectNum", ridMap);
    if (num.longValue() > 0L)
    {
      setMsg("用户已存在");
    }
    else
    {
      int result = this.sqlSession.insert("SUser.insertSave", ridMap);
      String userName = (String)ridMap.get("USERNAME");
      System.out.println("11111111111" + userName);
      this.sqlSession.insert("SDeptUser.insertSave", deptUser);
      this.sqlSession.insert("SRoleUser.insertSave", roleUser);
      if (result > 0) {
        setMsg("000:" + idcId);
      } else {
        setMsg("添加失败");
      }
    }
    return "empty";
  }
}
