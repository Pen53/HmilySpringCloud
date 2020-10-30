package com.mepeng.cn.concurrent.chapter1;

public class SingletonObject2 {
	private static SingletonObject2 instance;
	
	private SingletonObject2(){
		//empty
	}
	//实现lazy，但多线程下，可能会生成多个实例
	public static SingletonObject2 getInstance(){
		if(instance==null){
			instance = new SingletonObject2();
		}
		return instance;
	}
}
