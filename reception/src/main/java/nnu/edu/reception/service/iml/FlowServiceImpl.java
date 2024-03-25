package nnu.edu.reception.service.iml;

import lombok.extern.slf4j.Slf4j;
import nnu.edu.reception.common.utils.CommonUtil;
import nnu.edu.reception.dao.FlowMapper;
import nnu.edu.reception.pojo.Flow;
import nnu.edu.reception.service.FlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2024/01/30/20:43
 * @Description:
 */
@Service
@Slf4j
public class FlowServiceImpl implements FlowService {
    @Autowired
    FlowMapper flowMapper;
    @Override
    public void pushData(List<Flow> list) {
        if (list.size() > 0) {
            flowMapper.insertValue(list);
        }
    }

    @Override
    public List<Flow> getData(String stationId, String startTime, String endTime) {
        long st = Long.parseLong(startTime);
        long et = Long.parseLong(endTime);
        return flowMapper.selectDataByStationIdAndStartTimeAndEndTime(stationId, CommonUtil.timestamp2formattedTime(st), CommonUtil.timestamp2formattedTime(et));
    }

    @Override
    public List<Flow> pageQuery(String stationId, int page, int size) {
        return flowMapper.pageQuery(stationId, page, page * size);
    }
}
