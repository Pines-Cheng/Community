package com.forex.model.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;

public class NewsService {
	
	private static final String cancelAgreeSQL = "delete from news_agree where subject_id=? and user_id=?";

	@SuppressWarnings("deprecation")
	public String getNews(long userIbd, String newsUrl, int start, int size) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost post = new HttpPost(newsUrl);
//		String tagsStr = getUserTagsString(userId);
//		if (tagsStr != null) {
			List <NameValuePair> nvps = new ArrayList <NameValuePair>();
	        nvps.add(new BasicNameValuePair("start", start + ""));
	        nvps.add(new BasicNameValuePair("size", size + ""));
	        try {
				post.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
//		}
        try {
        	CloseableHttpResponse response = httpclient.execute(post);  
        	
        	try {  
                // 获取响应实体    
                HttpEntity entity = response.getEntity();  
                System.out.println("--------------------------------------");  
                // 打印响应状态    
                System.out.println(response.getStatusLine());  
                if (entity != null) {  
                    // 打印响应内容长度    
                    // 打印响应内容
                	String content = EntityUtils.toString(entity);
                    System.out.println("Response content: " + content);  
                    JSONObject jo = JSON.parseObject(content);
                    return jo.toJSONString();
                }  
                System.out.println("------------------------------------");  
            } catch(Exception e) {
            	e.printStackTrace();
            } finally {  
                response.close();  
            } 
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {  
                httpclient.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
		}
		return null;
	}
	
	public String getUserTagsString(long userId) {
		Record record = Db.findFirst("select tags from user_atten_tags where user_id=?", userId);
		if (record != null) {
			return record.getStr("tags");
		}
		return null;
	}
	
	@Before(Tx.class)
	public String calculateBuyTrend(long userId, List<String> newsIds) {
		List<Map<String, Long>> list = new ArrayList<Map<String,Long>>();
//		String sql = "select count(*) as num from trade_with_news where isBuy=? and user_id=? "
//				+ "and (news_ids like '?,%' or news_ids like '%,?,%' or news_ids like '%,?')";
//		for (String id : newsIds) {
//			Map<String, Long> map = new HashMap<String, Long>();
//			long buyNum = Db.findFirst(sql, true, userId, id, id, id).getLong("num");
//			long sellNum = Db.findFirst(sql, false, userId, id, id, id).getLong("num");
//			map.put("buyNum", buyNum);
//			map.put("sellNNum", sellNum);
//			list.add(map);
//		}
		return JsonKit.toJson(list);
	}
	
	public boolean tradeWithNews(long userId, String pair, String newsIds, boolean isBuy) {
		Record record = new Record().set("user_id", userId).
				set("type_pair", pair).set("news_ids", newsIds).set("isBuy", isBuy);
		return Db.save("trade_with_news", record);
	}
	
	public boolean addAttenTags(long userId, String tags, String tagsCode) {
		Record record = new Record().set("user_id", userId).set("tags", tags).set("tags_code", tagsCode);
		return Db.save("user_atten_tags", record);
	}
	
	/**给回复点赞**/
	public boolean addAgree(String newsId, long userId) {
		Record record = new Record().set("subject_id", newsId).set("user_id", userId);
		return Db.save("news_agree", record);
	}
	
	public boolean addAgree(String newsId, long userId, boolean isSupport) {
		Record record = new Record().set("subject_id", newsId)
				.set("user_id", userId).set("isSupport", isSupport);
		return Db.save("news_agree", record);
	}
	
	/**取消回复的点赞**/
	public boolean cancelAgree(long newsId, long userId) {
		return Db.update(cancelAgreeSQL, newsId, userId) > 0;
	}
}
