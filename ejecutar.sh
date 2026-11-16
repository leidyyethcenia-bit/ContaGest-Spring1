#!/bin/bash
# Ejecuta ContaGest Spring (Linux, Mac y GitHub Codespaces).
# Requiere JDK 17+ y Maven. La primera vez descarga las dependencias (1 a 3 minutos).
# Luego abra http://localhost:8080
cd "$(dirname "$0")" || exit 1

if ! command -v mvn > /dev/null 2>&1; then
    echo "Maven no esta instalado. Intentando instalarlo..."
    sudo apt-get update -qq && sudo apt-get install -y maven || {
        echo "No fue posible instalar Maven automaticamente. Instalelo desde https://maven.apache.org"
        exit 1
    }
fi

mvn spring-boot:run
