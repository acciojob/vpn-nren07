package com.driver.model;

import com.google.common.base.Enums;
import org.junit.jupiter.api.Test;

import javax.persistence.Entity;
import javax.persistence.Table;


public enum CountryName {
    IND("001"),
    USA("002"),
    AUS("003"),
    CHI("004"),
    JPN("005");

    private final String code;

    private CountryName(String s) {
        code = s;
    }

    public String toCode() {
        return this.code;
    }


}
