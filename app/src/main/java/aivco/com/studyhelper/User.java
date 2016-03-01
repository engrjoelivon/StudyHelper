package aivco.com.studyhelper;

/**
 * Defines getters and setters methods for a user
 */
public  class User {
    String username,password,email;
    public User(String username,String Password,String email)
    {
        this.username=username;
        this.password=Password;
        this.email=email;

    }
    public User(String email,String Password)
    {
        this.password=Password;
        this.email=email;

    }
    public User()
    {


    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}