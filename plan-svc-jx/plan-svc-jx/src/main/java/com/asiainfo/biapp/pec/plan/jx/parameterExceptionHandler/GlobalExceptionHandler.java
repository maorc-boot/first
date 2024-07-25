package com.asiainfo.biapp.pec.plan.jx.parameterExceptionHandler;

import com.asiainfo.biapp.pec.core.common.ActionResponse;
import org.springframework.core.annotation.Order;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;

/**
 * @projectName: serviceend-jx
 * @package: com.asiainfo.biapp.pec.plan.jx.parameterExceptionHandler
 * @className: MethodArgumentNotValidExceptionHandler
 * @author: chenlin
 * @description: 系统异常捕获没有处理此异常，添加此异常处理器
 * @date: 2023/6/12 11:08
 * @version: 1.0
 */
@RestControllerAdvice
@Order(1) //在系统的全局异常捕获之前
public class GlobalExceptionHandler {

    //参数列表校验规则抛出的异常
    @ExceptionHandler
    public ActionResponse handlerConstraintViolationException(ConstraintViolationException exception) {
        StringBuilder errorMessage = new StringBuilder();
        Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
        constraintViolations.forEach(o -> errorMessage.append(
                o.getMessage().contains("MultipartFile") ? "请选择上传的文件！" : o.getMessage() + "；")
        );
        return ActionResponse.getFaildResp(errorMessage.deleteCharAt(errorMessage.length() - 1).toString());
    }

    //实体类校验规则抛出的异常
    @ExceptionHandler
    public ActionResponse handlerBindException(BindException bindException) {
        StringBuilder errorMessage = new StringBuilder();
        List<FieldError> fieldErrors = bindException.getFieldErrors();
        fieldErrors.forEach(o -> errorMessage.append(
                o.getDefaultMessage().contains("MultipartFile") ? "请选择上传的文件！" : o.getDefaultMessage() + "；")
        );
        return ActionResponse.getFaildResp(errorMessage.deleteCharAt(errorMessage.length() - 1).toString());
    }
}
