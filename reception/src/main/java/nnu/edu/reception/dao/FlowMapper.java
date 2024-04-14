package nnu.edu.reception.dao;

import nnu.edu.reception.pojo.Flow;
import nnu.edu.reception.pojo.Tide;
import org.apache.ibatis.annotations.Param;

import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2024/01/30/20:23
 * @Description:
 */
@Repository
public interface FlowMapper {
    void insertValue(@Param("flowList") List<Flow> flowList);

    void createTable(@Param("tableName") String tableName);

    List<Flow> selectAllData(@Param("stationId") String stationId);

    List<Flow> pageQuery(@Param("stationId") String stationId, @Param("size") int size, @Param("start") int start);

    List<Flow> selectAllDataByTime(@Param("time") String time);

    List<Flow> selectDataByStationIdAndTime(@Param("stationId") String stationId, @Param("time") String time);

    List<Flow> selectDataAfterTime(@Param("stationId") String stationId, @Param("time") String time);

    List<Flow> selectDataByStationIdAndStartTimeAndEndTime(@Param("stationId") String stationId, @Param("startTime") String startTime, @Param("endTime") String endTime);

    List<Flow> selectDataByStartTimeAndEndTime(@Param("startTime") String startTime, @Param("endTime") String endTime);

    void insertValueByTableName(@Param("flowList") List<Flow> flowList, @Param("tableName") String tableName);

    void deleteDataByTime(@Param("time") String time);

    List<String> queryAllStationId();
}
