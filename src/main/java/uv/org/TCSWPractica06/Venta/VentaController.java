
package uv.org.TCSWPractica06.Venta;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import uv.org.TCSWPractica06.Clientes.Cliente;
import uv.org.TCSWPractica06.Clientes.ClienteRepository;
import uv.org.TCSWPractica06.DetalleVenta.DetalleVenta;
import uv.org.TCSWPractica06.DetalleVenta.DetalleVentaRepository;
import uv.org.TCSWPractica06.Productos.Producto;
import uv.org.TCSWPractica06.Productos.ProductoRepository;


@RestController
@RequestMapping("/ventas")
public class VentaController {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private DetalleVentaRepository detalleVentaRepository;

    @GetMapping
    public List<Venta> listar() {
        return ventaRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Venta> obtener(@PathVariable Long id) {
        return ventaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Venta> crear(@RequestBody VentaDTO dto) {
        dto.calcularMontoTotal();

        Cliente cliente = clienteRepository.findById(dto.getIdCliente())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        Venta venta = new Venta();
        venta.setCliente(cliente);
        venta.setFecha(dto.getFecha());
        venta.setMontoTotal(dto.getMontoTotal());

        Venta ventaGuardada = ventaRepository.save(venta);

        List<DetalleVenta> detalles = new ArrayList<>();
        for (VentaDTO.DetalleVentaDTO d : dto.getDetalles()) {
            Producto producto = productoRepository.findById(d.getIdProducto())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            DetalleVenta detalle = new DetalleVenta();
            detalle.setVenta(ventaGuardada);
            detalle.setProducto(producto);
            detalle.setCantidad(d.getCantidad());
            detalle.setPrecio(d.getPrecio());
            detalle.setMonto(d.getMonto());

            detalles.add(detalleVentaRepository.save(detalle));
        }

        ventaGuardada.setDetalles(detalles);
        return ResponseEntity.ok(ventaGuardada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (ventaRepository.existsById(id)) {
            ventaRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
