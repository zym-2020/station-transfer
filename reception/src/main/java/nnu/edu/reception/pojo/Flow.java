package nnu.edu.reception.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2024/01/30/20:19
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Flow {
    String stationId;
    String time;
    Double waterLevelValue;
    Double flowValue;
}
