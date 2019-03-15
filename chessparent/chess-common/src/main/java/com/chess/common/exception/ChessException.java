package com.chess.common.exception;

import com.chess.common.enumcodes.ExceptionEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChessException extends RuntimeException {
	/**
		 * 
		 */
	private static final long serialVersionUID = 1L;
	private ExceptionEnum exceptionEnum;
}
