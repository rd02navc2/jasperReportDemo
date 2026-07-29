package com.beyoung.parking.infrastructure;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LsmFileId implements Serializable {
    private String lsm01;
    private String lsm02;
    private String lsm03;
}