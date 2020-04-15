# Build
mvn clean package && docker build -t com.learner/vaadinintro .

# RUN

docker rm -f vaadinintro || true && docker run -d -p 8080:8080 -p 4848:4848 --name vaadinintro com.learner/vaadinintro 