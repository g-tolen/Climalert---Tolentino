package com.example.Climalert.services;

import com.example.Climalert.model.ClimaRegistro;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ClimaService {

  private final List<ClimaRegistro> historialLocal = new ArrayList<>();
  private final RestTemplate restTemplate = new RestTemplate();

  @Value("${weather.api.key}")
  private String apiKeyClima;

  @Value("${custom.sendgrid.api-key}")
  private String sendGridApiKey;

  public void consultarProveedorExterno() {
    try {
      String url = "http://api.weatherapi.com/v1/current.json?key=" + apiKeyClima + "&q=Buenos Aires";
      WeatherApiResponse response = restTemplate.getForObject(url, WeatherApiResponse.class);

      if (response != null && response.getCurrent() != null) {
        Double temp = response.getCurrent().getTemp_c();
        Integer hum = response.getCurrent().getHumidity();

        ClimaRegistro nuevo = new ClimaRegistro(temp, hum);
        historialLocal.add(nuevo);

        System.out.println(" Reporte del Clima en Buenos Aires");
        System.out.println("Temperatura: " + temp + "°C");
        System.out.println("Humedad: " + hum + "%");
      }
    } catch (Exception e) {
      System.err.println("Error al consultar la API: " + e.getMessage());
    }
  }

  public ClimaRegistro obtenerUltimoSinProcesar() {
    if (historialLocal.isEmpty()) return null;
    ClimaRegistro ultimo = historialLocal.get(historialLocal.size() - 1);
    return (!ultimo.isProcesado()) ? ultimo : null;
  }

  public void enviarAlertaPorMail(ClimaRegistro clima) {

    Email from = new Email("climalert4@gmail.com", "Sistema Climalert");
    String subject = "Climalert - Alerta climatica critica";

    String cuerpo = " CONDICIONES CLIMATICAS CRITICAS \n\n" +
        "Detalle del clima:\n" +
        " Temperatura: " + clima.getTemperatura() + "°C\n" +
        " Humedad: " + clima.getHumedad() + "%\n" +
        " Horario del registro: " + clima.getFechaHora() + "\n\n" +
        "Climalert";

    Content content = new Content("text/plain", cuerpo);
    Mail mail = new Mail();
    mail.setFrom(from);
    mail.setSubject(subject);
    mail.addContent(content);

    Personalization personalization = new Personalization();
    personalization.addTo(new Email("admin@clima.com"));
    personalization.addTo(new Email("emergencias@clima.com"));
    personalization.addTo(new Email("meteorologia@clima.com"));
    mail.addPersonalization(personalization);

    SendGrid sg = new SendGrid(sendGridApiKey);
    Request request = new Request();
    try {
      request.setMethod(Method.POST);
      request.setEndpoint("mail/send");
      request.setBody(mail.build());
      sg.api(request);
    } catch (IOException ex) {
      System.err.println("Error enviando alerta con SendGrid: " + ex.getMessage());
    }
  }

  public static class WeatherApiResponse {
    private InformacionClimatica current;
    public InformacionClimatica getCurrent() { return current; }
    public void setCurrent(InformacionClimatica current) { this.current = current; }
  }

  public static class InformacionClimatica {
    private Double temp_c;
    private Integer humidity;
    public Double getTemp_c() { return temp_c; }
    public void setTemp_c(Double temp_c) { this.temp_c = temp_c; }
    public Integer getHumidity() { return humidity; }
    public void setHumidity(Integer humidity) { this.humidity = humidity; }
  }
}