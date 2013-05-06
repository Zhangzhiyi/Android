package demo.camera;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

/**
 * 给类提供访问网络的方法
 * @author Administrator
 *
 */
public final class HttpConnect {
	
	/**
	 * 利用HttpClient获取指定的Url对应的HttpResponse对象
	 * @param url
	 * @return
	 */
	public static HttpResponse getResponseFromUrl(String url){
		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet(url);
			Log.v("URI : ", get.getURI().toString());
			HttpResponse response = client.execute(get);
			if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				return response;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 利用HttpClient获取指定Url对应的字符串对象
	 * @param url
	 * @return
	 */
	public static String getStringFromUrl(String url){
		try {
			StringBuilder result = new StringBuilder();
			HttpResponse res = HttpConnect.getResponseFromUrl(url);
			if(res != null){
				InputStream is = res.getEntity().getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(is));
				String line = "";
				while((line = reader.readLine()) != null){
					result.append(line);
				}
				is.close();
				return result.toString();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return null;
	}

}
