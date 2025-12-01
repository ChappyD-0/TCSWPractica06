

package uv.org.TCSWPractica06.Venta;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

public class VentaDTO {

    private LocalDate fecha;
    private BigDecimal montoTotal;
    private Long idCliente;
    private List<DetalleVentaDTO> detalles;

    public VentaDTO() {
        this.fecha = LocalDate.now();
        this.montoTotal = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
    }

    public void calcularMontoTotal() {
        if (detalles != null && !detalles.isEmpty()) {
            this.montoTotal = detalles.stream()
                    .map(DetalleVentaDTO::getMonto)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .setScale(2, RoundingMode.HALF_UP);
        } else {
            this.montoTotal = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public BigDecimal getMontoTotal() {
        return montoTotal;
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    public List<DetalleVentaDTO> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleVentaDTO> detalles) {
        this.detalles = detalles;
        calcularMontoTotal();
    }

    public static class DetalleVentaDTO {
        private Long idProducto;
        private int cantidad;
        private BigDecimal precio;

        public Long getIdProducto() {
            return idProducto;
        }

        public void setIdProducto(Long idProducto) {
            this.idProducto = idProducto;
        }

        public int getCantidad() {
            return cantidad;
        }

        public void setCantidad(int cantidad) {
            this.cantidad = cantidad;
        }

        public BigDecimal getPrecio() {
            return precio;
        }

        public void setPrecio(BigDecimal precio) {
            this.precio = precio.setScale(2, RoundingMode.HALF_UP);
        }

        public BigDecimal getMonto() {
            return precio.multiply(BigDecimal.valueOf(cantidad))
                    .setScale(2, RoundingMode.HALF_UP);
        }
    }
}
