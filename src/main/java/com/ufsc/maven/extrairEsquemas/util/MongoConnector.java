package com.ufsc.maven.extrairEsquemas.util;

import com.mongodb.MongoClient;
import com.mongodb.DB;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import java.util.Arrays;
import javax.swing.JOptionPane;

public class MongoConnector {

    private MongoClient mongo;
    private DB db;
    private String hostName;
    private MongoClient client;
    private MongoCredential credential;
    private MongoDatabase dbMongo;

    public boolean connect(String uri, String port, String user, String password, String DB) {
        if (user != null && !user.isEmpty()) {
            try {
                this.credential = MongoCredential.createCredential(user, "admin", password.toCharArray());
                this.client = new MongoClient(new ServerAddress(uri, Integer.valueOf(port)), Arrays.asList(credential));
                setDbMongo(this.client.getDatabase(DB));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Informação inválida, por favor verifique.");
                e.printStackTrace();
                return false;
            }
        } else {
            try {
                this.client = new MongoClient(new ServerAddress(uri, Integer.valueOf(port)));
                setDbMongo(this.client.getDatabase(DB));
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(null, "Argumentação inválida, por favor verifique");
                e.printStackTrace();
                return false;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Informação inválida, por favor verifique.");
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public void listAllKeys() {
        setMongo(new MongoClient(getHostName()));
        System.out.println("Keys: " + getMongo().listDatabaseNames());
        getMongo().close();
    }

    public void dropDatabase(String dataBase) {
        setMongo(new MongoClient(getHostName()));
        getMongo().dropDatabase(dataBase);
        getMongo().close();

    }

    /*
	public DBCollection createCollection(DBCollection coll) {
		DBCollection coll1;
		mongo = new MongoClient ("localhost");
		String name = "mycoll";
		DB db = new DB(mongo, name);
		coll1 = db.getCollection(name);
        System.out.println("Coleção criada com sucesso");
        System.out.println(coll1);

        return coll1;
	}
     */
    /**
     * @return the mongo
     */
    public MongoClient getMongo() {
        return mongo;
    }

    /**
     * @param mongo the mongo to set
     */
    public void setMongo(MongoClient mongo) {
        this.mongo = mongo;
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
     * @return the hostName
     */
    public String getHostName() {
        return hostName;
    }

    /**
     * @param hostName the hostName to set
     */
    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    /**
     * @return the dbMongo
     */
    public MongoDatabase getDbMongo() {
        return dbMongo;
    }

    /**
     * @param dbMongo the dbMongo to set
     */
    public void setDbMongo(MongoDatabase dbMongo) {
        this.dbMongo = dbMongo;
    }

}
