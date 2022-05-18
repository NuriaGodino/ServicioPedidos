package service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import dao.PedidosDao;
import model.Pedido;

@Service
public class PedidosServiceImpl implements PedidosService {
	
	RestTemplate template;
	String urlBase = "http://servicio-productos";
	
	PedidosDao pedidosDao;
	
	public PedidosServiceImpl(@Autowired RestTemplate template, @Autowired PedidosDao pedidosDao) {
		super();
		this.template = template;
		this.pedidosDao = pedidosDao;
	}

	@Override
	public void altaPedido(Pedido pedido) {
		ResponseEntity<Double> responsePrecio = template.exchange(urlBase + "/Producto/{codigoProducto}", HttpMethod.GET, null, Double.class, pedido.getCodigoProducto());
		double precioUnitario = responsePrecio.getBody();
		ResponseEntity<String> response = template.exchange(urlBase+"/Producto/{codigoProducto}/{unidades}", 
		HttpMethod.PUT, null, 
		String.class, 
		pedido.getCodigoProducto(), 
		pedido.getUnidades());
		String cuerpo = response.getBody();
		if(cuerpo.equals("true") || cuerpo != null) {
			pedido.setTotal(precioUnitario * pedido.getUnidades());
			pedido.setFechaPedido(new Date());
			pedidosDao.save(pedido);
		}
	}

	@Override
	public List<Pedido> pedidos() {
		return pedidosDao.findAll();
	}
	
	private void actualizar(int codigoProducto, int unidades) {
		template.put(urlBase + "Producto/{codigoProducto}/{unidades}", null, codigoProducto, unidades);
	}

}
