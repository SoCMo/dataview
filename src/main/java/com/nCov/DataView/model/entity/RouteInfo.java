package com.nCov.DataView.model.entity;

import com.nCov.DataView.model.request.RouteListRequest;
import lombok.Data;

@Data
public class RouteInfo extends RouteListRequest {
    private double sumTime;

    private double price;
}
