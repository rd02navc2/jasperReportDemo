package com.beyond.surrounding.ec.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.beyond.surrounding.ec.entity.TC_LRJ_FILE;
import com.beyond.surrounding.ec.repository.RedeemRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedeemService {
	
	private final RedeemRepository redeemRepository;

    public TC_LRJ_FILE getRule() {
        List<TC_LRJ_FILE> rules = redeemRepository.findRules();
        
        if (rules.isEmpty()) {
            return new TC_LRJ_FILE(); // Replicating legacy behavior
        }
        
        // Replicating legacy loop behavior (returning the last element)
        return rules.get(rules.size() - 1);
    }
}