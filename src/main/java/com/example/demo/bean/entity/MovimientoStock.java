package com.example.demo.bean.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "movimiento_stock")
public class MovimientoStock implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_movimiento")
	private Long idMovimiento;

	@Column(name = "id_producto", nullable = false)
	private Long idProducto;

	@Column(name = "tipo", nullable = false, length = 10)
	private String tipo;

	@Column(name = "cantidad", nullable = false)
	private Integer cantidad;

	@Column(name = "stock_anterior", nullable = false)
	private Integer stockAnterior;

	@Column(name = "stock_nuevo", nullable = false)
	private Integer stockNuevo;

	@Column(name = "referencia", length = 100)
	private String referencia;

	@Column(name = "fecha", nullable = false)
	private LocalDateTime fecha;
}