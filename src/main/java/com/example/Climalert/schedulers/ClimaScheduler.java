package com.example.Climalert.schedulers;

import com.example.Climalert.model.ClimaRegistro;
import com.example.Climalert.services.ClimaService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ClimaScheduler {

  private final ClimaService climaService;

  public ClimaScheduler(ClimaService climaService) {
    this.climaService = climaService;
  }

  @Scheduled(fixedRate = 300000)
  public void registrarClima() {
    climaService.consultarProveedorExterno();
  }

  @Scheduled(fixedRate = 60000)
  public void procesarAlertas() {
    ClimaRegistro ultimo = climaService.obtenerUltimoSinProcesar();

    if (ultimo != null) {
      if (ultimo.getTemperatura() > 35.0 && ultimo.getHumedad() > 60) {
        climaService.enviarAlertaPorMail(ultimo);
      }
      ultimo.setProcesado(true);
    }
  }
}