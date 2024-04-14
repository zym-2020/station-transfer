package nnu.edu.reception.common.config;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import nnu.edu.reception.common.utils.FileUtil;
import nnu.edu.reception.dao.FlowMapper;
import nnu.edu.reception.dao.TideMapper;
import nnu.edu.reception.pojo.Flow;
import nnu.edu.reception.pojo.Tide;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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

    @Value("${resourceDir}")
    String resourceDir;

    @Scheduled(cron = "0 0 15 * * ?")
    public void database2file() throws IOException, ParseException {

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String cachePath = Paths.get(resourceDir, "cache.json").toString();
        JSONObject cacheJson = FileUtil.readJson(cachePath);
        JSONArray tides = cacheJson.getJSONArray("tide");
        for (int i = 0; i < tides.size(); i++) {
            JSONObject tide = tides.getJSONObject(i);
            List<Tide> tideList = tideMapper.selectDataByStationIdAndStartTimeAndEndTime(tide.getString("stationId"), tide.getString("startTime"), tide.getString("endTime"));
            if (tideList.size() > 0) {
                Path path = Paths.get(tideDir, tide.getString("stationId"), tide.getString("stationName") + tide.getString("endTime") + ".txt");
                Files.createDirectories(path.getParent());
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(path.toString()))) {
                    for (Tide tideObj : tideList) {
                        writer.write(tideObj.getTime() + "\t" + tideObj.getTideValue() + "\n");
                    }
                } catch (IOException e) {
                    log.error("写入文件时出现错误：" + e.getMessage());
                }
                if (tide.getBoolean("updateWaterLevelFile")) {
                    try (PrintWriter writer = new PrintWriter(new FileOutputStream(Paths.get(resourceDir, tide.getString("stationName") + ".txt").toString(), true))) {
                        for (Tide tideObj : tideList) {
                            writer.write(tideObj.getTime() + "\t" + tideObj.getTideValue() + "\n");
                        }
                    } catch (IOException e) {
                        log.error("追加文件时出现错误：" + e.getMessage());
                    }
                    try (FileInputStream fis = new FileInputStream(Paths.get(resourceDir, tide.getString("stationName") + ".txt").toString()); FileOutputStream fos = new FileOutputStream(Paths.get(tide.getString("waterLevelAddress"), tide.getString("stationName") + tide.getString("endTime") + ".txt").toString())) {
                        int bytesRead;
                        byte[] buffer = new byte[1024];
                        while ((bytesRead = fis.read(buffer)) != -1) {
                            fos.write(buffer, 0, bytesRead);
                        }
                    } catch (IOException e) {
                        log.error("复制文件时发生错误：" + e.getMessage());
                    }
                }
            }
            tide.put("startTime", tide.getString("endTime"));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateFormat.parse(tide.getString("endTime")));
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            Date nextDay = calendar.getTime();
            tide.put("endTime", dateFormat.format(nextDay));
        }
        JSONArray flows = cacheJson.getJSONArray("flow");
        for (int i = 0; i < flows.size(); i++) {
            JSONObject flow = flows.getJSONObject(i);
            List<Flow> flowList = flowMapper.selectDataByStationIdAndStartTimeAndEndTime(flow.getString("stationId"), flow.getString("startTime"), flow.getString("endTime"));
            if (flowList.size() > 0) {
                Path path = Paths.get(flowDir, flow.getString("stationId"), flow.getString("stationName") + flow.getString("endTime") + ".txt");
                Files.createDirectories(path.getParent());
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(path.toString()))) {
                    for (Flow flowObj : flowList) {
                        writer.write(flowObj.getTime() + "\t" + flowObj.getWaterLevelValue() + "\t" + flowObj.getFlowValue() + "\n");
                    }
                } catch (IOException e) {
                    log.error("写入文件时出现错误：" + e.getMessage());
                }
                if (flow.getBoolean("updateWaterLevelFile")) {
                    try (PrintWriter writer = new PrintWriter(new FileOutputStream(Paths.get(resourceDir, flow.getString("stationName") + ".txt").toString(), true))) {
                        for (Flow flowObj : flowList) {
                            writer.write(flowObj.getTime() + "\t" + flowObj.getWaterLevelValue() + "\t" + flowObj.getFlowValue() + "\n");
                        }
                    } catch (IOException e) {
                        log.error("追加文件时出现错误：" + e.getMessage());
                    }
                    try (FileInputStream fis = new FileInputStream(Paths.get(resourceDir, flow.getString("stationName") + ".txt").toString()); FileOutputStream fos = new FileOutputStream(Paths.get(flow.getString("waterLevelAddress"), flow.getString("stationName") + flow.getString("endTime") + ".txt").toString())) {
                        int bytesRead;
                        byte[] buffer = new byte[1024];
                        while ((bytesRead = fis.read(buffer)) != -1) {
                            fos.write(buffer, 0, bytesRead);
                        }
                    } catch (IOException e) {
                        log.error("复制文件时发生错误：" + e.getMessage());
                    }
                }
            }
            flow.put("startTime", flow.getString("endTime"));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateFormat.parse(flow.getString("endTime")));
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            Date nextDay = calendar.getTime();
            flow.put("endTime", dateFormat.format(nextDay));
        }
        FileUtil.writeJson(cachePath, cacheJson);

    }
}
