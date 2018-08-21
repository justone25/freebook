package com.lee.freebook.ippool.ipmodel;

import lombok.Data;

import java.io.Serializable;

/**
 * 想要将该对象存储倒Redis List中，必须对其实现序列化于反序列化
 */
@Data
public class IpMessage implements Serializable {
    private static final long serialVersionUID = 1L;
    private String IPAddress;
    private String IPPort;
    private String IPType;
    private String IPSpeed;
}
