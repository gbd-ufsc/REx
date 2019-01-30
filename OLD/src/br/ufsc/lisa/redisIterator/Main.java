package br.ufsc.lisa.redisIterator;

import br.ufsc.lisa.redisIterator.Iterator;

public class Main {

	public static void main(String[] args) {
		
		Iterator iterator = new Iterator();
//		iterator.startServer();
//		iterator.listAllKeys();
//		iterator.scanKeys();
//		iterator.push("rianzz", "dsada132321");
//		iterator.push("das,l", "dsadsa213213");
//		iterator.push("caorl", "dsa213321");
//		iterator.push("leprlz", "qwewqsad1");
//		iterator.push("rioanzz", "qokwekqwe1");
//		iterator.push("lqpwz", "sawqeqw21,ol");
//		iterator.push("okqwe", "dasjidsiaj2");
//		iterator.push("elçefante", "sadasaoksd");
//		iterator.push("jkoqwe", "pqwpe21");
//		iterator.push("pobre", "ijsdadak1");
//		iterator.push("triste", "jiads");
//		iterator.push("sadd", "dkook123");
//		iterator.push("wpqoeqw", "dkosadkosa");
//		iterator.delete("rianzera");
		iterator.readRedis();
		
		System.out.println("End of program.");
		
	}

}
