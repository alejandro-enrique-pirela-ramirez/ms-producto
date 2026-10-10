package com.example.demo.bean.mapper;

import org.mapstruct.Mapper;

import com.example.demo.bean.dto.ProductoDto;
import com.example.demo.bean.entity.Producto;

@Mapper(componentModel = "spring")
public interface ProductoMapper {
  
	 public ProductoDto toDto (Producto producto);
	 public Producto toEntity (ProductoDto dto);
}
