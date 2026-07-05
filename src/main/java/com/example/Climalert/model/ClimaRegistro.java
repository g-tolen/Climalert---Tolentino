package com.example.Climalert.model;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class ClimaRegistro {
  private Double temperatura;
  private Integer humedad;
  private LocalDateTime fechaHora;
  private boolean procesado;

  public ClimaRegistro(Double temperatura, Integer humedad) {
    this.temperatura = temperatura;
    this.humedad = humedad;
    this.fechaHora = LocalDateTime.now();
    this.procesado = false;
  }
}