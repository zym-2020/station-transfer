package nnu.edu.reception.service.iml;

import nnu.edu.reception.common.utils.CommonUtil;
import nnu.edu.reception.dao.TideMapper;
import nnu.edu.reception.pojo.Tide;
import nnu.edu.reception.service.TideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2024/01/30/20:44
 * @Description:
 */
@Service
public class TideServiceImpl implements TideService {
    @Autowired
    TideMapper tideMapper;
    @Override
    public void pushData(List<Tide> list) {
        if (list.size() > 0) {
            tideMapper.insertValue(list);
        }
    }

    @Override
    public List<Tide> getData(String stationId, String startTime, String endTime) {
        long st = Long.parseLong(startTime);
        long et = Long.parseLong(endTime);
        return tideMapper.selectDataByStationIdAndStartTimeAndEndTime(stationId, CommonUtil.timestamp2formattedTime(st), CommonUtil.timestamp2formattedTime(et));
    }

    @Override
    public List<Tide> pageQuery(String stationId, int page, int size) {
        return tideMapper.pageQuery(stationId, page, page * size);
    }
}
