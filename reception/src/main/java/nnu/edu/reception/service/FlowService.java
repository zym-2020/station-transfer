package nnu.edu.reception.service;

import nnu.edu.reception.pojo.Flow;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2024/01/30/20:42
 * @Description:
 */
public interface FlowService {
    void pushData(List<Flow> list);

    List<Flow> getData(String stationId, String startTime, String endTime);

    List<Flow> pageQuery(String stationId, int page, int size);
}
