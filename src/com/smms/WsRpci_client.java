package com.smms;

import java.rmi.Remote;
import java.rmi.RemoteException;

public abstract interface WsRpci_client
  extends Remote
{
  public abstract String wscall(String paramString1, String paramString2, String paramString3, byte[] paramArrayOfByte)
    throws RemoteException;
}
