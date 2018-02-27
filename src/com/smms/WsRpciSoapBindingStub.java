package com.smms;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;
import javax.xml.namespace.QName;
import org.apache.axis.AxisFault;
import org.apache.axis.client.Call;
import org.apache.axis.client.Stub;
import org.apache.axis.constants.Style;
import org.apache.axis.constants.Use;
import org.apache.axis.description.OperationDesc;
import org.apache.axis.description.ParameterDesc;

public class WsRpciSoapBindingStub
  extends Stub
  implements WsRpci_client
{
  private Vector cachedSerClasses = new Vector();
  private Vector cachedSerQNames = new Vector();
  private Vector cachedSerFactories = new Vector();
  private Vector cachedDeserFactories = new Vector();
  static OperationDesc[] _operations = new OperationDesc[1];
  
  static
  {
    _initOperationDesc1();
  }
  
  private static void _initOperationDesc1()
  {
    OperationDesc oper = new OperationDesc();
    oper.setName("wscall");
    ParameterDesc param = new ParameterDesc(new QName("http://smms.com", "tokenid"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
    oper.addParameter(param);
    param = new ParameterDesc(new QName("http://smms.com", "rpc_code"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
    oper.addParameter(param);
    param = new ParameterDesc(new QName("http://smms.com", "rpc_json"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
    oper.addParameter(param);
    param = new ParameterDesc(new QName("http://smms.com", "rpc_byte"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "base64Binary"), byte[].class, false, false);
    oper.addParameter(param);
    oper.setReturnType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
    oper.setReturnClass(String.class);
    oper.setReturnQName(new QName("http://smms.com", "wscallReturn"));
    oper.setStyle(Style.WRAPPED);
    oper.setUse(Use.LITERAL);
    _operations[0] = oper;
  }
  
  public WsRpciSoapBindingStub()
    throws AxisFault
  {
    this(null);
  }
  
  public WsRpciSoapBindingStub(URL endpointURL, javax.xml.rpc.Service service)
    throws AxisFault
  {
    this(service);
    this.cachedEndpoint = endpointURL;
  }
  
  public WsRpciSoapBindingStub(javax.xml.rpc.Service service)
    throws AxisFault
  {
    if (service == null) {
      this.service = new org.apache.axis.client.Service();
    } else {
      this.service = service;
    }
    ((org.apache.axis.client.Service)this.service).setTypeMappingVersion("1.2");
  }
  
  protected Call createCall()
    throws RemoteException
  {
    try
    {
      Call _call = super._createCall();
      if (this.maintainSessionSet) {
        _call.setMaintainSession(this.maintainSession);
      }
      if (this.cachedUsername != null) {
        _call.setUsername(this.cachedUsername);
      }
      if (this.cachedPassword != null) {
        _call.setPassword(this.cachedPassword);
      }
      if (this.cachedEndpoint != null) {
        _call.setTargetEndpointAddress(this.cachedEndpoint);
      }
      if (this.cachedTimeout != null) {
        _call.setTimeout(this.cachedTimeout);
      }
      if (this.cachedPortName != null) {
        _call.setPortName(this.cachedPortName);
      }
      Enumeration keys = this.cachedProperties.keys();
      while (keys.hasMoreElements())
      {
        String key = (String)keys.nextElement();
        _call.setProperty(key, this.cachedProperties.get(key));
      }
      return _call;
    }
    catch (Throwable _t)
    {
      throw new AxisFault("Failure trying to get the Call object", _t);
    }
  }
  
  /* Error */
  public String wscall(String tokenid, String rpc_code, String rpc_json, byte[] rpc_byte)
    throws RemoteException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 115	org/apache/axis/client/Stub:cachedEndpoint	Ljava/net/URL;
    //   4: ifnonnull +11 -> 15
    //   7: new 241	org/apache/axis/NoEndPointException
    //   10: dup
    //   11: invokespecial 243	org/apache/axis/NoEndPointException:<init>	()V
    //   14: athrow
    //   15: aload_0
    //   16: invokevirtual 244	com/smms/WsRpciSoapBindingStub:createCall	()Lorg/apache/axis/client/Call;
    //   19: astore 5
    //   21: aload 5
    //   23: getstatic 19	com/smms/WsRpciSoapBindingStub:_operations	[Lorg/apache/axis/description/OperationDesc;
    //   26: iconst_0
    //   27: aaload
    //   28: invokevirtual 246	org/apache/axis/client/Call:setOperation	(Lorg/apache/axis/description/OperationDesc;)V
    //   31: aload 5
    //   33: iconst_1
    //   34: invokevirtual 250	org/apache/axis/client/Call:setUseSOAPAction	(Z)V
    //   37: aload 5
    //   39: ldc -3
    //   41: invokevirtual 255	org/apache/axis/client/Call:setSOAPActionURI	(Ljava/lang/String;)V
    //   44: aload 5
    //   46: aconst_null
    //   47: invokevirtual 258	org/apache/axis/client/Call:setEncodingStyle	(Ljava/lang/String;)V
    //   50: aload 5
    //   52: ldc_w 261
    //   55: getstatic 263	java/lang/Boolean:FALSE	Ljava/lang/Boolean;
    //   58: invokevirtual 219	org/apache/axis/client/Call:setProperty	(Ljava/lang/String;Ljava/lang/Object;)V
    //   61: aload 5
    //   63: ldc_w 269
    //   66: getstatic 263	java/lang/Boolean:FALSE	Ljava/lang/Boolean;
    //   69: invokevirtual 219	org/apache/axis/client/Call:setProperty	(Ljava/lang/String;Ljava/lang/Object;)V
    //   72: aload 5
    //   74: getstatic 271	org/apache/axis/soap/SOAPConstants:SOAP11_CONSTANTS	Lorg/apache/axis/soap/SOAP11Constants;
    //   77: invokevirtual 277	org/apache/axis/client/Call:setSOAPVersion	(Lorg/apache/axis/soap/SOAPConstants;)V
    //   80: aload 5
    //   82: new 37	javax/xml/namespace/QName
    //   85: dup
    //   86: ldc 39
    //   88: ldc 29
    //   90: invokespecial 43	javax/xml/namespace/QName:<init>	(Ljava/lang/String;Ljava/lang/String;)V
    //   93: invokevirtual 281	org/apache/axis/client/Call:setOperationName	(Ljavax/xml/namespace/QName;)V
    //   96: aload_0
    //   97: aload 5
    //   99: invokevirtual 284	com/smms/WsRpciSoapBindingStub:setRequestHeaders	(Lorg/apache/axis/client/Call;)V
    //   102: aload_0
    //   103: aload 5
    //   105: invokevirtual 288	com/smms/WsRpciSoapBindingStub:setAttachments	(Lorg/apache/axis/client/Call;)V
    //   108: aload 5
    //   110: iconst_4
    //   111: anewarray 291	java/lang/Object
    //   114: dup
    //   115: iconst_0
    //   116: aload_1
    //   117: aastore
    //   118: dup
    //   119: iconst_1
    //   120: aload_2
    //   121: aastore
    //   122: dup
    //   123: iconst_2
    //   124: aload_3
    //   125: aastore
    //   126: dup
    //   127: iconst_3
    //   128: aload 4
    //   130: aastore
    //   131: invokevirtual 293	org/apache/axis/client/Call:invoke	([Ljava/lang/Object;)Ljava/lang/Object;
    //   134: astore 6
    //   136: aload 6
    //   138: instanceof 149
    //   141: ifeq +9 -> 150
    //   144: aload 6
    //   146: checkcast 149	java/rmi/RemoteException
    //   149: athrow
    //   150: aload_0
    //   151: aload 5
    //   153: invokevirtual 297	com/smms/WsRpciSoapBindingStub:extractAttachments	(Lorg/apache/axis/client/Call;)V
    //   156: aload 6
    //   158: checkcast 50	java/lang/String
    //   161: areturn
    //   162: astore 7
    //   164: aload 6
    //   166: ldc 50
    //   168: invokestatic 300	org/apache/axis/utils/JavaUtils:convert	(Ljava/lang/Object;Ljava/lang/Class;)Ljava/lang/Object;
    //   171: checkcast 50	java/lang/String
    //   174: areturn
    //   175: astore 6
    //   177: aload 6
    //   179: athrow
    // Line number table:
    //   Java source line #97	-> byte code offset #0
    //   Java source line #98	-> byte code offset #7
    //   Java source line #100	-> byte code offset #15
    //   Java source line #101	-> byte code offset #21
    //   Java source line #102	-> byte code offset #31
    //   Java source line #103	-> byte code offset #37
    //   Java source line #104	-> byte code offset #44
    //   Java source line #105	-> byte code offset #50
    //   Java source line #106	-> byte code offset #61
    //   Java source line #107	-> byte code offset #72
    //   Java source line #108	-> byte code offset #80
    //   Java source line #110	-> byte code offset #96
    //   Java source line #111	-> byte code offset #102
    //   Java source line #112	-> byte code offset #108
    //   Java source line #114	-> byte code offset #136
    //   Java source line #115	-> byte code offset #144
    //   Java source line #118	-> byte code offset #150
    //   Java source line #120	-> byte code offset #156
    //   Java source line #121	-> byte code offset #162
    //   Java source line #122	-> byte code offset #164
    //   Java source line #125	-> byte code offset #175
    //   Java source line #126	-> byte code offset #177
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	180	0	this	WsRpciSoapBindingStub
    //   0	180	1	tokenid	String
    //   0	180	2	rpc_code	String
    //   0	180	3	rpc_json	String
    //   0	180	4	rpc_byte	byte[]
    //   19	133	5	_call	Call
    //   134	31	6	_resp	Object
    //   175	3	6	axisFaultException	AxisFault
    //   162	3	7	_exception	Exception
    // Exception table:
    //   from	to	target	type
    //   156	161	162	java/lang/Exception
    //   108	161	175	org/apache/axis/AxisFault
    //   162	174	175	org/apache/axis/AxisFault
  }
}
