#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
ENV_FILE="${ENV_FILE:-$ROOT_DIR/.env}"

usage() {
  cat <<'EOF'
Usage: ./scripts/dev.sh <command>

Commands:
  check-env   Validate .env exists and required variables are set
  infra-up    Start MySQL + Redis (docker-compose.dev.yml)
  infra-down  Stop MySQL + Redis
  backend     Run Spring Boot backend
  frontend    Run Vue frontend
  all         check-env + infra-up + print startup order
EOF
}

ensure_env_file() {
  if [[ ! -f "$ENV_FILE" ]]; then
    echo "[INFO] $ENV_FILE not found, creating from .env.example"
    cp "$ROOT_DIR/.env.example" "$ENV_FILE"
    echo "[WARN] Please edit $ENV_FILE before running backend."
  fi
}

load_env() {
  ensure_env_file
  set -a
  # shellcheck disable=SC1090
  source "$ENV_FILE"
  set +a
}

check_env() {
  load_env
  local required=(DB_URL DB_USERNAME DB_PASSWORD SERVER_PORT REDIS_HOST REDIS_PORT)
  for key in "${required[@]}"; do
    if [[ -z "${!key:-}" ]]; then
      echo "[ERROR] Missing required env: $key"
      exit 1
    fi
  done
  if [[ "${DB_PASSWORD}" == "change_me_db_password" ]]; then
    echo "[ERROR] DB_PASSWORD is still placeholder value in $ENV_FILE"
    exit 1
  fi
  echo "[OK] Environment looks good."
}

infra_up() {
  load_env
  docker compose -f "$ROOT_DIR/docker-compose.dev.yml" --env-file "$ENV_FILE" up -d
}

infra_down() {
  load_env
  docker compose -f "$ROOT_DIR/docker-compose.dev.yml" --env-file "$ENV_FILE" down
}

backend() {
  check_env
  cd "$ROOT_DIR/Dormitory_business"
  mvn spring-boot:run
}

frontend() {
  cd "$ROOT_DIR/vue"
  npm ci
  npm run serve
}

all() {
  check_env
  infra_up
  cat <<'EOF'

Startup order:
  1) ./scripts/dev.sh backend
  2) ./scripts/dev.sh frontend
EOF
}

case "${1:-}" in
  check-env) check_env ;;
  infra-up) infra_up ;;
  infra-down) infra_down ;;
  backend) backend ;;
  frontend) frontend ;;
  all) all ;;
  *) usage; exit 1 ;;
esac
