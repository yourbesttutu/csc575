package com.example.testrssi;

import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class WifiListActivity extends Activity {  
	  
    private WifiManager wifiManager;  
    List<ScanResult> list; 
    
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_wifi_list);  
        init();  
    }  
  
    private void init() {  
        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);  
          
        openWifi();  
        list = wifiManager.getScanResults(); 
         ListView listView = (ListView) findViewById(R.id.listView);  
        if (list == null) {  
            Toast.makeText(this, "wifi未打开！", Toast.LENGTH_LONG).show();  
        }else {  
            listView.setAdapter(new MyAdapter(this,list));
            listView.setOnItemClickListener(new OnItemClickListener() {  
            	  
                @Override  
                public void onItemClick(AdapterView<?> parent, View arg1, int arg2,  
                        long arg3) {  
                	//ListView listViews = (ListView)parent;
                    //setTitle("点击第"+arg2+"个项目");
                	 ScanResult scanResult = list.get(arg2);
                	 Toast.makeText(WifiListActivity.this, scanResult.BSSID, Toast.LENGTH_LONG).show();
                	Intent in = new Intent(WifiListActivity.this, Move.class);
                	in.putExtra("result",scanResult.BSSID); 
        			startActivity(in);
                }

            });  

        }  
          
    }  
      
    /** 
     *  打开WIFI 
     */  
    private void openWifi() {  
       if (!wifiManager.isWifiEnabled()) {  
        wifiManager.setWifiEnabled(true);  
       }  
        
    }  
  
    public class MyAdapter extends BaseAdapter {  
  
        LayoutInflater inflater;  
        List<ScanResult> list;  
        public MyAdapter(Context context, List<ScanResult> list) {  
            // TODO Auto-generated constructor stub  
            this.inflater = LayoutInflater.from(context);  
            this.list = list;  
        }  
  
        @Override  
        public int getCount() {  
            // TODO Auto-generated method stub  
            return list.size();  
        }  
  
        @Override  
        public Object getItem(int position) {  
            // TODO Auto-generated method stub  
            return position;  
        }  
  
        @Override  
        public long getItemId(int position) {  
            // TODO Auto-generated method stub  
            return position;  
        }  
  
        @Override  
        public View getView(int position, View convertView, ViewGroup parent) {  
            // TODO Auto-generated method stub 
        	/*
       getBSSID() 获取BSSID
       getDetailedStateOf() 获取客户端的连通性
       getHiddenSSID() 获得SSID 是否被隐藏
       getIpAddress() 获取IP 地址
       getLinkSpeed() 获得连接的速度
       getMacAddress() 获得Mac 地址
       getRssi() 获得802.11n 网络的信号
       getSSID() 获得SSID
       getSupplicanState() 返回具体客户端状态的信息

        	 */
            View view = null;  
            view = inflater.inflate(R.layout.item_wifi_list, null);  
            ScanResult scanResult = list.get(position);   
            TextView imformation = (TextView) view.findViewById(R.id.imformation);
            //signalStrenth.setText(String.valueOf(Math.abs(scanResult.level)));
            
             
            StringBuffer mStringBuffer = null;
            mStringBuffer = new StringBuffer(); 
            //level 转换rssi 说明http://www.zhihu.com/question/21106590
            //RSSI = level - NoiceFloor 
            //NoiceFloor一般取-96dBm
            //这样如果 level 是 -60dBm, RSSI 就是 36
            mStringBuffer = mStringBuffer.append("NO.").append(position + 1)   
                    .append(" \nSSID:").append(scanResult.SSID).append(" \nBSSID：")   
                    .append(scanResult.BSSID).append(" \ncapabilities：")   
                    .append(scanResult.capabilities).append(" \nfrequency：")   
                    .append(scanResult.frequency).append("\nRSSI：")   
                    .append(scanResult.level).append(" \ndescribeContents：")   
                    .append(scanResult.describeContents()).append("\n\n");
            imformation.setText(mStringBuffer.toString());
            //mLabelWifi.setText(mStringBuffer.toString());
            //System.out.println(mStringBuffer.toString());
            
            return view;  
        }

  
    }  
  
} 
