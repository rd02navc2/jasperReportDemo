package com.beyond.surrounding.pos.dto;

import com.beyond.surrounding.pos.entity.TD;
import com.beyond.surrounding.pos.entity.TP;
import com.beyond.surrounding.pos.entity.TR;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PosDetailDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private TD transaction;
    private List<TR> productions;
    private TP creditCard;
}