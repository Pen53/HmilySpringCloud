package com.mepeng.cn.concurrent.chapter1;
/*
 * 饿恶singletonģ模式
 */
public class SingletonObject1 {
	private static SingletonObject1 instance= new SingletonObject1();
	//实现lazy
	private SingletonObject1(){
		//empty
	}
	
	public static SingletonObject1 getInstance(){
		return instance;
	}
}
