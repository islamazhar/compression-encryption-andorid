package com.example.mazharul_islam.myapplication.utilities;



public class Element implements Comparable <Element>{
	public Integer curFrequency = 0;
	public  Character value = 0;
	public Integer level = 0;

	public Element(Integer _count, Character _value, Integer _level){
		value = _value;
		level = _level;
		curFrequency = _count;

	}


	@Override
	public String toString() {
		return "{level = "+level+" curFrequency = " + curFrequency +" char =" + value+"}";

	}

	@Override
	public int compareTo(Element arg0) {
		if(!arg0.level.equals(level)){
			return -arg0.level+level;
		}

		if(!arg0.curFrequency.equals(curFrequency)) {
			return -arg0.curFrequency+curFrequency;
		}
		return -arg0.value+value;
	}
	public static void main(String[] args){
	}
}
