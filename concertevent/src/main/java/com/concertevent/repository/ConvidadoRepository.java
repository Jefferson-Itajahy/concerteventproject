package com.concertevent.repository;

import org.springframework.data.repository.CrudRepository;

import com.concertevent.models.Convidado;
import com.concertevent.models.Evento;

public interface ConvidadoRepository extends CrudRepository<Convidado, String>{
	
	Iterable<Convidado> findByEvento(Evento evento);
	Convidado findByRg(String rg);
}
