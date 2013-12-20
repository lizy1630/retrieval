package com.thinkgem.jeesite.modules.util;

public class MyUtils {

	public static String toArrayStr(int num){
		String result = null;
		if(num>0)
		for(int i = 1 ;i<num+1;i++){
			if(i==1)
				if(i!=num)
					result = "[\""+i+"\",";
				else
					result = "[\""+i+"\"]";
					
			else if(i==num)
				result += "\""+i+"\"]";
			else
				result += "\""+i+"\",";
		}
		return result;
	}
	
	public static void main(String[] args) {
		System.out.println(toArrayStr(30));
	}
}
