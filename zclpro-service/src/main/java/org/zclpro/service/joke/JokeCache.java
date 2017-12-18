package org.zclpro.service.joke;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zclpro.common.httpclient.HttpClientUtil;
import org.zclpro.common.propertiestool.StringManager;
import org.zclpro.db.joke.entity.JokeText;
import org.zclpro.db.joke.impl.JokeTextMapper;
import org.zclpro.service.common.BaseCache;
import org.zclpro.service.common.Constants;

import com.alibaba.fastjson.JSONObject;
import com.mysql.jdbc.StringUtils;
@Service
public class JokeCache extends BaseCache{
	
	private final static StringManager stringManager = StringManager.getStringManageByFileName(Constants.JOKE_PROPERTIES_PATH);
	
	private final static String APPKEY = stringManager.getValue("appkey");
	
	private final static String URL = stringManager.getValue("url");
	
	private final static String CURPAGENO = stringManager.getValue("current.pageno");
	
	private final static String PAGESIZE = stringManager.getValue("page.size");
	
	@Autowired
	private JokeTextMapper jokeTextMapper;
	/**
	 * 拉取笑话信息
	 * 1.页码信息从缓存中读取，读不到默认为1,一次保存拉取后 +1
	 * 
	 */
	public void pullJokeInfos(){
		String curPageNo = redisCache.get(CURPAGENO);
		if(StringUtils.isNullOrEmpty(curPageNo)){
			curPageNo = "1";
			redisCache.set(CURPAGENO, curPageNo);
		}
		Map<String, Object> params = new HashMap<>();
		params.put("key", APPKEY);
		params.put("page",Integer.valueOf(curPageNo));
		params.put("pagesize", PAGESIZE);
		String result = HttpClientUtil.postRequest(URL, params);
		JSONObject object = JSONObject.parseObject(result);
		if (object.getIntValue("error_code") == 0){
			List<JokeText> jokes = object.parseArray(object.getJSONObject("result").getString("data"), JokeText.class);
			jokeTextMapper.batchSaveOrUpdate(jokes);
			redisCache.incrBy(CURPAGENO, 1);
		}
	}
	
	@Override
	public int getExpire() {
		return 0;
	}

}
