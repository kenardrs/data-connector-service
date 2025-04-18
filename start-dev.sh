#!/bin/bash


# função para parar os containers
function stop_containers() {
  echo "Parando os containers..."
  docker-compose -f ./src/main/docker/docker-compose.yml down
}

# configura o trap para capturar o SIGINT (Ctrl+C) e SIGTERM
trap stop_containers SIGINT SIGTERM

# Inicia o container do PostgreSQL
docker-compose -f ./src/main/docker/docker-compose.yml up -d postgres

# Aguarda o PostgreSQL estar pronto
echo "Aguardando o PostgreSQL iniciar..."
until docker exec postgres-container pg_isready -U postgres > /dev/null 2>&1; do
  sleep 1
done

# SET JAVA_HOME
export java21="/opt/homebrew/Cellar/openjdk@21/21.0.6/libexec/openjdk.jdk/Contents/Home"

# Inicia a aplicação Quarkus em modo dev
echo "Iniciando a aplicação Quarkus em modo dev..."
JAVA_HOME=$java21 mvn quarkus:dev

# Após o término do processo, para os containers
stop_containers