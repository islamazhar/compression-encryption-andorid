package utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class Disarrangement {
	double z, w;
	public Disarrangement(double _z, double _w) {
		z = _z;
		w = _w;
	}
	
	
	public int [] sequence(int n) {	
		ArrayList<Term> list = new ArrayList<Term>(); 
		
		
		for(int i=0;i<n;i++) {
			z = Math.cos(w*Math.acos(z));
			Term element = new Term(i,z);
			list.add(element);
		}
		Collections.sort(list, new Compare());
		int [] seq = new int[n];
		ArrayList<Integer> arr = new ArrayList<Integer>();
		for(int i=0;i<n;i++) {
			seq[i] = list.get(i).idx;
			if(seq[i] == i) {
				arr.add(i);
			}
			//System.out.println(list.get(i).val);
		}
		int len = arr.size();
		while(len>1) {
			int v1 = arr.get(len-1);
			int v2 = arr.get(len-2);
			System.out.println(v1+" "+v2);
			seq[v1] = v2;
			seq[v2] = v1;
			arr.remove(len-1);
			arr.remove(len-2);
			len-=2;
		}
		if(len==1) {
			for(int i=0;i<n;i++) {
				if(seq[arr.get(0)]!=seq[i]) {
					int v = seq[i];
					seq[i] = seq[arr.get(0)];
					seq[arr.get(0)] = v;
					break;
				}
			}
		}
		return seq;
	}
	public static void main(String[] args) {
		Disarrangement disarrangement = new Disarrangement(0.9,2);
		
		
		for(int num: disarrangement.sequence(10)) {
			
			System.out.print(num+" ");
		}
	}
}
class Term{
	public int idx;
	public double val;
	public Term(int _idx, double _val) {
		idx = _idx;
		val = _val;
	}
}


class Compare implements Comparator<Term> { 
	public int compare(Term x, Term y) { 
		return Double.compare(x.val, y.val);
	} 
}


