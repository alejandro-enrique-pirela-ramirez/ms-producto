# ms-producto con Config Server

El servicio utiliza `spring-cloud-starter-config` para cargar la configuracion remota antes de inicializar JPA. La identidad `ms-producto-facturador-electronico` coincide con el archivo de GitHub, el API Gateway y el cliente Feign de factura.

## Configuracion local y remota

En `src/main/resources/application.properties` se mantienen el nombre, el puerto `8098`, Eureka y las propiedades necesarias para conectarse al Config Server:

```properties
spring.config.import=configserver:${CONFIG_SERVER_URL:http://localhost:8888}
spring.cloud.config.label=${CONFIG_GIT_LABEL:main}
```

La importacion es obligatoria. Si el servidor o el archivo remoto no estan disponibles, producto falla al arrancar. No se necesita `bootstrap.properties` ni una anotacion adicional en la clase principal.

En la raiz del repositorio de configuracion, rama `main`, debe existir `ms-producto-facturador-electronico.properties` con las propiedades de MySQL y JPA. La configuracion actual apunta a `jdbc:mysql://localhost:3310/productofacturador`, que coincide con el servicio `productofacturador` del Docker Compose de la carpeta superior.

**No incluir `spring.config.import` en el archivo remoto.** Al procesarlo, el Config Server puede intentar consultarse a si mismo y bloquear tambien las peticiones de otros microservicios. `spring.cloud.config.label` se mantiene en el cliente, junto con la importacion. La copia local del repositorio de configuracion ya incluye esta correccion.

## Arranque en PowerShell

Usar JDK 25 y `JAVA_HOME` apuntando a ese JDK. Recargar las dependencias Maven si se trabaja desde un IDE.

1. Verificar que GitHub contenga el archivo remoto sin `spring.config.import` ni `spring.cloud.config.label`. Esta correccion se publico en el commit `62ab289`. Para futuros cambios, la copia local requiere commit y push para que el servidor que utiliza GitHub los lea.
2. Iniciar MySQL y Eureka. Desde la carpeta superior, MySQL puede iniciarse con `docker compose up -d productofacturador`.
3. Iniciar Config Server desde su carpeta con `.\mvnw.cmd spring-boot:run`. Si quedo bloqueado por la importacion remota incorrecta, reiniciarlo despues de corregir GitHub.
4. Comprobar el archivo servido sin imprimir credenciales:

   ```powershell
   $config = Invoke-RestMethod http://localhost:8888/ms-producto-facturador-electronico/default/main
   $config.propertySources | Select-Object name
   ```

5. Desde esta carpeta, iniciar producto:

   ```powershell
   .\mvnw.cmd spring-boot:run
   ```

El registro debe incluir `Located environment: name=ms-producto-facturador-electronico` y `Started MsProductoFApplication`. Se puede comprobar una consulta de solo lectura en `http://localhost:8098/api/productos/listar`.

## Variables opcionales

| Variable | Valor por defecto |
| --- | --- |
| `CONFIG_SERVER_URL` | `http://localhost:8888` |
| `CONFIG_GIT_LABEL` | `main` |
| `EUREKA_SERVER_URL` | `http://localhost:8761/eureka/` |

Establecerlas en la misma terminal desde la que se inicia el servicio:

```powershell
$env:CONFIG_SERVER_URL = 'http://localhost:8888'
$env:CONFIG_GIT_LABEL = 'main'
.\mvnw.cmd spring-boot:run
```

Los cambios en GitHub se cargan al reiniciar producto; no se ha configurado actualizacion automatica mediante Spring Cloud Bus.

## Pruebas

```powershell
.\mvnw.cmd clean test
```

Las dos pruebas comprueban el arranque completo con propiedades remotas y el rechazo de configuracion inexistente. Usan un servidor HTTP local y H2, sin requerir GitHub, MySQL ni Eureka. H2 tiene alcance `test`; el servicio sigue usando MySQL al ejecutarse normalmente.

Referencia: [Spring Cloud Config Client](https://docs.spring.io/spring-cloud-config/reference/client.html).
