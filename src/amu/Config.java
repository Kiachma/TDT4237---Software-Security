package amu;

public class Config {
    public final static String JDBC_RESOURCE = "jdbc/bookstore";
    
    public final static String EMAIL_SMTP_HOST = "smtp.gmail.com";
    public final static String EMAIL_SMTP_PORT = "587";
    public final static String EMAIL_SMTP_USER = "tdt4237.amu.darya";
    public final static String EMAIL_SMTP_PASSWORD = "jcmrgkikcdqoyjms"; // Application-specific password
    
    public final static String EMAIL_FROM_ADDR = "tdt4237.amu.darya@gmail.com";
    public final static String EMAIL_FROM_NAME = "Amu-Darya";
    
    public final static String PEPPER="NaCl"; // Changing this will invalidate all customer passwords in DB
    public final static String RECAPTCHA_PRIVATE_KEY="6LetKOgSAAAAAMKVfJSDVvZnfG21V9jsYvE9ebdU"; // Changing this will invalidate all customer passwords in DB
    public final static String RECAPTCHA_PUBLIC_KEY="6LetKOgSAAAAANQzykU7R3opFFcaAuQYJYqQKhmX";
    public final static int MAX_INPUT_LENGTH=255;
}
