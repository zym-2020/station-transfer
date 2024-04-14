package nnu.edu.reception.service;

import nnu.edu.reception.pojo.Flow;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
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

    void download(String stationId, HttpServletResponse response) throws IOException;

    void downloadAll(String stationId, HttpServletResponse response) throws IOException;
}
