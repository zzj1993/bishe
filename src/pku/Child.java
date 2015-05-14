package pku;

public class Child extends Parent{
	/*public void show(){
		System.out.println("bbb");
	}*/
	static{
		System.out.println("Child static");
	}
	public Child(String s){
//		super("pp1");
		System.out.println("Child"+s);
	}
	/*public Child(){
		super("pp2");
		System.out.println("Child");
	}*/
}
