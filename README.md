# Teste Técnico: Conversor de Moedas - Jaya Tech

## Visão Geral e Requisitos Obrigatórios

- Uma API Rest para realizar a conversão entre duas moedas, registrar transações por usuário e consultar o histórico de conversões.

- Converter pelo menos quatro moedas (BRL, USD, EUR, JPY) ​

- Persistir cada transação de conversão, incluindo ID do usuário, moeda/valor de origem e destino, taxa utilizada e data/hora UTC

- Expor endpoints para realizar conversões e listar o histórico de transações de um usuário

- Utilizar a API externa https://api.exchangeratesapi.io/latest?base=EUR que possui limitação de uso na versão free

## Itens Desejáveis Implementados

- Logs
- Tratamento de exceções
- Documentação
- Coesão de commits
- Mensagens de commits claras
- Configuração de lint
- Testes unitários
- Testes de integração
- Documentação dos endpoints
- Estar rodando e disponível (Hetzner)
- CI/CD

## Decisões

- **Código:** Aplicação dos princípios da Clean Architecture, DDD, SOLID e Clean Code para separar o código em suas devidas camadas, isolar as regras de negócio (o "coração" da aplicação) e tornar o código testável e manutenível.

- **Testes:** Adoção de TDD com a criação de testes unitários e de integração, garantindo o funcionamento correto dos componentes da aplicação, com uma cobertura mínima de 90% (jacoco).

- **Cache:** Utilização de cache para mitigar a limitação no consumo da API externa

## Possíveis Melhorias

- **Testes de Mutabilidade:** Para identificar lacunas nos testes e, eventualmente, projetar novos casos de teste e a garantir uma cobertura mais completa do código

- **Testes de Carga:** Para avaliar a performance e identificar possíveis gargalos no processamento.

- **HTTPS**: Disponibilizar a aplicação em um ambiente com certificado SSL

- **CI/CD & Quality Gates Mais Rígidos**: Adicionar análise estática de segurança (Snyk, SonarQube) e bloqueio de merges se vulnerabilidades ou a cobertura mínima não forem atingidas.

- **Provedores:** Disponibilizar mais moedas para conversão e agregar mais provedores (exchanges)

## Tech Stack

| Camada | Tecnologia |
|--|--|
| Linguagem | Kotlin |
| Ambiente | Java 17 |
| Framework | Spring Boot |
| API Externa | Feign Client |
| Testes | JUnit, Wiremock, Mockito, Testcontainers |
| Banco de Dados | PostgreSQL |
| Migrations | Liquibase|
| Cache | Redis |
| Logging| SLF4J |
| CI/CD | GitHub Actions |
| CD | Watchtower |
| Containerização | docker, docker compose, docker hub |

## Arquitetura & Camadas

- Utilização de uma abordagem com **Clean Architecture** e uma versão "**DDD‑light**", divididas em:

    - **Domain**: Regras de negócio e as entidades centrais (ex.: `Transaction`), sem dependências de frameworks. Facilitando os testes e mantendo o domínio independente de detalhes de implementação.

    - **Application**: Casos de uso (use‑cases) que orquestram a interação entre domínio, persistência e APIs externas (ex.: Listar transações de conversão, Criar transação de conversão), mantendo o código testável e manutenível.

    - **Infrastructure**: Adaptadores e integrações concretas (REST controllers, repositórios JPA, cliente Feign Client para a API de taxas, cache com Redis etc.), sendo responsáveis por interagirem com o mundo exterior.

## API Externa

-   **Endpoint**: `http://api.exchangeratesapi.io/latest?base=EUR`

-   **Limitação**: Só suporta base EUR e tem limite de requisições na versão gratuita (100 por mês)

-   **Estratégia**:
    -   Cache com TTL (Time to live) de 8 horas, uma vez que (em teoria) não há tanta variação no câmbio e este é um valor que entendo que não excederá o limite mensal.
    -   Tratamento de falha caso o serviço externo esteja indisponível.

## Estratégia de Testes

- **Unitários**: Entidade de domínio e casos de uso testados com Mockito.

- **Integração**: Componentes de banco de dados testados isoladamente

- **e2e**: 'End-To-End" do ponto de vista do backend, utilizando wiremock e testcontainers para chegar o mais próximo possível do cenário real da aplicação

## Documentação & Qualidade

-   **OpenAPI (Swagger UI)** Documentação completa em disponível em `/swagger-ui/index.html` para explorar e utilziar a API.

-   **Linters**: Klint e Detekt configurados no ambiente de desenvolvimento, build da aplicação e esteira de CI.

-   **Commits**: Adoção de conventional commits, com mensagens claras e atômicas, usando o padrão `<escopo>: <descrição>`.

## Deploy & CI/CD

- **Docker** Dockerfile multi‑stage para a construção de uma imagem enxuta

-  **GitHub Actions**: Pipeline: lint → build → testes → push de imagem Docker.

-   **Heroku (ou similar)**: Aplicação disponibiliza em uma VPS, com atualização automática da aplicação (via watchtower), sempre que há uma nova imagem "deployada" no container registry (docker hub)

## Execução Local

- Requisitos

    -	Ter o docker e docker compose instalados na sua máquina.

    -	Subistituir o valor da sua access key da API externa utilizada no projeto em `.env.local`

    -	Executar o seguinte comando na raiz do projeto:
         -	`docker compose --env-file ./docker/.env.local up -d --build`

    -	Acessar o swagger em http://localhost:8080/swagger-ui/index.html e utilizar a API

## Apreciação da API

- A API também pode ser testada na minha VPS no seguinte endereço: http://188.245.224.253/swagger-ui/index.html