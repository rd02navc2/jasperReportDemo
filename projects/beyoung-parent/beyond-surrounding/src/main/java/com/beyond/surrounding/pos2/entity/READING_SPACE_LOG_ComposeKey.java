package com.beyond.surrounding.pos2.entity;

import lombok.*;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class READING_SPACE_LOG_ComposeKey implements Serializable {
	
	private static final long serialVersionUID = 1L;

    private String center;
    private Date transactionDate;
    private String userId;
}