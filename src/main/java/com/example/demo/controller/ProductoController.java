package com.example.demo.controller;

import java.io.Serializable;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.bean.dto.MovimientoStockDto;
import com.example.demo.bean.dto.ProductoDto;
import com.example.demo.useCase.ProductoUseCase;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController implements Serializable {

	private static final long serialVersionUID = 1L;

	private final transient ProductoUseCase productoUseCase;

	@PostMapping
	public ResponseEntity<ProductoDto> registrar(@Valid @RequestBody ProductoDto dto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(productoUseCase.Registrar(dto));
	}

	@PutMapping
	public ResponseEntity<ProductoDto> actualizar(@Valid @RequestBody ProductoDto dto) {
		return ResponseEntity.ok(productoUseCase.Actualizar(dto));
	}

	@GetMapping("/{id}")
	public ResponseEntity<ProductoDto> buscarPorId(@PathVariable Integer id) {
		return ResponseEntity.ok(productoUseCase.BuscarPorId(id));
	}

	@GetMapping("listar")
	public ResponseEntity<List<ProductoDto>> listar() {
		return ResponseEntity.ok(productoUseCase.Listar());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
		productoUseCase.Eliminar(id);
		return ResponseEntity.noContent().build();
	}

	@PutMapping("/{id}/descontar-stock")
	public ResponseEntity<ProductoDto> descontarStock(@PathVariable Integer id, @RequestParam Integer cantidad,
			@RequestParam(required = false) String referencia) {
		return ResponseEntity.ok(productoUseCase.DescontarStock(id, cantidad, referencia));
	}

	@PutMapping("/{id}/ingresar-stock")
	public ResponseEntity<ProductoDto> ingresarStock(@PathVariable Integer id, @RequestParam Integer cantidad,
			@RequestParam(required = false) String referencia) {
		return ResponseEntity.ok(productoUseCase.IngresarStock(id, cantidad, referencia));
	}

	@GetMapping("/{id}/movimientos")
	public ResponseEntity<List<MovimientoStockDto>> movimientos(@PathVariable Integer id) {
		return ResponseEntity.ok(productoUseCase.Movimientos(id));
	}

}