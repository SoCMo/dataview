package com.nCov.DataView.controller;

import com.nCov.DataView.exception.AllException;
import com.nCov.DataView.model.response.Result;
import com.nCov.DataView.service.StudentService;
import com.nCov.DataView.tools.GaoDeTool;
import com.nCov.DataView.tools.ResultTool;
import org.apache.xmlbeans.impl.xb.xsdschema.All;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.Min;
import java.io.IOException;


@CrossOrigin
@RestController
@RequestMapping("/student")
@Validated
public class StudentController {
    @Resource
    private StudentService studentService;

    @Resource
    private GaoDeTool gaoDeTool;

    @GetMapping("/studentInfo")
    public Result studentInfo(@Min(value = 0, message = "偏移量最小为0") int index, int max) {
        return studentService.studentInfo(index, max);
    }

    @GetMapping("/testTool")
    public Result getResult(@RequestParam(value = "start") String start,
                            @RequestParam(value = "end") String end,
                            @RequestParam(value = "way") String way,
                            @RequestParam(value = "type") Integer type) throws AllException, IOException {
        return ResultTool.success(gaoDeTool.getSitesList(start, end, way, type));
    }
}
