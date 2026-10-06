# 💰 Empréstimo API

> 🚧 **Projeto em desenvolvimento**

API REST desenvolvida com **Java e Spring Boot** para gerenciamento de clientes e solicitações de empréstimos.

Este projeto está sendo desenvolvido como parte do meu processo de aprendizado e aperfeiçoamento em **desenvolvimento backend com Java, Spring Boot, JPA e MySQL**, buscando aplicar na prática conceitos de arquitetura, persistência de dados, validação e construção de APIs REST.

---

## 📌 Sobre o projeto

O **Empréstimo API** simula o backend de um sistema de empréstimos.

A aplicação permite trabalhar com informações de clientes e solicitações de empréstimos, incluindo cadastro, consulta, validação de dados e análise do empréstimo de acordo com as regras implementadas no projeto.

O projeto está sendo desenvolvido de forma incremental. Novas funcionalidades, melhorias e boas práticas serão adicionadas conforme sua evolução.

---

## 🚧 Status do projeto

**Em desenvolvimento**

### O que já foi desenvolvido

- [x] Estrutura inicial da aplicação Spring Boot
- [x] Integração com MySQL
- [x] Entidade de clientes
- [x] Repository para persistência de clientes
- [x] Service para regras relacionadas aos clientes
- [x] DTOs para requisição e resposta
- [x] Cadastro de clientes
- [x] Listagem de clientes
- [x] Busca de cliente por ID
- [x] Exclusão de cliente
- [x] Cadastro de empréstimos
- [x] Listagem de empréstimos
- [x] Associação de empréstimo a cliente
- [x] Validação dos dados recebidos
- [x] Tratamento de respostas HTTP
- [x] Definição de status do empréstimo
- [x] Tratamento de situações de erro

### Próximos passos

- [ ] Aprimorar as regras de análise de empréstimos
- [ ] Melhorar o tratamento global de exceções
- [ ] Implementar atualização de clientes
- [ ] Implementar atualização de empréstimos
- [ ] Implementar exclusão de empréstimos
- [ ] Criar testes unitários
- [ ] Criar testes de integração
- [ ] Implementar documentação com Swagger/OpenAPI
- [ ] Implementar autenticação e autorização
- [ ] Realizar deploy da API
- [ ] Continuar aprimorando a arquitetura e as regras de negócio

---

## 🛠️ Tecnologias utilizadas

| Tecnologia | Utilização |
|---|---|
| ☕ Java | Linguagem principal |
| 🌱 Spring Boot | Desenvolvimento da API |
| 🗄️ Spring Data JPA | Persistência de dados |
| 🔗 Hibernate | ORM |
| 🐬 MySQL | Banco de dados |
| 📦 Maven | Gerenciamento de dependências |
| 🔀 Git | Controle de versão |
| 🐙 GitHub | Hospedagem do código |

---

## 🏗️ Arquitetura

O projeto utiliza uma organização baseada em camadas, buscando manter as responsabilidades separadas e facilitar a manutenção da aplicação.

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
Database
