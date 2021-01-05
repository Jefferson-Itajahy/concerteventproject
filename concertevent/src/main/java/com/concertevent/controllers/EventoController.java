package com.concertevent.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.concertevent.models.Convidado;
import com.concertevent.models.Evento;
import com.concertevent.repository.ConvidadoRepository;
import com.concertevent.repository.EventoRepository;

@Controller
public class EventoController {
	
	@Autowired
	private EventoRepository er;
	
	@Autowired
	private ConvidadoRepository cr;
	
	@RequestMapping(value="/cadastrarEvento", method=RequestMethod.GET)
	public String form() {
		return "evento/formEvento";
	}

	@RequestMapping(value="/cadastrarEvento", method=RequestMethod.POST)
	public String form(Evento evento, RedirectAttributes attributes) {
		if(evento.getNome().equals("") || evento.getLocal().equals("") || evento.getData().equals("") || evento.getHorario().equals("")) {
			attributes.addFlashAttribute("mensagem", "Verifique os campos!");
			return "redirect:/cadastrarEvento";
		} else {
			
		er.save(evento);
		attributes.addFlashAttribute("mensagem", "Evento adicionado com sucesso!");
		return "redirect:/cadastrarEvento";
		}
	}
	
	@RequestMapping("/eventos")
	public ModelAndView listaEventos() {
		ModelAndView mv = new ModelAndView("index");
		Iterable<Evento> eventos = er.findAll();
		mv.addObject("eventos", eventos);
		return mv;
	}

	@RequestMapping(value="/{codigo}", method=RequestMethod.GET)
	public ModelAndView detalhesDoEvento(@PathVariable("codigo") long codigo){
		Evento evento = er.findByCodigo(codigo);
		ModelAndView mv = new ModelAndView("evento/detalhesDoEvento");
		mv.addObject("eventos", evento);
		
		Iterable<Convidado> convidados = cr.findByEvento(evento);
		mv.addObject("convidados", convidados);
		return mv;
	}
	
	@RequestMapping("/deletarEvento")
	public String deletarEvento(long codigo) {
		Evento evento = er.findByCodigo(codigo);
		er.delete(evento);
		return "redirect:/eventos";
	}
	
	@RequestMapping(value="/{codigo}",method=RequestMethod.POST)
	public String detalhesEvento(@PathVariable("codigo") long codigo, @Valid Convidado convidado, RedirectAttributes attributes) {
		if(convidado.getNomeConvidado().equals("") || convidado.getRg().equals("")) {
			attributes.addFlashAttribute("mensagem", "Verifique os Campos!");
			return "redirect:/{codigo}";
		} else {
			Evento evento = er.findByCodigo(codigo);
			convidado.setEvento(evento);
			cr.save(convidado);
			attributes.addFlashAttribute("mensagem", "Convidado adicionado com sucesso!");
			return "redirect:/{codigo}";
		}
	}
	
	@RequestMapping("/deletarConvidado")
	public String deletarConvidado(String rg) {
		Convidado convidado = cr.findByRg(rg);
		cr.delete(convidado);
		
		Evento evento = convidado.getEvento();
		long codigoLong = evento.getCodigo();
		String codigo = "" + codigoLong;
		return "redirect:/" + codigo;
		
	}
}
