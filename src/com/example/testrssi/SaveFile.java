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
	// Ҫ������ļ���
	private String fileName = "save.txt";
	private StringBuffer content;
	List<String> list;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.save_file);
		// ��ȡҳ���е����
		numberOfTime = (EditText) findViewById(R.id.addText);
		file = (EditText) findViewById(R.id.filename);
		location = (EditText) findViewById(R.id.location);
		interval = (EditText) findViewById(R.id.interval);
		showTextView = (TextView) findViewById(R.id.showText);
		Button addButton = (Button) this.findViewById(R.id.addButton);
		listView = (ListView) findViewById(R.id.listView);
		list=new ArrayList<String>();
		// Button showButton = (Button) this.findViewById(R.id.showButton);
		// �󶨵����¼�
		addButton.setOnClickListener(listener);
		// showButton.setOnClickListener(listener);

	}

	// ����������
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
	 * @author chenzheng_Java �����û���������ݵ��ļ�
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
			 * info.getBSSID()�� ��ȡBSSID��ַ�� info.getSSID()�� ��ȡSSID��ַ�� ��Ҫ���������ID
			 * info.getIpAddress()�� ��ȡIP��ַ��4�ֽ�Int, XXX.XXX.XXX.XXX ÿ��XXXΪһ���ֽ�
			 * info.getMacAddress()�� ��ȡMAC��ַ�� info.getNetworkId()�� ��ȡ����ID��
			 * info.getLinkSpeed()�� ��ȡ�����ٶȣ��������û���֪��һ��Ϣ�� info.getRssi()��
			 * ��ȡRSSI��RSSI���ǽ����ź�ǿ��ָʾ
			 */

			int Ip = info.getIpAddress();
			String strIp = "" + (Ip & 0xFF) + "." + ((Ip >> 8) & 0xFF) + "."
					+ ((Ip >> 16) & 0xFF) + "." + ((Ip >> 24) & 0xFF);
			SimpleDateFormat formatter = new SimpleDateFormat(
					"yyyy��MM��dd��    HH:mm:ss     ");
			Date curDate = new Date(System.currentTimeMillis());// ��ȡ��ǰʱ��
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
			 * �����û��ṩ���ļ������Լ��ļ���Ӧ��ģʽ����һ�������.�ļ�����ϵͳ��Ϊ�㴴��һ���ģ�
			 * ����Ϊʲô����ط�����FileNotFoundException�׳�����Ҳ�Ƚ����ơ���Context������������� public
			 * abstract FileOutputStream openFileOutput(String name, int mode)
			 * throws FileNotFoundException; openFileOutput(String name, int
			 * mode); ��һ�������������ļ����ƣ�ע��������ļ����Ʋ��ܰ����κε�/����/���ַָ�����ֻ�����ļ���
			 * ���ļ��ᱻ������/data/data/Ӧ������/files/chenzheng_java.txt �ڶ��������������ļ��Ĳ���ģʽ
			 * MODE_PRIVATE ˽�У�ֻ�ܴ�������Ӧ�÷��ʣ� �ظ�д��ʱ���ļ����� MODE_APPEND ˽��
			 * �ظ�д��ʱ�����ļ���ĩβ����׷�ӣ������Ǹ��ǵ�ԭ�����ļ� MODE_WORLD_READABLE ���� �ɶ�
			 * MODE_WORLD_WRITEABLE ���� �ɶ�д
			 */

			// File sdCardDir = Environment.getExternalStorageDirectory();
			fileName = file.getText().toString() + ".txt";
			File targetFile = new File(
					Environment.getExternalStorageDirectory(), fileName);
			// ��ָ���ļ����� RandomAccessFile����,��һ���������ļ����ƣ��ڶ��������Ƕ�дģʽ
			FileOutputStream raf = new FileOutputStream(targetFile);
			// ���ļ���¼ָ���ƶ������
			// raf.seek(targetFile.length());
			// ����ļ�����
			raf.write(content.toString().getBytes());
			raf.close();
			Toast.makeText(SaveFile.this, "����ɹ�", Toast.LENGTH_LONG).show();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		listView.setAdapter(new MyAdapter(this, list));
	}

	/**
	 * @author chenzheng_java ��ȡ�ղ��û����������
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
       getBSSID() ��ȡBSSID
       getDetailedStateOf() ��ȡ�ͻ��˵���ͨ��
       getHiddenSSID() ���SSID �Ƿ�����
       getIpAddress() ��ȡIP ��ַ
       getLinkSpeed() ������ӵ��ٶ�
       getMacAddress() ���Mac ��ַ
       getRssi() ���802.11n ������ź�
       getSSID() ���SSID
       getSupplicanState() ���ؾ���ͻ���״̬����Ϣ

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
