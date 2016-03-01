package aivco.com.studyhelper;

/**
 * An interface defined to be declared in any class that wants to be notified when error occurs.When defined its instance will
 * be passed to the class where the error is expected to take place,when the error occurs in the class the object will call error
 * so invocation can immediately move back to the class that needs notification.
 */
public interface ErrorHolder {
    public void error(String error);
}
