import com.myspring.bean.*;
import com.myspring.core.ApplicationContext;
import com.myspring.core.ClassPathXmlApplicationContext;

public class Test {

	public static void main(String[] args) {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("/ApplicationContext.xml");
		User user = (User) ctx.getBean("UserProxy");
		user.sayHello();
		Student student = (Student) ctx.getBean("StudentProxy");
		student.doHello();
		student.doThrows();
	}

}
