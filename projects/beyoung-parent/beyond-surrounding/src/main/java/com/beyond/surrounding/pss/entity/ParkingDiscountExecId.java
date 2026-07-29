package com.beyond.surrounding.pss.entity;

import lombok.*;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParkingDiscountExecId implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer pNo;
    private String carNo;
    private String discId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParkingDiscountExecId that = (ParkingDiscountExecId) o;
        return Objects.equals(pNo, that.pNo) &&
               Objects.equals(carNo, that.carNo) &&
               Objects.equals(discId, that.discId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pNo, carNo, discId);
    }
}