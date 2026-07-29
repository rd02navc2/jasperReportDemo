package com.beyoung.parking.infrastructure;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LrqFileId implements Serializable {
    private String lrq02;
    private String lrqplant;
}