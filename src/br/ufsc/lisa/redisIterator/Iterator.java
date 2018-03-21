package br.ufsc.lisa.redisIterator;

import java.util.List;
import java.util.Map.Entry;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import redis.clients.jedis.*;

public class Iterator {
	
	private Jedis jedis;
	private List<String> schemaJson;
	

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
		String nextCursor = scanResult.getStringCursor();
		JSONParser parser = new JSONParser();
		int counter = 0;

		while (true) {
			nextCursor = scanResult.getStringCursor();
			List<String> keys = scanResult.getResult();
			for (counter = 0; counter < keys.size(); counter++) {
				if(counter == keys.size()) {
//					counter = 0;
					break;
				}				
				try {
					JSONObject json = (JSONObject) parser.parse(jedis.rpop(keys.get(counter)));
					documentoJson(json);					
					System.out.println("Added to function 'documentoJson'");

				} catch (ParseException e) {
					System.out.println("Not a valid JSON");
				}
			}
			if (nextCursor.equals("0")) {
				break;
			}
			scanResult = jedis.scan(nextCursor);
		}
		jedis.close();
	}
	
	public JSONArray documentoJson(JSONObject json) {
		JSONObject jObject = new JSONObject();
		JSONArray jArray = new JSONArray();
		jArray.add(json);
		jObject.put("JSON Document", jArray);
		return jArray;
	}
	

	public void readRedis2() {
		Jedis jedis = new Jedis("localhost");

		String key = "THEKEY";
		ScanParams scanParams = new ScanParams().count(100);
		String cur = redis.clients.jedis.ScanParams.SCAN_POINTER_START;
		boolean cycleIsFinished = false;
		while (!cycleIsFinished) {
			ScanResult<Entry<String, String>> scanResult = jedis.hscan(key, cur, scanParams);
			List<Entry<String, String>> result = scanResult.getResult();
			System.out.println(result);
			// do whatever with the key-value pairs in result

			cur = scanResult.getStringCursor();
			if (cur.equals("0")) {
				cycleIsFinished = true;
			}
		}
		jedis.close();
	}

	public void readRedis3() {
		Jedis jedis = new Jedis("localhost");

		JSONParser parser = new JSONParser();
		ScanParams scanParams = new ScanParams();
		scanParams.match("*");
		int counter = 0;

		String cursor = redis.clients.jedis.ScanParams.SCAN_POINTER_START;
		boolean cycleIsFinished = false;
		while (!cycleIsFinished) {

			ScanResult<String> scanResult = jedis.scan(cursor, scanParams);
			List<String> result = scanResult.getResult();
			System.out.println(jedis.get(result.get(counter)));

			// try {
			// JSONObject json = (JSONObject) parser.parse(result.get(counter).toString());
			// } catch (ParseException e) {
			// e.printStackTrace();
			// }

			cursor = scanResult.getStringCursor();
			if (cursor.equals("0")) {
				cycleIsFinished = true;
			}
		}
		jedis.close();
	}
}
