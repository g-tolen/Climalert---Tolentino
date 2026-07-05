# Climalert

TP para la DDSI UTN FRBA

## Tecnologias utilizadas
- Java: Versión 21
- SpringBoot: Versión 3.5.16
- Para enviar el mail: SendGrid
- API Externa: WeatherAPI

## Configuracion y variables de entorno
Es obligatorio dar de alta las siguientes variables de entorno en tu IDE:

1. `WEATHER_API_KEY`: Token de autenticación provisto por la plataforma de WeatherAPI.
2. `SENDGRID_API_KEY`: API Key provista por SendGrid (formato `SG.xxxx`).

### Estructura de propiedades (`application.properties`):
```properties
spring.application.name=Climalert
server.port=8080

weather.api.key=${WEATHER_API_KEY}
custom.sendgrid.api-key=${SENDGRID_API_KEY}