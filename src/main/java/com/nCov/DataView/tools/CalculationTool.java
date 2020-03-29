package com.nCov.DataView.tools;

import com.nCov.DataView.dao.RouteCalDOMapper;
import com.nCov.DataView.exception.AllException;
import com.nCov.DataView.exception.EmAllException;
import com.nCov.DataView.model.ConstCorrespond;
import com.nCov.DataView.model.entity.RouteCalDO;
import com.nCov.DataView.model.entity.RouteCalDOExample;
import com.nCov.DataView.model.request.RouteCalRequest;
import com.nCov.DataView.model.response.Result;
import com.nCov.DataView.model.response.info.CityCal;
import com.nCov.DataView.model.response.info.CovRankResponse;
import com.nCov.DataView.model.response.info.RouteCalReponse;
import com.nCov.DataView.model.response.info.SumCalResponse;
import com.nCov.DataView.service.Impl.EpidemicServiceImpl;
import org.springframework.beans.BeanUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @description: 计算公式工具
 * @author: pongshy
 * @create: 2020/3/29 16:05
 */
public class CalculationTool {

}
