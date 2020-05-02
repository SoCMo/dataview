package com.nCov.DataView.model.request;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class RouteListRequest {
    @Valid
    @Size(min = 1, message = "至少有一个路段")
    private List<RouteCalRequest> routeCalRequestList;

    @NotEmpty
    private String start;

    @NotEmpty
    private String end;
};
