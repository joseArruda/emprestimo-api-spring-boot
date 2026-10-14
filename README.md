# 💰 Empréstimo API

API REST para gerenciamento de clientes e solicitações de empréstimos, desenvolvida com Java e Spring Boot.

O projeto tem como objetivo aplicar conceitos de desenvolvimento backend, arquitetura em camadas, persistência de dados, validação de entradas, tratamento de exceções e testes automatizados.

**Status:** Em desenvolvimento, com funcionalidades principais implementadas.

## ✨ Funcionalidades

- Cadastro, listagem, consulta, atualização e exclusão de clientes.
- Cadastro, listagem, consulta, atualização e exclusão de empréstimos.
- Associação entre clientes e seus empréstimos.
- Consulta de empréstimos por cliente.
- Filtro de empréstimos por status.
- Análise de empréstimos com base em uma regra de negócio.
- Validação de dados com Jakarta Bean Validation.
- Tratamento centralizado de exceções e respostas HTTP.
- Documentação interativa com Swagger/OpenAPI.
- Testes automatizados com JUnit, Spring Boot Test e MockMvc.

## 🛠️ Tecnologias

| Tecnologia                     | Utilização                          |
| ------------------------------ | ----------------------------------- |
| Java 26                        | Linguagem de desenvolvimento        |
| Spring Boot 4.1.1              | Framework da aplicação              |
| Spring Web MVC                 | Construção dos endpoints REST       |
| Spring Data JPA                | Persistência e acesso a dados       |
| Hibernate                      | Mapeamento objeto-relacional        |
| MySQL 8                        | Banco de dados                      |
| Jakarta Bean Validation        | Validação de entradas               |
| Maven Wrapper                  | Gerenciamento e execução do projeto |
| Springdoc OpenAPI / Swagger UI | Documentação interativa             |
| JUnit 5 e MockMvc              | Testes automatizados                |
| Git e GitHub                   | Controle de versão e hospedagem     |

## 🏗️ Arquitetura

O projeto utiliza uma arquitetura organizada em camadas, separando as responsabilidades da aplicação.

```text
src/main/java/com/jose/emprestimo_api/
├── config/       # Configurações do OpenAPI
├── controller/   # Endpoints HTTP
├── dto/          # Objetos de requisição e resposta
├── entity/       # Entidades e enumerações
├── exception/    # Tratamento global de exceções
├── repository/   # Acesso e persistência de dados
└── service/      # Regras de negócio
```

O fluxo principal segue a estrutura:

**Controller → Service → Repository → MySQL**

Os DTOs separam os dados recebidos e enviados pela API das entidades persistidas no banco.

## 📌 Principais endpoints

### Clientes

| Método | Endpoint         | Descrição           |
| ------ | ---------------- | ------------------- |
| GET    | `/clientes`      | Lista clientes      |
| GET    | `/clientes/{id}` | Consulta um cliente |
| POST   | `/clientes`      | Cadastra um cliente |
| PUT    | `/clientes/{id}` | Atualiza um cliente |
| DELETE | `/clientes/{id}` | Exclui um cliente   |

### Empréstimos

| Método | Endpoint                       | Descrição                                |
| ------ | ------------------------------ | ---------------------------------------- |
| GET    | `/emprestimos`                 | Lista empréstimos                        |
| GET    | `/emprestimos/{id}`            | Consulta um empréstimo                   |
| POST   | `/emprestimos`                 | Cadastra um empréstimo                   |
| PUT    | `/emprestimos/{id}`            | Atualiza um empréstimo                   |
| DELETE | `/emprestimos/{id}`            | Exclui um empréstimo                     |
| GET    | `/clientes/{id}/emprestimos`   | Lista empréstimos de um cliente          |
| GET    | `/emprestimos/status/{status}` | Filtra empréstimos por status            |
| POST   | `/emprestimos/analisar`        | Analisa um valor de empréstimo           |
| GET    | `/status`                      | Verifica a disponibilidade básica da API |

Os endpoints de cadastro e atualização validam os dados recebidos. A API também trata situações como recursos inexistentes, parâmetros inválidos e conflitos na exclusão de clientes com empréstimos associados.

## 📊 Regra de análise de empréstimos

A regra de análise implementada atualmente utiliza o valor solicitado:

- Valores de até R$ 50.000 são classificados como `APROVADO`.
- Valores superiores a R$ 50.000 são classificados como `PENDENTE`.
- Valores inválidos, como zero ou negativos, são rejeitados pela validação.

Essa regra é didática e demonstra a implementação de lógica de negócio. Ela não representa uma avaliação financeira real nem substitui uma análise de crédito.

Exemplo de requisição para `POST /emprestimos/analisar`:

```json
{
  "valor": 20000
}
```

Resposta esperada:

```json
"APROVADO"
```

## ✅ Validações e tratamento de erros

A API aplica regras de validação, entre elas:

- Nome obrigatório.
- E-mail obrigatório e com formato válido.
- Idade mínima de 18 anos.
- Salário maior que zero.
- Valor e prazo do empréstimo maiores que zero.
- Identificação de cliente obrigatória e positiva no cadastro de empréstimos.

As respostas utilizam códigos HTTP apropriados, como `201 Created`, `200 OK`, `204 No Content`, `400 Bad Request`, `404 Not Found` e `409 Conflict`, conforme a operação e a situação.

## 📖 Documentação interativa

Com a aplicação em execução localmente, acesse:

- **Swagger UI:** http://localhost:8080/swagger-ui/index.html
- **Especificação OpenAPI:** http://localhost:8080/v3/api-docs

A interface permite consultar os endpoints, visualizar os parâmetros e os modelos de requisição e resposta, além de experimentar as operações disponíveis.

## 🚀 Como executar o projeto

### Pré-requisitos

- Java 26, conforme a versão configurada no projeto.
- MySQL 8 ou compatível.
- Git, para clonar o repositório.

### 1. Clone o repositório

```bash
git clone https://github.com/joseArruda/emprestimo-api-spring-boot.git
cd emprestimo-api-spring-boot
```

### 2. Crie o banco de dados

No MySQL, execute:

```sql
CREATE DATABASE emprestimo_db;
```

### 3. Configure a conexão

Abra `src/main/resources/application.properties` e configure os dados de acesso ao seu MySQL:

```properties
spring.application.name=emprestimo_api
spring.datasource.url=jdbc:mysql://localhost:3306/emprestimo_db
spring.datasource.username=root

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

Se o MySQL exigir senha, configure-a localmente usando `spring.datasource.password`. **Não publique senhas ou credenciais reais no repositório.**

A configuração `ddl-auto=update` está direcionada ao ambiente de desenvolvimento. Para produção, recomenda-se utilizar migrações de banco controladas e configurações específicas por ambiente.

### 4. Execute a aplicação

No Windows PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

Em ambientes Linux ou macOS:

```bash
./mvnw spring-boot:run
```

Por padrão, a API estará disponível em `http://localhost:8080`.

## 🧪 Testes automatizados

O projeto possui uma suíte com **32 testes automatizados**, que verificaram com sucesso os principais cenários de inicialização, validação, CRUD, relacionamentos e filtros na última execução local registrada.

Para executar os testes no Windows:

```powershell
.\mvnw.cmd test
```

Em ambientes Linux ou macOS:

```bash
./mvnw test
```

**Resultado da última execução local:** 32 testes executados, zero falhas e zero erros.

Os testes utilizam a configuração de banco definida no projeto. Antes de executá-los, certifique-se de que o MySQL esteja disponível e que a base utilizada não contenha dados importantes. O isolamento dos testes em um banco separado é uma melhoria futura.

## 📍 Próximas melhorias

O projeto continua em desenvolvimento. Entre as possibilidades de evolução estão:

- Separação do banco de testes do banco de desenvolvimento.
- Automação dos testes com GitHub Actions.
- Aprimoramentos nas regras de negócio.
- Revisão de configurações e observabilidade.
- Autenticação e autorização, caso façam parte do escopo futuro.
- Preparação e execução de deploy.

## 👨‍💻 Autor

**José Arruda**

- GitHub: https://github.com/joseArruda
- LinkedIn: https://linkedin.com/in/jose-arruda-vieira/
- Portfólio: https://portifolioja.vercel.app/home

Projeto desenvolvido para praticar e demonstrar conhecimentos em Java, Spring Boot, APIs REST, persistência de dados, validação e testes automatizados.
