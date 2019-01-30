package com.ufsc.maven.extrairEsquemas.util;

import javax.swing.JOptionPane;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisConnector {

    private Jedis jedis;
    private JedisPool pool;
    private String password;
    //Iniciação do server redis

    public boolean connect(String uri, String port, String password) {

        try {
            pool = new JedisPool(new JedisPoolConfig(), uri, Integer.valueOf(port), 2000);
            
            jedis = pool.getResource();
            if (password != null & !password.isEmpty()) {
                this.password = password;
                jedis.auth(password);
            }
            jedis.connect();
            jedis.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Informação inválida, por favor verifique.");
            e.printStackTrace();
            return false;
        }
        this.password = password;
        // jedis = pool.getResource();
        return true;
    
    }
    //Opção mais "lenta", porém mais efetiva para exibir todas as chaves

    public Jedis getJedisConection(){        
            if (password != null & !password.isEmpty()) {
                this.password = password;
                jedis.auth(password);
            }
            jedis.connect();
            
        return jedis;
    }
    
    
    public void listAllKeys() {
        Jedis jedis = new Jedis("localhost");
        System.out.println("Keys: " + jedis.keys("*"));
        jedis.close();
    }

    /* Deleta uma chave do servidor,
	 * Como o comando é rpop ele deleta apenas o ultimo elemento da lista baseada na chave dada
	 * Exemplo: Com chaves 'a', 'b', 'c', 'avião', caso seja feito um Iterator.delete(a), 
	 * irá resultar nas chaves 'a', 'b','c' .
	 * Caso queira deletar a primeira chave deve-se usar lpop, caso queira deletar todas as chaves encontradas use del
     */
    public void delete(String key) {
        Jedis jedis = new Jedis("localhost");
        jedis.rpop(key);
        jedis.close();
    }
    //Torna o valor entregue como o valor da chave

    public void push(String key, String value) {
        Jedis jedis = new Jedis("localhost");
        jedis.set(key, value);
        jedis.close();
    }

}
