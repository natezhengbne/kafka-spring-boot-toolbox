package scanner.test.exception;

import com.github.natezhengbne.toolbox.exception.annotation.ExceptionHandlerMapping;

public class NotAnnotatedClass {
    @ExceptionHandlerMapping(NullPointerException.class)
    public void processNullPointerException (Throwable error) {
        System.out.println("Processed in System Exception : "+error.getMessage());
    }
}
