package com.passioncode.procurementsystem.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.passioncode.procurementsystem.dto.PurchaseOrderMapperDTO;

@Mapper
public interface PurchaseOrderMapper {

	String getString();
	
	List<PurchaseOrderMapperDTO> getList();

}
