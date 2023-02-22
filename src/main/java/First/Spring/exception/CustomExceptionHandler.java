package First.Spring.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<?> MethodArgumentNotValidExceptionMethod(MethodArgumentNotValidException e){
        Map<String, Object> res = new HashMap<>();
        res.put("msg","Validation Error");
        res.put("Status","400");
        List<Object> list = new ArrayList<>();
        for (FieldError error : e.getFieldErrors()) {
            Map map = new HashMap<>();
            map.put("field",error.getField());
            map.put("message",error.getDefaultMessage());
            map.put("object",error.getObjectName());
            list.add(map);
        }
        res.put("data", list);
        return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<?> SQLIntegrityConstraintViolationExceptionMethod(SQLIntegrityConstraintViolationException e){
        Map<String, Object> res = new HashMap<>();
        res.put("message",e.getMessage());
        res.put("status",HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(res,HttpStatus.EXPECTATION_FAILED);
    }
}
