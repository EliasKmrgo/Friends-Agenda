# ------------------------------------------------------------
# Comandos de conveniencia para Docker Compose
#
# Objetivos:
# - up: levanta los servicios en segundo plano
# - down: detiene y elimina los servicios (conserva volumen de datos)
# - logs: sigue los logs de todos los servicios
# - reseed: reinicia los servicios y recrea datos (elimina el volumen)
# ------------------------------------------------------------

up:
	docker compose --env-file .env up -d

down:
	docker compose down

logs:
	docker compose logs -f

reseed:
	docker compose down -v
	docker compose --env-file .env up -d

