FROM maven:3.9.6-eclipse-temurin-21

ENV PROJECT_HOME /usr/src/todosimpleapp
ENV JAR_NAME todosimpleapp.jar

# Criar diretório de destino
RUN mkdir -p $PROJECT_HOME
WORKDIR $PROJECT_HOME

# Copiar código-fonte da aplicação
COPY . .

# Empacotar a aplicação como um arquivo JAR
RUN mvn clean package -DskipTests

# Mover o arquivo JAR para o diretório correto
RUN mv $PROJECT_HOME/target/$JAR_NAME $PROJECT_HOME/

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "todosimpleapp.jar"]
