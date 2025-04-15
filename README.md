# PizzaDemo API

API RESTful para o sistema de pedidos de pizza PizzaDemo, desenvolvida com Spring Boot, MySQL e JWT.

## Tecnologias Utilizadas

- Java 11
- Spring Boot 2.7.0
- Spring Security + JWT
- Spring Data JPA
- MySQL
- Maven
- Lombok

## Requisitos

- Java 11+
- Maven
- MySQL

## Configuração do Ambiente

1. Clone o repositório:
```bash
git clone <repository-url>
cd pizzademo-api
```

2. Configure o banco de dados MySQL no arquivo `application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/pizzademo?createDatabaseIfNotExist=true
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
```

3. Compile o projeto:
```bash
mvn clean install
```

4. Execute a aplicação:
```bash
mvn spring-boot:run
```

A API estará disponível em `http://localhost:8080`

## Endpoints da API

### Autenticação
- `POST /api/auth/register` - Cadastrar novo usuário
- `POST /api/auth/login` - Login de usuário

### Pizzas
- `GET /api/pizzas` - Listar todas as pizzas
- `GET /api/pizzas/featured` - Listar pizzas em destaque
- `GET /api/pizzas/{id}` - Buscar pizza por ID
- `POST /api/pizzas` - Criar nova pizza (ADMIN)
- `PUT /api/pizzas/{id}` - Atualizar pizza (ADMIN)
- `DELETE /api/pizzas/{id}` - Excluir pizza (ADMIN)

### Extras (Bordas e Bebidas)
- `GET /api/extras` - Listar todos os extras
- `GET /api/extras/bordas` - Listar bordas recheadas
- `GET /api/extras/bebidas` - Listar bebidas
- `GET /api/extras/{id}` - Buscar extra por ID
- `POST /api/extras` - Criar novo extra (ADMIN)
- `PUT /api/extras/{id}` - Atualizar extra (ADMIN)
- `DELETE /api/extras/{id}` - Excluir extra (ADMIN)

### Combos
- `GET /api/combos` - Listar todos os combos
- `GET /api/combos/{id}` - Buscar combo por ID
- `POST /api/combos` - Criar novo combo (ADMIN)
- `PUT /api/combos/{id}` - Atualizar combo (ADMIN)
- `DELETE /api/combos/{id}` - Excluir combo (ADMIN)

### Pedidos
- `GET /api/orders` - Listar todos os pedidos (ADMIN)
- `GET /api/orders/user/{userId}` - Listar pedidos do usuário
- `GET /api/orders/{id}` - Buscar pedido por ID
- `POST /api/orders` - Criar novo pedido
- `PUT /api/orders/{id}/status` - Atualizar status do pedido (ADMIN)
- `DELETE /api/orders/{id}` - Excluir pedido (ADMIN)

## Exemplos de Uso

### Cadastro de Usuário
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "João Silva",
    "email": "joao@email.com",
    "phone": "11999999999",
    "cep": "12345678",
    "password": "senha123"
  }'
```

### Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "joao@email.com",
    "password": "senha123"
  }'
```

### Criar Pedido
```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer seu_token_jwt" \
  -d '{
    "userId": 1,
    "items": [
      {
        "type": "PIZZA",
        "itemId": 1,
        "quantity": 1,
        "unitPrice": 45.00,
        "extras": [1]
      },
      {
        "type": "BEBIDA",
        "itemId": 1,
        "quantity": 1,
        "unitPrice": 15.00
      }
    ],
    "totalPrice": 65.00
  }'
```

## Estrutura do Projeto

```
pizzademo-api/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/pizzademo/
│   │   │       ├── config/
│   │   │       ├── controller/
│   │   │       ├── dto/
│   │   │       ├── model/
│   │   │       ├── repository/
│   │   │       ├── security/
│   │   │       └── PizzaDemoApplication.java
│   │   └── resources/
│   │       └── application.properties
├── pom.xml
└── README.md
```

## Segurança

A API utiliza JWT (JSON Web Token) para autenticação. Para acessar endpoints protegidos, inclua o token JWT no header da requisição:

```
Authorization: Bearer seu_token_jwt
```

## Dados Iniciais

O sistema é inicializado com dados de exemplo incluindo:
- Pizzas: Pepperoni, Queijo, Marguerita
- Bordas: Cheddar, Catupiry, Requeijão, Chocolate
- Bebidas: Coca-Cola, Guaraná, Fanta, Heineken
- Combos: Família, Casal, Individual
