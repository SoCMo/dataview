package com.nCov.DataView.model.request;

import lombok.Data;

import javax.validation.Valid;
import java.util.List;

@Data
public class RouteListRequest {
    @Valid
    List<RouteCalRequest> routeCalRequestList;
};
