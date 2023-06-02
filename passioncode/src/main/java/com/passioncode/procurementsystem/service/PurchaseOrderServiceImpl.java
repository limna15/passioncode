package com.passioncode.procurementsystem.service;

import org.springframework.stereotype.Service;

import com.passioncode.procurementsystem.repository.PurchaseOrderRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class PurchaseOrderServiceImpl implements PurchaseOrderService{
	
	private final PurchaseOrderRepository purchaseOrderRepository;

}
