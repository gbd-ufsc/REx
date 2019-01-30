package com.ufsc.maven.extrairEsquemas.presenter;

import com.ufsc.maven.extrairEsquemas.presenter.Teste;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.json.simple.JSONObject;

import redis.clients.jedis.Jedis;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
//Interrompido até segunda ordem
public class Serializar{

	public void serializarObjeto(JSONObject objetoJSON) throws Exception{
		Jedis jedis = new Jedis("localhost");
		try {
			
			FileOutputStream fout = new FileOutputStream("/home/lisa/test.ser");
	        ObjectOutputStream oos = new ObjectOutputStream(fout);

	        oos.writeObject(objetoJSON.toJSONString());
	        oos.close();
	        System.out.println("Done");
	  
	       }catch (Exception e) {
	           e.printStackTrace();
	           System.out.println("Erro geral");
	       }
	    jedis.close();
		}

		public JSONObject extrairObjeto() throws Exception {
			Jedis jedis = new Jedis("localhost");

			String objString;
			JSONParser parser = new JSONParser();
			JSONObject myObj = null;
			try{
		            
		           /*
		            * Responsável por carregar o arquivo test.ser
		            * */
		           FileInputStream fin = new FileInputStream("/home/lisa/test.ser");
		          
		           /*
		            * Responsável por ler o objeto referente ao arquivo
		            * */
		           ObjectInputStream ois = new ObjectInputStream(fin);
		            

		           objString =  (String)ois.readObject();
		           ois.close();
		           myObj = (JSONObject) parser.parse(objString);
		           System.out.println(myObj);


			   }catch (ParseException e) {
				System.out.println("Erro de parse");
			    
		       }catch(Exception ex){
		           ex.printStackTrace(); 
		           System.out.println("Erro geral");

		       }
			jedis.close();
			return myObj;
			} 
}
