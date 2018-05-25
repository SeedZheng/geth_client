import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class JunitTest {
	
	@Test
	public void test1(){
		String uuid=UUID.randomUUID().toString();
		System.out.println(uuid);
		System.out.println(uuid.length());
	}
	
	@Test
	public void testMap(){
		/*Map<String,String> map=new HashMap<>();
		map.put("t1", "1");
		map.put("t2", "2");
		map.put("t3", "3");
		
		String json=JSON.toJSONString(map);
		System.out.println(json);*/
		
		String json="{\"t1\":\"1\",\"t2\":\"2\",\"t3\":\"3\"}";
		Map map=JSONObject.parseObject(json, Map.class);
		Iterator<String> iterator=map.keySet().iterator();
		while(iterator.hasNext()){
			String k=iterator.next();
			String v=(String) map.get(k);
			System.out.println(k+":"+v);
		}
	}
	
	@Test
	public void testStr(){
		//String str="account:0x123456|banance:123.4567";
		String str="account:0x123456,1";
		String[] ss=str.split(",");
		for(String s:ss){
			System.out.println(s);
		}
	}

}
