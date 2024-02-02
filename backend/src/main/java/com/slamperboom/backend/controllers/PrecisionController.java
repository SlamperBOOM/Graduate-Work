package com.slamperboom.backend.controllers;

import com.slamperboom.backend.frontendDTO.PrecisionRequestDTO;
import com.slamperboom.backend.mathematics.MathService;
import com.slamperboom.backend.mathematics.results.ResultDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("precision")
public class PrecisionController {
    @Autowired
    private MathService mathService;

    @GetMapping("predict")
    public List<ResultDTO> makePrecision(@RequestBody PrecisionRequestDTO requestDTO){
        return mathService.makePrecision(requestDTO.taxName(), requestDTO.methodName(), requestDTO.params());
    }

    @GetMapping("")
    public
}
