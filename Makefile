maven = ./mvnw

ifeq ($(OS),Windows_NT)
	maven = mvnw.cmd
endif

build: ## Build the application as a docker image
	$(maven) clean package

run: ## Run the application
	java -jar target/liquibaserefactoringcolumnname.jar

up: ## Start the dockerized local env
	docker-compose pull --ignore-pull-failures && docker-compose up -d

down: ## Stop the dockerized local env
	docker-compose down

help: ## This help dialog.
	@echo "Usage: make [target]. Find the available targets below:"
	@echo "$$(grep -hE '^\S+:.*##' $(MAKEFILE_LIST) | sed 's/:.*##\s*/:/' | column -c2 -t -s :)"
