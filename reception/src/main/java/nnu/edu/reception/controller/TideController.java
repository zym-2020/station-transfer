package nnu.edu.reception.controller;

import com.alibaba.fastjson2.JSONObject;
import nnu.edu.reception.common.result.JsonResult;
import nnu.edu.reception.common.result.ResultUtils;
import nnu.edu.reception.pojo.Tide;
import nnu.edu.reception.service.TideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2024/01/30/20:36
 * @Description:
 */
@RestController
@RequestMapping("/tide")
public class TideController {
    @Autowired
    TideService tideService;

    @RequestMapping(value = "/pushData", method = RequestMethod.POST)
    public JsonResult pushData(@RequestBody List<Tide> list) {
        tideService.pushData(list);
        return ResultUtils.success();
    }

    @RequestMapping(value = "/getData/{stationId}/{startTime}/{endTime}", method = RequestMethod.GET)
    public JsonResult getData(@PathVariable String stationId, @PathVariable String startTime, @PathVariable String endTime) {
        return ResultUtils.success(tideService.getData(stationId, startTime, endTime));
    }

    @RequestMapping(value = "/pageQuery/{stationId}/{page}/{size}", method = RequestMethod.GET)
    public JsonResult pageQuery(@PathVariable String stationId, @PathVariable int page, @PathVariable int size) {
        return ResultUtils.success(tideService.pageQuery(stationId, page, size));
    }

    @RequestMapping(value = "/download/now/{stationId}", method = RequestMethod.GET)
    public void download(@PathVariable String stationId, HttpServletResponse response) throws IOException {
        tideService.download(stationId, response);
    }

    @RequestMapping(value = "/downloadAll/{stationId}", method = RequestMethod.GET)
    public void downloadAll(@PathVariable String stationId, HttpServletResponse response) throws IOException {
        tideService.downloadAll(stationId, response);
    }
}
