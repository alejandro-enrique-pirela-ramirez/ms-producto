package com.example.demo.bean.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ProductoDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long idProducto;

	@NotBlank(message = "El código del producto es obligatorio")
	@Pattern(regexp = "^[A-Z0-9-]{3,20}$", message = "El código debe tener entre 3 y 20 caracteres (letras mayúsculas, números o guion)")
	private String codigoProducto;

	@NotBlank(message = "El nombre del producto es obligatorio")
	@Size(max = 100, message = "El nombre no debe exceder 100 caracteres")
	@Pattern(regexp = "^[\\p{L}0-9 .,\\-/()]+$", message = "El nombre contiene caracteres no permitidos")
	private String nombreProducto;

	@Size(max = 255, message = "La descripción no debe exceder 255 caracteres")
	@Pattern(regexp = "^[\\p{L}0-9 .,;:\\-/()%#]*$", message = "La descripción contiene caracteres no permitidos")
	private String descripcion;

	@NotBlank(message = "La unidad de medida es obligatoria")
	@Pattern(regexp = "^(UNIDAD|KG|LITRO|M3|CAJA|BALON|PAQUETE)$", message = "Unidad de medida no válida")
	private String unidadMedida;

	@NotNull(message = "El precio es obligatorio")
	@DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
	@Digits(integer = 10, fraction = 2, message = "El precio admite hasta 10 enteros y 2 decimales")
	private BigDecimal precio;

	@NotNull(message = "El stock es obligatorio")
	@Min(value = 0, message = "El stock no puede ser negativo")
	private Integer stock;

	@NotNull(message = "El estado activo es obligatorio")
	private Boolean activo;

}