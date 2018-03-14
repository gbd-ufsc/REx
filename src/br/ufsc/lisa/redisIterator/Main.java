package br.ufsc.lisa.redisIterator;

import br.ufsc.lisa.redisIterator.Iterator;

public class Main {

	public static void main(String[] args) {
		
		Iterator iterator = new Iterator();
//		iterator.startServer();
//		iterator.listAllKeys();
//		iterator.scanKeys();
//		iterator.push("rianzz", "sadas");
//		iterator.delete("rianzera");
		iterator.readRedis2();
		
	}

}
