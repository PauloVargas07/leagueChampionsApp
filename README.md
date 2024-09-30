# League of Legends Champions App

## Descrição do Projeto

Este projeto é um aplicativo Android desenvolvido em **Kotlin**, utilizando **Jetpack Compose** para a interface do usuário. Ele exibe uma lista de campeões do jogo **League of Legends**, com informações obtidas através de uma API. O app possui duas atividades principais:

1. **MainActivity**: Mostra uma lista de campeões.
2. **ChampionDetailsActivity**: Mostra detalhes de um campeão selecionado.

## Estrutura do Projeto

O projeto contém os seguintes arquivos principais:

### Pacote: `com.example.leaguechampions.models`

- **Champion**: Modelo de dados que representa um campeão do jogo.

### Pacote: `com.example.leaguechampions.activities`

- **MainActivity**: A atividade principal do aplicativo, onde é exibida a lista de campeões. Os campeões são exibidos em uma lista com a possibilidade de pesquisa.
- **ChampionDetailsActivity**: Atividade que mostra os detalhes de um campeão selecionado, incluindo imagem, descrição e estatísticas.

### Pacote: `com.example.leaguechampions.viewmodels`

- **ChampionsViewModel**: Classe `ViewModel` responsável por realizar a requisição HTTP para obter os dados dos campeões. Os dados são armazenados em um `StateFlow` que é observado pela UI.

### Recursos Utilizados

- **Jetpack Compose**: Utilizado para construir a interface do usuário de forma declarativa.
- **ViewModel & StateFlow**: Para gerenciamento de estado e observação de dados.
- **HTTP Requests**: Feitos através da biblioteca padrão de `HttpURLConnection`.
- **Coil**: Para carregar imagens dos campeões a partir de URLs.
- **Gson**: Para serializar e desserializar objetos JSON.

## Funcionalidades

1. **Lista de Campeões**:
   - Exibe uma lista de campeões obtida através de uma requisição HTTP.
   - Possui um campo de pesquisa para facilitar a busca por campeões.

2. **Detalhes do Campeão**:
   - Ao clicar em um campeão da lista, são exibidos detalhes como nome, título, descrição e estatísticas do personagem.
