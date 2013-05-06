package demo.camera;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;

/**
 * 该类提供对M3U文件的解析
 * @author Administrator
 *
 */
public final class M3UParser {
	
	/**
	 * 从指定的Url进行解析,返回一个包含FilePath对象的列表
	 * FilePath封装每一个Audio路径。
	 * @param url
	 * @return
	 */
	public static List<FilePath> parseFromUrl(String url){
		List<FilePath> resultList = null;
		HttpResponse res = HttpConnect.getResponseFromUrl(url);
		try {
			if(res != null){
				resultList = new ArrayList<M3UParser.FilePath>();
				InputStream in = res.getEntity().getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(in));
				String line = "";
				while((line = reader.readLine()) != null){
					if(line.startsWith("#")){
						//这里是Metadata信息
					}else if(line.length() > 0 && line.startsWith("http://")){
						//这里是一个指向的音频流路径
						FilePath filePath = new FilePath(line);
						resultList.add(filePath);
					}
				}
				in.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}
	
	/**
	 * 返回List<String>类型
	 * @param url
	 * @return
	 */
	public static List<String> parseStringFromUrl(String url){
		List<String> resultList = null;
		HttpResponse res = HttpConnect.getResponseFromUrl(url);
		try {
			if(res != null){
				resultList = new ArrayList<String>();
				InputStream in = res.getEntity().getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(in));
				String line = "";
				while((line = reader.readLine()) != null){
					if(line.startsWith("#")){
						//这里是Metadata信息
					}else if(line.length() > 0 && line.startsWith("http://")){
						//这里是一个指向的音频流路径
						resultList.add(line);
					}
				}
				in.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;		
	}
	
	
	//解析后的实体对象
	static class FilePath{
		
		private String filePath;
		
		public FilePath(String filePath){
			this.filePath = filePath;
		}

		public String getFilePath() {
			return filePath;
		}

		public void setFilePath(String filePath) {
			this.filePath = filePath;
		}
		
		
	}

}
