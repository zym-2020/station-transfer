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
 * @Date: 2024/01/30/20:25
 * @Description:
 */
@Repository
public interface TideMapper {
    void insertValue(@Param("tideList") List<Tide> tideList);

    void createTable(@Param("tableName") String tableName);

    List<Tide> pageQuery(@Param("stationId") String stationId, @Param("size") int size, @Param("start") int start);

    List<Tide> selectAllDataByTime(@Param("time") String time);

    List<Tide> selectDataByStationIdAndTime(@Param("stationId") String stationId, @Param("time") String time);

    List<Tide> selectDataAfterTime(@Param("stationId") String stationId, @Param("time") String time);

    List<Tide> selectDataByStationIdAndStartTimeAndEndTime(@Param("stationId") String stationId, @Param("startTime") String startTime, @Param("endTime") String endTime);

    List<Tide> selectDataByStartTimeAndEndTime(@Param("startTime") String startTime, @Param("endTime") String endTime);

    void insertValueByTableName(@Param("tideList") List<Tide> tideList, @Param("tableName") String tableName);

    void deleteDataByTime(@Param("time") String time);

    List<String> queryAllStationId();
}
