package com.leo.reggie.common;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException exception){
        String message = exception.getMessage();
        log.info(message);
        if(message.contains("Duplicate entry")){
            String[] split = message.split(" ");
            String msg = split[2]+" has already existed!";
            return R.error(msg);
        }



    return R.error("Unkown error!");
    }


    @ExceptionHandler(CustomException.class)
    public R<String> exceptionHandler(CustomException exception){


        return R.error(exception.getMessage());
    }


}
