package nnu.edu.reception.service.iml;

import lombok.extern.slf4j.Slf4j;
import nnu.edu.reception.common.utils.CommonUtil;
import nnu.edu.reception.dao.TideMapper;
import nnu.edu.reception.pojo.Tide;
import nnu.edu.reception.service.TideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2024/01/30/20:44
 * @Description:
 */
@Service
@Slf4j
public class TideServiceImpl implements TideService {
    @Autowired
    TideMapper tideMapper;

    @Value("${resourceDir}")
    String resourceDir;

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

    @Override
    public void download(String stationId, HttpServletResponse response) throws IOException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = new Date();
        String currentDateString = dateFormat.format(currentDate);
        Path path = Paths.get(resourceDir, "temp", stationId + ".txt");
        String tempPath = path.toString();
        Files.createDirectories(path.getParent());
        List<Tide> tideList = tideMapper.selectDataAfterTime(stationId, currentDateString);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempPath))) {
            for (Tide tideObj : tideList) {
                writer.write(tideObj.getTime() + "\t" + tideObj.getTideValue() + "\n");
            }
        } catch (IOException e) {
            log.error("写入文件时出现错误：" + e.getMessage());
        }
        File file = new File(tempPath);
        response.setContentType("application/octet-stream");
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(stationId + ".txt", "UTF-8"));
        response.addHeader("Content-Length", "" + file.length());
        InputStream in = new FileInputStream(file);
        ServletOutputStream sos = response.getOutputStream();
        byte[] b = new byte[1024];
        int len;
        while((len = in.read(b)) > 0) {
            sos.write(b, 0, len);
        }
        sos.flush();
        sos.close();
        in.close();
    }
}
