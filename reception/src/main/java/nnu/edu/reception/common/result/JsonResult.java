package nnu.edu.reception.common.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2024/01/30/20:01
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JsonResult<T> {
    private Integer code;

    private String msg;

    private T data;
}
