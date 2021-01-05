package com.concertevent.repository;

import org.springframework.data.repository.CrudRepository;

import com.concertevent.models.Evento;

public interface EventoRepository extends CrudRepository<Evento, String>{
	
	Evento findByCodigo(long codigo);

}
