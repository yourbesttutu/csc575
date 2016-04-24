package com.example.testrssi;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.testrssi.WifiListActivity.MyAdapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SaveFile extends Activity {
	private EditText numberOfTime;
	private EditText file;
	private EditText location;
	private EditText interval;
	private TextView showTextView;
	private ListView listView;
	// 要保存的文件名
	private String fileName = "save.txt";
	private StringBuffer content;
	List<String> list;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.save_file);
		// 获取页面中的组件
		numberOfTime = (EditText) findViewById(R.id.addText);
		file = (EditText) findViewById(R.id.filename);
		location = (EditText) findViewById(R.id.location);
		interval = (EditText) findViewById(R.id.interval);
		showTextView = (TextView) findViewById(R.id.showText);
		Button addButton = (Button) this.findViewById(R.id.addButton);
		listView = (ListView) findViewById(R.id.listView);
		list=new ArrayList<String>();
		// Button showButton = (Button) this.findViewById(R.id.showButton);
		// 绑定单击事件
		addButton.setOnClickListener(listener);
		// showButton.setOnClickListener(listener);

	}

	// 声明监听器
	private View.OnClickListener listener = new OnClickListener() {
		public void onClick(View v) {
			Button view = (Button) v;
			switch (view.getId()) {
			case R.id.addButton:
				save();
				break;
			/*
			 * case R.id.showButton: read(); break;
			 */
			}

		}

	};

	/**
	 * @author chenzheng_Java 保存用户输入的内容到文件
	 */
	@SuppressLint("NewApi")
	private void save() {
		content = new StringBuffer();
		int times = Integer.parseInt(numberOfTime.getText().toString());
		int interval_ms = Integer.parseInt(interval.getText().toString());
		while (times > 0) {
			WifiInfo info = ((WifiManager) getSystemService(WIFI_SERVICE))
					.getConnectionInfo();

			/*
			 * info.getBSSID()； 获取BSSID地址。 info.getSSID()； 获取SSID地址。 需要连接网络的ID
			 * info.getIpAddress()； 获取IP地址。4字节Int, XXX.XXX.XXX.XXX 每个XXX为一个字节
			 * info.getMacAddress()； 获取MAC地址。 info.getNetworkId()； 获取网络ID。
			 * info.getLinkSpeed()； 获取连接速度，可以让用户获知这一信息。 info.getRssi()；
			 * 获取RSSI，RSSI就是接受信号强度指示
			 */

			int Ip = info.getIpAddress();
			String strIp = "" + (Ip & 0xFF) + "." + ((Ip >> 8) & 0xFF) + "."
					+ ((Ip >> 16) & 0xFF) + "." + ((Ip >> 24) & 0xFF);
			SimpleDateFormat formatter = new SimpleDateFormat(
					"yyyy年MM月dd日    HH:mm:ss     ");
			Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
			String str = formatter.format(curDate);
            String tmp="\nMAC : " + info.getBSSID() + "\nSSID : "
					+ info.getSSID() + "\nIpAddress : " + strIp
					+ "\nNetworkId : " + info.getNetworkId() + "\nLinkSpeed : "
					+ info.getLinkSpeed() + "Mbps" + "\nRssi : "
					+ info.getRssi() + "\nSavetime : " + str;
			content.append(tmp);
			//showTextView.setText(content.toString());
			times--;
			try {
				Thread.sleep(interval_ms);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			list.add(tmp);
		}
		content.append("\n" + location.getText().toString());
		try {
			/*
			 * 根据用户提供的文件名，以及文件的应用模式，打开一个输出流.文件不存系统会为你创建一个的，
			 * 至于为什么这个地方还有FileNotFoundException抛出，我也比较纳闷。在Context中是这样定义的 public
			 * abstract FileOutputStream openFileOutput(String name, int mode)
			 * throws FileNotFoundException; openFileOutput(String name, int
			 * mode); 第一个参数，代表文件名称，注意这里的文件名称不能包括任何的/或者/这种分隔符，只能是文件名
			 * 该文件会被保存在/data/data/应用名称/files/chenzheng_java.txt 第二个参数，代表文件的操作模式
			 * MODE_PRIVATE 私有（只能创建它的应用访问） 重复写入时会文件覆盖 MODE_APPEND 私有
			 * 重复写入时会在文件的末尾进行追加，而不是覆盖掉原来的文件 MODE_WORLD_READABLE 公用 可读
			 * MODE_WORLD_WRITEABLE 公用 可读写
			 */

			// File sdCardDir = Environment.getExternalStorageDirectory();
			fileName = file.getText().toString() + ".txt";
			File targetFile = new File(
					Environment.getExternalStorageDirectory(), fileName);
			// 以指定文件创建 RandomAccessFile对象,第一个参数是文件名称，第二个参数是读写模式
			FileOutputStream raf = new FileOutputStream(targetFile);
			// 将文件记录指针移动到最后
			// raf.seek(targetFile.length());
			// 输出文件内容
			raf.write(content.toString().getBytes());
			raf.close();
			Toast.makeText(SaveFile.this, "保存成功", Toast.LENGTH_LONG).show();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		listView.setAdapter(new MyAdapter(this, list));
	}

	/**
	 * @author chenzheng_java 读取刚才用户保存的内容
	 */
	private void read() {
		try {
			FileInputStream inputStream = this.openFileInput(fileName);
			byte[] bytes = new byte[1024];
			ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
			while (inputStream.read(bytes) != -1) {
				arrayOutputStream.write(bytes, 0, bytes.length);
			}
			inputStream.close();
			arrayOutputStream.close();
			String content = new String(arrayOutputStream.toByteArray());
			showTextView.setText(content);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	public class MyAdapter extends BaseAdapter {  
		  
        LayoutInflater inflater;  
        List<String> list;  
        public MyAdapter(Context context, List<String> list) {  
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
            String scanResult = list.get(position);   
            TextView imformation = (TextView) view.findViewById(R.id.imformation);
            //signalStrenth.setText(String.valueOf(Math.abs(scanResult.level)));

          
            imformation.setText(scanResult);
            //mLabelWifi.setText(mStringBuffer.toString());
            //System.out.println(mStringBuffer.toString());
            
            return view;  
        }

  
    }  

}
