package com.passioncode.procurementsystem.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.passioncode.procurementsystem.dto.DetailPurchaseOrderMapperDTO;

@Mapper
public interface DetailPurchaseOrderMapper {
	
	String getString();
	
	List<DetailPurchaseOrderMapperDTO> getList();

}
