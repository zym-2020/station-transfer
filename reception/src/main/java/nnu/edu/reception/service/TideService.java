package nnu.edu.reception.service;

import nnu.edu.reception.pojo.Flow;
import nnu.edu.reception.pojo.Tide;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2024/01/30/20:43
 * @Description:
 */
public interface TideService {
    void pushData(List<Tide> list);

    List<Tide> getData(String stationId, String startTime, String endTime);

    List<Tide> pageQuery(String stationId, int page, int size);

    void download(String stationId, HttpServletResponse response) throws IOException;
}
