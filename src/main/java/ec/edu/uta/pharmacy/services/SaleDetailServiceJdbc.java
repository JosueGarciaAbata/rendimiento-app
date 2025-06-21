package ec.edu.uta.pharmacy.services;

import ec.edu.uta.pharmacy.entities.SaleDetail;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SaleDetailServiceJdbc {

    private final JdbcTemplate jdbcTemplate;

    public SaleDetailServiceJdbc(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcTemplate.setFetchSize(1000);
    }
    public List<SaleDetail> readFirstNDetailsJdbc(int count) {
        String sql = """
            SELECT sd.id,
                   sd.sale_id,
                   sd.medication_id,
                   sd.quantity,
                   sd.unit_price
              FROM sale_details sd
             ORDER BY sd.id
             LIMIT ?
            """;

        return jdbcTemplate.query(
                sql,
                new Object[]{count},
                (rs, rowNum) -> {
                    SaleDetail d = new SaleDetail();
                    d.setId(rs.getLong("id"));
                    d.setQuantity(rs.getInt("quantity"));
                    d.setUnitPrice(rs.getBigDecimal("unit_price"));
                    return d;
                }
        );
    }
}

