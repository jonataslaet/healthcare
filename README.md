# 🏥 Healthcare API

Sistema para gerenciamento de pacientes, desenvolvido em **Java 21 + Spring Boot**, com foco em boas práticas de arquitetura, testes automatizados, tratamento consistente de erros e organização de código. 

Este projeto foi desenvolvido como parte de um **teste técnico**, priorizando clareza, qualidade e manutenibilidade.

Atualmente, o repositório segue uma abordagem de **monorepo**, contendo apenas a API backend no diretório `backend-java`. Essa decisão foi tomada visando à **evolução futura do projeto**, onde uma aplicação frontend (por exemplo, em Angular) será adicionada no diretório `frontend-angular`, integrando-se diretamente com esta API.

No momento, o escopo da aplicação é um **CRUD de pacientes**, responsável exclusivamente pelo gerenciamento básico de dados de pacientes. No entanto, a estrutura do projeto foi pensada para ser **extensível e escalável**, permitindo que, no futuro, a mesma base possa ser utilizada para gerenciar os pacientes seguindo regras de negócio mais complexas, além de repetir esse cenário com outras entidades do domínio de saúde, como hospitais, unidades de atendimento ou recursos correlatos, sem a necessidade de grandes refatorações arquiteturais.

## 📑 Sumário

- [Funcionalidades](#-funcionalidades)
- [Tecnologias Utilizadas](#-tecnologias-utilizadas)
- [Arquiteturas Utilizadas](#-arquiteturas-utilizadas)
- [Tratamento de Erros](#-tratamento-de-erros)
- [Testes](#-testes)
- [Como Executar o Projeto](#-como-executar-o-projeto)
- [Utilizando a API com Postman](#-utilizando-a-api-com-postman)
- [Perguntas e Respostas](#-perguntas-e-respostas)
---

## ✨ Funcionalidades

- CRUD de pacientes
- Listagem paginada e filtrada
- Validação de duplicidade de e-mail
- Tratamento global de erros padronizado
- Testes unitários e de controller
- Suporte a enum com validação customizada

---

## 🛠️ Tecnologias Utilizadas

- **Java 21**
- **Spring Boot 4.0.2**
- Spring Web
- Spring Data 
- PostgreSQL
- H2 Database (testes)
- JUnit 5
- Mockito
- AssertJ
- Maven

---

## 🧱 Arquiteturas Utilizadas

O projeto adota, de forma complementar, as seguintes arquiteturas, cada uma atuando em um nível diferente do sistema:

### Arquitetura Cliente–Servidor

A aplicação segue o modelo **cliente–servidor**, no qual o backend expõe uma API REST que pode ser consumida por diferentes clientes, como aplicações web, mobile ou ferramentas de integração.  
No cenário atual, a API é consumida diretamente (ex.: Postman), mas foi pensada para integração futura com um frontend dedicado.

---

### Arquitetura Monolítica

O backend é desenvolvido como um **monólito**, ou seja, uma única aplicação Spring Boot, implantada e executada como um único artefato.  
Apesar de estar organizado em um repositório no formato **monorepo**, com a possibilidade de coexistência de frontend e backend em diretórios distintos, o backend permanece monolítico do ponto de vista de deploy e execução.

Essa abordagem facilita o desenvolvimento inicial, manutenção e testes, ao mesmo tempo em que não impede uma evolução futura para outros estilos arquiteturais, se necessário.

---

### Arquitetura em Camadas

Internamente, o backend segue uma **arquitetura em camadas**, promovendo separação de responsabilidades, organização do código e facilidade de testes.

Estrutura lógica das camadas:

```text
Controller
   ↓
Service
   ↓
Repository
```

### Principais pacotes:

- `controllers` – endpoints REST
- `services` – regras de negócio
- `repositories` – acesso a dados
- `entities` – modelo de domínio (JPA)
- `mappers` – conversão Entity ↔ DTO
- `exceptions` – exceções de domínio
- `handlers` – tratamento global de erros
- `test` – suporte a testes unitários das camadas `services` e `controllers`

---

## 🚦 Tratamento de Erros

Todos os erros da API seguem um **contrato padrão**.

### Exemplo de erro de recurso não encontrado
```json
{
  "timestamp": "2026-02-06T15:20:30Z",
  "status": 404,
  "error": "Recurso não encontrado",
  "message": "Paciente não encontrado",
  "path": "/patients/99"
}
```

### Status HTTP utilizados

```text
400 → Requisição inválida
404 → Recurso não encontrado
409 → Conflito (duplicidade)
422 → Conteúdo inválido (ex: enum inválido)
```
---
## 🧪 Testes

O projeto possui uma estratégia de testes focada em **qualidade, isolamento e previsibilidade**, cobrindo diferentes camadas da aplicação de forma adequada.

### Testes Unitários

Os testes unitários validam regras de negócio e comportamentos isolados, sem dependência de infraestrutura externa.

```text
- Services (PatientServiceTests)
  - Criação de pacientes
  - Atualização de pacientes
  - Validação de duplicidade de e-mail
  - Tratamento de exceções de domínio

- Mappers
  - Conversão entre Entity e DTO

- Enums
  - Testes parametrizados
  - Validação de todos os valores possíveis
  - Tratamento de valores inválidos
  
- PatientControllerTests
  - Testes de endpoints REST
  - Testes de sucesso (200 / 201)
  - Testes de erro (404 / 409 / 422)
  - Validação do contrato de resposta HTTP
  
- Nenhum teste depende de banco de dados real
- Não são utilizados containers ou infraestrutura externa
- Os testes são rápidos, determinísticos e adequados para CI
```
---
## 🔄 CI/CD

O projeto utiliza **GitHub Actions** para automação de **build e execução de testes**, garantindo qualidade contínua do código a cada alteração.

### Estratégia adotada

```text
- Pipeline separado por responsabilidade
- Execução automática em push e pull request
- Foco em validação rápida e determinística
- Preparado para ambiente de monorepo
```
---
## ▶️ Como Executar o Projeto
Os passos a seguir deve ser considerados na ordem em que estão dispostos.
### Pré-requisitos

Antes de executar a aplicação, certifique-se de ter instalado:

- `Java 21 ou superior`
- `Maven 3.9 ou superior`
- `Docker`

### Estrutura de diretórios do projeto

```text
healthcare/
├── backend-java/
│   ├── src/
│   ├── pom.xml
│   └── mvnw
```

### 📄 Criar arquivo de variáveis de ambiente (.env)

Crie um arquivo chamado **`.env`** no diretório principal do projeto com o seguinte conteúdo:

```env
POSTGRES_USER=username-postgres
POSTGRES_PASSWORD=password-postgres
POSTGRES_DB=database-postgres
```

### 🐳 Executar Docker Container

O projeto disponibiliza um container Docker para execução de um **SGBD PostgreSQL**, facilitando a execução local e preparando o ambiente para cenários mais próximos de produção. Os comandos a seguir são executados a partir do diretório principal do projeto.

O container utiliza variáveis de ambiente para configuração do banco de dados.

**Para ligar o container**, execute:

```bash
docker-compose -p healthcare -f docker-compose.yml up -d
```
O comando acima faz o seguinte:
- Cria a rede healthcare-network
- Sobe o container healthcare-postgres-server
- Expõe o PostgreSQL na porta 5432

**Para desligar o container**, execute:

```bash
docker-compose -p healthcare -f docker-compose.yml down -v
```
O comando acima faz o seguinte:
- Faz parar os containers em execução do projeto healthcare
- Remove os containers criados pelo docker-compose
- Remove a rede healthcare-network
- Remove os volumes associados (-v), apagando os dados do banco

### ▶️ Executar a Aplicação (Backend)
Há mais uma forma de executar a aplicação. Neste caso do backend, pelo menos até o momento, será exposta apenas a que é pelo ambiente de desenvolvimento integrado (IDE).

#### ▶️ Pela IDE
Também é possível executar a aplicação diretamente pela **IDE de sua preferência** (IntelliJ IDEA, Eclipse, VS Code, etc.), configurando as variáveis de ambiente na configuração de execução.

##### Passo a passo

1. Abra o diretório **`backend-java`** com a IDE escolhida.
2. Localize a classe principal da aplicação (`HealthcareApplication`).
3. Crie ou edite a **Run/Debug Configuration** da aplicação Spring Boot.
4. No campo **Environment Variables (Variáveis de Ambiente)**, informe as seguintes variáveis:

```text
APP_PROFILE=development
DATABASE_HOST=localhost
DATABASE_PORT=5432
DATABASE_NAME=database-postgres
DATABASE_USER=username-postgres
DATABASE_PASSWORD=password-postgres
```
5. Salve a configuração e execute a aplicação

Resultado esperado:
- O profile development será ativado
- A aplicação irá se conectar ao PostgreSQL (via Docker)
- O Spring Boot será iniciado normalmente
- A API ficará disponível em http://localhost:8080
---
## 📮 Utilizando a API com Postman

A API pode ser utilizada por meio do **Postman**.  
O projeto disponibiliza **dois arquivos** para importação:
```text
1. Healthcare.postman_collection.json
2. healthcare-development.postman_environment.json
```

Ambos estão no diretório healthcare.

##### Importar os arquivos no Postman

1. Abra o Postman.
2. Clique em Import.
3. Importe os dois arquivos supracitados.

##### Configurar environment importado
No arquivo de Environment, é necessário configurar as variáveis conforme o ambiente local onde a aplicação está rodando:
```text
protocol = http
hostname = localhost
port     = :8080
```
##### Executando as requisições
1. Certifique-se de que a aplicação Spring Boot está em execução.
2. Selecione a Collection importada.
3. Execute as requisições disponíveis (POST, GET, PUT, DELETE).

- O uso de Environment evita hardcoding de URLs
- Facilita a troca entre ambientes (local, homologação, produção)
- Mantém a Collection reutilizável
---
## ❓ Perguntas e respostas

A seguir estão as respostas às perguntas propostas no teste técnico, formuladas de maneira **sucinta, objetiva e alinhada ao perfil de um desenvolvedor backend Java pleno**.



### 01. Como você escolheria a stack tecnológica para esse projeto?

Eu escolheria a stack considerando **maturidade**, **produtividade**, **ecossistema** e **aderência ao problema**. No caso deste projeto, escolhi Java com Spring Boot, por ser minha stack de maior expertise técnica, e também por oferecer robustez, ampla adoção no mercado, excelente suporte a testes e facilidade de integração com bancos relacionais, sendo adequada para APIs REST corporativas.
```text
- Evolução gradual da aplicação
- Manutenção de longo prazo
- Padronização de código
- Integração com ferramentas de CI/CD
```

### 02. Quais critérios usa para definir arquitetura de backend, frontend e mobile?

Existem critérios diversos que poderiam ser adotados para essa finalidade. Eu cito alguns que eu usaria a seguir:

```text
- Complexidade do domínio
- Escalabilidade esperada
- Separação de responsabilidades
- Facilidade de manutenção
- Perfil da equipe
```
No backend, priorizo arquiteturas simples e bem estruturadas. No frontend e mobile, foco em desacoplamento via API e reutilização de contratos.

### 03. Como garantir qualidade de código na equipe?

A qualidade de código é garantida por meio de práticas contínuas adotadas pelo time ao longo do desenvolvimento, dentre as quais posso citar: 

```text
- Testes automatizados em diferentes camadas
- Revisões de código (code review)
- Adoção de padrões e convenções
- Integração contínua com execução de testes
- Código simples, legível e bem organizado
```

Essas práticas reduzem erros, facilitam a manutenção e promovem compartilhamento de conhecimento entre os membros da equipe.

### 04. Como você define priorização de tarefas em uma sprint?

Eu definiria a priorização considerando uma combinação de fatores técnicos e de negócio, dentre os quais posso citar alguns: 

```text
- Valor de negócio entregue
- Dependências entre tarefas
- Riscos técnicos
- Esforço e complexidade
```
As decisões são tomadas de forma colaborativa, normalmente durante o refinamento do backlog, buscando maximizar valor entregue e minimizar riscos ao longo do sprint. 

### 05. Qual sua estratégia para gerenciar integrações com serviços externos?

Minha estratégia certamente seria focada em **isolamento**, **resiliência** e **facilidade de manutenção**.

```text
- Isolar integrações em camadas específicas
- Utilizar contratos bem definidos
- Tratar falhas, timeouts e exceções
- Mockar serviços externos em testes
```

A abordagem acima reduz acoplamento, facilita testes automatizados e minimiza impactos de falhas externas na aplicação. 

### 06. Como você lidaria com falhas em produção?

Eu lidaria da maneira mais rápida e controlada possível, sempre me baseando em informações confiáveis, como:

```text
- Monitoramento e alertas para detecção rápida
- Logs claros e estruturados
- Tratamento adequado de exceções
- Correções incrementais e rollback quando necessário
```

O objetivo é minimizar o impacto para o usuário final e garantir estabilidade do sistema enquanto a causa raiz é identificada e corrigida.

### 07. Qual abordagem adotaria para CI/CD nessa API?

Eu adotaria uma abordagem de CI/CD focada em **automação**, **simplicidade** e **confiabilidade**, e foi justamente o que adotei neste projeto.

```text
- Build automatizado a cada push e pull request
- Execução de testes automatizados no pipeline
- Separação clara por ambientes
- Pipelines rápidos e previsíveis
```
Essa estratégia ajuda a evitar regressões, garante qualidade contínua do código e facilita a evolução segura da aplicação.

### 08. Como você decide entre REST, GraphQL ou outra forma de API?

Eu primeiramente olho o contexto e as necessidade do projeto para então decidir entre elas.

```text
- REST: simplicidade, padronização e facilidade de manutenção
- GraphQL: múltiplos clientes e necessidade de consultas flexíveis
```
Entendo que, para APIs CRUD simples como esta, a abordagem REST foi a escolha mais adequada. 

### 09. Como avalia desempenho e otimização de APIs?

Eu avalio desempenho e a otimização de APIs com base em dados reais e métricas observáveis, evitando otimizações prematuras. Cito algumas:

```text
- Análise de métricas e logs
- Testes de carga e estresse
- Avaliação de consultas ao banco de dados
- Uso adequado de índices e redução de payloads
```

### 10. Como você documenta decisões técnicas e garante o conhecimento compartilhado na equipe?

Os principais critérios que utilizo são clareza e acessibilidade, nessa ordem. O objetivo é facilitar o compartilhamento de conhecimento e a manutenção do projeto.

```text
- README atualizado com decisões relevantes
- Documentação de arquitetura e padrões adotados
- Código legível e bem organizado
- Compartilhamento de conhecimento por meio de code reviews e discussões técnicas
```
A abordagem acima reduz dependência de conhecimento tácito e facilita o onboarding de novos membros da equipe.

---