package com.nCov.DataView.model.response.info;

import lombok.Data;

@Data
public
class AreaCalInfo {
    double sum;

    String name;

    public AreaCalInfo(String name, String sumScore) {
        this.name = name;
        this.sum = Double.parseDouble(sumScore);
    }
}
