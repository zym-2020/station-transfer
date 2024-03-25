package nnu.edu.reception.common.exception;

import nnu.edu.reception.common.result.JsonResult;
import nnu.edu.reception.common.result.ResultUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2024/01/30/20:04
 * @Description:
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = MyException.class)
    @ResponseBody
    public JsonResult myExceptionHandler(MyException e) {
        return ResultUtils.fail(e.getCode(), e.getMsg());
    }
}
