package com.nCov.DataView.controller;

import com.nCov.DataView.model.response.Result;
import com.nCov.DataView.service.StudentService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.Min;


@CrossOrigin
@RestController
@RequestMapping("/student")
@Validated
public class StudentController {
    @Resource
    private StudentService studentService;

    @GetMapping("/studentInfo")
    public Result studentInfo(@Min(value = 0, message = "偏移量最小为0") int minIndex, int maxIndex) {
        return studentService.studentInfo(minIndex, maxIndex);
    }
}
