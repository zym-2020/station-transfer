package nnu.edu.reception.controller;

import nnu.edu.reception.common.result.JsonResult;
import nnu.edu.reception.common.result.ResultUtils;
import nnu.edu.reception.pojo.Flow;
import nnu.edu.reception.service.FlowService;
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
@RequestMapping("/flow")
public class FlowController {
    @Autowired
    FlowService flowService;

    @RequestMapping(value = "/pushData", method = RequestMethod.POST)
    public JsonResult pushData(@RequestBody List<Flow> list) {
        flowService.pushData(list);
        return ResultUtils.success();
    }

    @RequestMapping(value = "/getData/{stationId}/{startTime}/{endTime}", method = RequestMethod.GET)
    public JsonResult getData(@PathVariable String stationId, @PathVariable String startTime, @PathVariable String endTime) {
        return ResultUtils.success(flowService.getData(stationId, startTime, endTime));
    }

    @RequestMapping(value = "/pageQuery/{stationId}/{page}/{size}", method = RequestMethod.GET)
    public JsonResult pageQuery(@PathVariable String stationId, @PathVariable int page, @PathVariable int size) {
        return ResultUtils.success(flowService.pageQuery(stationId, page, size));
    }

    @RequestMapping(value = "/download/now/{stationId}", method = RequestMethod.GET)
    public void download(@PathVariable String stationId, HttpServletResponse response) throws IOException {
        flowService.download(stationId, response);
    }
}
