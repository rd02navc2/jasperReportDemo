package com.beyoung.member.infrastructure;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LpkFileId implements Serializable {
    private String lpk01;
    private String lpk09;
}
