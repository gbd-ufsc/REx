package br.ufsc.lisa.redisIterator;
import java.util.List;
import org.json.simple.JSONObject;
import redis.clients.jedis.*;


public class Iterator {
	
	private Jedis jedis;

	public void startServer() { 
	      Jedis jedis = new Jedis("localhost"); 
	      System.out.println("Connection to server sucessfully"); 
	      jedis.close();
	     }
	
	public void checkConnection() {
		
		 System.out.println("Server is running: "+jedis.ping()); 
	   	}
	
	public void listAllKeys() {
	    Jedis jedis = new Jedis("localhost"); 
		System.out.println("Keys: "+ jedis.keys("*"));
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
		if(jedis != null) {
			ScanParams params = new ScanParams();
//			params.match("*rian*");
			
			

			ScanResult<String> scanResult = jedis.scan("0", params);
			List<String> keys = scanResult.getResult();
			String nextCursor = scanResult.getStringCursor();
			int counter = 0;

			while (true) {
			    for (String key : keys) {
//			        createSchema(key);
			        System.out.println(nextCursor + "nextCursor");
			        JSONObject jsonObj = new JSONObject();
			    

			    // An iteration also ends at "0"
			    if (nextCursor.equals("0")) {
			        break;
			    }

			    scanResult = jedis.scan(nextCursor, params);
			    nextCursor = scanResult.getStringCursor();
			    keys = scanResult.getResult();
			    System.out.println(keys = scanResult.getResult());
			}
		}
		jedis.close();
		
	}
}
