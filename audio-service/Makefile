VERSION=v1
DOCKERUSER=mspawanranjith
PROJECT=audio-service
build:
	docker build -f Dockerfile -t ${PROJECT} .

push:
	docker tag ${PROJECT} $(DOCKERUSER)/${PROJECT}:$(VERSION)
	docker push $(DOCKERUSER)/${PROJECT}:$(VERSION)
	docker tag ${PROJECT} $(DOCKERUSER)/${PROJECT}:latest
	docker push $(DOCKERUSER)/${PROJECT}:latest

deploy:
	kubectl apply -f ./kubernetes

cleanup:
	kubectl delete -f ./kubernetes

logs:
	kubectl logs -l app=audio-service -f