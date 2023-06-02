package com.passioncode.procurementsystem.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import com.passioncode.procurementsystem.dto.ProgressCheckMapperDTO;

@Mapper
public interface PurchaseOrderMapper {

	String getTime();

	List<ProgressCheckMapperDTO> getList();

}
