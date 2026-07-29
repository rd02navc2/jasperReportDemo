package com.beyond.surrounding.bonus.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.*;

@Data
@XmlRootElement(name = "CounterRequestBody")
@XmlAccessorType(XmlAccessType.FIELD)
public class CounterRequestBody {

    @XmlElement(name = "sCounterId")
    @JsonProperty("counterId")
    private String counterId;

}