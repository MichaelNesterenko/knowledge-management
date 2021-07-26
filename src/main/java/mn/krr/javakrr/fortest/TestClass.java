package mn.krr.javakrr.fortest;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

public class TestClass {

	@MyCustomAnnotation
	public void method(String Object) {
		var var1 = new ArrayList<>();
	}

}

@Retention(RetentionPolicy.RUNTIME)
@interface MyCustomAnnotation {
	
}
