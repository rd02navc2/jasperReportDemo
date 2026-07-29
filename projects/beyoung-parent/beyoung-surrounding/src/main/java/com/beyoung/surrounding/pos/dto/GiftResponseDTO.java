package com.beyoung.surrounding.pos.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GiftResponseDTO implements Serializable {
	
    private static final long serialVersionUID = 1L;


    private Trans trans;

    public Trans getTrans() {
        return trans;
    }

    public void setTrans(Trans trans) {
        this.trans = trans;
    }

    public static class Trans {
        private String T3900;

        public String getT3900() {
            return T3900;
        }

        public void setT3900(String t3900) {
            T3900 = t3900;
        }
    }
    
}