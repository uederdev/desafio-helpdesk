# Especificação Técnica do Teste Prático - Java Pleno

## 1. Contexto do Sistema
Você deve desenvolver uma API REST para gerenciar chamados de suporte. A regra de negócio principal é que existem tipos diferentes de chamados (**Bug**, **Melhoria** e **Suporte**), cada um com atributos específicos, mas todos compartilham uma base comum. O sistema deve gerenciar Usuários, Chamados e Comentários.

---

## 2. Stack Tecnológica Obrigatória
* **Java:** 17+ (ou 11+)
* **Spring Boot:** 3.x (Web, Data JPA, Validation)
* **Banco de Dados:** PostgreSQL 14 (executando via Docker)
* **Gerenciador de Dependências:** Maven ou Gradle
* **Infraestrutura Local:** Docker Compose (para subir o banco)

---

## 3. Modelagem Orientada a Objetos (Herança)
A hierarquia de classes deve ser implementada com Herança na camada de Model, utilizando a estratégia `SINGLE_TABLE` do JPA (uma única tabela `tickets` com uma coluna discriminadora `ticket_type`).

### 📋 Classe Abstrata `BaseTicket`
Atributos comuns a todos os chamados:
* `id` (Long, gerado automaticamente)
* `title` (String, obrigatório, máx. 255)
* `description` (String, obrigatório, texto longo)
* `status` (Enum: `OPEN`, `IN_PROGRESS`, `RESOLVED`, `CLOSED`)
* `priority` (Enum: `LOW`, `MEDIUM`, `HIGH`, `CRITICAL`)
* `createdAt` (LocalDateTime, imutável)
* `updatedAt` (LocalDateTime, atualizado automaticamente)
* `dueDate` (LocalDate, opcional)
* `assignedTo` (Relacionamento ManyToOne com `User`)
* `createdBy` (Relacionamento ManyToOne com `User`, imutável)

### 🐛 Subclasse `BugTicket` (tipo = "BUG")
* `severity` (Enum: `MINOR`, `MAJOR`, `CRITICAL`, obrigatório)
* `stepsToReproduce` (String, texto longo, obrigatório)
* `affectedVersion` (String, obrigatório)

### 🚀 Subclasse `FeatureTicket` (tipo = "FEATURE")
* `targetVersion` (String, obrigatório)
* `businessValue` (Integer, 1 a 100, obrigatório)
* `estimatedHours` (Double, obrigatório)

### 🛠️ Subclasse `SupportTicket` (tipo = "SUPPORT")
* `customerImpact` (String, obrigatório)
* `resolutionNotes` (String, opcional)
* `category` (String, obrigatório - ex: "BILLING", "TECHNICAL", "ACCESS")

### 👤 Entidade `User`
* `id` (Long)
* `name` (String, obrigatório)
* `email` (String, único, obrigatório)
* `role` (Enum: `ADMIN`, `AGENT`, `REQUESTER`)

### 💬 Entidade `Comment`
* `id` (Long)
* `content` (String, obrigatório)
* `createdAt` (LocalDateTime)
* `ticket` (ManyToOne com `BaseTicket`)
* `author` (ManyToOne com `User`)

---

## 4. Configuração do Banco de Dados (Docker)
Forneça um arquivo `docker-compose.yml` na raiz do projeto:

```yaml
version: '3.8'
services:
  postgres:
    image: postgres:14-alpine
    container_name: helpdesk-db
    environment:
      POSTGRES_DB: helpdesk_db
      POSTGRES_USER: admin
      POSTGRES_PASSWORD: admin123
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data
volumes:
  postgres_data:
```
*A aplicação deve conectar-se via `application.properties` ou `application.yml` com as credenciais acima.*

---

## 5. Especificação Completa da API REST
Todos os endpoints devem retornar JSON e utilizar os códigos de status HTTP adequados (`200`, `201`, `400`, `404`, `422`, etc.).

### 5.1. Endpoints de Usuário (`/api/users`)

| Método | Endpoint | Descrição | Payload Request | Payload Response (Exemplo) |
| :--- | :--- | :--- | :--- | :--- |
| **GET** | `/api/users` | Lista todos os usuários | N/A | `[ { "id": 1, "name": "João Silva", "email": "joao@email.com", "role": "AGENT" } ]` |
| **POST** | `/api/users` | Cria um novo usuário | `{ "name": "Maria", "email": "maria@email.com", "role": "AGENT" }` | `{ "id": 2, "name": "Maria", "email": "maria@email.com", "role": "AGENT" }` |
| **GET** | `/api/users/{id}` | Busca usuário por ID | N/A | `{ "id": 1, "name": "João Silva", "email": "joao@email.com", "role": "AGENT" }` |

### 5.2. Endpoints de Chamados (`/api/tickets`) - Foco da Herança
#### 🔍 **GET `/api/tickets`**
Listagem com filtros opcionais via *query params*:
* `type` (`BUG`, `FEATURE`, `SUPPORT`)
* `status` (`OPEN`, `IN_PROGRESS`, `RESOLVED`, `CLOSED`)
* `assignedTo` (ID do usuário)
* `page` e `size` (paginação)

**Resposta:** Um objeto paginado contendo o campo `type` para identificar qual DTO específico será retornado em cada item.

#### 📥 **POST `/api/tickets`**
Criação de chamado utilizando polimorfismo via payload (o payload deve conter o campo `type` para determinar qual subclasse instanciar).

##### Exemplo de Payload para BUG
```json
{
  "type": "BUG",
  "title": "Falha no cálculo de juros",
  "description": "O sistema está calculando 10% a mais",
  "priority": "CRITICAL",
  "dueDate": "2026-09-01",
  "assignedToId": 5,
  "severity": "CRITICAL",
  "stepsToReproduce": "1. Acesse conta. 2. Clique em simular.",
  "affectedVersion": "v2.3.0"
}
```

##### Exemplo de Payload para FEATURE
```json
{
  "type": "FEATURE",
  "title": "Relatório de métricas",
  "description": "Gerar gráfico de desempenho semanal",
  "priority": "MEDIUM",
  "dueDate": "2026-10-10",
  "assignedToId": 3,
  "targetVersion": "v3.0.0",
  "businessValue": 85,
  "estimatedHours": 12.5
}
```

##### Exemplo de Payload para SUPPORT
```json
{
  "type": "SUPPORT",
  "title": "Usuário sem acesso ao módulo X",
  "description": "Cliente reporta erro 403",
  "priority": "HIGH",
  "assignedToId": 2,
  "customerImpact": "5 usuários bloqueados",
  "category": "TECHNICAL",
  "resolutionNotes": null
}
```

**Response (`201 Created`):** Retorna o objeto criado com o mesmo formato do respectivo tipo, incluindo o `id` gerado, `createdAt` e `createdById` (pode ser simulado como um header fixo `X-User-Id` para fins do teste).

#### 🔍 **GET `/api/tickets/{id}`**
Busca detalhada do chamado.  
**Response (`200 OK`):** Retorna o DTO específico contendo todos os atributos da subclasse mapeada.

#### 🔄 **PUT `/api/tickets/{id}`**
Atualização **COMPLETA** (substituição total da entidade).  
⚠️ **Regra Importante:** O `PUT` deve substituir todos os campos editáveis enviados. O `ticketType` não pode ser alterado (se enviado, deve ser ignorado ou gerar erro). O `createdAt`, `createdBy` e `id` nunca são atualizados.

**Atributos atualizados no PUT por tipo:**
* **Campos da classe base:** `title`, `description`, `status`, `priority`, `dueDate`, `assignedToId`.
* **Campos do subtipo (BUG):** `severity`, `stepsToReproduce`, `affectedVersion`.
* **Campos do subtipo (FEATURE):** `targetVersion`, `businessValue`, `estimatedHours`.
* **Campos do subtipo (SUPPORT):** `customerImpact`, `resolutionNotes`, `category`.

*Nota: O campo `type` NÃO é atualizado. Se o payload tentar alterar o `type` de um chamado existente (ex: de `SUPPORT` para `BUG`), a API deve retornar `HTTP 409 Conflict` ou `HTTP 400 Bad Request` com uma mensagem clara.*

##### Exemplo de requisição PUT para um Bug (ID=10)
```json
{
  "title": "Falha no cálculo de juros - Correção",
  "description": "Sistema estava aplicando taxa errada",
  "status": "IN_PROGRESS",
  "priority": "CRITICAL",
  "dueDate": "2026-08-20",
  "assignedToId": 7,
  "severity": "CRITICAL",
  "stepsToReproduce": "1. Acesse conta com saldo. 2. Simule.",
  "affectedVersion": "v2.3.1"
}
```

**Response (`200 OK`):** Retorna o objeto atualizado.

#### ✏️ **PATCH `/api/tickets/{id}/status`**
Atualização parcial (apenas status, simulando uma regra de negócio real).  
**Payload:** `{ "status": "RESOLVED" }`  
**Response (`200 OK`):** Retorna o objeto atualizado (independente do tipo).

#### ❌ **DELETE `/api/tickets/{id}`**
Remove o chamado do sistema. Retorna `204 No Content`.

---

### 5.3. Endpoints de Comentários (`/api/tickets/{ticketId}/comments`)

| Método | Endpoint | Descrição | Payload Request |
| :--- | :--- | :--- | :--- |
| **GET** | `/api/tickets/{ticketId}/comments` | Lista os comentários vinculados ao chamado | N/A |
| **POST** | `/api/tickets/{ticketId}/comments` | Adiciona um novo comentário ao chamado | `{ "content": "Texto do comentário", "authorId": 1 }` |

---

## 6. Regras de Negócio Obrigatórias (Validações)
1. **Fluxo de Status (Status Flow):** Não permitir mudar o status de `RESOLVED` para `OPEN`, a menos que o usuário executor tenha a role `ADMIN`. Essa validação deve residir estritamente na camada de Serviço.
2. **Campos Obrigatórios:** Validar os dados de entrada usando Bean Validation (`@NotNull`, `@Size`, `@NotBlank`, etc.) nos DTOs de entrada.
3. **Responsável (Assigned To):** Verificar se o `assignedToId` realmente existe na base de dados antes de efetuar a persistência do chamado.
4. **Polimorfismo na Criação:** O payload de criação de chamados obrigatoriamente deve conter a chave `type`. Caso contrário, a API deve retornar `HTTP 400 Bad Request`.

---

## 7. Estrutura de Pastas Esperada (Arquitetura)
```text
src/main/java/com/helpdesk/
├── HelpdeskApplication.java
├── config/               # Configurações do projeto (WebConfig, OpenAPI, etc.)
├── controller/           # Camada de controle REST (UserController, TicketController, CommentController)
├── model/                # Entidades de domínio (User, BaseTicket, BugTicket, etc.) e Enums
├── dto/                  # Objetos de Transferência de Dados (Request/Response) e Mappers
├── repository/           # Interfaces de persistência JPA Repositories
├── service/              # Camada de serviços (Interfaces e Implementações das Regras de Negócio)
└── exception/            # Manipulador global de exceções (@ControllerAdvice)
```

---

## 8. Critérios de Avaliação (Pesos)

| Critério | Peso | Descrição |
| :--- | :---: | :--- |
| **OOP & Herança** | 30% | Correção no mapeamento da estratégia `SINGLE_TABLE`, uso correto de polimorfismo e herança no JPA/Hibernate. |
| **Arquitetura & Clean Code** | 20% | Separação de responsabilidades, organization de pacotes, legibilidade e uso correto de DTOs. |
| **Camada REST & HTTP Status** | 20% | Uso correto dos métodos HTTP, tratamento adequado de respostas e semântica de erros. |
| **Regras de Negócio & Testes** | 20% | Implementação rigorosa das validações de fluxo, validações de payload e integridade referencial. |
| **Configuração & Infraestrutura** | 10% | Funcionamento do ambiente Docker Compose e correta integração com a aplicação. |