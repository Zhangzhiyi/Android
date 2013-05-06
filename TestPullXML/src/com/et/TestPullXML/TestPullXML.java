package com.et.TestPullXML;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import android.app.Activity;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
/**
 * <?xml version="1.0" encoding="UTF-8"?>
	<persons>
		<person id="23">
			<name>zhangsan</name>
			<age>25</age>
		</person>
		<person id="20">
			<name>lisi</name>
			<age>28</age>
		</person>
	</persons>
	每两个tag之间都有一个空白text节点，这些空白text节点都是为了使得XML文件看起来更美观而形成，
	但是解析器并不知道这一点，所以解析器仍然认为它们是有用的文本数据(由空白类字符组成).
**/
public class TestPullXML extends Activity {
	
	private InputStream is;
	private Button mPullXMLBtn;
	private Button mParseXMLtoBundle;
	private Button mUpdateAttrs;
	
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main);
        mPullXMLBtn = (Button) findViewById(R.id.button1);
        
        mPullXMLBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				parseXML();
			}
		});
        mParseXMLtoBundle = (Button) findViewById(R.id.button2);		   
        mParseXMLtoBundle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				parseXMLtoBundle();
			}
		});
        mUpdateAttrs = (Button) findViewById(R.id.button3);
        mUpdateAttrs.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				updateAttrs();
			}
		});
    }
    public void parseXML(){
    	try {
        	is = getResources().getAssets().open("persons.xml");
        	XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			XmlPullParser mParser = factory.newPullParser();
        	//XmlResourceParser mParser = getResources().getXml(R.xml.persons);
			mParser.setInput(is, "utf-8");
			int eventType = mParser.getEventType();
			while(eventType!=XmlPullParser.END_DOCUMENT){
				switch (eventType) {
					case XmlPullParser.START_DOCUMENT:
						Log.i("START_DOCUMENT", "START_DOCUMENT");
						break;
					case XmlPullParser.START_TAG:
						Log.i("start tag:", mParser.getName());
						if (mParser.getName().equals("person")) {
							String id = mParser.getAttributeValue(0);
							Log.i("person:id", "" + id);
						}
						break;
					case XmlPullParser.TEXT:
						//判断是否是空白text节点
						if(!mParser.isWhitespace()){
							Log.i("text:", mParser.getText());
							//mParser.setProperty(mParser.getName(), "111111111111111");
						}
						break;
					case XmlPullParser.END_TAG:
						Log.i("end tag:", mParser.getName());
						break;
					case XmlPullParser.END_DOCUMENT:
						Log.i("END_DOCUMENT", "END_DOCUMENT");
						break;
				}
				eventType = mParser.next();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    public void parseXMLtoBundle(){
    	Bundle bundle = new Bundle();
    	XmlResourceParser mParser = getResources().getXml(R.xml.extra);   	
    	try {
    		/**注意这里要next两次, 这个方法运行效率比较慢**/
    		mParser.next();
    		mParser.next();
			getResources().parseBundleExtras(mParser, bundle);
			
			boolean flag = bundle.getBoolean("flag");
			if (flag){
				Log.i("k1", bundle.getString("k1"));
				bundle.putString("da", "ET");
			}
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    public void updateAttrs(){
    	try {
    		//InputStream is = getResources().openRawResource(R.xml.extra);
    		InputStream is = getResources().getAssets().open("extra.xml");
    		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(is);

			//Element element=document.getDocumentElement();  
			NodeList nodes = document.getElementsByTagName("extra");
			int length = nodes.getLength();
			Log.i("lenght", "" + length);
			for (int i = 0; i < length; i++) {
				Node node = nodes.item(i);
				NamedNodeMap nodeMap = node.getAttributes();
				Log.i("attrs length", "" + nodeMap.getLength());
				Node value = nodeMap.item(1);
				String attrname = value.getNodeName();
				Log.i("name", attrname);
			}
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    /**写XML**/
    public void buildXML(List<Person> persons,OutputStream outputStream) throws Exception  
    {  
        XmlSerializer serializer=Xml.newSerializer();  
        serializer.setOutput(outputStream, "utf-8");  
          
        serializer.startDocument("utf-8", true);  
        serializer.startTag(null, "persons");  
          
        for(Person person:persons)  
        {  
            serializer.startTag(null, "person");  
            serializer.attribute(null, "id", person.getId().toString());  
              
            serializer.startTag(null, "name");  
            serializer.text(person.getName());  
            serializer.endTag(null, "name");  
              
            serializer.startTag(null, "age");  
            serializer.text(person.getAge().toString());  
            serializer.endTag(null, "age");  
              
            serializer.endTag(null, "person");  
        }  
          
        serializer.endTag(null, "persons");  
        serializer.endDocument();  
        outputStream.close();  
    }  
}