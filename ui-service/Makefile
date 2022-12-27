VERSION=v1
DOCKERUSER=gagankshetty
PROJECT=frontend-service
build:
	docker build -f Dockerfile -t ${PROJECT} --platform=linux/amd64 .

push:
	docker tag ${PROJECT} $(DOCKERUSER)/${PROJECT}:$(VERSION)
	docker push $(DOCKERUSER)/${PROJECT}:$(VERSION)
	docker tag ${PROJECT} $(DOCKERUSER)/${PROJECT}:latest
	docker push $(DOCKERUSER)/${PROJECT}:latest

deploy:
	kubectl apply -f ./kubernetes

deploy-gke:
	kubectl apply -f ./kubernetes
	kubectl apply -f ./kubernetes-gke

cleanup:
	kubectl delete -f ./kubernetes

cleanup-gke:
	kubectl delete -f ./kubernetes
	kubectl delete -f ./kubernetes-gke

logs:
	kubectl logs -l app=${PROJECT} -f