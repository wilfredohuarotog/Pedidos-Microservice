package com.pedidos.infrastructure.csv;

import com.opencsv.bean.CsvToBeanBuilder;
import com.pedidos.domain.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Component
public class CsvProcessor {

    public List<PedidoCsvRow> procesarArchivo(MultipartFile archivo) {
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(archivo.getInputStream(), StandardCharsets.UTF_8)
            );

            List<PedidoCsvRow> filas = new CsvToBeanBuilder<PedidoCsvRow>(reader)
                    .withType(PedidoCsvRow.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build()
                    .parse();

            log.info("CSV procesado exitosamente. Filas: {}", filas.size());
            return filas;

        } catch (Exception e) {
            log.error("Error procesando archivo CSV", e);
            throw new ValidationException("Error al procesar archivo CSV: " + e.getMessage());
        }
    }
}