package com.nano.lanshare.socket.net;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;


public class TCPSocketRequest extends SocketRequest
{
    public static interface TCPRequestCallback 
    {
        abstract void onStart();
        abstract void onError(String errorInfo);
        abstract void onFinish();
        abstract void onProgressUpdate(int sizeTransferred);
    }
    
    private Socket mSocket = null;
    private TCPRequestCallback mCallback = null;
    private int mTargetPort = 0;
    private InetAddress mTargetAddress = null;
    private InputStream mInputStream = null;
    
    public TCPSocketRequest(int port, InetAddress addr)
    {
        mTargetPort = port;
        mTargetAddress = addr;
    }
    
    public void setCallBack(TCPRequestCallback callback)
    {
        mCallback = callback;
    }
    
    @Override
    public void startRequst()
    {
        try
        {
            mSocket = new Socket(mTargetAddress, mTargetPort);
            mSocket.setSoTimeout(mTimeout);
            
            OutputStream os = mSocket.getOutputStream();
            
            //read the input stream by unit of 4K
            byte[] buffer = new byte[4*1024]; 
            int len = 0;
            
            if (mCallback != null)
            {
                mCallback.onStart();
            }
            
            int time = 0;
            while((len = mInputStream.read(buffer)) != -1)
            {
                os.write(buffer, 0, len);                
                
                //need to update progress 
                mCallback.onProgressUpdate(4096*(time++) + len);
            }
            
            os.flush();
        } 
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                mCallback.onFinish();
                mSocket.close();
            } 
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

    }
    
    public void setData(InputStream istream)
    {
        mInputStream = istream;
    }
    
    public void setData(File file)
    {
        try
        {
            mInputStream = new FileInputStream(file);
        } 
        catch (FileNotFoundException e)
        {
            if (mCallback != null)
            {
                //TODO: define the message in a separate file
                mCallback.onError("Cannot open the file");
            }
            e.printStackTrace();
        }
    }
    
}
