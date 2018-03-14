package br.ufsc.lisa.redisIterator;

import java.util.List;
import java.util.Map.Entry;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import redis.clients.jedis.*;

public class Iterator {

	private Jedis jedis;

	public void startServer() {
		Jedis jedis = new Jedis("localhost");
		System.out.println("Connection to server sucessfully");
		jedis.close();
	}

	public void checkConnection() {

		System.out.println("Server is running: " + jedis.ping());
	}

	public void listAllKeys() {
		Jedis jedis = new Jedis("localhost");
		System.out.println("Keys: " + jedis.keys("*"));
		jedis.close();
	}

	public void push(String key, String value) {
		Jedis jedis = new Jedis("localhost");
		jedis.rpush(key, value);
		jedis.close();
	}

	public void delete(String key) {
		Jedis jedis = new Jedis("localhost"); 
		jedis.rpop(key);
		jedis.close();
	}
	
	public void readRedis() {
		Jedis jedis = new Jedis("localhost");
		
		ScanResult<String> scanResult = jedis.scan("0");
		List<String> keys = scanResult.getResult();
		String nextCursor = scanResult.getStringCursor();
		JSONParser parser = new JSONParser();
		int counter = 0;
		
		while(true) {
			
			if(nextCursor.equals("0")) {
				break;
			}
			
			scanResult = jedis.scan(nextCursor);
			nextCursor = scanResult.getStringCursor();
			keys = scanResult.getResult();
			for(counter = 0; counter <= keys.size(); counter++) {
				try {
					JSONObject json = (JSONObject) parser.parse(keys.get(counter).toString());

				} catch (ParseException e) {
					e.printStackTrace();
				} 
			}
			System.out.println(keys = scanResult.getResult());
		}
		jedis.close();
	}
	
	public void readRedis2() {
		Jedis jedis = new Jedis("localhost");

		String key = "THEKEY";
		ScanParams scanParams = new ScanParams().count(100);
		String cur = redis.clients.jedis.ScanParams.SCAN_POINTER_START; 
		boolean cycleIsFinished = false;
		while(!cycleIsFinished){
		  ScanResult<Entry<String, String>> scanResult = jedis.scan(key, cur, scanParams);
		  List<Entry<String, String>> result = scanResult.getResult();
		  System.out.println(result);
		  //do whatever with the key-value pairs in result

		  cur = scanResult.getStringCursor();
		  if (cur.equals("0")){
		    cycleIsFinished = true;
		  }                 
		}
		jedis.close();
	}
}