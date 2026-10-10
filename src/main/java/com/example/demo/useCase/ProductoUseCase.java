package com.example.demo.useCase;

import java.util.List;

import com.example.demo.bean.dto.MovimientoStockDto;
import com.example.demo.bean.dto.ProductoDto;


public interface ProductoUseCase {
     public ProductoDto Registrar(ProductoDto dto);
     public ProductoDto Actualizar(ProductoDto dto);
     public ProductoDto BuscarPorId(Integer id);
     public List<ProductoDto>Listar();
     public void Eliminar (Integer id);
     public ProductoDto DescontarStock(Integer id, Integer cantidad, String referencia);

 	public ProductoDto IngresarStock(Integer id, Integer cantidad, String referencia);

 	public List<MovimientoStockDto> Movimientos(Integer id);
     


     
}
