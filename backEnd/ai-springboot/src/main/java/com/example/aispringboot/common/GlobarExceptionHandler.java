package com.example.aispringboot.common;

import com.example.aispringboot.exception.BusinessException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.stream.Collectors;

@RestControllerAdvice

public class GlobarExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    //处理参数校验异常
    public Result<String> handlerExecption(MethodArgumentNotValidException e){
        //处理异常数据的处理
        String message = e.getBindingResult().getFieldErrors().stream().map(
                FieldError::getDefaultMessage
        ).collect(Collectors.joining(","));

        return Result.error(ResultCode.PARAM_ERROR.getCode(),ResultCode.PARAM_ERROR.getCode(),message);
    }

    // 处理业务异常
    @ExceptionHandler(BusinessException.class)
    public Result<?> handleBusinessException(BusinessException e){
        // 如果异常携带额外数据(比如携带 data)
        if (e.getData() != null){
            return Result.error(e.getCode(),e.getMessage(),e.getData());
        }
        return Result.error(e.getCode(),e.getMessage(),null);
    }
}
