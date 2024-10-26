# Java HTTP Server Example

Este repositorio contiene un ejemplo de servidor HTTP en Java, utilizando `com.sun.net.httpserver.HttpServer`. El proyecto implementa un servidor simple con métodos `GET`, `POST`, `PUT` y `DELETE` para gestionar datos de usuario en memoria, ideal para probar conceptos de manejo de solicitudes HTTP en Java.

## Características

- **POST**: Agrega nuevos usuarios a un almacenamiento en memoria (`ArrayList<Usuario>`).
- **GET**: Recupera datos de un usuario específico, identificado por su índice en la lista.
- **PUT**: Actualiza los datos de un usuario existente, identificado también por su índice.

## Estructura del Proyecto

- **Main**: Configura el servidor en el puerto `8000` y define los endpoints.
- **Handlers**: Carpeta que contiene los controladores para cada tipo de solicitud HTTP (`EchoPostHandler`, `EchoGetHandler`, `EchoUpdateHandler`, `EchoHeaderHandler`), cada uno implementado para manejar su funcionalidad correspondiente.
- **Usuario**: Clase que representa los datos de un usuario, con campos básicos como `name` y `hobby`.
