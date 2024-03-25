package nnu.edu.reception.common.config;

import lombok.extern.slf4j.Slf4j;
import nnu.edu.reception.dao.FlowMapper;
import nnu.edu.reception.dao.TideMapper;
import nnu.edu.reception.pojo.Flow;
import nnu.edu.reception.pojo.Tide;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2024/03/22/13:16
 * @Description:
 */
@Component
@Slf4j
public class TimeTask {
    @Autowired
    FlowMapper flowMapper;

    @Autowired
    TideMapper tideMapper;

    @Value("${flowDir}")
    String flowDir;

    @Value("${tideDir}")
    String tideDir;

    @Scheduled(cron = "0 0 8 * * ?")
    public void database2file() throws IOException {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String endTime = currentDate.format(formatter);
        String startTime = currentDate.minusDays(1).format(formatter);
        List<String> flowStationIds = flowMapper.queryAllStationId();
        List<String> tideStationIds = tideMapper.queryAllStationId();
        for (String stationId : flowStationIds) {
            List<Flow> flowList = flowMapper.selectDataByStationIdAndStartTimeAndEndTime(stationId, startTime, endTime);
            Path path = Paths.get(flowDir, stationId, startTime + ".txt");
            Files.createDirectories(path.getParent());
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(path.toString()))) {
                for (Flow flow : flowList) {
                    writer.write(flow.getTime() + "\t" + flow.getWaterLevelValue() + "\t" + flow.getFlowValue() + "\n");
                }
            } catch (IOException e) {
                log.error("写入文件时出现错误：" + e.getMessage());
            }
        }
        for (String stationId : tideStationIds) {
            List<Tide> tideList = tideMapper.selectDataByStationIdAndStartTimeAndEndTime(stationId, startTime, endTime);
            Path path = Paths.get(tideDir, stationId, startTime + ".txt");
            Files.createDirectories(path.getParent());
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(path.toString()))) {
                for (Tide flow : tideList) {
                    writer.write(flow.getTime() + "\t" + flow.getTideValue() + "\n");
                }
            } catch (IOException e) {
                log.error("写入文件时出现错误：" + e.getMessage());
            }
        }
    }
}
