package com.lexue.http;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author danielmiao
 *
 */
@Data
@AllArgsConstructor
public class CommonResponse implements Serializable{


	/**
	 * 返回码
	 */
	private int rpco;

	/**
	 * 错误信息
	 */
	private String msg;
}
