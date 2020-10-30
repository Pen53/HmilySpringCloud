package com.mepeng.cn.concurrent.chapter1;

public class SingletonObject3 {
	private static SingletonObject3 instance;
	
	private SingletonObject3(){
		
	}
	
	//实现lazy，但多线程下，不会生成多个实例，但是使用时不能并发使用
	public static synchronized SingletonObject3 getInstance(){
		if(instance==null){
			instance = new SingletonObject3();
		}
		return SingletonObject3.instance;
	}
}

