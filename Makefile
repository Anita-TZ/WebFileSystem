.PHONY: docker-up
docker-up:
	docker-compose -f docker-compose.yaml up --build -f




.PHONY: docker-down
docker-down: 
	docker-compose -f docker-compose.yaml down
	docker system prune