package com.chess.common.advice;

import com.chess.common.enumcodes.ExceptionEnum;
import com.chess.common.exception.ChessException;
import com.chess.common.util.Msg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;


//统一异常处理
@RestControllerAdvice
@Slf4j
public class BasicExceptionHandle {

	@ExceptionHandler(value = ChessException.class)
	public Msg handleException(ChessException e) {
		Msg msg = new Msg();
		ExceptionEnum exceptionEnum = e.getExceptionEnum();
		msg.setCode(exceptionEnum.getCode());
		msg.setMessage(exceptionEnum.getMessage());
		return msg;
	}

	//参数绑定异常处理
/*	@ExceptionHandler(value = BindException.class)
	public Msg myBindException(BindException e){
		List<FieldError> fieldErrors = e.getFieldErrors();
		String collect = fieldErrors.stream().map(fild -> fild.getDefaultMessage())
				.collect(Collectors.joining("|"));
		Msg msg = new Msg();
		msg.setCode(110);
		msg.setMessage(collect);
		return msg;
	}*/

	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	public Msg ValidParamBodyException(MethodArgumentNotValidException exception){
		BindingResult bindingResult = exception.getBindingResult();
		Msg msg = new Msg();
		if(bindingResult.hasErrors()){
			List<ObjectError> allErrors = bindingResult.getAllErrors();
			String collect = allErrors.stream().map(err -> err.getDefaultMessage()).collect(Collectors.joining("!"));
			msg.setCode(110);
			msg.setMessage(collect);
		}
		return msg;
	}
	
	/*@ResponseBody
	@ExceptionHandler(value =Exception.class)
	public Msg RException(Exception e) {
		Msg msg = new Msg();
		msg.setCode(500);
		msg.setMessage("服务器异常");
		log.info("服务器未检测异常{},{}",e.getCause(),e.getMessage());
		return msg;
	}*/
}
