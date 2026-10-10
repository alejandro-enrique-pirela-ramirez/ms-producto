package com.example.demo.model;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.bean.dto.MovimientoStockDto;
import com.example.demo.bean.dto.ProductoDto;
import com.example.demo.bean.entity.MovimientoStock;
import com.example.demo.bean.entity.Producto;
import com.example.demo.bean.mapper.MovimientoStockMapper;
import com.example.demo.bean.mapper.ProductoMapper;
import com.example.demo.repository.MovimientoStockRepository;
import com.example.demo.repository.ProductoRepository;
import com.example.demo.useCase.ProductoUseCase;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductoModel implements ProductoUseCase {

	private final ProductoRepository productoRepository;
	private final ProductoMapper productoMapper;
	private final MovimientoStockRepository movimientoRepository;
	private final MovimientoStockMapper movimientoMapper;

	@Override
	@Transactional
	public ProductoDto Registrar(ProductoDto dto) {
		Producto producto = productoMapper.toEntity(dto);
		producto.setIdProducto(null);
		return productoMapper.toDto(productoRepository.save(producto));
	}

	@Override
	@Transactional
	public ProductoDto Actualizar(ProductoDto dto) {
		Producto producto = obtener(dto.getIdProducto().intValue());
		producto.setCodigoProducto(dto.getCodigoProducto());
		producto.setNombreProducto(dto.getNombreProducto());
		producto.setDescripcion(dto.getDescripcion());
		producto.setUnidadMedida(dto.getUnidadMedida());
		producto.setPrecio(dto.getPrecio());
		producto.setStock(dto.getStock());
		producto.setActivo(dto.getActivo());
		return productoMapper.toDto(productoRepository.save(producto));
	}

	@Override
	@Transactional(readOnly = true)
	public ProductoDto BuscarPorId(Integer id) {
		return productoMapper.toDto(obtener(id));
	}

	@Override
	@Transactional(readOnly = true)
	public List<ProductoDto> Listar() {
		return productoRepository.findAll().stream().map(productoMapper::toDto).toList();
	}

	@Override
	@Transactional
	public void Eliminar(Integer id) {
		Producto producto = obtener(id);
		producto.setActivo(false);
		productoRepository.save(producto);
	}

	@Override
	@Transactional
	public ProductoDto DescontarStock(Integer id, Integer cantidad, String referencia) {
		validarCantidad(cantidad);
		Producto producto = obtener(id);
		if (producto.getStock() < cantidad) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Stock insuficiente para " + producto.getCodigoProducto());
		}
		return mover(producto, "SALIDA", cantidad, producto.getStock() - cantidad, referencia);
	}

	@Override
	@Transactional
	public ProductoDto IngresarStock(Integer id, Integer cantidad, String referencia) {
		validarCantidad(cantidad);
		Producto producto = obtener(id);
		return mover(producto, "INGRESO", cantidad, producto.getStock() + cantidad, referencia);
	}

	@Override
	@Transactional(readOnly = true)
	public List<MovimientoStockDto> Movimientos(Integer id) {
		obtener(id);
		return movimientoRepository.findByIdProductoOrderByFechaDesc(id.longValue()).stream().map(movimientoMapper::toDto).toList();
	}

	private ProductoDto mover(Producto producto, String tipo, Integer cantidad, Integer stockNuevo, String referencia) {
		MovimientoStock movimiento = new MovimientoStock();
		movimiento.setIdProducto(producto.getIdProducto());
		movimiento.setTipo(tipo);
		movimiento.setCantidad(cantidad);
		movimiento.setStockAnterior(producto.getStock());
		movimiento.setStockNuevo(stockNuevo);
		movimiento.setReferencia(referencia);
		movimiento.setFecha(LocalDateTime.now());
		movimientoRepository.save(movimiento);
		producto.setStock(stockNuevo);
		return productoMapper.toDto(productoRepository.save(producto));
	}

	private void validarCantidad(Integer cantidad) {
		if (cantidad == null || cantidad <= 0) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La cantidad debe ser mayor a 0");
		}
	}

	private Producto obtener(Integer id) {
		return productoRepository.findById(id.longValue())
				.orElseThrow(() -> new EntityNotFoundException("Producto no encontrado: " + id));
	}

}