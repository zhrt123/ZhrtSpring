import com.myspring.bean.*;
import com.myspring.core.ApplicationContext;
import com.myspring.core.ClassPathXmlApplicationContext;

public class Test {

	public static void main(String[] args) {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("/ApplicationContext.xml");
		User user = (User) ctx.getBean("UserProxy");
		user.sayHello();
		
		
		System.out.println("");
		Student student = (Student) ctx.getBean("StudentProxy");
		student.doHello();
		System.out.println("");
		student.doThrows();
		System.out.println("");
		A a = (A) ctx.getBean("A");
		B b = (B) ctx.getBean("B");
		
		System.out.println(a);
		System.out.println(b);
	}

}
