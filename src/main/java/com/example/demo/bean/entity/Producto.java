package com.example.demo.bean.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.RequiredArgsConstructor;


@Data
@RequiredArgsConstructor
@Entity
@Table(name = "producto")
public class Producto  implements Serializable{

	private static final long serialVersionUID = 1L;
    
	@Column(name = "id_producto")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idProducto;
	
	
	@Column(name = "codigo_producto")
	private String codigoProducto;
	
	@Column(name = "nombre_producto")
	private String nombreProducto;
	
	@Column(name = "descripcion")
	private String descripcion;
	
	@Column(name = "unidad_medida")
	private String unidadMedida;
	
	@Column(name = "precio")
	private BigDecimal precio;
	
	@Column(name = "stock")
	private Integer stock;
	
	@Column(name = "activo")
	private Boolean activo;
	
	
	
	
}
