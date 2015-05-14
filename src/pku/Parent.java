package pku;

public class Parent {
	static{
		System.out.println("Parent static");
	}
	public Parent(){
		System.out.println("Parent");
	}
	public Parent(String s){
		System.out.println("Parent"+s);
	}
	public void show(){
		System.out.println("aaa");
	}
}
