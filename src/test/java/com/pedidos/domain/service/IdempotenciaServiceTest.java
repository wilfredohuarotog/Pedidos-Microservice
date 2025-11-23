package com.pedidos.domain.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.MessageDigest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class IdempotenciaServiceTest {

    private IdempotenciaService idempotenciaService;

    @BeforeEach
    void setUp() {
        idempotenciaService = new IdempotenciaService();
    }

    @Test
    void calcularHashArchivo_deberiaRetornarHashCorrecto() throws Exception {
        // Arrange
        MultipartFile mockFile = Mockito.mock(MultipartFile.class);
        byte[] contenido = "archivo de prueba".getBytes();
        when(mockFile.getBytes()).thenReturn(contenido);

        // Calcular hash esperado manualmente
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        String esperado = java.util.HexFormat.of().formatHex(digest.digest(contenido));

        // Act
        String resultado = idempotenciaService.calcularHashArchivo(mockFile);

        // Assert
        assertEquals(esperado, resultado);
        verify(mockFile, times(1)).getBytes();
    }

    @Test
    void calcularHashArchivo_deberiaLanzarRuntimeException_siIOException() throws Exception {
        // Arrange
        MultipartFile mockFile = Mockito.mock(MultipartFile.class);
        when(mockFile.getBytes()).thenThrow(new IOException("Error al leer archivo"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                idempotenciaService.calcularHashArchivo(mockFile)
        );

        assertTrue(exception.getMessage().contains("Error calculando hash"));
        verify(mockFile, times(1)).getBytes();
    }
}
