package aivco.com.studyhelper;

/**
 * Created by joel on 7/19/15.
 */
public class RegistrationValidation {
    /////this function will check the text if it contains both @ and the . sign that guarantees and email/////
public Boolean emailValidation(String email )
{
    if(email.contains("@")&& email.contains(".")&& email.length()>6)
    {return true;}
    else
        return false;



}
    ////returns a boolean to validate if password lenght is more than 4 /////
public Boolean passwordValidation(String password)
{

   if(password.length()>4)
   {return true;}
   else
       return false;
}
public Boolean SpasswordValidation(String password)
    {

        if(password.length()>4)
        {return true;}
        else
            return false;
    }
    ////validates if username length is more than 3 values/////
public boolean namesValidation(String name)
{
  if(name.length()>3)
      return true;
   else
      return false;

}
public Boolean ConfirmPassword(String password,String password2)
{
     if(password.equals(password2))
         return true;
     else
         return false;
}

}
