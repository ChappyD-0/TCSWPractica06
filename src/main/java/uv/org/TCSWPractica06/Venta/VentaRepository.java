
package uv.org.TCSWPractica06.Venta;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface VentaRepository extends JpaRepository<Venta, Long> {
    
}
