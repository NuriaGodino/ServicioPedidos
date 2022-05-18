package service;
import java.util.List;

import model.Pedido;

public interface PedidosService {
	void altaPedido(Pedido pedido);
	List<Pedido> pedidos();
}
