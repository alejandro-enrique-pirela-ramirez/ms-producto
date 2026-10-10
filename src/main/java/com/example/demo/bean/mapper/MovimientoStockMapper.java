package com.example.demo.bean.mapper;

import org.mapstruct.Mapper;

import com.example.demo.bean.dto.MovimientoStockDto;
import com.example.demo.bean.entity.MovimientoStock;

@Mapper(componentModel = "spring")
public interface MovimientoStockMapper {

	MovimientoStockDto toDto(MovimientoStock movimiento);
}