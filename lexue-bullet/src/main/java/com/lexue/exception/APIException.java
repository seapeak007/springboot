package com.lexue.exception;

public class APIException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5414370935144695634L;
	private int errorCode;

	public APIException() {
		super();
	}

	public APIException(String msg, Throwable throwable) {
		super(msg, throwable);
	}

	public APIException(int errorCode, String msg) {
		super(msg);
		this.errorCode = errorCode;
	}

	public APIException(int errorCode, String msg, Throwable throwable) {
		super(msg, throwable);
		this.errorCode = errorCode;
	}

	@Override
	public String getMessage() {
		return super.getMessage();
	}

	@Override
	public String toString() {
		if (this.getErrorCode() == 0) {
			return super.toString();
		}
		return this.getErrorCode() + ":" + super.toString();

	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
}
