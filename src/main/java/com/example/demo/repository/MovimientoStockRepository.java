package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.bean.entity.MovimientoStock;

public interface MovimientoStockRepository extends JpaRepository<MovimientoStock, Long> {

	List<MovimientoStock> findByIdProductoOrderByFechaDesc(Long idProducto);
}