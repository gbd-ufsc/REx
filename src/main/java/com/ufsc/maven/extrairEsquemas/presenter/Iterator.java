package com.ufsc.maven.extrairEsquemas.presenter;

import com.ufsc.maven.extrairEsquemas.util.RedisConnector;
import com.ufsc.maven.extrairEsquemas.util.MongoConnector;

import com.ufsc.maven.extrairEsquemas.views.MainApp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanResult;

public class Iterator {

    private DB db;
    private DBCollection coll;

    private MongoConnector mongo;
    private RedisConnector redis;
    private MongoDatabase mongodb;
//    private BasicDBObject dbObj;
//    private String name;
//    private String Mongo_IP;
//    private int Mongo_port;
//    private String Redis_IP;
//    private int Redis_port;
//    /**

    public Iterator(MongoConnector mongo, RedisConnector redis) {
        //clear mongo
        this.mongo = mongo;
        this.redis = redis;
        mongodb = mongo.getDbMongo();

        try {
            mongo.dropDatabase(mongodb.getName());
        } catch (MongoException e) {
            System.out.println("Database vazia");
        } catch (NullPointerException e) {
            System.out.println("Teste");
        }
    }

//    public String ipRedis(String uri){
//        Redis_IP = uri;
//        return Redis_IP;
//    }
//    
//    public int portRedis(int port){
//        Redis_port = port;
//        return Redis_port;
//    }
//    
//    public String ipMongo(String uri){
//        Mongo_IP = uri;
//        return Mongo_IP;
//    }
//    public int portMongo(int port){
//        Mongo_port = port;
//        return Mongo_port;
//    }
//    
//    public String collectionMongo(String collection){
//        name = collection;
//        return name;
//    }

    /*
	 * Comando para ler os valores do redis e detectar se são ou não JSON.
	 * Em si ele percorre todas as chaves do redis e, para cada chave, ele detecta se a chave-valor é do tipo JSON
	 * Caso seja ela será adicionada ao documento Json (essa é a parte que eu irei estruturar)
	 * Em caso de não ser ela será gerada em um esquema de chave-valor
     */
    public void readRedis() throws Exception {

        //System.out.println("Conexão feita com sucesso");
        ScanResult<String> scanResult = redis.getJedisConection().scan("0");
        String nextCursor = scanResult.getStringCursor();
        JSONParser parser = new JSONParser();
        int counter = 0;
        while (true) {
            nextCursor = scanResult.getStringCursor();
            List<String> keys = scanResult.getResult();
            for (counter = 0; counter < keys.size(); counter++) {
                try {
                    String key = keys.get(counter);
                    JSONObject json = (JSONObject) parser.parse(redis.getJedisConection().get(key));
                    insertJSON(buildRawSchema(json));
                    
                    //System.out.println("Added to function 'documentoJson'");
                } catch (Exception e) {
                    System.out.println(e);
                //} catch (ParseException e) {
                    System.out.println("Not a valid JSON");
                }
            }
            if (nextCursor.equals("0")) {
                break;
            }
            scanResult = redis.getJedisConection().scan(nextCursor);
        }
        redis.getJedisConection().close();
    }

    /*
	//Adiciona o documento 	JSONObject em um JSONArray 
	public JSONArray documentoJson(JSONObject json) {
		buildRawSchema(json);
		JSONObject jObject = new JSONObject();
		JSONArray jArray = new JSONArray();
		jArray.add(json);
		//System.out.println(jArray);

		//jObject.put("JSON Document", jArray);
		
		return jArray;
	}
     */
    public JSONObject buildRawSchema(JSONObject json) {
        /*A função percorre todas as chaves do json, detectando qual é o tipo do valor, a cada vez que detecta o tipo
		 * do valor	ele substitui o que está escrito para o tipo do valor (exemplo: Nome: "String") no json.
		 * O que provavelmente precisa: método para salvar a variável (possivelmente é o método do redis no qual já
		 * foi listado), após isso acho que seria feito a interface.
         */
        for (Object key : json.keySet()) {
            Object value = json.get(key);
            //System.out.println(key + " = " + value);
            if (value instanceof JSONObject) {
                json.replace(key, buildRawSchema((JSONObject) value));
            } else if (value instanceof JSONArray) {
                json.replace(key, buildRawSchema((JSONObject) value));
            } else if (value instanceof Number) {
                json.replace(key, "Number");
            } else if (value instanceof Boolean) {
                json.replace(key, "Boolean");

            } else if (value instanceof String) {
                json.replace(key, "String");

            } else {
                json.replace(key, "null");
            }
        }

        return json;
    }

    public void insertJSON(JSONObject json) {
        System.out.println(mongodb.getName());
        mongodb.getCollection(mongodb.getName()).insertOne(new Document(json));

    }

    /**
     * @return the db
     */
    public DB getDb() {
        return db;
    }

    /**
     * @param db the db to set
     */
    public void setDb(DB db) {
        this.db = db;
    }

    /**
     * @return the coll
     */
    public DBCollection getColl() {
        return coll;
    }

    /**
     * @param coll the coll to set
     */
    public void setColl(DBCollection coll) {
        this.coll = coll;
    }

}
