package edu.gmu.isa681.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.gmu.isa681.dao.UnregisteredPlayerDao;
import edu.gmu.isa681.model.UnregisteredPlayer;

@Service("unregisteredPlayerService")
@Transactional
public class UnregisteredPlayerServiceImpl implements UnregisteredPlayerService{

	@Autowired
	private UnregisteredPlayerDao dao;
	
	public String checkPlayer(UnregisteredPlayer uPlayer) {
		String errMsg = "";
		
        if (!uPlayer.getPassword().equals(uPlayer.getPassword2())) {
        	errMsg = "Passwords do not match";
        } if (isValidPlayerFirstName(uPlayer.getFirstName()) == 0) {
        	errMsg = "Invalid First Name.  <br>"
        			+ "Only [a-zA-Z ] characters allowed, with a limit of 1 to 32 characters.";
        } else if (isValidPlayerLastName(uPlayer.getLastName()) == 0) {
        	errMsg = "Invalid Last Name.  <br>"
        			+ "Only [a-zA-Z '.] characters allowed, between 1 and 32 characters.";
        } else if (isValidPlayerSso(uPlayer.getSsoId()) == 0) {
        	errMsg = "Invalid SSO ID.  <br>"
        			+ "Only alpha-numeric characters allowed, between 1 and 10 characters.";
        } else if (isMatchPlayerSso(uPlayer.getSsoId()) == 0) {
        	errMsg = "SSO ID already in use.";
        } else if (isValidPlayerPassword(uPlayer.getPassword()) == 0) {
        	errMsg = "Invalid Password.  <br>"
        			+ "Passwords must contain: <br>"
        			+ "1. at least 1 number, <br>"
        			+ "2. at least 1 upper-case character, <br>"
        			+ "3. at least 1 lower-case character, <br>"
        			+ "4. at least 1 special character (of @#$%^&+=), <br>"
        			+ "5. no white space.  <br>"
        			+ "Password length must be between 12 to 32 characters.";
        } else if (isValidPlayerEmail(uPlayer.getEmail()) == 0) {
        	errMsg = "Invalid Email Address.  <br>"
        			+ "Only valid email formats allowed, between 6 and 64 characters.";
        } else if (isMatchPlayerEmail(uPlayer.getEmail()) == 0) {
        	errMsg = "Email Address already in use.";
        }
		
		return errMsg;
	}
	
	public Integer isValidPlayerFirstName(String name) {
	    Integer isValid = 0;
	    String expression = "^[a-zA-Z ]*$"; 
	    CharSequence inputStr = name;
	    
	    Pattern pattern = Pattern.compile(expression);
	    Matcher matcher = pattern.matcher(inputStr);
	    if (name.length() >= 1 && name.length() <= 32 && matcher.find()) {
	        isValid = 1;
	    }
	    return isValid;
	}
	
	public Integer isValidPlayerLastName(String name) {
	    Integer isValid = 0;
	    String expression = "^[a-zA-Z '.]*$"; 
	    CharSequence inputStr = name;
	    
	    Pattern pattern = Pattern.compile(expression);
	    Matcher matcher = pattern.matcher(inputStr);
	    if (name.length() >= 1 && name.length() <= 32 && matcher.find()) {
	        isValid = 1;
	    }
	    return isValid;
	}

	public Integer isValidPlayerSso(String sso) {
	    Integer isValid = 0;
	    String expression = "^[a-zA-Z0-9]*$"; 
	    CharSequence inputStr = sso;
	    
	    Pattern pattern = Pattern.compile(expression);
	    Matcher matcher = pattern.matcher(inputStr);
	    if (sso.length() >= 1 && sso.length() <= 10 && matcher.find()) {
	        isValid = 1;
	    }
	    return isValid;
	}
	
	public Integer isValidPlayerPassword(String password) {
	    Integer isValid = 0;
	    String expression = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{12,32}$"; 
	    CharSequence inputStr = password;
	    
	    Pattern pattern = Pattern.compile(expression);
	    Matcher matcher = pattern.matcher(inputStr);
	    if (matcher.find()) {
	        isValid = 1;
	    }
	    return isValid;
	}

	//regex from http://www.mkyong.com/regular-expressions/how-to-validate-email-address-with-regular-expression/
	public Integer isValidPlayerEmail(String email) {
	    Integer isValid = 0;
	    String expression = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	    CharSequence inputStr = email;
	    
	    Pattern pattern = Pattern.compile(expression);
	    Matcher matcher = pattern.matcher(inputStr);
	    if (email.length() <= 64 && matcher.find()) {
	        isValid = 1;
	    }
	    return isValid;
	}

	public Integer isMatchPlayerSso(String sso) {
		return dao.isMatchPlayerSso(sso);
	}

	public Integer isMatchPlayerEmail(String email) {
		return dao.isMatchPlayerEmail(email);
	}

}
