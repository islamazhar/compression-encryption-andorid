package com.example.mazharul_islam.myapplication.utilities;



public class Element implements Comparable <Element>{
	//public String encodedString = null;
	public Integer count = 0;
	public  Character value = 0;
	
	public Element(Integer _count, Character _value){
		//encodedString = "";
		count = _count;
		value = _value;
	}
	@Override
	public boolean equals(Object arg0) {
		Element t = (Element) arg0;
		return t.count == count && 
			//	t.encodedString.equals(encodedString) && 
				t.value == value;
	}
	@Override
	public int hashCode() {
		return value.hashCode() ;
	}
	@Override
	public String toString() {
		return "{count = " + count +" char =" + value+"}"; 
		
	}
	@Override
	public int compareTo(Element arg0) {
				
		if(!arg0.count.equals(count)) {
			//System.out.println(arg0.count + " "+count);
			return -arg0.count+count;
		}
		return -arg0.value+value;
	}
	public static void main(String[] args) { 
	    		RedBlackBST<Element> ts = new RedBlackBST<Element>();
	    		ts.put(new Element(138,'a'));
	    		ts.put(new Element(138,'b'));
	    		ts.put(new Element(138,'z'));
	    		System.out.println(ts.toString());
	    }
}
