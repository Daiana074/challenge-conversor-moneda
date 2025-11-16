# 🪙 Conversor de Moneda — Java + API de tasas de cambio

Este proyecto es un **Conversor de Moneda en Java** que utiliza una API de tasas de cambio en tiempo real para realizar conversiones entre diversas monedas.
El usuario puede elegir **qué moneda tiene** (origen) y **a qué moneda desea convertir** (destino), además de consultar un historial con todas las operaciones realizadas.

---

## 🚀 Características principales

* Conversión entre varias monedas:

  * USD, ARS, BOB, BRL, EUR, CLP y COP.
* Selección de **moneda de origen** y **moneda destino**.
* Conversión basada en tasas reales obtenidas desde la API:
  **[https://api.exchangerate-api.com/v4/latest/USD](https://api.exchangerate-api.com/v4/latest/USD)**
* Cálculo correcto incluso cuando ninguna de las dos monedas es USD,
  aplicando la fórmula:
  `cantidadDestino = cantidadOrigen * (tasaDestino / tasaOrigen)`
* Historial de conversiones con:

  * Fecha y hora exacta
  * Moneda origen → destino
  * Resultado final
* Manejo de entradas inválidas (errores de usuario).
* Interfaz por consola clara y ordenada.

---

## 🛠️ Tecnologías utilizadas

* **Java 17+**
* **HttpClient** (para realizar solicitudes HTTP)
* **Gson** (para procesar JSON)
* **Java Time API** (LocalDateTime, DateTimeFormatter)
* **Scanner** (interacción con el usuario)

---

## 📌 Cómo funciona

1. El programa obtiene las tasas de cambio desde la API.
2. Muestra un menú principal:

   * Realizar conversión
   * Ver historial
   * Salir
3. Cuando se elige convertir:

   * Se selecciona moneda de origen
   * Se selecciona moneda destino
   * Se ingresa la cantidad a convertir
4. El sistema realiza el cálculo usando las tasas recibidas de la API.
5. El resultado se muestra en pantalla y se guarda en el historial.
6. El usuario puede consultar todas sus conversiones cuando quiera.

---

## ▶️ Ejemplo de uso

```
=== Bienvenido/a al Conversor de Moneda ===
1. Realizar conversión
2. Mostrar historial
3. Salir

Elija la moneda:
1. USD
2. ARS
3. BOB
...
Ingrese la cantidad en ARS a convertir:
```

Salida:

```
1000.0000 ARS = 1.2000 USD
```

---

## 📄 Notas

* La API usada es gratuita y puede tener límites de consultas.
* Las tasas cambian con el tiempo; cada ejecución obtiene valores actualizados.
* El historial se mantiene solo durante la ejecución (no persiste en archivo).

---

