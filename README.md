# 2025-tp-template

Template para TP UTN-DDS Sábados


Configura estas variables de entorno en Render (Environment):
OTEL_SERVICE_NAME=fuentes
OTEL_TRACES_EXPORTER=otlp
OTEL_METRICS_EXPORTER=otlp
OTEL_EXPORTER_OTLP_ENDPOINT=https://otlp.datadoghq.com
OTEL_EXPORTER_OTLP_HEADERS=DD-API-KEY=TU_DATADOG_API_KEY
OTEL_RESOURCE_ATTRIBUTES=deployment.environment=render

Opcionales:
OTEL_LOGS_EXPORTER=none
OTEL_INSTRUMENTATION_COMMON.experimental.controller-telemetry.enabled=true