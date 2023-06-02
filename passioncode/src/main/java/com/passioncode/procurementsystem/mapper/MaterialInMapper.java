package com.passioncode.procurementsystem.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.passioncode.procurementsystem.dto.MaterialInDTO;
import com.passioncode.procurementsystem.dto.MaterialInMapperDTO;

@Mapper
public interface MaterialInMapper {

	String getTime();
	
	List<MaterialInMapperDTO> getList();
}
