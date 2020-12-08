package scanner.test.exception;

import com.github.natezhengbne.toolbox.exception.annotation.ExceptionAdvice;
import com.github.natezhengbne.toolbox.exception.annotation.ExceptionHandlerMapping;

import java.lang.reflect.InvocationTargetException;

@ExceptionAdvice
public class AnnotatedExceptionHandler2 {
    @ExceptionHandlerMapping(NullPointerException.class)
    public void processNullPointerException (Throwable error) {
        System.out.println("Processed in System Exception : "+error.getMessage());
    }

    @ExceptionHandlerMapping(InvocationTargetException.class)
    public void processInvocationTargetException (Throwable error) {
        System.out.println("Processed in Invocation Target Exception : "+error.getMessage());
    }

    public void testMethod2() {}
}
