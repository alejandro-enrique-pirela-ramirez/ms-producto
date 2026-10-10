package com.example.demo.bean.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class MovimientoStockDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long idMovimiento;

	private Long idProducto;

	private String tipo;

	private Integer cantidad;

	private Integer stockAnterior;

	private Integer stockNuevo;

	private String referencia;

	private LocalDateTime fecha;
}