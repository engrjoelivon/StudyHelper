package aivco.com.studyhelper;

/**
 *Interface that can be implemented or instantiated to define codes to respond when changes are to be made to a spinner object
 * the setSpinnerChange method accept int that will represent different cases,this is appropriate in a case where
 * there exist more than one spinner object,using the spinnerhelper interface in a class.In a case where it is just one spinner class
 * there will be no need to define a switch case.
 */
public interface SpinnerHelper {
   static final int CASEONE=1;
   static final int CASETWO=2;


    public void setSpinnerChange(int cases);

}
