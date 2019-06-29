package com.questionbank.exception;

import java.util.Map;

public class AuthException extends Exception{

	private static final long serialVersionUID = 1L;

	public AuthException(Map<String,String> errorMap){
    	super();
    }
}
