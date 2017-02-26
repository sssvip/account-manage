package com.bjhetang.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by David on 2017/2/26.
 */
public class AccountManagementDTO implements Serializable {
    private HashMap<String, Object> out;

    public AccountManagementDTO(Object data) {
        out = new HashMap<String, Object>();
        out.put("data", data);
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this.out);
        } catch (JsonProcessingException e) {
            return "";
        }
    }
}
