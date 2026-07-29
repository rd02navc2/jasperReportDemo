package com.beyoung.member.infrastructure;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LpjFileId implements Serializable {
    private String lpj01;
    private String lpj09;
}