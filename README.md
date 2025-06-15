# GearHubMobile

Aplicación móvil para la gestión y exploración de comunidades y threads, desarrollada en Kotlin usando Jetpack Compose y patron MVVM con ayuda de hilt.

## Características

- Listado de comunidades con scroll horizontal.
- Listado de threads con scroll vertical.
- Integración con API REST usando Retrofit y Gson.
- Arquitectura MVVM.
- UI moderna con Material 3 y Jetpack Compose.

## Estructura del Proyecto

- `data/models`: Modelos de datos (`CommunityDto`, `Thread`, etc.).
- `data/api`: Interfaces de Retrofit para la comunicación con el backend.
- `data/repository`: Repositorios para la gestión de datos.
- `ui/screens`: Pantallas principales de la app (por ejemplo, `HomeScreen`).
- `viewmodel`: Lógica de presentación y gestión de estado.

## Instalación

1. Clona el repositorio:
   ```sh
   git clone https://github.com/rasitoo/GearHubMobile.git
   ```
2. Abre el proyecto en Android Studio.
3. Sincroniza los gradle scripts.
4. Ejecuta la app en un emulador o dispositivo físico.

## Configuración

- Asegúrate de tener configurado el endpoint de la API en los archivos de Retrofit.
- Si es necesario, ajusta los permisos en el `AndroidManifest.xml`.

## Dependencias principales

- Kotlin
- Jetpack Compose
- Material 3
- Retrofit
- Gson
- Coroutines

## Contribución

1. Haz un fork del repositorio.
2. Crea una rama para tu feature o fix.
3. Haz tus cambios y realiza un commit.
4. Envía un pull request.

## Licencia

Este proyecto está bajo la licencia MIT.
